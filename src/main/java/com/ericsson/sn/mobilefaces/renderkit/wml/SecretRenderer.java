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

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.util.FormValueHolder;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicInputRenderer;

/**
 * <p>
 *      SecretRenderer is a renderer to render the <code>UIInput</code>
 *      as a password input box on WML page.
 * </p>
 *
 * <p>
 *      This password input box is rendered with &lt;input&gt tag.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Input"
 *                  renderer-type="javax.faces.Secret"
 *                  description="SecretRenderer is the renderer for a password input box on WML page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class SecretRenderer extends BasicInputRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public SecretRenderer() {
        super(LangConstant.WML);
    }

    /**
     * See also
     * <a href="http://java.sun.com/javaee/javaserverfaces/1.2/docs/api/javax/faces/render/Renderer.html#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)">javax.faces.render.Renderer</a>
     * @see javax.faces.render.Renderer#encodeEnd(FacesContext context, UIComponent component)
     */
    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
    throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String value = getCurrentValue( context, component) ;
        boolean redisplay = getBooleanValue(component.getAttributes().get("redisplay"), "true");
        if (!redisplay || value == null) value = "";
        if ( encodeDisableComponent(context, component, value, LangConstant.PASS_THROUGH_ATTR_WML, false)) return;
        FormValueHolder holder = getFormValueHolder(context, component);
        String id = translateID(component.getClientId(context));
        holder.setFormValue(component.getClientId(context), "$(" + id + ")");
        writer.startElement(LangConstant.TAG_INPUT, component);
        writeID(context, component, true, id);
        writer.writeAttribute(LangConstant.ATT_TYPE, LangConstant.VALUE_PASSWORD, "type");
        writer.writeAttribute(LangConstant.ATT_VALUE, value, "value");
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_WML);
        writer.endElement(LangConstant.TAG_INPUT);
    }
}
