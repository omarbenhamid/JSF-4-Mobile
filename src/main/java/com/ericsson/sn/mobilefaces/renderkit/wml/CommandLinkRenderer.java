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

import java.util.Map;
import java.util.Iterator;
import java.io.IOException;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicRenderer;

/**
 * <p>
 *      CommandLinkRenderer is a renderer
 *      to render the <code>UICommand<code>
 *      as a link button on WML page.
 * </p>
 *
 * <p>
 *      This CommandLink is rendered
 *      with &lt;anchor&gt and &lt;go&gt tag.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Command"
 *                  renderer-type="javax.faces.Link"
 *                  description="CommandLinkRenderer is the renderer for a link button on WML page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class CommandLinkRenderer extends BasicRenderer{

    private String clientId = null;

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public CommandLinkRenderer() {
        super(LangConstant.WML);
    }

    /**
     * See also
     * <a href="http://java.sun.com/javaee/javaserverfaces/1.2/docs/api/javax/faces/render/Renderer.html#decode(FacesContext context, UIComponent component)">javax.faces.render.Renderer</a>
     * @see javax.faces.render.Renderer#decode(FacesContext context, UIComponent component)
     */
    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode( context, component);
        UICommand command = (UICommand) component;
        String id = component.getClientId(context);
        // if the button is clicked, add an action event
        Map requestParam = context.getExternalContext().getRequestParameterMap();
        if (requestParam.get(id) == null) return;
        addActionEvent(component);
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

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(LangConstant.TAG_ANCHOR, component);
        writeID(context, component, false, null);
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_WML);
    }

    /**
     * See also
     * <a href="http://java.sun.com/javaee/javaserverfaces/1.2/docs/api/javax/faces/render/Renderer.html#encodeChildren(FacesContext context, UIComponent component)">javax.faces.render.Renderer</a>
     * @see javax.faces.render.Renderer#encodeChildren(FacesContext context, UIComponent component)
     */
    @Override
    public void encodeChildren(FacesContext context, UIComponent component)
    throws IOException {
        super.encodeChildren(context, component);
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
        super.encodeEnd(context, component);

        ResponseWriter writer = context.getResponseWriter();
        String type = (String) component.getAttributes().get("type");

        if (type != null && type.equalsIgnoreCase(LangConstant.VALUE_RESET)) {
            writeReset( context, component);
        } else {
            String viewId = context.getViewRoot().getViewId();
            String actionURL = context.getExternalContext().encodeActionURL(
                    context.getApplication().getViewHandler().getActionURL(context, viewId));
            writer.startElement(LangConstant.TAG_GO, component);
            writer.writeAttribute(LangConstant.ATT_METHOD, LangConstant.VALUE_POST, "method");
            if (!writeWMLCommandScript(context, component, actionURL))
                writer.writeAttribute(LangConstant.ATT_HREF, actionURL, "href");
            getFormValueHolder(context,component).setFormValue(component.getClientId(context), LangConstant.VALUE_SUBMIT);
            addFormFields( context, (UICommand) component);
            getFormValueHolder(context,component).removeFormValue(component.getClientId(context));
            writer.endElement(LangConstant.TAG_GO);
            String value = (String)((UICommand) component).getValue();
            if (value == null) value = "";
            writer.write(value);
        }
        writer.endElement(LangConstant.TAG_ANCHOR);
        return;
    }

    /**
     * <p>Set to render the children</p>
     * @return true
     */
    @Override
    public boolean getRendersChildren() {
        return true;
    }

    // write a reset button
    private void writeReset(FacesContext context, UIComponent component)
    throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(LangConstant.TAG_REFRESH, component);
        writer.endElement(LangConstant.TAG_REFRESH);
    }
}
