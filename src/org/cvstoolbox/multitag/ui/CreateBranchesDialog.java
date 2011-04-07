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

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author Łukasz Zieliński
 */
public class CreateBranchesDialog extends CvsTagDialog {
    private final JPanel myPanel;
    private final TextFieldWithBrowseButton myTagName;
    private final JCheckBox myOverrideExisting;
    private final JCheckBox mySwitchToThisTag;
    private final JLabel myBranchLabel;
    private final JLabel myErrorLabel;

    public CreateBranchesDialog(final Collection<FilePath> files, final Project project) {
        myBranchLabel = new JLabel(CvsBundle.message("label.branch.name"));
        myErrorLabel = new JLabel();
        mySwitchToThisTag = new JCheckBox(CvsBundle.message("checkbox.switch.to.this.branch"));
        myOverrideExisting = new JCheckBox("Override existing/allow move branch (-F -B)");
        myTagName = new TextFieldWithBrowseButton();
        myTagName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String branchName = TagsHelper.chooseBranch(collectVcsRoots(project, files), project, false);
                if (branchName != null) {
                    myTagName.setText(branchName);
                }
            }
        });

        myPanel = new JPanel(new MigLayout("fill, insets 0 0 0 0, hidemode 2"));
        myPanel.add(myBranchLabel, "");
        myPanel.add(myTagName, "spanx, growx, pushx, wrap");
        myPanel.add(myErrorLabel, "spanx, wrap");
        myPanel.add(myOverrideExisting, "spanx, wrap");
        myPanel.add(mySwitchToThisTag, "spanx, wrap");

        setTitle("Create/Move Branch");

        CvsFieldValidator.installOn(this, myTagName.getTextField(), myErrorLabel);
        init();
    }

    public static Collection<FilePath> collectVcsRoots(final Project project, final Collection<FilePath> files) {
        Collection<FilePath> result = new HashSet<FilePath>();
        for (FilePath filePath : files) {
            final VirtualFile root = ProjectLevelVcsManager.getInstance(project).getVcsRootFor(filePath);
            if (root != null) {
                result.add(VcsContextFactory.SERVICE.getInstance().createFilePathOn(root));
            }
        }
        return result;
    }

    public String getTagName() {
        return myTagName.getText();
    }

    public boolean getOverrideExisting() {
        return myOverrideExisting.isSelected();
    }

    protected JComponent createCenterPanel() {
        return myPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return myTagName.getTextField();
    }

    protected String getDimensionServiceKey() {
        return "CVSToolBox.CreateBranchesDialog";
    }

    public boolean switchToThisBranch() {
        return mySwitchToThisTag.isSelected();
    }

    public boolean tagFieldIsActive() {
        return true;
    }
}
