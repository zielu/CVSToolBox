package org.cvstoolbox.multitag.config;

import org.cvstoolbox.multitag.MultiTagConfiguration;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Łukasz Zieliński
 */
public class TagsConfig {

    public static TagsConfiguration adaptAsTags(final MultiTagConfiguration config) {
        return new TagsConfiguration() {
            @Override
            public Collection<String> getAvailable() {
                return new ArrayList<String>(config.availableTags);
            }

            @Override
            public Collection<String> getSelected() {
                return new ArrayList<String>(config.selectedTags);
            }

            @Override
            public void setAvailable(Collection<String> available) {
                config.setAvailableTags(available);
            }
        };
    }

    public static TagsConfiguration adaptAsBranches(final MultiTagConfiguration config) {
        return new TagsConfiguration() {
            @Override
            public Collection<String> getAvailable() {
                return new ArrayList<String>(config.availableBranches);
            }

            @Override
            public Collection<String> getSelected() {
                return new ArrayList<String>(config.selectedBranches);
            }

            @Override
            public void setAvailable(Collection<String> available) {
                config.setAvailableBranches(available);
            }
        };
    }
}
