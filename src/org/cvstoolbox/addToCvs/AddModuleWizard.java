/* 
 * @(#) $Id:  $
 */
package org.cvstoolbox.addToCvs;

import com.intellij.cvsSupport2.cvsBrowser.CvsElement;
import com.intellij.cvsSupport2.ui.experts.CvsWizard;
import com.intellij.cvsSupport2.ui.experts.SelectCVSConfigurationStep;
import com.intellij.cvsSupport2.ui.experts.SelectCvsElementStep;
import com.intellij.cvsSupport2.ui.experts.importToCvs.SelectImportLocationStep;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.TreeSelectionModel;
import java.io.File;

/**
 * <p></p>
 * <br/>
 * <p>Created on 17.04.13</p>
 *
 * @author Lukasz Zielinski
 */
public class AddModuleWizard extends CvsWizard {
    private final SelectCVSConfigurationStep mySelectCVSConfigurationStep;
    private final SelectCvsElementStep mySelectCvsElementStep;
    private final SelectImportLocationStep mySelectAdditionLocationStep;
    private final AddSettingsStep myAddSettingsStep;

    public AddModuleWizard(Project project, VirtualFile selectedFile) {
        super("Add module to CVS...", project);
        mySelectCVSConfigurationStep = new SelectCVSConfigurationStep(project, this);
        mySelectCvsElementStep = new SelectCvsElementStep(
                "Select Directory to Add to",
                this,
                project,
                mySelectCVSConfigurationStep,
                true,
                TreeSelectionModel.SINGLE_TREE_SELECTION,
                false,
                false);

        mySelectAdditionLocationStep = new SelectImportLocationStep(
                "Select directory to Add",
                this,
                project,
                selectedFile);
        myAddSettingsStep = new AddSettingsStep("Addition Settings", this, mySelectAdditionLocationStep);

        addStep(mySelectCVSConfigurationStep);
        addStep(mySelectCvsElementStep);
        addStep(mySelectAdditionLocationStep);
        addStep(myAddSettingsStep);

        init();
    }

    @Nullable
    public AdditionDetails createAdditionDetails() {
        final CvsElement module = mySelectCvsElementStep.getSelectedCvsElement();
        final String moduleName = myAddSettingsStep.getModuleName();
        final String importModuleName = module.getElementPath().equals(".") ? moduleName : module.getElementPath() + "/" + moduleName;

        final File selectedFile = mySelectAdditionLocationStep.getSelectedFile();
        if (selectedFile == null) {
            return null;
        }
        return new AdditionDetails(
                selectedFile,
                importModuleName,
                mySelectCVSConfigurationStep.getSelectedConfiguration()
        );
    }
}
