/*
 * CVSToolBox IntelliJ IDEA Plugin
 *
 * Copyright (C) 2013, Łukasz Zieliński
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.intellij.cvsSupport2.CvsVcs2;
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
import com.intellij.openapi.vcs.checkin.VcsCheckinHandlerFactory;
import com.intellij.openapi.vcs.ui.RefreshableOnComponent;
import com.intellij.openapi.vfs.VirtualFile;
import org.cvstoolbox.filter.Filters;
import org.cvstoolbox.handlers.MultitagHandler;
import org.cvstoolbox.multitag.ui.MultiTagCheckinPanel;
import org.cvstoolbox.util.CvsHelper;
import org.cvstoolbox.util.EDTInvoker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Łukasz Zieliński
 */
public class MultiTagCheckinHandlerFactory extends VcsCheckinHandlerFactory {
    private final Logger LOG = Logger.getInstance(getClass().getName());

    MultiTagCheckinHandlerFactory() {
        super(CvsVcs2.getKey());
    }

    @NotNull
    public CheckinHandler createVcsHandler(final CheckinProjectPanel panel) {
        return new CheckinHandler() {
            private MultiTagCheckinPanel checkinPanel;

            @Nullable
            public RefreshableOnComponent getAfterCheckinConfigurationPanel(Disposable parentDisposable) {
                MultiTagConfiguration configuration = panel.getProject().getComponent(MultiTagConfiguration.class);
                checkinPanel = new MultiTagCheckinPanel(configuration);
                return checkinPanel;
            }

            private void tagFiles(final Collection<VirtualFile> files, final Project project) {                
                final Collection<String> tagNames = new ArrayList<String>(checkinPanel.getTagNames());
                final boolean overrideExisting = checkinPanel.getOverrideExisting();
                final boolean makeNewFilesReadOnly = CvsConfiguration.getInstance(project).MAKE_NEW_FILES_READONLY;                
                final Task.Backgroundable task = new Task.Backgroundable(project,
                        "CVS Create Tag", false, PerformInBackgroundOption.DEAF) {
                    
                    private List<VcsException> errors = Collections.emptyList();
                    private boolean nothingToTag;
                    
                    public void run(@NotNull final ProgressIndicator indicator) {
                        indicator.setIndeterminate(true);
                        if (LOG.isDebugEnabled()) {
                            for (VirtualFile file : files) {
                                LOG.debug("Tagging input file: "+file.getUrl());
                            }
                        }
                        Predicate<VirtualFile> filter = Predicates.and(Filters.fileExistsInCvs(), Filters.keepNonEmptyDirectoriesAndFiles(), 
                                Predicates.not(Filters.fileLocallyDeleted()));
                        Iterable<VirtualFile> toTag = Iterables.filter(files, filter);                        
                        if (LOG.isDebugEnabled()) {
                            for (VirtualFile file : toTag) {
                                LOG.debug("Will tag file: "+file.getUrl());
                            }
                        }
                        final CvsHandler handler = MultitagHandler.createTagsHandler(
                            CvsHelper.toFilePaths(toTag), tagNames, overrideExisting, makeNewFilesReadOnly, project);
                        if (handler != null) {
                            final CvsOperationExecutor executor = new CvsOperationExecutor(project);
                            boolean performedAction = false;
                            if (!indicator.isCanceled()) {
                                indicator.startNonCancelableSection();
                                performedAction = true;
                                executor.performActionSync(handler, CvsOperationExecutorCallback.EMPTY);
                            }
                            errors = executor.getResult().getErrors();
                            if (performedAction) {
                                indicator.finishNonCancelableSection();
                            }
                        } else {
                            nothingToTag = true;
                        }                        
                    }

                    @Nullable
                    public NotificationInfo getNotificationInfo() {
                        String text = " Tags Created";
                        if (errors.size() == 0 && nothingToTag) {
                            return new NotificationInfo("CVS Create Tag", "CVS Tagging Finished", "Nothing to tag", true);                            
                        } else if (errors.size() > 0) {                            
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

            @Override
            public void checkinSuccessful() {
                if (checkinPanel != null && checkinPanel.shouldTag()) {
                    final Project project = panel.getProject();
                    MultiTagConfiguration configuration = project.getComponent(MultiTagConfiguration.class);
                    Collection<String> tagNames = checkinPanel.getTagNames();
                    configuration.setSelectedTags(tagNames);

                    List<VirtualFile> selectedFiles = new ArrayList<VirtualFile>(panel.getVirtualFiles());
                    LOG.debug("Tagging after successful commit");
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
                        LOG.debug("Tagging after failed commit");
                        tagFiles(toTag.values(), panel.getProject());
                    }
                }
            }
        };
    }
}
