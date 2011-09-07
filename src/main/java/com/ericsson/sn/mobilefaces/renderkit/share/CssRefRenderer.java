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

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import com.ericsson.sn.mobilefaces.device.DeviceDataFactory;

/**
 * <p>
 *      CssRefRenderer is a JSF renderer for generating a CSS
 *      import reference.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Output"
 *                  renderer-type="com.ericsson.sn.mobilefaces.ScriptRef"
 *                  description="CssRefRenderer is the renderer for CSS import reference."
 *
 * 
 * @version 1.0
 */
public class CssRefRenderer extends BasicRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public CssRefRenderer() {
        super(LangConstant.HTML);
    }

    /**
     * See also
     * <a href="http://java.sun.com/javaee/javaserverfaces/1.2/docs/api/javax/faces/render/Renderer.html#encodeBegin(javax.faces.context.FacesContext, javax.faces.component.UIComponent)">javax.faces.render.Renderer</a>
     * @see javax.faces.render.Renderer#encodeBegin(FacesContext context, UIComponent component)
     */
    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
    throws IOException {
        super.encodeBegin(context, component);
        String markup = DeviceDataFactory.getCurrentDevice().getMarkup();
        String fileext;
        if (markup == null || !(markup.equals(LangConstant.MARKUP_HTML_BASIC)
            || markup.equals(LangConstant.MARKUP_XHTML_MP))) return;
        else if (markup.equals(LangConstant.MARKUP_XHTML_MP)) fileext = ".cssmp";
        else fileext = ".css";
        String href = (String) component.getAttributes().get(LangConstant.ATT_HREF) + fileext;
        href = context.getApplication().getViewHandler().getResourceURL(context, href);
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(LangConstant.TAG_LINK, component);
        writer.writeAttribute(LangConstant.ATT_TYPE, LangConstant.VALUE_TEXT_CSS, "type");
        writer.writeAttribute(LangConstant.ATT_REL, LangConstant.VALUE_STYLESHEET, "rel");
        writer.writeAttribute(LangConstant.ATT_HREF, href, "href");
        writer.endElement(LangConstant.TAG_LINK);
    }
}
