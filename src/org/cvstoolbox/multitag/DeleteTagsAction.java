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
import com.intellij.cvsSupport2.cvshandlers.CommandCvsHandler;
import com.intellij.cvsSupport2.cvshandlers.CvsHandler;
import com.intellij.cvsSupport2.cvsoperations.cvsTagOrBranch.ui.DeleteTagDialog;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.actions.VcsContext;
import org.cvstoolbox.cvsoperations.TagsHelperEx;
import org.cvstoolbox.handlers.MultitagHandler;
import org.cvstoolbox.multitag.ui.DeleteTagsDialog;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Łukasz Zieliński
 */
public class DeleteTagsAction extends ActionOnSelectedElement {
    private static final Logger LOG = Logger.getInstance("#org.cvstoolbox.multitag.DeleteTagsAction");

    public DeleteTagsAction() {
        super(false);
        CvsActionVisibility visibility = getVisibility();
        visibility.canBePerformedOnSeveralFiles();
        visibility.addCondition(FILES_EXIST_IN_CVS);
    }

    protected String getTitle(VcsContext context) {
        return "Delete Tags...";
    }

    protected CvsHandler getCvsHandler(CvsContext context) {
        try {
            Project project = context.getProject();
            Collection<String> tags = TagsHelperEx.collectAllBranches(collectFiles(context), project, false);
            if (tags.size() > 0) {
                MultiTagConfiguration configuration = project.getComponent(MultiTagConfiguration.class);
                DeleteTagsDialog deleteTagDialog = new DeleteTagsDialog(tags);
                deleteTagDialog.setConfiguration(configuration);
                deleteTagDialog.show();
                if (deleteTagDialog.isOK()) {
                    Collection<String> tagNames = deleteTagDialog.getTagNames();
                    configuration.setDeletedTags(tagNames);
                    return MultitagHandler.createRemoveTagsAction(context.getSelectedFiles(), tagNames);
                }
            }
            return CvsHandler.NULL;
        } catch (VcsException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return CvsHandler.NULL;
        }
    }

    private Collection<FilePath> collectFiles(VcsContext context) {
        return Arrays.asList(context.getSelectedFilePaths());
    }

}
