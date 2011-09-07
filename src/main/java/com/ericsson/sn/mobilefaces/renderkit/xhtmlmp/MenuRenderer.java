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
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.script.ScriptRenderFactory;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicMenuRenderer;

/**
 * <p>
 *      MenuRenderer is rendered as
 *      <code>UISelectOne<code> or <code>UISelectMany<code>
 *      as a menu list on XHTML-MP page.
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
 *                  description="MenuRenderer is the renderer for single-select menu on XHTML-MP page"
 * @jsf.render-kit  component-family="javax.faces.SelectMany"
 *                  renderer-type="javax.faces.Menu"
 *                  description="MenuRenderer is the renderer for multi-select menu on XHTML-MP page"
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public class MenuRenderer extends BasicMenuRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public MenuRenderer() {
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
        super.encodeEnd( context, component);

        ResponseWriter writer = context.getResponseWriter();
        // write the <select> tag
        writer.startElement(LangConstant.TAG_SELECT, component);
        // write the id
        writeID(context, component, true, null);
        // write the multiple attribute if need
        if (component instanceof UISelectMany) {
            writer.writeAttribute(LangConstant.ATT_MULTIPLE, "multiple", "multiple");
        }
        // pass simple attributes
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP);
        // write script with script renderer
        ScriptRenderFactory.getScriptRenderer().encodeScript(context, component);

        // get the number of options we need to render
        int itemCount = getOptionNumber(context, component)- getDisableCount(context, component);

        // add the size attribute if need
        Object size = component.getAttributes().get("size");
        if (size == null ||
                ((size instanceof Integer) &&
                ((Integer) size).intValue() == Integer.MIN_VALUE)) {
            if (isSizeable())
                writer.writeAttribute(LangConstant.ATT_SIZE, itemCount, "size");
            else
                writer.writeAttribute(LangConstant.ATT_SIZE, 1, "size");
        }
        // render the option group
        renderGroup(context, component);
        writer.endElement(LangConstant.TAG_SELECT);
    }

    /**
     * <p>Render the option group.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @throws java.io.IOException Failed to write options
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
                // render as a group
                writer.startElement(LangConstant.TAG_OPTION_GROUP, component);
                writer.writeAttribute(LangConstant.ATT_LABEL, curItem.getLabel(), "label");
                SelectItem[] itemsArray = ((SelectItemGroup) curItem).getSelectItems();
                for (int i=0; i<itemsArray.length; ++i) {
                    // render every option item in this group
                    renderOption(context, component, itemsArray[i]);
                }
                writer.endElement(LangConstant.TAG_OPTION_GROUP);
            } else {
                // render the option item
                renderOption(context, component, curItem);
            }
        }
    }

    /**
     * <p>Render an option item.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @param curItem current option item
     * @throws java.io.IOException Failed to write options
     */
    protected void renderOption(FacesContext context, UIComponent component,
            SelectItem curItem) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        // simple return if this item is disabled
        if (curItem.isDisabled()) return;

        writer.writeText("\t", null);
        // write <option>
        writer.startElement(LangConstant.TAG_OPTION, component);
        // get and write the option value
        String valueString = getFormattedValue(context, component, curItem.getValue());
        writer.writeAttribute(LangConstant.ATT_VALUE, valueString, "value");
        // get the submitted values
        Object submittedValues[] = getSubmittedSelectedValues( context, component);
        // get select state of current option item
        boolean isSelected;
        if (submittedValues != null) {
            isSelected = isSelected(valueString, submittedValues);
        } else {
            Object selectedValues = getCurrentSelectedValues( context, component);
            isSelected = isSelected(curItem.getValue(), selectedValues);
        }
        // write select state of current option item
        if (isSelected) {
            writer.writeAttribute(LangConstant.ATT_SELECTED, Boolean.TRUE, null);
        }

        // write the item label with style class
        String labelClass = (String) component.getAttributes().get("enabledClass");
        if (labelClass != null) {
            writer.writeAttribute(LangConstant.ATT_CLASS, labelClass, "labelClass");
        }
        writer.writeText(curItem.getLabel(), "label");
        writer.endElement(LangConstant.TAG_OPTION);
        writer.writeText("\n", null);
    }
}
