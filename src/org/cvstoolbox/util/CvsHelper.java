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

package org.cvstoolbox.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FilePath;
import com.intellij.openapi.vcs.FilePathImpl;
import com.intellij.openapi.vcs.ProjectLevelVcsManager;
import com.intellij.openapi.vcs.actions.VcsContextFactory;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author Łukasz Zieliński
 */
public class CvsHelper {

    public static Collection<FilePath> collectVcsRoots(Project project, Collection<FilePath> files) {
        Collection<FilePath> result = new HashSet<FilePath>();
        for (FilePath filePath : files) {
            VirtualFile root = ProjectLevelVcsManager.getInstance(project).getVcsRootFor(filePath);
            if (root != null) {
                result.add(VcsContextFactory.SERVICE.getInstance().createFilePathOn(root));
            }
        }
        return result;
    }

    public static FilePath[] toFilePaths(VirtualFile[] files) {
        FilePath[] paths = new FilePath[files.length];
        for (int i = 0; i < paths.length; i++) {
            paths[i] = new FilePathImpl(files[i]);
        }
        return paths;
    }
    
    public static FilePath[] toFilePaths(Iterable<VirtualFile> files) {
        List<FilePath> paths = Lists.newArrayListWithExpectedSize(100);
        for (VirtualFile file : files) {
            paths.add(new FilePathImpl(file));            
        }
        return paths.toArray(new FilePath[paths.size()]);
    }
}
