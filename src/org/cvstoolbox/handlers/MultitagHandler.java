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

package org.cvstoolbox.handlers;

import com.intellij.CvsBundle;
import com.intellij.cvsSupport2.CvsUtil;
import com.intellij.cvsSupport2.cvshandlers.CommandCvsHandler;
import com.intellij.cvsSupport2.cvshandlers.CvsHandler;
import com.intellij.cvsSupport2.cvshandlers.FileSetToBeUpdated;
import com.intellij.cvsSupport2.cvsoperations.common.CompositeOperaton;
import com.intellij.cvsSupport2.cvsoperations.cvsTagOrBranch.BranchOperation;
import com.intellij.cvsSupport2.cvsoperations.cvsUpdate.UpdateOperation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vfs.VirtualFile;
import org.cvstoolbox.cvsoperations.BranchOperationEx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Łukasz Zieliński
 */
public class MultitagHandler {

    public static CvsHandler createTagsHandler(FilePath[] selectedFiles, Collection<String> tagNames,
                                               boolean switchToThisAction, String switchToTag,
                                               boolean overrideExisting,
                                               boolean makeNewFilesReadOnly, Project project) {
        CompositeOperaton operation = new CompositeOperaton();
        for (String tagName : tagNames) {
            operation.addOperation(new BranchOperation(selectedFiles, tagName, overrideExisting, true));
        }
        if (switchToThisAction) {
            operation.addOperation(new UpdateOperation(selectedFiles, switchToTag, makeNewFilesReadOnly, project));
        }
        return new CommandCvsHandler(CvsBundle.message("operation.name.create.tag"),
                operation,
                FileSetToBeUpdated.selectedFiles(selectedFiles));
    }

    public static CvsHandler createBranchesHandler(FilePath[] selectedFiles, String branchName,
                                                   boolean switchToThisAction, boolean overrideExisting,
                                                   boolean makeNewFilesReadOnly, Project project) {
        CompositeOperaton operation = new CompositeOperaton();
        boolean allowMoveDelete = overrideExisting;
        operation.addOperation(new BranchOperationEx(selectedFiles, branchName, overrideExisting, false, allowMoveDelete));
        if (switchToThisAction) {
            operation.addOperation(new UpdateOperation(selectedFiles, branchName, makeNewFilesReadOnly, project));
        }
        return new CommandCvsHandler(CvsBundle.message("operation.name.create.branch"), operation,
                FileSetToBeUpdated.selectedFiles(selectedFiles));
    }

    public static CvsHandler createTagsHandler(VirtualFile[] selectedFiles, Collection<String> tagNames,
                                               boolean overrideExisting,
                                               boolean makeNewFilesReadOnly, Project project) {
        if (selectedFiles.length > 0) {
            CompositeOperaton operation = new CompositeOperaton();
            for (String tagName : tagNames) {
                operation.addOperation(new BranchOperationEx(selectedFiles, tagName, overrideExisting, true));
            }
            return new CommandCvsHandler(CvsBundle.message("operation.name.create.tag"),
                    operation,
                    FileSetToBeUpdated.selectedFiles(selectedFiles));
        } else {
            return null;
        }
    }
}
