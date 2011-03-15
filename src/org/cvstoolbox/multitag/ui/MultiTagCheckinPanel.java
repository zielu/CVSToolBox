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

import com.intellij.openapi.vcs.ui.RefreshableOnComponent;
import net.miginfocom.swing.MigLayout;
import org.cvstoolbox.multitag.MultiTagConfiguration;
import org.jetbrains.annotations.NonNls;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

/**
 * @author Łukasz Zieliński
 */
public class MultiTagCheckinPanel implements RefreshableOnComponent {
    private final MultiTagsSelection tagsSelection;
    private final MultiTagConfiguration configuration;
    private final JPanel content;
    private final JLabel selectionError;
    private final JCheckBox tag = new JCheckBox("Multitag commited files");
    private final JCheckBox myOverrideExisting = new JCheckBox("Override existing (-F)");

    public MultiTagCheckinPanel(MultiTagConfiguration configuration) {
        this.configuration = configuration;
        this.tagsSelection = new MultiTagsSelection();
        content = new JPanel(new BorderLayout());
        JPanel center = new JPanel(new MigLayout("fillx"));
        selectionError = new JLabel();

        center.add(myOverrideExisting, "spanx, growx, wrap");
        center.add(selectionError, "spanx, wrap");
        center.add(tagsSelection.getComponent(), "spanx, growx, pushx");
        tagsSelection.setConfiguration(configuration);
        content.add(tag, BorderLayout.NORTH);
        content.add(center, BorderLayout.CENTER);
        tag.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEnable();
                updateSelectionError();
            }
        });
        tagsSelection.addTagListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateSelectionError();
            }
        });
        tagsSelection.registerActionAccelerators(content);
    }

    private void updateSelectionError() {
        if (tagsSelection.getTagNames().isEmpty() && tag.isSelected()) {
            @NonNls final String text = "<html><font color='red'><b>none selected</b></font></html>";
            selectionError.setText(text);
        } else {
            selectionError.setText("");
        }
    }

    private void updateEnable() {
        boolean state = tag.isSelected();
        tagsSelection.setEnabled(state);
        myOverrideExisting.setEnabled(state);
    }

    @Override
    public JComponent getComponent() {
        return content;
    }

    @Override
    public void refresh() {
        tag.setSelected(configuration.TAG_AFTER_PROJECT_COMMIT);
        tagsSelection.setConfiguration(configuration);
        myOverrideExisting.setSelected(configuration.OVERRIDE_EXISTING_TAG_FOR_PROJECT);
        updateSelectionError();
        updateEnable();
    }

    @Override
    public void saveState() {
        configuration.TAG_AFTER_PROJECT_COMMIT = tag.isSelected();
        configuration.OVERRIDE_EXISTING_TAG_FOR_PROJECT = myOverrideExisting.isSelected();
    }

    public Collection<String> getTagNames() {
        return tagsSelection.getTagNames();
    }

    public Collection<String> getAvailableTagNames() {
        return tagsSelection.getAvailableTagNames();
    }

    public boolean shouldTag() {
        return tag.isSelected() && !getTagNames().isEmpty();
    }

    public boolean getOverrideExisting() {
        return myOverrideExisting.isSelected();
    }

    @Override
    public void restoreState() {
        refresh();
    }
}
