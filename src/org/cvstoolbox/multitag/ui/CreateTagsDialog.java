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

package org.cvstoolbox.multitag.ui;

import ca.odell.glazedlists.swing.EventComboBoxModel;
import com.intellij.CvsBundle;
import com.intellij.cvsSupport2.cvsoperations.cvsTagOrBranch.TagsHelper;
import com.intellij.cvsSupport2.cvsoperations.cvsTagOrBranch.ui.CvsTagDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FilePath;
import net.miginfocom.swing.MigLayout;
import org.cvstoolbox.multitag.ExistingTagsProvider;
import org.cvstoolbox.multitag.MultiTagConfiguration;
import org.cvstoolbox.util.CvsHelper;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

public class CreateTagsDialog extends CvsTagDialog {
    private final Project project;
    private final Collection<FilePath> files;

    private JPanel myPanel;
    private JCheckBox myOverrideExisting;
    private JCheckBox mySwitchToThisTag;
    private JComboBox mySwitchToThisTagCombo;
    private final MultiTagsSelection tagsSelection;

    public CreateTagsDialog(final Collection<FilePath> files, final Project project) {
        this.project = project;
        this.files = files;
        tagsSelection = new MultiTagsSelection();
        tagsSelection.enableExistingTagsSelection(new ExistingTagsProvider() {
            @Override
            public String getExistingTag() {
                return TagsHelper.chooseBranch(CvsHelper.collectVcsRoots(project, files), project, false);
            }
        });
        myOverrideExisting = new JCheckBox("Override existing (-F)");
        mySwitchToThisTag = new JCheckBox(CvsBundle.message("checkbox.switch.to.this.tag"));
        mySwitchToThisTagCombo = new JComboBox(new EventComboBoxModel<String>(tagsSelection.getTagSelection()));

        setTitle("Create tags");

        myPanel = new JPanel(new MigLayout("fill, insets 0 0 0 0"));
        myPanel.add(tagsSelection.getComponent(), "growx, pushx, growy, pushy, wrap");

        myPanel.add(myOverrideExisting, "spanx, growx, wrap");
        myPanel.add(mySwitchToThisTag, "split 2");
        myPanel.add(mySwitchToThisTagCombo, "spanx, growx, wrap");

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
        tagsSelection.registerActionAccelerators(myPanel);
        setOkStatus();
        updateSwitchToThisTagCombo();
    }

    public void setConfiguration(MultiTagConfiguration configuration) {
        tagsSelection.setConfiguration(configuration);
    }

    private void setOkStatus() {
        boolean noSelection = getTagNames().isEmpty();
        setOKActionEnabled(!noSelection);
    }

    private void updateSwitchToThisTagCombo() {
        mySwitchToThisTagCombo.setModel(new EventComboBoxModel<String>(tagsSelection.getTagSelection()));
        mySwitchToThisTagCombo.setEnabled(mySwitchToThisTag.isSelected());
        if (mySwitchToThisTag.isEnabled()) {
            if (mySwitchToThisTagCombo.getSelectedIndex() == - 1 && mySwitchToThisTagCombo.getItemCount() > 0) {
                mySwitchToThisTagCombo.setSelectedIndex(0);
            }
        }
    }

    public Collection<String> getTagNames() {
        return tagsSelection.getTagNames();
    }

    public Collection<String> getAvailableTagNames() {
        return tagsSelection.getAvailableTagNames();
    }

    public String getSwitchToTagName() {
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
        return "CVSToolBox.CreateTagsDialog";
    }

    public boolean switchToThisBranch() {
        return mySwitchToThisTag.isSelected() && (mySwitchToThisTagCombo.getSelectedIndex() != -1);
    }

    public boolean tagFieldIsActive() {
        return true;
    }
}
