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
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.util.Log;
import com.ericsson.sn.mobilefaces.util.RenderUtils;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicTableRenderer;

/**
 * <p>
 *      GridRenderer is a renderer to render the <code>UIPanel</code>
 *      as a Grid of components on XHTML-MP page.
 * </p>
 *
 * <p>
 *      This Grid arranges some components layout
 *      with &lt;table&gt tag.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Panel"
 *                  renderer-type="javax.faces.Grid"
 *                  description="GridRenderer is the renderer for a component grid on XHTML-MP page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class GridRenderer extends BasicTableRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public GridRenderer() {
        super(LangConstant.XHTML_MP);
    }

    /**
     * <p>Set to render the children</p>
     */
    @Override
    public boolean getRendersChildren() {
        return true;
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
        // write the grid with <table>
        writer.startElement(LangConstant.TAG_TABLE, component);
        // write id
        writeID(context, component, false, null);
        // pass simple attributes
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP);
        writer.writeText("\n", null);
        // write the header of this grid
        encodeTitle(context, writer, component, HEADER, true);
    }

    /**
     * See also
     * <a href="http://java.sun.com/javaee/javaserverfaces/1.2/docs/api/javax/faces/render/Renderer.html#encodeChildren(FacesContext context, UIComponent component)">javax.faces.render.Renderer</a>
     * @see javax.faces.render.Renderer#encodeChildren(FacesContext context, UIComponent component)
     */
    @Override
    public void encodeChildren(FacesContext context, UIComponent component)
    throws IOException {
        super.encodeChildren( context, component);
        // get the variables of this grid
        ResponseWriter writer = context.getResponseWriter();
        int columns = getColumnMax(component);
        // get style and style class
        String columnClassesValues = (String) component.getAttributes().get("columnClasses");
        String columnClasses[] = RenderUtils.parseStrings(columnClassesValues, ",");
        int columnStyle = 0;
        int columnStyles = columnClasses.length;
        String rowClassesValues = (String) component.getAttributes().get("rowClasses");
        String rowClasses[] = RenderUtils.parseStrings(rowClassesValues, ",");
        int rowStyle = 0;
        int rowStyles = rowClasses.length;
        boolean open = false;
        UIComponent facet = null;
        Iterator kids = null;
        int i = 0;

        // render the children components
        if (null != (kids = getChildren(component))) {
            while (kids.hasNext()) {
                UIComponent child = (UIComponent) kids.next();
                if ((i % columns) == 0) {
                    if (open) {
                        writer.endElement(LangConstant.TAG_TR);
                        writer.writeText("\n", null);
                        open = false;
                    }
                    writer.startElement(LangConstant.TAG_TR, component);
                    if (rowStyles > 0) {
                        writer.writeAttribute(LangConstant.ATT_CLASS, rowClasses[rowStyle++], "rowClasses");
                        if (rowStyle >= rowStyles) {
                            rowStyle = 0;
                        }
                    }
                    writer.writeText("\n", null);
                    open = true;
                    columnStyle = 0;
                }
                writer.startElement(LangConstant.TAG_TD, component);
                if (columnStyles > 0) {
                    try {
                        writer.writeAttribute(LangConstant.ATT_CLASS,
                                columnClasses[columnStyle++],
                                "columns");
                    } catch (ArrayIndexOutOfBoundsException e) {
                        Log.err("Children out of bound!", GridRenderer.class);
                        Log.err(e, GridRenderer.class);
                    }
                    if (columnStyle >= columnStyles) {
                        columnStyle = 0;
                    }
                }
                encodeRecursive(context, child);
                writer.endElement(LangConstant.TAG_TD);
                writer.writeText("\n", null);
                i++;
            }
        }
        if (open) {
            writer.endElement(LangConstant.TAG_TR);
            writer.writeText("\n", null);
        }
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
        // Render the ending of this grid with </table>
        ResponseWriter writer = context.getResponseWriter();
        // write the footer of this grid
        encodeTitle(context, writer, component, FOOTER, true);
        writer.endElement(LangConstant.TAG_TABLE);
        writer.writeText("\n", null);
    }

    /**
     * <p>Get the number of columns of this grid</p>
     */
    @Override
    protected int getColumnMax(UIComponent component) {
        int count;
        Object value = component.getAttributes().get("columns");
        if ((value != null) && (value instanceof Integer)) {
            count = ((Integer) value).intValue();
        } else {
            count = 2;
        }
        if (count < 1) {
            count = 1;
        }
        return (count);
    }
}
