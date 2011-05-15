package org.cvstoolbox.multitag.config;

import java.util.Collection;

/**
 * @author Łukasz Zieliński
 */
public interface TagsConfiguration {
    Collection<String> getAvailable();
    Collection<String> getSelected();

    void setAvailable(Collection<String> available);
}
