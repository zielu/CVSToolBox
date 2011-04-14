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

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.ListSelection;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.UniqueList;
import ca.odell.glazedlists.swing.EventListModel;
import ca.odell.glazedlists.swing.EventSelectionModel;
import com.intellij.CvsBundle;
import com.intellij.cvsSupport2.cvsoperations.cvsTagOrBranch.ui.TagNameFieldOwner;
import com.intellij.cvsSupport2.ui.experts.importToCvs.CvsFieldValidator;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.MultiLineLabelUI;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBList;
import net.miginfocom.swing.MigLayout;
import org.cvstoolbox.multitag.ExistingTagsProvider;
import org.cvstoolbox.multitag.config.TagsConfiguration;
import org.cvstoolbox.multitag.res.MultiTagsSelectionRes;
import org.cvstoolbox.resources.Resources;
import org.cvstoolbox.ui.DialogBuilderTagNameAdapter;
import org.cvstoolbox.util.StringComparator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Łukasz Zieliński
 */
public class MultiTagsSelection {
    private final MultiTagsSelectionRes textResources;

    private JPanel content;
    private JPanel buttonPanel;
    private JBList tagsList;
    private JScrollPane tagsListScroll;
    private EventList<String> tagsListContent;
    private EventList<String> tagsSelection;

    private Action addNewAction;
    private Action addExistingAction;
    private Action removeAction;

    private JButton addExistingButton;

    private final KeyStroke addNewKeyStroke = KeyStroke.getKeyStroke("shift EQUALS");
    private final KeyStroke addExistingKeyStroke = KeyStroke.getKeyStroke("control shift EQUALS");
    private final KeyStroke removeKeyStroke = KeyStroke.getKeyStroke("DELETE");

    private ExistingTagsProvider existingTagsProvider;
    private TagsConfiguration configuration;

