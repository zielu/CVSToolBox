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

package org.cvstoolbox.filter;

import java.util.Arrays;
import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.intellij.cvsSupport2.CvsUtil;
import com.intellij.cvsSupport2.CvsVcs2;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vcs.ProjectLevelVcsManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Łukasz Zieliński
 */
public class Filters {
    private static final Logger LOG = Logger.getInstance(Filters.class.getName());

    public static Predicate<VirtualFile> keepNonEmptyDirectoriesAndFiles() {
        return new Predicate<VirtualFile>() {
            @Override
            public boolean apply(@Nullable VirtualFile file) {
                if (file != null) {
                    if (file.isDirectory()) {
                        return file.getChildren().length == 1 && file.getChildren()[0].isDirectory() &&
                            CvsUtil.CVS.equals(file.getChildren()[0].getName());
                    }
                    return true;
                }
                return false;
            }
        };
    }

    public static Predicate<VirtualFile> fileUnderCvsVcs(@NotNull final Project project) {
        return new Predicate<VirtualFile>() {
            private ProjectLevelVcsManager vcsManager = ProjectLevelVcsManager.getInstance(project);
            
            @Override
            public boolean apply(@Nullable VirtualFile file) {
                if (file != null) {
                    boolean result;
                    AbstractVcs vcs = vcsManager.getVcsFor(file);
                    if (vcs != null) {
                        String vcsName = vcs.getName();
                        if (CvsVcs2.getKey().getName().equals(vcsName)) {
                            result = true;
                        } else {
                            result = false;
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Not under CVS ["+vcsName+"]: "+file.getUrl());    
                            }                            
                        }
                    } else {
                        result = false;
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Not under any VCS: "+file.getUrl());
                        }
                    }
                    return result;
                }
                return false;
            }
        };
    } 

    public static Predicate<VirtualFile> fileLocallyAdded() {
        return new Predicate<VirtualFile>() {
            @Override
            public boolean apply(@Nullable VirtualFile file) {
                if (file != null) {
                    return CvsUtil.fileIsLocallyAdded(file);
                } else {
                    return false;
                }
            }
        };
    }
    
    public static Predicate<VirtualFile> fileExistsInCvs() {
        return new Predicate<VirtualFile>() {
            @Override
            public boolean apply(@Nullable VirtualFile file) {
                if (file != null) {
                    boolean result = CvsUtil.fileExistsInCvs(file);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Exists in CVS ["+result+"]: "+file.getUrl());
                    }
                    return result;
                } else {
                    return false;
                }
            }
        };
    }
    
    public static Predicate<VirtualFile> fileLocallyDeleted() {
        return new Predicate<VirtualFile>() {
            @Override
            public boolean apply(@Nullable VirtualFile file) {
                if (file != null) {
                    boolean result = CvsUtil.fileIsLocallyRemoved(file);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Locally deleted ["+result+"]: "+file.getUrl());
                    }
                    return result;    
                } else {
                    return false;
                }
            }
        };
    }
    
    public static VirtualFile[] pruneEmptyDirectories(VirtualFile[] toPrune) {
        return pruneEmptyDirectories(Arrays.asList(toPrune));
    }
           
    public static VirtualFile[] pruneEmptyDirectories(Collection<VirtualFile> toPrune) {
        return Lists.newArrayList(Iterables.filter(toPrune, keepNonEmptyDirectoriesAndFiles())).toArray(new VirtualFile[toPrune.size()]);        
    }
    
    public static VirtualFile[] pruneNotUnderCvs(Project project, VirtualFile[] toCheck) {
        if (toCheck.length > 0) {
            return Lists.newArrayList(Iterables.filter(Arrays.asList(toCheck), fileUnderCvsVcs(project))).toArray(new VirtualFile[toCheck.length]);                       
        } else {
            return toCheck;
        }
    }
}
