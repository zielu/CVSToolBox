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