    public MultiTagsSelection(MultiTagsSelectionRes textResources) {
        this.textResources = textResources;

        tagsListContent = new SortedList<String>(new UniqueList<String>(
                new BasicEventList<String>()), new StringComparator(true));
        tagsList = new JBList(new EventListModel<String>(tagsListContent));
        tagsList.getEmptyText().setText(textResources.get(MultiTagsSelectionRes.Key.EmptyList));
        EventSelectionModel<String> tagsSelectionModel = new EventSelectionModel<String>(tagsListContent);
        tagsSelection = tagsSelectionModel.getSelected();
        tagsList.setSelectionModel(tagsSelectionModel);
        tagsList.setSelectionMode(ListSelection.MULTIPLE_INTERVAL_SELECTION_DEFENSIVE);
        tagsList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setRemoveActionState();
            }
        });
        tagsListScroll = ScrollPaneFactory.createScrollPane(tagsList);

        JButton newButton = new JButton(getAddNewAction());

        addExistingButton = new JButton(getAddExistingAction());
        addExistingButton.setVisible(false);
        getAddExistingAction().setEnabled(false);

        JButton removeButton = new JButton(getRemoveAction());

        buttonPanel = new JPanel(new MigLayout("flowy, insets 0, hidemode 2"));
        buttonPanel.add(newButton, "split 4");
        buttonPanel.add(addExistingButton, "");
        buttonPanel.add(removeButton, "");

        content = new JPanel(new MigLayout("fill, insets 0"));
        content.add(tagsListScroll, "growx, pushx, growy, pushy");
        content.add(buttonPanel, "growy, wrap");
    }

    public void setAfterActionsComponent(JComponent component) {
        buttonPanel.add(component, "");
    }

    public void setEnabled(boolean enabled) {
        tagsList.setEnabled(enabled);
        getAddNewAction().setEnabled(enabled);
        if (addExistingButton.isVisible()) {
            addExistingButton.setEnabled(enabled);
        }
        getRemoveAction().setEnabled(enabled);
    }

    private Action getAddNewAction() {
        if (addNewAction == null) {
            addNewAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    final DialogBuilder builder = new DialogBuilder(content);
                    builder.addOkAction();
                    builder.addCancelAction();
                    builder.setTitle(textResources.get(MultiTagsSelectionRes.Key.AddNewTitle));
                    JPanel content = new JPanel(new MigLayout("fillx"));
                    final JTextField tf = new JTextField();
                    builder.setPreferedFocusComponent(tf);
                    JLabel errorLabel = new JLabel();
                    errorLabel.setUI(new MultiLineLabelUI());

                    tf.setColumns(15);
                    content.add(new JLabel(textResources.get(MultiTagsSelectionRes.Key.AddNewLabel)), "");
                    content.add(tf, "growx, pushx, wrap");
                    content.add(errorLabel, "spanx, growx");
                    builder.setCenterPanel(content);
                    TagNameFieldOwner owner = new DialogBuilderTagNameAdapter(builder);
                    CvsFieldValidator.installOn(owner, tf, errorLabel);
                    if (builder.show() == DialogWrapper.OK_EXIT_CODE) {
                        tagsListContent.add(tf.getText());
                        saveConfiguration();
                        tagsList.requestFocusInWindow();
                    }
                }
            };
            //addNewAction.putValue(Action.NAME, "+N");
            addNewAction.putValue(Action.SHORT_DESCRIPTION, textResources.get(MultiTagsSelectionRes.Key.AddNewName)+
                    " [Shift + =]");
            addNewAction.putValue(Action.SMALL_ICON, Resources.getAddTagIcon());
        }
        return addNewAction;
    }

    private Action getAddExistingAction() {
        if (addExistingAction == null) {
            addExistingAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String branchName = existingTagsProvider.getExistingTag();
                    if (branchName != null) {
                        tagsListContent.add(branchName);
                        saveConfiguration();
                        tagsList.requestFocusInWindow();
                    }
                }
            };

            addExistingAction.putValue(Action.SHORT_DESCRIPTION,
                    textResources.get(MultiTagsSelectionRes.Key.AddExistingName)+" [Ctrl + Shift + =]");
            addExistingAction.putValue(Action.SMALL_ICON, Resources.getTagIcon());
        }
        return addExistingAction;
    }

    private Action getRemoveAction() {
        if (removeAction == null) {
            removeAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    List<String> names = new ArrayList<String>(tagsSelection);
                    StringBuilder message;
                    String title;
                    if (names.size() == 1) {
                        message = new StringBuilder(textResources.get(MultiTagsSelectionRes.Key.RemoveTagPrefix)).
                                append(": ").append(names.get(0));
                        title = textResources.get(MultiTagsSelectionRes.Key.RemoveTagTitle);
                    } else {
                        message = new StringBuilder(textResources.get(MultiTagsSelectionRes.Key.RemoveTagsPrefix)).
                                append(" ").append(names.size()).append(" ").
                                append(textResources.get(MultiTagsSelectionRes.Key.RemoveTagsPostfix)).append(":");
                        for (String name : names) {
                            message.append("\n- ").append(name);
                        }
                        title = textResources.get(MultiTagsSelectionRes.Key.RemoveTagsTitle);
                    }
                    int result = Messages.showYesNoDialog(content,
                            message.toString(), title, Messages.getQuestionIcon());
                    if (result == Messages.OK) {
                        tagsListContent.removeAll(names);
                        saveConfiguration();
                        tagsList.requestFocusInWindow();
                    }
                }
            };

            removeAction.putValue(Action.SHORT_DESCRIPTION, textResources.get(MultiTagsSelectionRes.Key.RemoveTagName)+
                    " [Delete]");
            removeAction.putValue(Action.SMALL_ICON, Resources.getDeleteTagIcon());
        }
        return removeAction;
    }

    private void setRemoveActionState() {
        getRemoveAction().setEnabled(!tagsSelection.isEmpty());
    }

    public Collection<String> getTagNames() {
        return new ArrayList<String>(tagsSelection);
    }

    public EventList<String> getTagSelection() {
        return tagsSelection;
    }

    public Collection<String> getAvailableTagNames() {
        return new ArrayList<String>(tagsListContent);
    }

    public void registerActionAccelerators(JComponent target) {
        target.getActionMap().put("add.new", getAddNewAction());
        target.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(addNewKeyStroke, "add.new");
        target.getActionMap().put("add.existing", getAddExistingAction());
        target.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(addExistingKeyStroke, "add.existing");
        target.getActionMap().put("remove", getRemoveAction());
        target.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(removeKeyStroke, "remove");
    }

    public void enableExistingTagsSelection(ExistingTagsProvider existingTagsProvider) {
        this.existingTagsProvider = existingTagsProvider;
        boolean state = this.existingTagsProvider != null;
        addExistingButton.setVisible(state);
        getAddExistingAction().setEnabled(state);
        content.revalidate();
        content.repaint();
    }

    public void setConfiguration(TagsConfiguration configuration) {
        this.configuration = configuration;
        tagsListContent.addAll(configuration.getAvailable());
        ListSelectionModel selectionModel = tagsList.getSelectionModel();
        for (String selected : configuration.getSelected()) {
            int index = tagsListContent.indexOf(selected);
            selectionModel.addSelectionInterval(index, index);
        }
        setRemoveActionState();
    }

    private void saveConfiguration() {
        if (configuration != null) {
            configuration.setAvailable(getAvailableTagNames());
        }
    }

    public JComponent getComponent() {
        return content;
    }

    public JComponent getPreferredFocusedComponent() {
        return tagsList;
    }

    public void addTagListSelectionListener(ListSelectionListener listener) {
        tagsList.getSelectionModel().addListSelectionListener(listener);
    }

    public void removeTagListSelectionListener(ListSelectionListener listener) {
        tagsList.getSelectionModel().removeListSelectionListener(listener);
    }
}
