/* 
 * @(#) $Id:  $
 */
package org.cvstoolbox.addToCvs;

import com.intellij.cvsSupport2.connections.CvsEnvironment;
import com.intellij.cvsSupport2.cvsoperations.common.CvsExecutionEnvironment;
import com.intellij.cvsSupport2.cvsoperations.common.CvsOperation;
import com.intellij.cvsSupport2.errorHandling.CannotFindCvsRootException;
import com.intellij.openapi.vcs.VcsException;
import org.jetbrains.annotations.NotNull;
import org.netbeans.lib.cvsclient.command.CommandAbortedException;

import java.util.Collection;

/**
 * <p></p>
 * <br/>
 * <p>Created on 17.04.13</p>
 *
 * @author Lukasz Zielinski
 */
public class AddOperation extends CvsOperation {
    private final AdditionDetails details;

    public AddOperation(AdditionDetails details) {
        this.details = details;
    }

    @Override
    public void execute(CvsExecutionEnvironment executionEnvironment, boolean underReadAction) throws VcsException, CommandAbortedException {
        //TODO: auto-generated method implementation
    }

    @Override
    public void appendSelfCvsRootProvider(@NotNull Collection<CvsEnvironment> roots) throws CannotFindCvsRootException {
        
    }

    @Override
    public String getLastProcessedCvsRoot() {
        return null;  //TODO: auto-generated method implementation
    }
}
