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
 *      BodyRenderer is a renderer for
 *      generating WML card tag &lt;card&gt;
 * </p>
 *
 * <p>
 *      The card in MobileFaces has two modes:
 *      multi-cards and single cards. The <code>cards</code>
 *      attributes of &lt;m:card&gt is used to switch the modes.
 *
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Output"
 *                  renderer-type="com.ericsson.sn.mobilefaces.Body"
 *                  description="BodyRenderer is the renderer for generating WML card tag"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class BodyRenderer extends BasicRenderer {

    private boolean cards = false;

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public BodyRenderer() {
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
        // get the multi-card attribute setting
        cards = getBooleanValue(component.getAttributes().get(LangConstant.TAG_WML_CARDS), "true");
        // get the card title
        Object title = component.getAttributes().get(LangConstant.TAG_WML_TITLE);

        if (title == null) title = DEFAULT_TITLE;

        if (!cards) {
            // single card mode and render the card tag directly
            writer.startElement(LangConstant.TAG_CARD, component);
            writer.writeAttribute(LangConstant.ATT_ID, DEFAULT_ID, "id");
            writer.writeAttribute(LangConstant.ATT_TITLE, title, "title");
        } else {
            // multi card mode and just save the state
            component.getAttributes().put(LangConstant.WML_FIRST_CARD, "true");
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
        ResponseWriter writer = context.getResponseWriter();
        // render the end tag by card mode
        if (!cards)
            writer.endElement(LangConstant.TAG_CARD);
        else
            component.getAttributes().remove(LangConstant.WML_FIRST_CARD);
    }

    // default values
    private static final String DEFAULT_TITLE = "WML PAGE";
    private static final String DEFAULT_ID = "WMLCard";
}
