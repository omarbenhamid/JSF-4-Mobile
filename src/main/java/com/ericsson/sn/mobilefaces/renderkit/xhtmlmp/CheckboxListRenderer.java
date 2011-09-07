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

import java.util.Iterator;
import java.io.IOException;

import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.script.ScriptRenderFactory;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicMenuRenderer;

/**
 * <p>
 *      CheckboxListRenderer is a renderer to render the <code>UISelectMany</code>
 *      as a Checkbox List on XHTML-MP page.
 * </p>
 *
 * <p>
 *      This Checkbox List has similar layout
 *      with a list of &lt;input&gt checkbox tag.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.SelectMany"
 *                  renderer-type="javax.faces.Checkbox"
 *                  description="CheckboxListRenderer is the renderer for CheckboxList on XHTML-MP page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class CheckboxListRenderer extends BasicMenuRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public CheckboxListRenderer() {
        super(LangConstant.XHTML_MP);
    }

    /**
     * <p>The option submit value is array or long string pattern</p>
     * @return true
     */
    @Override
    public boolean isArrayParams() {
        return true;
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

        ResponseWriter writer = context.getResponseWriter();
        String alignStr = (String) component.getAttributes().get("layout");
        if (alignStr != null) {
            PAGE_VERTICAL = alignStr.equalsIgnoreCase("pageDirection");
        } else {
            PAGE_VERTICAL = false;
        }
        writer.startElement(LangConstant.TAG_SPAN, component);
        writeID(context, component, false, null);
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP);
        ScriptRenderFactory.getScriptRenderer().encodeScript(context, component);
        Iterator items = getSelectItems(context, component);
        SelectItem curItem = null;
        while (items.hasNext()) {
            curItem = (SelectItem) items.next();
            if (curItem instanceof SelectItemGroup) {
                if (curItem.getLabel() != null) {
                    writer.startElement(LangConstant.TAG_STRONG, component);
                    writer.writeText(curItem.getLabel(), "label");
                    writer.endElement(LangConstant.TAG_STRONG);
                    writeSpace(writer, component);
                }
                SelectItem[] gourpItems = ((SelectItemGroup) curItem).getSelectItems();
                for (int i=0; i<gourpItems.length; ++i) {
                    renderOption(context, component, gourpItems[i]);
                    writeSpace(writer, component);
                }
            } else {
                renderOption(context, component, curItem);
                writeSpace(writer, component);
            }
        }
        writer.endElement(LangConstant.TAG_SPAN);
    }

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     * @param writer
     * @param component
     * @throws java.io.IOException
     */
    protected void writeSpace(ResponseWriter writer, UIComponent component)
    throws IOException {
        if (PAGE_VERTICAL) {
            //write a blank line
            writer.startElement(LangConstant.TAG_BR, component);
            writer.endElement(LangConstant.TAG_BR);
        }
        writer.writeText("\n", null);
    }

    /**
     * <p>Render a disabled item with plain text.</p>
     * @param writer ResponseWriter
     * @param component UIComponent
     * @param curItem diabled item
     * @throws java.io.IOException Failed to write tags
     * @return true if the item is disabled
     */
    protected boolean renderDisabledItem(ResponseWriter writer, UIComponent component, SelectItem curItem)
    throws IOException {
        boolean isDisable = false;
        if (curItem.isDisabled()) {
            String labelClass = (String) component.getAttributes().get("disabledClass");
            writer.startElement(LangConstant.TAG_SPAN, component);
            if (labelClass != null) {
                writer.writeAttribute(LangConstant.ATT_CLASS, labelClass, "labelClass");
            }
            writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP);
            writer.writeText(curItem.getLabel(), "value");
            writer.endElement(LangConstant.TAG_SPAN);
            isDisable = true;
        }
        return isDisable;
    }

    /**
     * <p>Render a Checkbox option.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @param curItem the checkbox option for rendering
     * @throws java.io.IOException Failed to write tags
     */
    protected void renderOption(FacesContext context, UIComponent component, SelectItem curItem)
    throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        // write as diabled item if need
        if (renderDisabledItem( writer, component, curItem)) return;

        // disable the check box if the attribute is set.
        String labelClass = (String) component.getAttributes().get("enabledClass");
        String value = getFormattedValue(context, component, curItem.getValue());
        boolean disabled = getBooleanValue(component.getAttributes().get("disabled"), "true");
        boolean isSelected = false;


        // write the label
        writer.startElement(LangConstant.TAG_LABEL, component);
        // write the style class
        if (labelClass != null) {
            writer.writeAttribute(LangConstant.ATT_CLASS, labelClass, "labelClass");
        }

        // write a checkbox
        writer.startElement(LangConstant.TAG_INPUT, component);
        writer.writeAttribute(LangConstant.ATT_NAME, component.getClientId(context), "clientId");
        writer.writeAttribute(LangConstant.ATT_VALUE, value, "value");
        writer.writeAttribute(LangConstant.ATT_TYPE, "checkbox", null);
        // check the submitted values to find the state of this option
        Object submittedValues[] = getSubmittedSelectedValues(context, component);
        if (submittedValues != null) {
            isSelected = isSelected( value, submittedValues);
        } else {
            Object selectedValues = getCurrentSelectedValues(context, component);
            isSelected = isSelected( curItem.getValue(), selectedValues);
        }
        // write the submit state if need
        if (isSelected) {
            writer.writeAttribute(LangConstant.ATT_CHECKED, Boolean.TRUE, null);
        }
        // pass simple attributes
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP);
        writer.endElement(LangConstant.TAG_INPUT);
        // write option lable if need
        String itemLabel = curItem.getLabel();
        if (itemLabel != null) {
            writer.writeText(" ", null);
            writer.writeText(itemLabel, "label");
        }
        writer.endElement(LangConstant.TAG_LABEL);
    }

    /**
     * <p>Checkbox layout</p>
     */
    protected boolean PAGE_VERTICAL = false;

}
