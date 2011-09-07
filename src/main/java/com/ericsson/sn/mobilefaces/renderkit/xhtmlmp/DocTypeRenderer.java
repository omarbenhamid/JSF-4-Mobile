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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletResponse;

import com.ericsson.sn.mobilefaces.renderkit.share.BasicDocTypeRenderer;

/**
 * <p>
 *      DocTypeRenderer is a JSF renderer for generating XHTML-MP DTD head.
 * </p>
 *
 * <p>
 *      There are two versions for the XHTML-MP DOCTYPE:
 *      <code>xtmlmp1.0 and xtmlmp1.1</code>.
 *      The version can be set in the version attribute of &lt;m:doctype&gt;.
 *
 * </p>
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Output"
 *                  renderer-type="com.ericsson.sn.mobilefaces.DocType"
 *                  description="DocTypeRenderer is the renderer for DocType head on XHTML-MP page"
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public class DocTypeRenderer extends BasicDocTypeRenderer {

    // For XHTML-MP 1.1
    private static final String XML1_1_HEAD = "<!DOCTYPE html PUBLIC \"-//OMA//DTD XHTML Mobile 1.1//EN\" \"http://www.openmobilealliance.org/tech/DTD/xhtml-mobile11.dtd\">\r\n";
    // For XHTML-MP 1.0
    private static final String XML1_0_HEAD = "<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.0//EN\" \"http://www.openmobilealliance.org/tech/DTD/xhtml-mobile10.dtd\">\r\n";

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

        // write the xml declaration directly to the reponse, after clearing
        // the buffer, so that it will end up at the very top of the document
        HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();
        response.getWriter().write(XML_HEAD);

        String dtd = (String) component.getAttributes().get("DTD");
        // write XHTML-MP DTD head, default is xtmlmp 1.0
        if (dtd == null || dtd.indexOf("xtmlmp1.0") != -1) {
            writer.write(XML1_0_HEAD);
        } else if (dtd.indexOf("xtmlmp1.1") != -1) {
            writer.write(XML1_1_HEAD);
        } else {
            writer.write(XML1_0_HEAD);
        }
    }
}
