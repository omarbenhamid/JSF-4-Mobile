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
import javax.servlet.http.HttpServletResponse;

import com.ericsson.sn.mobilefaces.renderkit.share.BasicDocTypeRenderer;

/**
 * <p>
 *      DocTypeRenderer is a JSF renderer for generating WML DTD head.
 * </p>
 *
 * <p>
 *      There are three versions for the WML DOCTYPE:
 *      <code>wml1.1, wml1.2 and wml1.3</code>.
 *      The version can be set in the version attribute of &lt;m:doctype&gt;.
 *
 * </p>
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Output"
 *                  renderer-type="com.ericsson.sn.mobilefaces.DocType"
 *                  description="DocTypeRenderer is the renderer for DocType head on WML page"
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public class DocTypeRenderer extends BasicDocTypeRenderer {

    // For WML 1.1
    private static final String WML1_HEAD = "<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.1//EN\" \"http://www.wapforum.org/DTD/wml_1.1.xml\">\r\n";
    // For WML 1.2
    private static final String WML2_HEAD = "<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.2//EN\" \"http://www.wapforum.org/DTD/wml12.dtd\">\r\n";
    // For WML 1.3
    private static final String WML3_HEAD = "<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.3//EN\" \"http://www.wapforum.org/DTD/wml13.dtd\">\r\n";

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
        // write WML DTD head, default is wml 1.3 head
        if (dtd == null || dtd.indexOf("wml1.3") != -1) {
            writer.write(WML3_HEAD);
        } else if (dtd.indexOf("wml1.2") != -1){
            writer.write(WML2_HEAD);
        } else if (dtd.indexOf("wml1.1") != -1){
            writer.write(WML1_HEAD);
        } else {
            writer.write(WML3_HEAD);
        }
    }
}
