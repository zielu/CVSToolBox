package org.cvstoolbox.util;

import org.apache.log4j.Logger;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Łukasz Zieliński
 */
public class EDTInvoker {
    private static final Logger LOG = Logger.getLogger(EDTInvoker.class);


    public static void invokeAndWaitNoExceptions(Runnable task) {
        if (SwingUtilities.isEventDispatchThread()) {
            task.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(task);
            } catch (InterruptedException e) {
                LOG.error("InvokeAndWait failure", e);
            } catch (InvocationTargetException e) {
                LOG.error("InvokeAndWait failure", e);
            }
        }
    }
}
