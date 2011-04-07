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
import org.cvstoolbox.multitag.ui.CreateBranchesDialog;

import java.util.Arrays;

/**
 * @author Łukasz Zieliński
 */
public class MultiBranchAction extends ActionOnSelectedElement {

    public MultiBranchAction() {
        super(true);
        CvsActionVisibility visibility = getVisibility();
        visibility.canBePerformedOnSeveralFiles();
        visibility.canBePerformedOnLocallyDeletedFile();
        visibility.addCondition(FILES_EXIST_IN_CVS);
    }

    protected String getTitle(VcsContext context) {
        return "Create/Move Branch...";
    }

    protected CvsHandler getCvsHandler(CvsContext context) {
        FilePath[] selectedFiles = context.getSelectedFilePaths();
        Project project = context.getProject();
        CreateBranchesDialog createBranchDialog = new CreateBranchesDialog(Arrays.asList(selectedFiles), project);
        createBranchDialog.show();
        if (!createBranchDialog.isOK()) return CvsHandler.NULL;

        return MultitagHandler.createBranchesHandler(selectedFiles,
                createBranchDialog.getTagName(),
                createBranchDialog.switchToThisBranch(),
                createBranchDialog.getOverrideExisting(),
                CvsConfiguration.getInstance(context.getProject()).MAKE_NEW_FILES_READONLY, project);
    }
}
