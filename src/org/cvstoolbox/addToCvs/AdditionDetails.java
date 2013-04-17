/* 
 * @(#) $Id:  $
 */
package org.cvstoolbox.addToCvs;

import com.intellij.cvsSupport2.connections.CvsEnvironment;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * <p></p>
 * <br/>
 * <p>Created on 17.04.13</p>
 *
 * @author Lukasz Zielinski
 */
public class AdditionDetails {
    private final File myBaseImportDirectory;
    private final String myModuleName;
    private final CvsEnvironment myEnvironment;

    public AdditionDetails(@NotNull File myBaseImportDirectory, String myModuleName, CvsEnvironment myEnvironment) {
        this.myBaseImportDirectory = myBaseImportDirectory;
        this.myModuleName = myModuleName;
        this.myEnvironment = myEnvironment;
    }
}
