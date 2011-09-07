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
package com.ericsson.sn.mobilefaces.renderkit.html;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicDocTypeRenderer;

/**
 * <p>
 *      DocTypeRenderer is a JSF renderer for generating HTML DTD head.
 * </p>
 *
 * <p>
 *      There are three versions for the HTML DOCTYPE:
 *      <code>Transitional, Strict and Frameset</code>.
 *      The version can be set in the version attribute of &lt;m:doctype&gt;.
 * </p>
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @jsf.render-kit  component-family="javax.faces.Output"
 *                  renderer-type="com.ericsson.sn.mobilefaces.DocType"
 *                  description="DocTypeRenderer is the renderer for DocType head on HTML page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class DocTypeRenderer extends BasicDocTypeRenderer {

    // Transitional DTD
    private static final String HTML_TRAN_HEAD = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\r\n";
    // Strict DTD
    private static final String HTML_STRICT_HEAD = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">\r\n";
    // Frameset DTD
    private static final String HTML_FRAME_HEAD = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\" \"http://www.w3.org/TR/html4/frameset.dtd\">\r\n";

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

        String dtd = (String) component.getAttributes().get("DTD");
        // Write HTML DTD head
        if (dtd == null || dtd.indexOf("Transitional") != -1) {
            writer.write(HTML_TRAN_HEAD);
        } else if (dtd.indexOf("Strict") != -1){
            writer.write(HTML_STRICT_HEAD);
        } else if (dtd.indexOf("Frameset") != -1){
            writer.write(HTML_FRAME_HEAD);
        } else {
            writer.write(HTML_TRAN_HEAD);
        }
    }
}
