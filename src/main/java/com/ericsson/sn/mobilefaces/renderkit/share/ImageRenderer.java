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
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.script.ScriptRenderFactory;
import com.ericsson.sn.mobilefaces.media.MediaAdaptorFactory;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicRenderer;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;

/**
 * <p>
 *      ImageRenderer is a renderer to render the <code>UIGraphic</code>
 *      as a image on XHTML-MP page.
 * </p>
 *
 * <p>
 *      The image will be rendered as &lt;img&gt; tag.
 *      This image can be adpated by
 *      {@link com.ericsson.sn.mobilefaces.media.MediaAdaptor}.
 *      Add the MediaAdaptor with  &lt;m:media-adaptor&gt;.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Graphic"
 *                  renderer-type="javax.faces.Image"
 *                  description="ImageRenderer is the renderer for a image on XHTML-MP page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class ImageRenderer extends BasicRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public ImageRenderer() {
        super(LangConstant.XHTML_MP);
    }

    /**
     * See also
     * <a href="http://java.sun.com/javaee/javaserverfaces/1.2/docs/api/javax/faces/render/Renderer.html#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)">javax.faces.render.Renderer</a>
     * @see javax.faces.render.Renderer#encodeEnd(FacesContext context, UIComponent component)
     */
    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
    throws IOException {
        super.encodeEnd(context, component);
        ResponseWriter writer = context.getResponseWriter();
        // get the image attributes
        String altText = (String) component.getAttributes().get(LangConstant.ATT_ALT);
        // get the image source path
        String value = (String) ((UIGraphic) component).getValue();
        // get the strategy of media adaptor
        String mediaStrategy = getMediaStrategy(component);
        if (value == null) {
            value = "";
        } else if (mediaStrategy != null) {
            value = MediaAdaptorFactory.doAdapt("image", value, mediaStrategy);
        } else {
            value = context.getApplication().getViewHandler().getResourceURL(context, value);
        }
        // write the <img> tag
        writer.startElement(LangConstant.TAG_IMG, component);
        // write id
        writeID(context, component, false, null);
        // write image source path
        writer.writeAttribute(LangConstant.ATT_SRC, value, "value");
        // write alt
        if (altText == null) {
            writer.writeAttribute(LangConstant.ATT_ALT, "", "alt");
        }
        // pass simple attributes
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP);
        // write script by script renderer
        ScriptRenderFactory.getScriptRenderer().encodeScript(context, component);
        writer.endElement(LangConstant.TAG_IMG);
    }
}
