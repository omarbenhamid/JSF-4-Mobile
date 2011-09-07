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

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicRenderer;

/**
 * <p>
 *      CardRenderer is a renderer to render
 *      a card on WML page with multi-card mode.
 * </p>
 *
 * <p>
 *      This card is rendered with &lt;card&gt; and
 *      will add prev and forward button automatically.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Output"
 *                  renderer-type="com.ericsson.sn.mobilefaces.Card"
 *                  description="CardRenderer is the renderer for card tag on WML page"
 *
 * @author Daning Yang
 * @version 1.1
 */
public class CardRenderer extends BasicRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public CardRenderer() {
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
        Object id = component.getAttributes().get(LangConstant.WML_CARD_ID);
        writer.startElement(LangConstant.TAG_CARD, component);
        if (id != null)
            writer.writeAttribute(LangConstant.ATT_ID, id, LangConstant.WML_CARD_ID);
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_WML);
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
        Object next = component.getAttributes().get(LangConstant.WML_NEXT);
        Object type = component.getAttributes().get(LangConstant.WML_COMMAND_TYPE);
        if (next != null) {
            if (type == null || type.equals(LangConstant.WML_COMMAND_BUTTON))
                writeNextButton(writer, component, (String)next);
            else if (type.equals(LangConstant.WML_COMMAND_LINK))
                writeNextLink(writer, component, (String)next);
        }

        if (!isFirstCard(component)) {
            if (type == null || type.equals(LangConstant.WML_COMMAND_BUTTON))
                writePrevButton(writer, component);
            else if (type.equals(LangConstant.WML_COMMAND_LINK))
                writePrevLink(writer, component);
        }

        writer.endElement(LangConstant.TAG_CARD);
    }

    private boolean isFirstCard(UIComponent component) {
        UIComponent parent = component.getParent();
        while (parent != null) {
            Object first = parent.getAttributes().get(LangConstant.WML_FIRST_CARD);
            if (first != null && ((String)first).equals("true")) {
                parent.getAttributes().put(LangConstant.WML_FIRST_CARD,"false");
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    // write the next button with link layout
    private void writeNextLink(ResponseWriter writer, UIComponent component, String next)
    throws IOException {
        writer.startElement(LangConstant.TAG_ANCHOR, component);
        writer.writeText("Next>>",null);
        writer.startElement(LangConstant.TAG_GO, component);
        writer.writeAttribute(LangConstant.ATT_HREF, "#" + next, LangConstant.ATT_HREF);
        writer.endElement(LangConstant.TAG_GO);
        writer.endElement(LangConstant.TAG_ANCHOR);
    }

    // write the prev button with link layout
    private void writePrevLink(ResponseWriter writer, UIComponent component)
    throws IOException {
        writer.startElement(LangConstant.TAG_ANCHOR, component);
        writer.writeText("<<Prev",null);
        writer.startElement(LangConstant.TAG_PREV, component);
        writer.endElement(LangConstant.TAG_PREV);
        writer.endElement(LangConstant.TAG_ANCHOR);
    }

    // write the next button with softkey layout
    private void writeNextButton(ResponseWriter writer, UIComponent component, String next)
    throws IOException {
        writer.startElement(LangConstant.TAG_DO, component);
        writer.writeAttribute(LangConstant.ATT_TYPE, LangConstant.VALUE_ACCEPT, LangConstant.ATT_TYPE);
        writer.writeAttribute(LangConstant.ATT_LABEL, "Next>>", LangConstant.ATT_LABEL);
        writer.startElement(LangConstant.TAG_GO, component);
        writer.writeAttribute(LangConstant.ATT_HREF, "#" + next, LangConstant.ATT_HREF);
        writer.endElement(LangConstant.TAG_GO);
        writer.endElement(LangConstant.TAG_DO);
    }

    // write the prev button with softkey layout
    private void writePrevButton(ResponseWriter writer, UIComponent component)
    throws IOException {
        writer.startElement(LangConstant.TAG_DO, component);
        writer.writeAttribute(LangConstant.ATT_TYPE, LangConstant.VALUE_ACCEPT, LangConstant.ATT_TYPE);
        writer.writeAttribute(LangConstant.ATT_LABEL, "<<Prev", LangConstant.ATT_LABEL);
        writer.startElement(LangConstant.TAG_PREV, component);
        writer.endElement(LangConstant.TAG_PREV);
        writer.endElement(LangConstant.TAG_DO);
    }
}
