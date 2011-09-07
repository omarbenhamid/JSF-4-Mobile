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

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.IOException;

import javax.faces.component.UIData;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 * <p>
 *      BasicTableRenderer implements methods
 *      for header or footer facet render.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @author Daning Yang
 * @version 1.0
 */
public abstract class BasicTableRenderer extends BasicRenderer {

    /**
     * <p>Create the instance.</p>
     * @param markup markup language type for constants
     */
    public BasicTableRenderer(String markup) {
        super(markup);
    }

    /**
     * <p>Responsible for render children.</p>
     * @return true for render children
     */
    @Override
    public boolean getRendersChildren() {
        return true;
    }

    /**
     * <p>Render the header or footer facet for the table.</p>
     * @param context FacesContext
     * @param writer ResponseWriter
     * @param component UIComponent
     * @param type facet type: header or footer
     * @param hasStyle has the style setting or not
     * @throws java.io.IOException except if cannot write
     */
    protected void encodeTitle(FacesContext context, ResponseWriter writer,
            UIComponent component, String type, boolean hasStyle)
            throws IOException {
        UIComponent title = component.getFacet(type);
        String titleClass = null;
        String titleTag = null;
        if (hasStyle) {
            titleClass = (String) component.getAttributes().get(type + "Class");
            if (type.equals(HEADER))
                titleTag = LangConstant.TAG_TH;
            else
                titleTag = LangConstant.TAG_TD;
        } else {
            titleTag = LangConstant.TAG_TD;
        }

        if (title != null && title.isRendered()) {
            writer.startElement(LangConstant.TAG_TR, title);
            writer.startElement(titleTag, title);
            if (titleClass != null) {
                writer.writeAttribute(LangConstant.ATT_CLASS, titleClass, type+"Class");
            }
            if (hasStyle) {
                writer.writeAttribute(LangConstant.ATT_COLSPAN, "" + getColumnMax(component), null);
                if(type.equals(HEADER))
                    writer.writeAttribute(LangConstant.ATT_SCOPE, "col", null);
            }
            encodeRecursive(context, title);
            writer.endElement(titleTag);
            writer.endElement(LangConstant.TAG_TR);
            writer.writeText("\n", null);
        }
    }

    /**
     * <p>Render the header or footer facet for the table for small screen.
     *    Since mobile device has small screen, it should render a simple
     *    title facet.</p>
     * @param context FacesContext
     * @param writer ResponseWriter
     * @param component UIComponent
     * @param type facet type: header or footer
     * @throws java.io.IOException except if cannot write
     */
    protected void encodeSimpleTitle(FacesContext context, ResponseWriter writer, UIComponent component, String type)
    throws IOException {
        // Render the header facets (if any)
        UIComponent title = component.getFacet(type);
        if (title != null && !title.isRendered()) {
            encodeRecursive(context, title);
            writer.startElement(LangConstant.TAG_BR, title);
            writer.endElement(LangConstant.TAG_BR);
            writer.writeText("\n", null);
        }
    }

    /**
     * <p>Render the header or footer data facet for the table.</p>
     * @param context FacesContext
     * @param writer ResponseWriter
     * @param data UIData to get the data from the component
     * @param type facet type: header or footer
     * @param hasStyle has the style setting or not
     * @throws java.io.IOException except if cannot write
     */
    protected void encodeTitleData(FacesContext context, ResponseWriter writer, UIData data, String type, boolean hasStyle)
    throws IOException {
        int titleMax = getFacetMax(data, type);
        String titleClass = null;
        String titleTag = null;
        if (hasStyle) {
            titleClass = (String) data.getAttributes().get(type + "Class");
            if(type.equals(HEADER))
                titleTag = LangConstant.TAG_TH;
            else
                titleTag = LangConstant.TAG_TD;
        } else {
            titleTag = LangConstant.TAG_TD;
        }
        if (titleMax > 0) {
            writer.startElement(LangConstant.TAG_TR, data);
            writer.writeText("\n", null);
            Iterator columns = getColumns(data);
            while (columns.hasNext()) {
                UIColumn column = (UIColumn) columns.next();
                writer.startElement(titleTag, column);
                if (titleClass != null) {
                    writer.writeAttribute(LangConstant.ATT_CLASS, titleClass, type+"Class");
                }
                if (hasStyle && type.equals(HEADER)) {
                    writer.writeAttribute(LangConstant.ATT_SCOPE, "col", null);
                }
                UIComponent facet = column.getFacet(type);
                if (facet != null && facet.isRendered()) {
                    encodeRecursive(context, facet);
                }
                writer.endElement(titleTag);
                writer.writeText("\n", null);
            }
            writer.endElement(LangConstant.TAG_TR);
            writer.writeText("\n", null);
        }
    }

    /**
     * <p>Return an Iterator over the <code>UIColumn</code> children
     * of the specified <code>UIData</code> that have a
     * <code>rendered</code> property of <code>true</code>.</p>
     *
     *
     * @param data <code>UIData</code> for which to extract children
     * @return column iterator for facet children
     */
    protected Iterator getColumns(UIData data) {
        List<UIColumn> results = new ArrayList<UIColumn>();
        Iterator kids = data.getChildren().iterator();
        // reconstruct the interator to remove the kid which doesnt render
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if ((kid instanceof UIColumn) && kid.isRendered()) {
                results.add((UIColumn)kid);
            }
        }
        return (results.iterator());
    }

    /**
     * <p>Return the number of child <code>UIColumn</code> components
     * nested in the specified <code>UIData</code> that have a facet with
     * the specified name.</p>
     *
     *
     * @param data <code>UIData</code> component being analyzed
     * @param name Name of the facet being analyzed
     * @return Facet number
     */
    protected int getFacetMax(UIData data, String name) {
        int n = 0;
        Iterator kids = getColumns(data);
        // render all facet children
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (getFacet(kid, name) != null) {
                n++;
            }
        }
        return (n);
    }

    /**
     * <p>Get the column number</p>
     * @param component the component of data
     * @return column number
     */
    protected int getColumnMax(UIComponent component) {
        int columns = 0;
        Iterator kids = getColumns((UIData)component);
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            columns++;
        }
        return (columns);
    }

    /**
     * <p>facet type: header</p>
     */
    protected static final String HEADER = "header";

    /**
     * <p>facet type: footer</p>
     */
    protected static final String FOOTER = "footer";
}
