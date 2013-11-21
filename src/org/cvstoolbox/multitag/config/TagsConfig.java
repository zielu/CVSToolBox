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

package org.cvstoolbox.multitag.config;

import org.cvstoolbox.multitag.MultiTagConfiguration;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Łukasz Zieliński
 */
public class TagsConfig {

    public static TagsConfiguration adaptAsTags(final MultiTagConfiguration config) {
        return new TagsConfiguration() {
            @Override
            public Collection<String> getAvailable() {
                return new ArrayList<String>(config.availableTags);
            }

            @Override
            public Collection<String> getSelected() {
                return new ArrayList<String>(config.selectedTags);
            }

            @Override
            public void setAvailable(Collection<String> available) {
                config.setAvailableTags(available);
            }
        };
    }

    public static TagsConfiguration adaptAsBranches(final MultiTagConfiguration config) {
        return new TagsConfiguration() {
            @Override
            public Collection<String> getAvailable() {
                return new ArrayList<String>(config.availableBranches);
            }

            @Override
            public Collection<String> getSelected() {
                return new ArrayList<String>(config.selectedBranches);
            }

            @Override
            public void setAvailable(Collection<String> available) {
                config.setAvailableBranches(available);
            }
        };
    }
}
