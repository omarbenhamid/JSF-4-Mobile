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

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;

import com.ericsson.sn.mobilefaces.script.ScriptRenderFactory;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicInputRenderer;

/**
 * <p>
 *      CheckboxRenderer is a renderer to render the <code>UISelectBoolean</code>
 *      as a single Checkbox on XHTML-MP page.
 * </p>
 *
 * <p>
 *      This Checkbox has similar layout as HTML checkbox
 *      with &lt;input&gt tag.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.SelectBoolean"
 *                  renderer-type="javax.faces.Checkbox"
 *                  description="CheckboxRenderer is the renderer for a single Checkbox on XHTML-MP page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class CheckboxRenderer extends BasicInputRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public CheckboxRenderer() {
        super(LangConstant.XHTML_MP);
    }

    /**
     * See also
     * <a href="http://java.sun.com/javaee/javaserverfaces/1.2/docs/api/javax/faces/render/Renderer.html#decode(FacesContext context, UIComponent component)">javax.faces.render.Renderer</a>
     * @see javax.faces.render.Renderer#decode(FacesContext context, UIComponent component)
     */
    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode( context, component);
        String clientId = component.getClientId(context);
        // Convert the new value
        UIInput uiInput = (UIInput) component;
        Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String newValue = (String) requestParameterMap.get(clientId);
        //check this checkbox state
        if (newValue == null) {
            newValue = "false";
        } else if (newValue.equalsIgnoreCase("on") ||
                newValue.equalsIgnoreCase("yes") ||
                newValue.equalsIgnoreCase("true")) {
            newValue = "true";
        }
        // submit the checkbox state
        submitValue(component, newValue);
    }

    /**
     * See also
     * <a href="http://java.sun.com/javaee/javaserverfaces/1.2/docs/api/javax/faces/render/Renderer.html#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)">javax.faces.render.Renderer</a>
     * @see javax.faces.render.Renderer#encodeEnd(FacesContext context, UIComponent component)
     */
    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
    throws IOException {
        super.encodeEnd(context, component);
        // do nothing if read-only
        if (isReadOnly(component)) return;

        ResponseWriter writer = context.getResponseWriter();
        String value = getCurrentValue( context, component) ;
        // write the checkbox with <input> tag
        writer.startElement(LangConstant.TAG_INPUT, component);
        // write the component it
        writeID(context, component, true, null);
        // write checkbox type
        writer.writeAttribute(LangConstant.ATT_TYPE, "checkbox", "type");
        // write checkbox state
        if (value != null && value.equals("true")) {
            writer.writeAttribute(LangConstant.ATT_CHECKED, Boolean.TRUE, "value");
        }
        // pass simple attributes
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP);
        // write script with script renderer
        ScriptRenderFactory.getScriptRenderer().encodeScript(context, component);
        writer.endElement(LangConstant.TAG_INPUT);
    }

    /**
     * <p>Overwrite getConvertedValue method for converting value.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @param submittedValue submit value for converting
     * @throws javax.faces.convert.ConverterException Failed to do the convert
     * @return converted value
     */
    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue)
    throws ConverterException {
        String newValue = (String) submittedValue;
        Object convertedValue = Boolean.valueOf(newValue);
        return convertedValue;
    }
}
