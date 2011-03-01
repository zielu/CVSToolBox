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

import com.intellij.cvsSupport2.actions.ActionOnSelectedElement;
import com.intellij.cvsSupport2.actions.actionVisibility.CvsActionVisibility;
import com.intellij.cvsSupport2.actions.cvsContext.CvsContext;
import com.intellij.cvsSupport2.config.CvsConfiguration;
import com.intellij.cvsSupport2.cvshandlers.CvsHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vcs.actions.VcsContext;
import org.cvstoolbox.handlers.MultitagHandler;
import org.cvstoolbox.multitag.ui.CreateTagsDialog;

import java.util.Arrays;

/**
 * <p></p>
 * <br/>
 *
 * @author Lukasz Zielinski
 */
public class MultiTagAction extends ActionOnSelectedElement {

    public MultiTagAction() {
        super(true);
        CvsActionVisibility visibility = getVisibility();
        visibility.canBePerformedOnSeveralFiles();
        visibility.canBePerformedOnLocallyDeletedFile();
        visibility.addCondition(FILES_EXIST_IN_CVS);
    }

    protected String getTitle(VcsContext context) {
        return "Create tags";
    }

    protected CvsHandler getCvsHandler(CvsContext context) {
        FilePath[] selectedFiles = context.getSelectedFilePaths();
        Project project = context.getProject();
        MultiTagConfiguration configuration = project.getComponent(MultiTagConfiguration.class);
        CreateTagsDialog dialog = new CreateTagsDialog(Arrays.asList(selectedFiles), project);
        dialog.setConfiguration(configuration);
        dialog.show();
        //save available tags
        if (!dialog.isOK()) {
            return CvsHandler.NULL;
        }
        //save selection on OK
        configuration.setSelectedTags(dialog.getTagNames());

        return MultitagHandler.createTagsHandler(selectedFiles, dialog.getTagNames(),
                dialog.switchToThisBranch(), dialog.getSwitchToTagName(), dialog.getOverrideExisting(),
                CvsConfiguration.getInstance(project).MAKE_NEW_FILES_READONLY, project);
    }
}
