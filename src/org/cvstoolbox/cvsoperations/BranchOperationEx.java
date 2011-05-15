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

package org.cvstoolbox.cvsoperations;

import com.intellij.cvsSupport2.connections.CvsRootProvider;
import com.intellij.cvsSupport2.cvsoperations.common.CvsExecutionEnvironment;
import com.intellij.cvsSupport2.cvsoperations.cvsTagOrBranch.BranchOperation;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vfs.VirtualFile;
import org.netbeans.lib.cvsclient.command.Command;
import org.netbeans.lib.cvsclient.command.tag.TagCommand;

/**
 * @author Łukasz Zieliński
 */
public class BranchOperationEx extends BranchOperation {
    private final boolean allowMoveDeleteBranchTag;

    public BranchOperationEx(VirtualFile[] files, String branchName,
                           boolean overrideExisting, boolean isTag) {
        this(files, branchName, overrideExisting, isTag, false);
    }

    public BranchOperationEx(VirtualFile[] files, String branchName,
                           boolean overrideExisting, boolean isTag, boolean allowMoveDeleteBranchTag) {
        super(new FilePath[0], branchName, overrideExisting, isTag);
        this.allowMoveDeleteBranchTag = allowMoveDeleteBranchTag;
        addFiles(files);
    }

    public BranchOperationEx(FilePath[] files, String branchName,
                           boolean overrideExisting, boolean isTag, boolean allowMoveDeleteBranchTag) {
        super(files, branchName, overrideExisting, isTag);
        this.allowMoveDeleteBranchTag = allowMoveDeleteBranchTag;
    }

    protected Command createCommand(CvsRootProvider root, CvsExecutionEnvironment cvsExecutionEnvironment) {
        TagCommand result = (TagCommand) super.createCommand(root, cvsExecutionEnvironment);
        if (allowMoveDeleteBranchTag) {
            result.setAllowMoveDeleteBranchTag(allowMoveDeleteBranchTag);
        }
        return result;
    }
}
