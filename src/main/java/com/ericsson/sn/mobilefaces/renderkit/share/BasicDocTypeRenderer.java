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

import javax.faces.render.Renderer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *      BasicDocTypeRenderer is a basic renderer for generating DTD head.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @author Daning Yang
 * @version 1.0
 */
public class BasicDocTypeRenderer extends Renderer {

    /**
     * <p>Renderer Family</p>
     */
    public static final String FAMILY = "javax.faces.Output";
    /**
     * <p>Renderer Type</p>
     */
    public static final String TYPE = "com.ericsson.sn.mobilefaces.DocType";
    /**
     * <p>XML head for adding to all kind xml pages.</p>
     **/
    protected static String XML_HEAD= "<?xml version=\"1.0\"?>\r\n";

    /**
     * See also
     * <a href="http://java.sun.com/javaee/javaserverfaces/1.2/docs/api/javax/faces/render/Renderer.html#encodeBegin(javax.faces.context.FacesContext, javax.faces.component.UIComponent)">javax.faces.render.Renderer</a>
     * @param context context
     * @param component UI component
     * @throws java.io.IOException io exception
     * @see javax.faces.render.Renderer#encodeBegin(FacesContext context, UIComponent component)
     */
    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
    throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();
        // add content type
        response.setContentType(writer.getContentType());
        // add character encoding
        response.setCharacterEncoding(writer.getCharacterEncoding());
        // add cache control
        String cache = (String) component.getAttributes().get("CACHE");
        if (cache == null) {
            response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
            response.setHeader("Pragma", "no-cache"); //HTTP 1.0
        } else {
            response.setHeader("Cache-Control", cache); //HTTP 1.1
            response.setHeader("Pragma", cache); //HTTP 1.0
        }

        // rewrite the xml head with encoding string
        XML_HEAD= "<?xml version=\"1.0\" encoding=\"" + writer.getCharacterEncoding() + "\"?>\r\n";
    }
}
