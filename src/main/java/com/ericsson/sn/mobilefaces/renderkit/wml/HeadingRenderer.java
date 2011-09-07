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
 *      HeadingRenderer is a JSF renderer for
 *      generating heading tag by style tags
 *      such as &lt;stronge&gt; to simulate &lt;h1&gt; to &lt;h6&gt;.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @jsf.render-kit  component-family="javax.faces.Output"
 *                  renderer-type="com.ericsson.sn.mobilefaces.Heading"
 *                  description="HeadingRenderer is the renderer for heading tag on WML page"
 * @author Daning Yang
 * 
 * @version 1.1
 */
public class HeadingRenderer extends BasicRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public HeadingRenderer() {
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
        // get heading simulate tag
        String tag = getTag((String) component.getAttributes().get("HEADING"));
        // write the heading tag
        writer.startElement(LangConstant.TAG_P, component);
        if (tag != null)
            writer.startElement( tag, component);
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
        // write the heading end tag
        String tag = getTag((String) component.getAttributes().get("HEADING"));
        if (tag != null)
            writer.endElement(tag);
        writer.startElement(LangConstant.TAG_BR, component);
        writer.endElement(LangConstant.TAG_BR);
        writer.endElement(LangConstant.TAG_P);
    }

    // get the heheadingader simulate tag based on heading level
    private String getTag(String heading) {
        int hl = (new Integer(heading));

        String tag = null;

        switch(hl) {
            case 1:
                tag = LangConstant.TAG_BIG;
                break;
            case 2:
                tag = LangConstant.TAG_STRONG;
                break;
            case 3:
                tag = LangConstant.TAG_B;
                break;
            case 4:
                tag = LangConstant.TAG_U;
                break;
            case 5:
                tag = LangConstant.TAG_I;
                break;
            case 6:
                tag = LangConstant.TAG_I;
                break;
        }
        return tag;
    }
}
