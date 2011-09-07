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

import javax.faces.component.UIData;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.util.RenderUtils;
import com.ericsson.sn.mobilefaces.script.ScriptRenderFactory;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicTableRenderer;

/**
 * <p>
 *      TableRenderer is a renderer to render
 *      a data table on XHTML-MP page.
 * </p>
 *
 * <p>
 *      This table will be rendered with &lt;table&gt
 *      tag to arrage the data.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Data"
 *                  renderer-type="javax.faces.Table"
 *                  description="TableRenderer is the renderer for a data table on XHTML-MP page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class TableRenderer extends BasicTableRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public TableRenderer() {
        super(LangConstant.XHTML_MP);
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
        UIData data = (UIData) component;
        data.setRowIndex(-1);
        // write <table>
        writer.startElement(LangConstant.TAG_TABLE, data);
        // write table id
        writeID(context, component, false, null);
        // pass simple attributes
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP);
        // write script with script renderer
        ScriptRenderFactory.getScriptRenderer().encodeScript(context, component);
        writer.writeText("\n", null);
        // encode the header
        encodeTitle(context, writer, data, HEADER, true);
        // encode the header data
        encodeTitleData(context, writer, data, HEADER, true);
        writer.writeText("\n", null);
        data.setRowIndex(-1);
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

        UIData data = (UIData) component;
        // get the style classes for column and row
        String columnClassesValues = (String) data.getAttributes().get("columnClasses");
        String columnClasses[] = RenderUtils.parseStrings(columnClassesValues, ",");
        int columnStyleNum = columnClasses.length;
        String rowClassesValues = (String) data.getAttributes().get("rowClasses");
        String rowClasses[] = RenderUtils.parseStrings(rowClassesValues, ",");
        int rowStyleNum = rowClasses.length;

        ResponseWriter writer = context.getResponseWriter();

        int index = 0;
        int rowIndex = data.getFirst() - 1;
        int rowNum = data.getRows();
        int rowStyleIndex = 0;

        // Iterate over the rowNum of data
        while (true) {
            if ((rowNum > 0) && (++index > rowNum)) {
                break;
            }

            data.setRowIndex(++rowIndex);
            if (!data.isRowAvailable()) {
                break;
            }
            // write the beginning of this row
            writer.startElement(LangConstant.TAG_TR, data);
            // write row style classe
            if (rowStyleNum > 0) {
                writer.writeAttribute( LangConstant.ATT_CLASS, rowClasses[rowStyleIndex++], "rowClasses");
                if (rowStyleIndex >= rowStyleNum) {
                    rowStyleIndex = 0;
                }
            }
            writer.writeText("\n", null);
            int columnStyleIndex = 0;
            Iterator kids = getColumns(data);
            // render all columns
            while (kids.hasNext()) {
                UIColumn column = (UIColumn) kids.next();
                writer.startElement(LangConstant.TAG_TD, column);
                // write column style class
                if (columnStyleNum > 0) {
                    writer.writeAttribute(LangConstant.ATT_CLASS, columnClasses[columnStyleIndex++], "columnClasses");
                    if (columnStyleIndex >= columnStyleNum) {
                        columnStyleIndex = 0;
                    }
                }
                // encode all subkids
                Iterator subkids = getChildren(column);
                while (subkids.hasNext()) {
                    encodeRecursive(context, (UIComponent) subkids.next());
                }
                writer.endElement(LangConstant.TAG_TD);
                writer.writeText("\n", null);
            }
            writer.endElement(LangConstant.TAG_TR);
            writer.writeText("\n", null);
        }
        data.setRowIndex(-1);
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
        UIData data = (UIData) component;
        data.setRowIndex(-1);
        // write the footer
        encodeTitle(context, writer, data, FOOTER, true);
        // write the footer data
        encodeTitleData(context, writer, data, FOOTER, true);
        writer.endElement(LangConstant.TAG_TABLE);
        writer.writeText("\n", null);
    }
}
