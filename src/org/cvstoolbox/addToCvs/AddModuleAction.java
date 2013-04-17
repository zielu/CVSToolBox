/* 
 * @(#) $Id:  $
 */
package org.cvstoolbox.addToCvs;

import com.intellij.cvsSupport2.actions.ActionOnSelectedElement;
import com.intellij.cvsSupport2.actions.cvsContext.CvsContext;
import com.intellij.cvsSupport2.cvshandlers.CvsHandler;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vcs.actions.VcsContext;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * <p></p>
 * <br/>
 * <p>Created on 17.04.13</p>
 *
 * @author Lukasz Zielinski
 */
public class AddModuleAction extends ActionOnSelectedElement {

    private AdditionDetails myAdditionDetails;

    public AddModuleAction() {
        super(false);
    }

    private boolean hasCvsEntry(VirtualFile parent) {
        VirtualFile cvsDir = parent.findChild("CVS");
        return cvsDir != null && cvsDir.isDirectory();
    }

    @Override
    public void update(AnActionEvent e) {
        boolean state = false;
        Module module = e.getData(DataKeys.MODULE_CONTEXT);
        if (module != null) {
            VirtualFile moduleFile = module.getModuleFile();
            if (moduleFile != null && moduleFile.getParent() != null) {
                if (!hasCvsEntry(moduleFile.getParent())) {
                    state = true;
                }
            }
/*            if (!state) {
                ModuleRootManager rootManager = ModuleRootManager.getInstance(module);
                for (VirtualFile root : rootManager.getContentRoots()) {
                    if (!hasCvsEntry(root)) {
                        
                    }
                }
            }*/
        }
        e.getPresentation().setVisible(state);
        e.getPresentation().setEnabled(state);
    }

    @Override
    protected String getTitle(VcsContext context) {
        return "Add module to CVS...";
    }
    
    @Override
    protected CvsHandler getCvsHandler(CvsContext context) {
        final VirtualFile selectedFile = context.getSelectedFile();
        final AddModuleWizard importWizard = new AddModuleWizard(context.getProject(), selectedFile);
        importWizard.show();
        if (!importWizard.isOK()) {
            return CvsHandler.NULL;
        }
        myAdditionDetails = importWizard.createAdditionDetails();
        if (myAdditionDetails == null) return CvsHandler.NULL;
        return AdditionHandler.createHandler(myAdditionDetails);
    }
}
