/*
 * CVSToolBox IntelliJ IDEA Plugin
 *
 * Copyright (C) 2011, Łukasz Zieliński
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This plugin uses
 * FAMFAMFAM Silk Icons http://www.famfamfam.com/lab/icons/silk
 */

package org.cvstoolbox.multitag;

import com.intellij.cvsSupport2.config.CvsConfiguration;
import com.intellij.cvsSupport2.cvsExecution.CvsOperationExecutor;
import com.intellij.cvsSupport2.cvsExecution.CvsOperationExecutorCallback;
import com.intellij.cvsSupport2.cvshandlers.CvsHandler;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory;
import com.intellij.openapi.vcs.ui.RefreshableOnComponent;
import com.intellij.openapi.vfs.VirtualFile;
import org.cvstoolbox.filter.Filters;
import org.cvstoolbox.handlers.MultitagHandler;
import org.cvstoolbox.multitag.ui.MultiTagCheckinPanel;
import org.cvstoolbox.util.EDTInvoker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Łukasz Zieliński
 */
public class MultiTagCheckinHandlerFactory extends CheckinHandlerFactory {
    private final Logger LOG = Logger.getInstance(getClass().getName());

    @NotNull
    public CheckinHandler createHandler(final CheckinProjectPanel panel) {
        //TODO: z panelu mozna wyciagnac jakie pliki zostaly wybrane
        return new CheckinHandler() {
            private MultiTagCheckinPanel checkinPanel;

            @Nullable
            public RefreshableOnComponent getAfterCheckinConfigurationPanel(Disposable parentDisposable) {
                checkinPanel = null;
                if (panel.vcsIsAffected("CVS")) {
                    MultiTagConfiguration configuration = panel.getProject().getComponent(MultiTagConfiguration.class);
                    checkinPanel = new MultiTagCheckinPanel(configuration);
                    return checkinPanel;
                } else {
                    return null;
                }
            }

            private void tagFiles(Collection<VirtualFile> files, final Project project) {
                VirtualFile[] toTag = Filters.pruneEmptyDirectories(files);
                final CvsHandler handler = MultitagHandler.createTagsHandler(
                        toTag, checkinPanel.getTagNames(),
                        checkinPanel.getOverrideExisting(),
                        CvsConfiguration.getInstance(project).MAKE_NEW_FILES_READONLY, project);
                if (handler != null) {
                    final Task.Backgroundable task = new Task.Backgroundable(project,
                            "CVS Create Tag", false, new PerformInBackgroundOption() {
                                @Override
                                public boolean shouldStartInBackground() {
                                    return false;
                                }

                                @Override
                                public void processSentToBackground() {
                                }
                            }) {
                        private List<VcsException> errors = Collections.emptyList();

                        public void run(@NotNull final ProgressIndicator indicator) {
                            indicator.setIndeterminate(true);
                            final CvsOperationExecutor executor = new CvsOperationExecutor(project);
                            executor.performActionSync(handler, CvsOperationExecutorCallback.EMPTY);
                            errors = executor.getResult().getErrors();
                        }

                        @Nullable
                        public NotificationInfo getNotificationInfo() {
                            String text = " Tags Created";
                            if (errors.size() > 0) {
                                text += ", " + errors.size() + " Tag(s) Failed To Create";
                            }
                            return new NotificationInfo("CVS Create Tag", "CVS Tagging Finished", text, true);
                        }
                    };

                    EDTInvoker.invokeAndWaitNoExceptions(new Runnable() {
                        @Override
                        public void run() {
                            ProgressManager.getInstance().run(task);
                        }
                    });
                }
            }

            @Override
            public void checkinSuccessful() {
                if (checkinPanel != null && checkinPanel.shouldTag()) {
                    final Project project = panel.getProject();
                    MultiTagConfiguration configuration = project.getComponent(MultiTagConfiguration.class);
                    Collection<String> tagNames = checkinPanel.getTagNames();
                    configuration.setSelectedTags(tagNames);

                    List<VirtualFile> selectedFiles = new ArrayList<VirtualFile>(panel.getVirtualFiles());
                    tagFiles(selectedFiles, project);
                }
            }

            @Override
            public void checkinFailed(List<VcsException> exceptions) {
                if (checkinPanel != null && checkinPanel.shouldTag()) {
                    MultiTagConfiguration configuration = panel.getProject().getComponent(MultiTagConfiguration.class);
                    Collection<String> tagNames = checkinPanel.getTagNames();
                    configuration.setSelectedTags(tagNames);
                    Collection<VirtualFile> selectedFiles = panel.getVirtualFiles();
                    Map<String, VirtualFile> toTag = new LinkedHashMap<String, VirtualFile>();
                    for (VirtualFile file : selectedFiles) {
                        toTag.put(file.getPath(), file);
                    }
                    for (VcsException exception : exceptions) {
                        if (exception.getVirtualFile() != null) {
                            toTag.remove(exception.getVirtualFile().getPath());
                        }
                    }
                    if (!toTag.isEmpty()) {
                        tagFiles(toTag.values(), panel.getProject());
                    }
                }
            }
        };
    }
}
