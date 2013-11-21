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

package org.cvstoolbox.multitag.ui;

import ca.odell.glazedlists.swing.EventComboBoxModel;
import com.intellij.CvsBundle;
import com.intellij.cvsSupport2.cvsoperations.cvsTagOrBranch.TagsHelper;
import com.intellij.cvsSupport2.cvsoperations.cvsTagOrBranch.ui.CvsTagDialog;
import com.intellij.cvsSupport2.ui.experts.importToCvs.CvsFieldValidator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vcs.ProjectLevelVcsManager;
import com.intellij.openapi.vcs.actions.VcsContextFactory;
import com.intellij.openapi.vfs.VirtualFile;
import net.miginfocom.swing.MigLayout;
import org.cvstoolbox.multitag.ExistingTagsProvider;
import org.cvstoolbox.multitag.config.TagsConfiguration;
import org.cvstoolbox.multitag.res.ResProvider;
import org.cvstoolbox.util.CvsHelper;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author Łukasz Zieliński
 */
public class CreateBranchesDialog extends CvsTagDialog {
    private final JPanel myPanel;
    private final JCheckBox myOverrideExisting;
    private final JCheckBox mySwitchToThisTag;
    private final JComboBox mySwitchToThisTagCombo;
    private final MultiTagsSelection tagsSelection;

    public CreateBranchesDialog(final Collection<FilePath> files, final Project project) {
        tagsSelection = new MultiTagsSelection(ResProvider.getBranchesRes());
        tagsSelection.enableExistingTagsSelection(new ExistingTagsProvider() {
            @Override
            public String getExistingTag() {
                return TagsHelper.chooseBranch(CvsHelper.collectVcsRoots(project, files), project);
            }
        });
        mySwitchToThisTag = new JCheckBox(CvsBundle.message("checkbox.switch.to.this.branch"));
        mySwitchToThisTagCombo = new JComboBox(new EventComboBoxModel<String>(tagsSelection.getTagSelection()));
        myOverrideExisting = new JCheckBox("Override/move branch (-F -B)");

        myPanel = new JPanel(new MigLayout("fill, insets 0 0 0 0, hidemode 2"));
        myPanel.add(tagsSelection.getComponent(), "growx, pushx, growy, pushy, wrap");
        myPanel.add(myOverrideExisting, "spanx, wrap");
        myPanel.add(mySwitchToThisTag, "split 2");
        myPanel.add(mySwitchToThisTagCombo, "spanx, growx, wrap");

        setTitle("Create/Move Branch");

        tagsSelection.addTagListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateSwitchToThisTagCombo();
                setOkStatus();
            }
        });

        mySwitchToThisTag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSwitchToThisTagCombo();
            }
        });

        init();
        setOkStatus();
        updateSwitchToThisTagCombo();
    }

    private void setOkStatus() {
        boolean noSelection = getTagNames().isEmpty();
        setOKActionEnabled(!noSelection);
    }

    private void updateSwitchToThisTagCombo() {
        mySwitchToThisTag.setEnabled(!tagsSelection.getTagSelection().isEmpty());
        mySwitchToThisTagCombo.setModel(new EventComboBoxModel<String>(tagsSelection.getTagSelection()));
        mySwitchToThisTagCombo.setEnabled(mySwitchToThisTag.isEnabled() && mySwitchToThisTag.isSelected());
        if (mySwitchToThisTagCombo.isEnabled()) {
            if (mySwitchToThisTagCombo.getSelectedIndex() == - 1 && mySwitchToThisTagCombo.getItemCount() > 0) {
                mySwitchToThisTagCombo.setSelectedIndex(0);
            }
        }
    }

    public void setConfiguration(TagsConfiguration configuration) {
        tagsSelection.setConfiguration(configuration);
    }

    public Collection<String> getTagNames() {
        return tagsSelection.getTagNames();
    }

    public String getSwitchToBranchName() {
        return (String) mySwitchToThisTagCombo.getSelectedItem();
    }

    public boolean getOverrideExisting() {
        return myOverrideExisting.isSelected();
    }

    protected JComponent createCenterPanel() {
        return myPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return tagsSelection.getPreferredFocusedComponent();
    }

    protected String getDimensionServiceKey() {
        return "CVSToolBox.CreateBranchesDialog";
    }

    public boolean switchToThisBranch() {
        return mySwitchToThisTag.isEnabled() && mySwitchToThisTag.isSelected() && (mySwitchToThisTagCombo.getSelectedIndex() != -1);
    }

    public boolean tagFieldIsActive() {
        return true;
    }
}
