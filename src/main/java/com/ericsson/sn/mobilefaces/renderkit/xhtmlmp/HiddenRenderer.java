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
package com.ericsson.sn.mobilefaces.renderkit.xhtmlmp;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicInputRenderer;

/**
 * <p>
 *      HiddenRenderer is a renderer to render the <code>UIInput</code>
 *      as a hidden field of form on XHTML-MP page.
 * </p>
 *
 * <p>
 *      This HiddenRenderer render the field
 *      with &lt;input&gt tag.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Input"
 *                  renderer-type="javax.faces.Hidden"
 *                  description="HiddenRenderer is the renderer for a hidden field on XHTML-MP page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class HiddenRenderer extends BasicInputRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public HiddenRenderer() {
        super(LangConstant.XHTML_MP);
    }

    /**
     * See also
     * <a href="http://java.sun.com/javaee/javaserverfaces/1.2/docs/api/javax/faces/render/Renderer.html#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)">javax.faces.render.Renderer</a>
     * @see javax.faces.render.Renderer#encodeEnd(FacesContext context, UIComponent component)
     */
    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
    throws IOException{
        super.encodeEnd( context, component);

        ResponseWriter writer = context.getResponseWriter();
        // get the field value
        String value = getCurrentValue( context, component) ;
        String clientId = component.getClientId(context);
        // write <input> tag
        writer.startElement(LangConstant.TAG_INPUT, component);
        // write id
        writeID(context, component, true, null);
        // write hidden type
        writer.writeAttribute(LangConstant.ATT_TYPE, LangConstant.VALUE_HIDDEN, LangConstant.ATT_TYPE);
        // write the value
        if (value != null) {
            writer.writeAttribute(LangConstant.ATT_VALUE, value, "value");
        }
        writer.endElement(LangConstant.TAG_INPUT);
        // add the field to the form
        FormRenderer.addHiddenField(context, clientId, true);
    }
}
