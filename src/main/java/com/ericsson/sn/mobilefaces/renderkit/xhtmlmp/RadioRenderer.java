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

import javax.faces.model.SelectItem;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.script.ScriptRenderFactory;

/**
 * <p>
 *      RadioRenderer is a renderer to render
 *      <code>UISelectOne<code> or <code>UISelectMany<code>
 *      as a list of radio buttons on XHTML-MP page.
 * </p>
 *
 * <p>
 *      This Radio has similar layout as HTML radio
 *      with &lt;select&gt tag.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.SelectOne"
 *                  renderer-type="javax.faces.Radio"
 *                  description="RadioRenderer is the renderer for a list of radio buttons on XHTML-MP page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class RadioRenderer extends CheckboxListRenderer {

    /**
     * <p>rewrite this methods to render option item with radio layout</p>
     * @param context FacesContext
     * @param component UIComponent
     * @param curItem current radio item
     * @throws java.io.IOException Failed to write items
     */
    @Override
    protected void renderOption(FacesContext context, UIComponent component, SelectItem curItem)
    throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        // render the item as plain text if disabled
        if (renderDisabledItem( writer, component, curItem)) return;

        UISelectOne selectOne = (UISelectOne) component;
        // get current values
        Object curValue = selectOne.getSubmittedValue();
        if (curValue == null) {
            curValue = selectOne.getValue();
        }

        // write the lable of this item
        writer.startElement(LangConstant.TAG_LABEL, component);
        // write style class attribute
        String labelClass = (String) component.getAttributes().get("enabledClass");
        if (labelClass != null) {
            writer.writeAttribute(LangConstant.ATT_CLASS, labelClass, "labelClass");
        }
        // write the radio
        writer.startElement(LangConstant.TAG_INPUT, component);
        // set the radio layout
        writer.writeAttribute(LangConstant.ATT_TYPE, "radio", "type");
        // write the radio state
        if (curItem.getValue() != null && curItem.getValue().equals(curValue)) {
            writer.writeAttribute(LangConstant.ATT_CHECKED, Boolean.TRUE, null);
        }
        // write the name and value of this item
        writer.writeAttribute(LangConstant.ATT_NAME, component.getClientId(context), "clientId");
        writer.writeAttribute(LangConstant.ATT_VALUE, getFormattedValue(context, component, curItem.getValue()), "value");
        // pass simple attributes
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP);
        // write script with script renderer
        ScriptRenderFactory.getScriptRenderer().encodeScript(context, component);
        writer.endElement(LangConstant.TAG_INPUT);
        // write the lable if need
        String itemLabel = curItem.getLabel();
        if (itemLabel != null) {
            writer.writeText(" ", null);
            writer.writeText(itemLabel, "label");
        }
        writer.endElement(LangConstant.TAG_LABEL);
    }
}
