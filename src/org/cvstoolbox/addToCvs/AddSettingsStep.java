/* 
 * @(#) $Id:  $
 */
package org.cvstoolbox.addToCvs;

import com.intellij.cvsSupport2.ui.experts.CvsWizard;
import com.intellij.cvsSupport2.ui.experts.WizardStep;
import com.intellij.cvsSupport2.ui.experts.importToCvs.SelectImportLocationStep;
import com.intellij.openapi.util.io.FileUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.io.File;

/**
 * <p></p>
 * <br/>
 * <p>Created on 17.04.13</p>
 *
 * @author Lukasz Zielinski
 */
public class AddSettingsStep extends WizardStep {
    private final SelectImportLocationStep mySelectImportLocationStep;

    private JPanel myPanel;
    private JLabel myModuleNameLabel;
    private JTextField myModuleName;

    private File myDirectoryToImport;
    
    public AddSettingsStep(String description, CvsWizard wizard, SelectImportLocationStep selectImportLocationStep) {
        super(description, wizard);
        mySelectImportLocationStep = selectImportLocationStep;

        myPanel = new JPanel(new MigLayout("fillx, ins 0"));
        myModuleName = new JTextField();
        myModuleNameLabel = new JLabel("Name in repository:");
        myModuleNameLabel.setLabelFor(myModuleName);

        myPanel.add(myModuleNameLabel);
        myPanel.add(myModuleName, "spanx, growx, pushx, wrap");
        
        init();
    }

    @Override
    public void activate() {
        final File selectedFile = mySelectImportLocationStep.getSelectedFile();
        if (!FileUtil.filesEqual(selectedFile, myDirectoryToImport)) {
            myDirectoryToImport = selectedFile;
            myModuleName.setText(myDirectoryToImport.getName());
            myModuleName.selectAll();
        }
    }

    @Override
    public void saveState() {
        super.saveState();
        //TODO: capture dialog state
    }

    @Override
    public boolean nextIsEnabled() {
        return false;
    }

    @Override
    protected JComponent createComponent() {
        return myPanel;
    }

    public String getModuleName() {
        return myModuleName.getText().trim();
    }
}
