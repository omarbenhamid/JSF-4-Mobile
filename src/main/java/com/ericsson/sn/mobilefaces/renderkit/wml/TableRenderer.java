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

import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;

import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicTableRenderer;

/**
 * <p>
 *      TableRenderer is a renderer to render
 *      a data table on WML page.
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
 *                  description="TableRenderer is the renderer for a data table on WML page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class TableRenderer extends BasicTableRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public TableRenderer() {
        super(LangConstant.WML);
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
        int columns = getColumnMax(component);
        writer.startElement(LangConstant.TAG_P, data);
        writer.startElement(LangConstant.TAG_TABLE, data);
        writeID(context, component, false, null);
        writer.writeAttribute(LangConstant.ATT_COLUMNS, columns, "columns");
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_WML);
        writer.writeText("\n", null);
        encodeTitle(context, writer, data, HEADER, false);
        encodeTitleData(context, writer, data, HEADER, false);
        writer.writeText("\n", null);
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
        ResponseWriter writer = context.getResponseWriter();

        int index = 0;
        int rowIndex = data.getFirst() - 1;
        int rowNum = data.getRows();
        while (true) {
            if ((rowNum > 0) && (++index > rowNum)) {
                break;
            }

            data.setRowIndex(++rowIndex);

            if (!data.isRowAvailable()) {
                break;
            }

            writer.startElement(LangConstant.TAG_TR, data);
            writer.writeText("\n", null);
            Iterator kids = getColumns(data);
            while (kids.hasNext()) {
                UIColumn column = (UIColumn) kids.next();
                writer.startElement(LangConstant.TAG_TD, column);
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
        encodeTitle(context, writer, data, FOOTER, false);
        encodeTitleData(context, writer, data, FOOTER, false);
        writer.endElement(LangConstant.TAG_TABLE);
        writer.endElement(LangConstant.TAG_P);
        writer.writeText("\n", null);
    }
}
