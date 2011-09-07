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

import java.util.Map;
import java.io.IOException;

import javax.faces.component.UIInput;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;

import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicInputRenderer;

/**
 * <p>
 *      CheckboxRenderer is a renderer to render the <code>UISelectBoolean</code>
 *      as a single Checkbox on WML page.
 * </p>
 *
 * <p>
 *      This Checkbox is rendered
 *      with &lt;select&gt tag.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.SelectBoolean"
 *                  renderer-type="javax.faces.Checkbox"
 *                  description="CheckboxRenderer is the renderer for a single Checkbox on WML page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class CheckboxRenderer extends BasicInputRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public CheckboxRenderer() {
        super(LangConstant.WML);
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

        UIInput uiInput = (UIInput) component;
        Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
        String newValue = (String) requestParameterMap.get(clientId);

        if (newValue == null) {
            newValue = "false";
        } else if (newValue.equalsIgnoreCase(OPTION_NAME)){
            newValue = "true";
        }
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
        // get current checkbox state
        String value = getCurrentValue( context, component) ;
        String id = translateID(component.getClientId(context));

        writer.startElement(LangConstant.TAG_SELECT, component);
        writeID(context, component, true, id);
        if (value != null && value.equals("true")) {
            writer.writeAttribute(LangConstant.ATT_IVALUE, OPTION_NAME, "ivalue");
        }
        writer.writeAttribute(LangConstant.ATT_MULTIPLE, "true", "multiple");
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_WML);
        writeWMLMenuScript( context, component);
        writer.startElement(LangConstant.TAG_OPTION, component);
        writer.writeAttribute(LangConstant.ATT_VALUE, OPTION_NAME, "value");
        writer.endElement(LangConstant.TAG_OPTION);
        writer.endElement(LangConstant.TAG_SELECT);
        getFormValueHolder(context, component).setFormValue(component.getClientId(context), "$(" + id + ")");
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue)
    throws ConverterException {
        String newValue = (String) submittedValue;
        Object convertedValue = Boolean.valueOf(newValue);
        return convertedValue;
    }

    private static final String OPTION_NAME = "default_option";
}
