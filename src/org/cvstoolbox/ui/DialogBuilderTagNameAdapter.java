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

package org.cvstoolbox.ui;

import com.intellij.cvsSupport2.cvsoperations.cvsTagOrBranch.ui.TagNameFieldOwner;
import com.intellij.openapi.ui.DialogBuilder;

/**
 * @author Łukasz Zieliński
 */
public class DialogBuilderTagNameAdapter implements TagNameFieldOwner {
    private final DialogBuilder builder;

    public DialogBuilderTagNameAdapter(DialogBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void enableOkAction() {
        builder.setOkActionEnabled(true);
    }

    @Override
    public void disableOkAction(String errorMeesage) {
        builder.setOkActionEnabled(false);
    }

    @Override
    public boolean tagFieldIsActive() {
        return true;
    }
}
