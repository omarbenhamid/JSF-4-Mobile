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

import java.util.Map;

import javax.faces.component.UIInput;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.ericsson.sn.mobilefaces.util.RenderUtils;

/**
 * <p>
 *      BasicInputRenderer implements the decode and
 *      convert value method for most input based Renderers.
 * </p>
 *
 *
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public abstract class BasicInputRenderer extends BasicRenderer {

    /**
     * <p>Create a instance for input renderers.</p>
     * @param markup markup language type for constants
     */
    public BasicInputRenderer(String markup) {
        super(markup);
    }

    /**
     * <p>Decode the request to handle the submit value.</p>
     * @param context FacesContext
     * @param component UIComponent
     */
    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode( context, component);
        // get request parameters
        String id = component.getClientId(context);
        Map requestParam = context.getExternalContext().getRequestParameterMap();
        if (id != null && requestParam.containsKey(id)) {
            // submit the input value
            submitValue(component, requestParam.get(id));
        }
    }

    /**
     * <p>Get current value for the input compoment. The value may need be formatted.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @return current formatted value
     */
    protected String getCurrentValue(FacesContext context, UIComponent component) {
        String value = null;
        // try to read submitted value
        if (component instanceof UIInput) {
            value = (String)((UIInput) component).getSubmittedValue();
        }
        // if cannot read value, try to find formatted value
        if (value == null) {
            value = getFormattedValue(context, component, getHolderValue(component));
        }
        return value;
    }

    /**
     * <p>Format the current value if necessary.</p>
     *
     * @param context FacesContext
     * @param component UIComponent
     * @param value current value which need do format
     * @return formatted value
     * @throws javax.faces.convert.ConverterException  except if cannot do convert
     */
    protected String getFormattedValue(FacesContext context, UIComponent component, Object value)
    throws ConverterException {
        if(value == null) return "";

        if (component instanceof ValueHolder) {
            // get the convertor to format value
            Converter converter = RenderUtils.getConvertor(context, component, value, false);
            if (converter != null) {
                return converter.getAsString(context, component, value);
            }
        }
        return value.toString();
    }

    /**
     * <p>Set submit value to the input component.</p>
     * @param component UIComponent
     * @param value submit value
     */
    protected void submitValue(UIComponent component, Object value) {
        if (component instanceof UIInput) {
            ((UIInput) component).setSubmittedValue(value);
        }
    }

    /**
     * <p>Convert the submit value if necessary.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @param submittedValue value which need convert
     * @throws javax.faces.convert.ConverterException except if cannot do convert
     * @return converted value
     */
    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue)
    throws ConverterException {
        if(submittedValue == null) return null;

        // get the convertor to format value
        Converter converter = RenderUtils.getConvertor(context, component, submittedValue, true);

        if (converter != null) {
            return converter.getAsObject(context, component, (String)submittedValue);
        } else {
            return submittedValue;
        }
    }

    // read the value from value holder
    private Object getHolderValue(UIComponent component) {
        if (component instanceof ValueHolder) {
            Object value = ((ValueHolder) component).getValue();
            return value;
        }
        return null;
    }
}
