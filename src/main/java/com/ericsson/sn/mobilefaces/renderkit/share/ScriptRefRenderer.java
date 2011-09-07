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
package com.ericsson.sn.mobilefaces.renderkit.share;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

import com.ericsson.sn.mobilefaces.script.ScriptRenderer;
import com.ericsson.sn.mobilefaces.script.ScriptRenderFactory;

/**
 * <p>
 *      ScriptRefRenderer is a JSF renderer for generating script
 *      import reference by calling current ScriptRenderer.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Output"
 *                  renderer-type="com.ericsson.sn.mobilefaces.ScriptRef"
 *                  description="ScriptRefRenderer is the renderer for script import reference."
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public class ScriptRefRenderer extends BasicRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public ScriptRefRenderer() {
        super(LangConstant.HTML);
    }

    /**
     * See also
     * <a href="http://java.sun.com/javaee/javaserverfaces/1.2/docs/api/javax/faces/render/Renderer.html#encodeBegin(javax.faces.context.FacesContext, javax.faces.component.UIComponent)">javax.faces.render.Renderer</a>
     * @see javax.faces.render.Renderer#encodeBegin(FacesContext context, UIComponent component)
     */
    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
    throws IOException {
        super.encodeBegin(context, component);
        // get the script renderer to render the reference
        ScriptRenderer sr = ScriptRenderFactory.getScriptRenderer();
        sr.encodeScriptHead(context, component);
    }
}
