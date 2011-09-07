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

import java.util.Iterator;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.script.ScriptRenderFactory;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicInputRenderer;

/**
 * <p>
 *      OutputLinkRenderer is a renderer for
 *      a simple link on XHTML-MP page.
 * </p>
 *
 * <p>
 *      This link is rendered with &lt;a&gt tag.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Output"
 *                  renderer-type="javax.faces.Link"
 *                  description="OutputLinkRenderer is the renderer for a simple link on XHTML-MP page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class OutputLinkRenderer extends BasicInputRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public OutputLinkRenderer() {
        super(LangConstant.XHTML_MP);
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
        // get the url value of this link
        String value = getCurrentValue(context, component);
        if (value == null)   value = "";
        // write <a>
        writer.startElement(LangConstant.TAG_A, component);
        // write id
        writeID(context, component, false, null);
        // write url
        value = encodeUrl( value, context, component);
        writer.writeURIAttribute(LangConstant.ATT_HREF, value, "href");
        // pass simple attributes
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP);
        // write script with script renderer
        ScriptRenderFactory.getScriptRenderer().encodeScript(context, component);
        writer.flush();
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
        // encode all the children in this link
        // often render the label of image of this link
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.encodeBegin(context);
            if (kid.getRendersChildren()) {
                kid.encodeChildren(context);
            }
            kid.encodeEnd(context);
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
        // write the </a>
        writer.endElement(LangConstant.TAG_A);
    }

    /**
     * <p>this renderer will render children by itself.</p>
     * @return true
     */
    @Override
    public boolean getRendersChildren() {
        return true;
    }

    /**
     * <p>simply get the component value.</p>
     * @param component UIComponent
     * @return the value of component
     */
    protected Object getValue(UIComponent component) {
        return ((UIOutput) component).getValue();
    }

    // encode the url with get parameters
    private String encodeUrl(String baseUrl, FacesContext context, UIComponent component) {
        Hashtable paramList = getParamList(context, component);
        StringBuffer buff = new StringBuffer();
        buff.append(baseUrl);
        if (paramList.size() > 0) { buff.append("?"); }
        // add parameters to the url
        Enumeration e = paramList.elements();
        while (e.hasMoreElements()) {
            String name = (String)e.nextElement();
            buff.append(name);
            buff.append("=");
            buff.append(paramList.get(name));
            buff.append("&");

        }
        // encode the url
        return context.getExternalContext().encodeResourceURL(buff.toString());
    }
}
