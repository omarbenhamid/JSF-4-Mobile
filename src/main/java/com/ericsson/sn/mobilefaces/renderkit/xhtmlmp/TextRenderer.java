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

import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.script.ScriptRenderFactory;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicInputRenderer;

/**
 * <p>
 *      TextRenderer is a renderer to render a plain text abd input field on XHTML-MP page.
 * </p>
 *
 * <p>
 *      This Text is rendered as plain text or &lt;span&gt tag and &lt;input&gt .
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Output"
 *                  renderer-type="javax.faces.Text"
 *                  description="TextRenderer is the renderer for a plain text on XHTML-MP page"
 * @jsf.render-kit  component-family="javax.faces.Input"
 *                  renderer-type="javax.faces.Text"
 *                  description="TextRenderer is the renderer for a input field on XHTML-MP page"
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public class TextRenderer extends BasicInputRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public TextRenderer() {
        super(LangConstant.XHTML_MP);
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
        ResponseWriter writer = context.getResponseWriter();
        // get current value
        String value = getCurrentValue( context, component) ;
        if (value ==null) value = "";

        if (component instanceof UIInput) {
            // encode as plain text if the input field is disabled
            if (encodeDisableComponent(context, component, value, LangConstant.PASS_THROUGH_ATTR_XHTML_MP, true)) return;
            // write <input>
            writer.startElement(LangConstant.TAG_INPUT, component);
            // write id
            writeID(context, component, true, null);
            writer.writeAttribute(LangConstant.ATT_TYPE, LangConstant.VALUE_TEXT, "type");
            // write a default value
            writer.writeAttribute(LangConstant.ATT_VALUE, value, "value");
            // pass simple attributes
            writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP);
            // write script with script renderer
            ScriptRenderFactory.getScriptRenderer().encodeScript(context, component);
            writer.endElement(LangConstant.TAG_INPUT);
        } else if (component instanceof UIOutput) {
            // encode the output plain text
            if (hasPassAttributes(component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP) ||
                    super.needWriteID(component)) {
                // write <span> if need
                writer.startElement(LangConstant.TAG_SPAN, component);
                // write attributes for this text
                writeID(context, component, false, null);
                writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP);
                ScriptRenderFactory.getScriptRenderer().encodeScript(context, component);
            }
            if (value.length()>0) {
                // write the text with escape setting
                boolean escape = getBooleanValue(component.getAttributes().get("escape"), "true");
                if (escape) {
                    writer.writeText(value, "value");
                } else {
                    writer.write(value);
                }
            }
            // write </span> if need
            if (hasPassAttributes(component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP) ||
                    super.needWriteID(component)) {
                writer.endElement(LangConstant.TAG_SPAN);
            }
        }
    }
}
