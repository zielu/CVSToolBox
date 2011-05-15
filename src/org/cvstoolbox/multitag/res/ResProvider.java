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

package org.cvstoolbox.multitag.res;

import com.intellij.CvsBundle;

/**
 * @author Łukasz Zieliński
 */
public class ResProvider {
    public static MultiTagsSelectionRes getTagsRes() {
        return new MultiTagsSelectionRes()
                .put(MultiTagsSelectionRes.Key.EmptyList, "Add available tags ->")
                .put(MultiTagsSelectionRes.Key.AddNewTitle, "Add new tag")
                .put(MultiTagsSelectionRes.Key.AddNewLabel, CvsBundle.message("label.tag.name"))
                .put(MultiTagsSelectionRes.Key.AddNewName, "Add new")
                .put(MultiTagsSelectionRes.Key.AddExistingName, "Add existing")
                .put(MultiTagsSelectionRes.Key.RemoveTagPrefix, "Remove tag")
                .put(MultiTagsSelectionRes.Key.RemoveTagTitle, "Remove tag")
                .put(MultiTagsSelectionRes.Key.RemoveTagsPrefix, "Remove")
                .put(MultiTagsSelectionRes.Key.RemoveTagsPostfix, "tags")
                .put(MultiTagsSelectionRes.Key.RemoveTagsTitle, "Remove tags")
                .put(MultiTagsSelectionRes.Key.RemoveTagName, "Remove")

                ;
    }

    public static MultiTagsSelectionRes getBranchesRes() {
        return new MultiTagsSelectionRes()
                .put(MultiTagsSelectionRes.Key.EmptyList, "Add available branches ->")
                .put(MultiTagsSelectionRes.Key.AddNewTitle, "Add new branch")
                .put(MultiTagsSelectionRes.Key.AddNewLabel, CvsBundle.message("label.branch.name"))
                .put(MultiTagsSelectionRes.Key.AddNewName, "Add new")
                .put(MultiTagsSelectionRes.Key.AddExistingName, "Add existing")
                .put(MultiTagsSelectionRes.Key.RemoveTagPrefix, "Remove branch")
                .put(MultiTagsSelectionRes.Key.RemoveTagTitle, "Remove branch")
                .put(MultiTagsSelectionRes.Key.RemoveTagsPrefix, "Remove")
                .put(MultiTagsSelectionRes.Key.RemoveTagsPostfix, "branches")
                .put(MultiTagsSelectionRes.Key.RemoveTagsTitle, "Remove branches")
                .put(MultiTagsSelectionRes.Key.RemoveTagName, "Remove")

                ;
    }
}
