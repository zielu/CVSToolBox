/* 
 * @(#) $Id:  $
 */
package org.cvstoolbox.addToCvs;

import com.intellij.cvsSupport2.cvshandlers.CommandCvsHandler;
import com.intellij.cvsSupport2.cvshandlers.CvsHandler;

/**
 * <p></p>
 * <br/>
 * <p>Created on 17.04.13</p>
 *
 * @author Lukasz Zielinski
 */
public class AdditionHandler {
    
    public static CvsHandler createHandler(AdditionDetails details) {
        return new CommandCvsHandler("Add module to CVS", new AddOperation(details), false);
    }
}
