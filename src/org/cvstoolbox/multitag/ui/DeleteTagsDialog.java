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

/*
 * @(#) $Id:  $
 */
package org.cvstoolbox.multitag.ui;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.ListSelection;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.UniqueList;
import ca.odell.glazedlists.swing.EventListModel;
import ca.odell.glazedlists.swing.EventSelectionModel;
import com.intellij.cvsSupport2.cvsoperations.cvsTagOrBranch.ui.CvsTagDialog;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBList;
import net.miginfocom.swing.MigLayout;
import org.cvstoolbox.multitag.MultiTagConfiguration;
import org.cvstoolbox.util.StringComparator;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Łukasz Zieliński
 */
public class DeleteTagsDialog extends CvsTagDialog {
    private final Collection<String> availableTags;
    private JPanel panel;
    private EventList<String> tagsListContent;
    private JBList tagsList;
    private JScrollPane tagsListScroll;
    private EventList<String> tagsSelection;

    public DeleteTagsDialog(Collection<String> availableTags) {
        this.availableTags = availableTags;

        setTitle("Delete tags");
        panel = new JPanel(new MigLayout("fill, insets 0 0 0 0"));
        tagsListContent = new SortedList<String>(new UniqueList<String>(
                new BasicEventList<String>()), new StringComparator(true));
        tagsListContent.addAll(availableTags);
        tagsList = new JBList(new EventListModel<String>(tagsListContent));
        EventSelectionModel<String> tagsSelectionModel = new EventSelectionModel<String>(tagsListContent);
        tagsSelection = tagsSelectionModel.getSelected();
        tagsList.setSelectionModel(tagsSelectionModel);
        tagsList.setSelectionMode(ListSelection.MULTIPLE_INTERVAL_SELECTION_DEFENSIVE);
        tagsList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setOkStatus();
            }
        });
        tagsListScroll = ScrollPaneFactory.createScrollPane(tagsList);
        panel.add(tagsListScroll, "growx, pushx, growy, pushy, spanx");
        init();
        setOkStatus();
    }

    private void setOkStatus() {
        boolean noSelection = getTagNames().isEmpty();
        setOKActionEnabled(!noSelection);
    }

    public void setConfiguration(MultiTagConfiguration configuration) {
        ListSelectionModel selectionModel = tagsList.getSelectionModel();
        for (String deleted : configuration.deletedTags) {
            int index = tagsListContent.indexOf(deleted);
            if (index > -1) {
                selectionModel.addSelectionInterval(index, index);
            }
        }
    }

    public Collection<String> getTagNames() {
        return new ArrayList<String>(tagsSelection);
    }

    @Override
    protected JComponent createCenterPanel() {
        return panel;
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return tagsList;
    }

    @Override
    public boolean tagFieldIsActive() {
        return true;
    }
}
