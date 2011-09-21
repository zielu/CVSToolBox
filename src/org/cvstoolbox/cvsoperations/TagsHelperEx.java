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
package org.cvstoolbox.cvsoperations;

import com.intellij.CvsBundle;
import com.intellij.cvsSupport2.cvsExecution.CvsOperationExecutor;
import com.intellij.cvsSupport2.cvsExecution.CvsOperationExecutorCallback;
import com.intellij.cvsSupport2.cvsExecution.ModalityContextImpl;
import com.intellij.cvsSupport2.cvshandlers.CommandCvsHandler;
import com.intellij.cvsSupport2.cvsoperations.common.CvsOperation;
import com.intellij.cvsSupport2.cvsoperations.cvsLog.LogOperation;
import com.intellij.cvsSupport2.cvsoperations.cvsTagOrBranch.BranchesProvider;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.cvsIntegration.CvsResult;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vcs.VcsException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Łukasz Zieliński
 */
public class TagsHelperEx {
    private static final Logger LOG = Logger.getInstance("#org.cvstoolbox.cvsoperations.TagsHelperEx");

    public static Collection<String> collectAllBranches(Collection<FilePath> files,
                                                         Project project,
                                                         boolean forTemporaryConfiguration) throws VcsException {
        ArrayList<String> result = new ArrayList<String>();
        if (files.isEmpty()) {
            return result;
        }
        return getBranchesProvider(new LogOperation(files), project, forTemporaryConfiguration).getAllBranches();
    }

    private static BranchesProvider getBranchesProvider(CvsOperation operation, Project project, boolean forTemporaryConfiguration) throws VcsException {
        LOG.assertTrue(operation instanceof BranchesProvider);
        CvsOperationExecutor executor = new CvsOperationExecutor(true, project,
                new ModalityContextImpl(ModalityState.defaultModalityState(),
                        forTemporaryConfiguration));
        CommandCvsHandler handler = new CommandCvsHandler(CvsBundle.message("load.tags.operation.name"), operation, true) {
            public String getCancelButtonText() {
                return CvsBundle.message("button.text.stop");
            }
        };
        executor.performActionSync(handler,
                CvsOperationExecutorCallback.EMPTY);
        CvsResult executionResult = executor.getResult();
        if (!executionResult.hasNoErrors()) {
            throw executionResult.composeError();
        }
        return (BranchesProvider) operation;
    }

}
