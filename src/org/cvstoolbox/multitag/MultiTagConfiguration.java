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

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Łukasz Zieliński
 */
@State(
  name="MultiTagConfiguration",
  storages= {
    @Storage(
      id="other",
      file = "$WORKSPACE_FILE$"
    )}
)
public class MultiTagConfiguration implements PersistentStateComponent<MultiTagConfiguration> {
    public List<String> availableTags = new ArrayList<String>();
    public List<String> selectedTags = new ArrayList<String>();

    public List<String> availableBranches = new ArrayList<String>();
    public List<String> selectedBranches = new ArrayList<String>();

    public boolean TAG_AFTER_PROJECT_COMMIT = false;
    public boolean OVERRIDE_EXISTING_TAG_FOR_PROJECT = false;

    public List<String> deletedTags = new ArrayList<String>();

    @Override
    public MultiTagConfiguration getState() {
        return this;
    }

    public void setAvailableTags(Collection<String> availableTags) {
        this.availableTags = new ArrayList<String>(availableTags);
    }

    public void setSelectedTags(Collection<String> selectedTags) {
        this.selectedTags = new ArrayList<String>(selectedTags);
    }

    public void setAvailableBranches(Collection<String> availableBranches) {
        this.availableBranches = new ArrayList<String>(availableBranches);
    }

    public void setSelectedBranches(Collection<String> selectedBranches) {
        this.selectedBranches = new ArrayList<String>(selectedBranches);
    }

    public void setDeletedTags(Collection<String> deletedTags) {
        this.deletedTags = new ArrayList<String>(deletedTags);
    }

    @Override
    public void loadState(MultiTagConfiguration state) {
        setAvailableTags(state.availableTags);
        setSelectedTags(state.selectedTags);
        setAvailableBranches(state.availableBranches);
        setSelectedBranches(state.selectedBranches);
        TAG_AFTER_PROJECT_COMMIT = state.TAG_AFTER_PROJECT_COMMIT;
        OVERRIDE_EXISTING_TAG_FOR_PROJECT = state.OVERRIDE_EXISTING_TAG_FOR_PROJECT;
        setDeletedTags(state.deletedTags);
    }
}
