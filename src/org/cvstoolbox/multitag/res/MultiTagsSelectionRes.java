package org.cvstoolbox.multitag.res;

import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

/**
 * @author Łukasz Zieliński
 */
public class MultiTagsSelectionRes {
    public enum Key {
        EmptyList,
        AddNewTitle,
        AddNewLabel,
        AddNewName,
        AddExistingName,
        RemoveTagTitle,
        RemoveTagPrefix,
        RemoveTagsTitle,
        RemoveTagsPrefix,
        RemoveTagsPostfix,
        RemoveTagName,

    }

    private final EnumMap<Key, String> resources = new EnumMap<Key, String>(Key.class);

    public MultiTagsSelectionRes put(@NotNull Key key, @NotNull String resource) {
        resources.put(key, resource);
        return this;
    }

    public String get(@NotNull Key key) {
        String resource = resources.get(key);
        if (resource == null) {
            throw new NullPointerException("No resource found for key: "+key);
        }
        return resource;
    }
}
