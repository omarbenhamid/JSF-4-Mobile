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

import java.util.Map;
import java.io.IOException;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.script.ScriptRenderFactory;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicRenderer;

/**
 * <p>
 *      ButtonRenderer is a renderer to render the <code>UICommand</code>
 *      as a Button on XHTML-MP page.
 * </p>
 *
 * <p>
 *      This Button has similar layout as HTML button with &lt;input&gt;
 *      tag.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Command"
 *                  renderer-type="javax.faces.Button"
 *                  description="ButtonRenderer is the renderer for UICommand component on XHTML-MP page"
 *
 * @author Daning Yang
 * @version 1.1
 */
public class ButtonRenderer extends BasicRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public ButtonRenderer() {
        super(LangConstant.XHTML_MP);
    }

    /**
     * See also
     * <a href="http://java.sun.com/javaee/javaserverfaces/1.2/docs/api/javax/faces/render/Renderer.html#decode(FacesContext context, UIComponent component)">javax.faces.render.Renderer</a>
     * @see javax.faces.render.Renderer#decode(FacesContext context, UIComponent component)
     */
    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode(context, component);
        String id = component.getClientId(context);
        Map requestParam = context.getExternalContext().getRequestParameterMap();
        if (requestParam.get(id) == null) return;
        String type = (String) component.getAttributes().get("type");
        if ((type != null) && (type.toLowerCase().equals("reset"))) {
            return;
        }
        addActionEvent(component);
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
        ResponseWriter writer = context.getResponseWriter();
        String label = (String)((UICommand) component).getValue();
        String type = (String) component.getAttributes().get("type");
        if (label == null) label = "";
        if (encodeDisableComponent(context, component, label, LangConstant.PASS_THROUGH_ATTR_XHTML_MP, true)) return;
        if (type == null || !type.equalsIgnoreCase(LangConstant.VALUE_RESET)) {
            type = LangConstant.VALUE_SUBMIT;
            component.getAttributes().put("type", type);
        }
        writer.startElement(LangConstant.TAG_INPUT, component);
        writeID(context, component, true, null);
        writer.writeAttribute(LangConstant.ATT_TYPE, type.toLowerCase(), "type");
        if (!label.equals(""))
            writer.writeAttribute(LangConstant.ATT_VALUE, label, "value");
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP);
        ScriptRenderFactory.getScriptRenderer().encodeScript(context, component);
        writer.endElement(LangConstant.TAG_INPUT);
    }
}
