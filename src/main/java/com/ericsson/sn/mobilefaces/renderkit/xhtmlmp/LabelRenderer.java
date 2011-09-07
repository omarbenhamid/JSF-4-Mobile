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

import com.ericsson.sn.mobilefaces.script.ScriptRenderFactory;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicInputRenderer;

/**
 * <p>
 *      LabelRenderer is a renderer to render the Label
 *      as a label for some other components on XHTML-MP page.
 * </p>
 *
 * <p>
 *      This label is rendered
 *      with &lt;label&gt tag.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Output"
 *                  renderer-type="javax.faces.Label"
 *                  description="LabelRenderer is the renderer for a label of some other components on XHTML-MP page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class LabelRenderer extends BasicInputRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public LabelRenderer() {
        super(LangConstant.XHTML_MP);
    }

    /**
     * See also
     * <a href="http://java.sun.com/javaee/javaserverfaces/1.2/docs/api/javax/faces/render/Renderer.html#encodeBegin(javax.faces.context.FacesContext, javax.faces.component.UIComponent)">javax.faces.render.Renderer</a>
     * @see javax.faces.render.Renderer#encodeBegin(FacesContext context, UIComponent component)
     */
    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
    throws IOException {
        super.encodeBegin( context, component);
        ResponseWriter writer = context.getResponseWriter();
        // get the for component
        String forValue = (String) component.getAttributes().get("for");
        UIComponent forComponent = null;
        String forClientId = null;

        // get current lable id
        if (forValue != null) {
            forComponent = getForComponent(context, forValue, component);
            if (forComponent == null) {
                forClientId = getForComponentId(context, component,  forValue);
            } else {
                forClientId = forComponent.getClientId(context);
            }
        }
        // get lable value
        String value = getCurrentValue(context, component);
        // set this label need be rendered
        component.getAttributes().put(RENDER_END_ELEMENT, "yes");
        // write the <label> tag
        writer.startElement(LangConstant.TAG_LABEL, component);
        // write id
        writeID(context, component, false, null);
        // write for id
        if (forClientId != null) {
            writer.writeAttribute(LangConstant.ATT_FOR, forClientId, "for");
        }
        // pass simple attributes
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP);
        // write script with script renderer
        ScriptRenderFactory.getScriptRenderer().encodeScript(context, component);
        // write the label value
        if (value != null && value.length()>0)
            writer.write(value);
        writer.writeText("\n", null);

        writer.flush();
    }

    /**
     * See also
     * <a href="http://java.sun.com/javaee/javaserverfaces/1.2/docs/api/javax/faces/render/Renderer.html#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)">javax.faces.render.Renderer</a>
     * @see javax.faces.render.Renderer#encodeEnd(FacesContext context, UIComponent component)
     */
    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
    throws IOException {
        super.encodeEnd( context, component);
        // if this label is rendered, write the end tag and clear the mark
        String render = (String) component.getAttributes().get(RENDER_END_ELEMENT);
        if (render != null && render.equals("yes")) {
            component.getAttributes().remove(RENDER_END_ELEMENT);
            ResponseWriter writer = context.getResponseWriter();
            writer.endElement(LangConstant.TAG_LABEL);
        }
    }

    private static final String RENDER_END_ELEMENT = "com.ericsson.sn.mobilefaces.LABEL_END_ELEMENT";
}
