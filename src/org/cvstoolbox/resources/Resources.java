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

package org.cvstoolbox.resources;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.net.URL;

/**
 * @author Łukasz Zieliński
 */
public class Resources {

    private static URL getIconURL(String name) {
        return Resources.class.getResource(name);
    }

    public static Icon getTagIcon() {
        return new ImageIcon(getIconURL("tag_blue.png"));
    }

    public static Icon getAddTagIcon() {
        return new ImageIcon(getIconURL("tag_blue_add.png"));
    }

    public static Icon getDeleteTagIcon() {
        return new ImageIcon(getIconURL("tag_blue_delete.png"));
    }

    public static Icon getEditTagIcon() {
        return new ImageIcon(getIconURL("tag_blue_edit.png"));
    }

    public static Icon getCrossIcon() {
        return new ImageIcon(getIconURL("cross.png"));
    }
}
