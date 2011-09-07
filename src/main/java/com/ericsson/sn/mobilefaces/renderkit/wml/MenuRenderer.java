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

import java.util.Iterator;
import java.io.IOException;

import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.util.FormValueHolder;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicMenuRenderer;

/**
 * <p>
 *      MenuRenderer is rendered as
 *      <code>UISelectOne<code> or <code>UISelectMany<code>
 *      as a menu list on WML page.
 * </p>
 *
 * <p>
 *      This menu list is rendered with &lt;select&gt tag.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.SelectOne"
 *                  renderer-type="javax.faces.Menu"
 *                  description="MenuRenderer is the renderer for single-select menu on WML page"
 * @jsf.render-kit  component-family="javax.faces.SelectMany"
 *                  renderer-type="javax.faces.Menu"
 *                  description="MenuRenderer is the renderer for multi-select menu on WML page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class MenuRenderer extends BasicMenuRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public MenuRenderer() {
        super(LangConstant.WML);
    }

    /**
     * <p>The option submit value is array or long string pattern</p>
     * @return false
     */
    @Override
    public boolean isArrayParams() {
        return false;
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
        String id = translateID(component.getClientId(context));
        writer.startElement(LangConstant.TAG_SELECT, component);
        writeID(context, component, true, id);
        if (component instanceof UISelectMany) {
            writer.writeAttribute(LangConstant.ATT_MULTIPLE, "true", "multiple");
        } else {
            writer.writeAttribute(LangConstant.ATT_MULTIPLE, "false", "multiple");
        }
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_WML);
        writeWMLMenuScript( context, component);
        String value = getSelectedValue(context, component);
        if (value != null && value.length() > 0) {
            writer.writeAttribute(LangConstant.ATT_VALUE, value, "value");
        }

        // render the option item group
        renderGroup(context, component);
        writer.endElement(LangConstant.TAG_SELECT);

        // add submit value reference to holder
        FormValueHolder holder = getFormValueHolder(context, component);
        holder.setFormValue(component.getClientId(context), "$(" + id + ")");
    }

    /**
     * <p>Render the option group</p>
     * @param context FacesContext
     * @param component UIComponent
     * @throws java.io.IOException Failed to render the group
     */
    protected void renderGroup(FacesContext context, UIComponent component)
    throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        // get the option items
        Iterator items = getSelectItems(context, component);
        SelectItem curItem = null;
        while (items.hasNext()) {
            curItem = (SelectItem) items.next();
            if (curItem instanceof SelectItemGroup) {
                writer.startElement(LangConstant.TAG_OPTION_GROUP, component);
                writer.writeAttribute(LangConstant.ATT_TITLE, curItem.getLabel(), "label");
                // render the options in this group
                SelectItem[] itemsArray = ((SelectItemGroup) curItem).getSelectItems();
                for (int i = 0; i < itemsArray.length; ++i) {
                    renderOption(context, component, itemsArray[i]);
                }
                writer.endElement(LangConstant.TAG_OPTION_GROUP);
            } else {
                // render a option
                renderOption(context, component, curItem);
            }
        }
    }

    /**
     * <p>Render a option item</p>
     * @param context FacesContext
     * @param component UIComponent
     * @param curItem current option item
     * @throws java.io.IOException Failed to render this item
     */
    protected void renderOption(FacesContext context, UIComponent component, SelectItem curItem)
    throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        if (curItem.isDisabled()) {
            return;
        }

        writer.writeText("\t", null);
        // render a option
        writer.startElement(LangConstant.TAG_OPTION, component);
        String valueString = getFormattedValue(context, component, curItem.getValue());
        writer.writeAttribute(LangConstant.ATT_VALUE, valueString, "value");
        // render the label
        writer.writeText(curItem.getLabel(), "label");
        writer.endElement(LangConstant.TAG_OPTION);
        writer.writeText("\n", null);
    }

    // get the option state
    private String getSelectedValue(FacesContext context, UIComponent component) {
        String value = "";
        // get options
        Iterator items = getSelectItems(context, component);
        SelectItem curItem = null;
        String getValue = null;
        // check the option states
        while (items.hasNext()) {
            curItem = (SelectItem) items.next();
            if (curItem instanceof SelectItemGroup) {
                SelectItem[] itemsArray = ((SelectItemGroup) curItem).getSelectItems();
                for (int i=0; i<itemsArray.length; ++i) {
                    getValue = checkSelected(context, component, itemsArray[i]);
                    if(getValue != null)
                        value = value + getValue + ";";
                }
            } else {
                getValue = checkSelected(context, component, curItem);
                if(getValue != null)
                    value = value + getValue + ";";
            }
        }
        return value;
    }

    // check selected state
    private String checkSelected(FacesContext context, UIComponent component,
            SelectItem curItem) {
        String valueString = getFormattedValue(context, component, curItem.getValue());
        Object submittedValues[] = getSubmittedSelectedValues( context, component);
        boolean isSelected;
        if (submittedValues != null) {
            isSelected = isSelected(valueString, submittedValues);
        } else {
            Object selectedValues = getCurrentSelectedValues( context, component);
            isSelected = isSelected(curItem.getValue(), selectedValues);
        }

        if (isSelected) {
            return valueString;
        } else {
            return null;
        }
    }
}
