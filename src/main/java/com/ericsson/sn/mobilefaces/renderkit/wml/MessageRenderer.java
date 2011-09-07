/**
 * MobileFaces is a core library based on the JavaServer(tm) Faces (JSF) architecture for extending web applications to mobile browsing devices.
 * Ericsson AB - Daning Yang
 *
 * 
 * Project info: https://mobilejsf.dev.java.net/
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.ericsson.sn.mobilefaces.renderkit.wml;

import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicMessageRenderer;

/**
 * <p>
 *      MessageRenderer is a renderer to render message item on WML page.
 * </p>
 *
 * <p>
 *      This MessageRenderer is rendered as plain text.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Message"
 *                  renderer-type="javax.faces.Message"
 *                  description="MessageRenderer is the renderer for a message item on WML page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class MessageRenderer extends BasicMessageRenderer{

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public MessageRenderer() {
        super(LangConstant.WML);
    }

    /**
     * <p>this is not a multi message item.</p>
     * @return false
     */
    @Override
    protected boolean isMulti() {
        return false;
    }

    /**
     * <p>this cannot use the css.</p>
     * @return false
     */
    @Override
    protected boolean hasStyle() {
        return false;
    }
}
