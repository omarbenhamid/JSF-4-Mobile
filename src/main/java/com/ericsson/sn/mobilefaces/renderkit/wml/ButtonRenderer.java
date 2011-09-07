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
import java.io.IOException;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.media.MediaAdaptorFactory;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicRenderer;

/**
 * <p>
 *      ButtonRenderer is a renderer to render the <code>UICommand</code>
 *      as a softkey on WML page.
 * </p>
 *
 * <p>
 *      This Button is rendered with &lt;do&gt; and
 *      &lt;go&gt; tag as a softkey.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Command"
 *                  renderer-type="javax.faces.Button"
 *                  description="ButtonRenderer is the renderer for softkey on WML page"
 *
 * @author Daning Yang
 * @version 1.1
 */
public class ButtonRenderer extends BasicRenderer{

    private boolean isImage = false;

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public ButtonRenderer() {
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
        String id = component.getClientId(context);
        Map requestParam = context.getExternalContext().getRequestParameterMap();
        // if the button is clicked, add an action event
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
        // do nothing if read-only
        if (isReadOnly(component)) return;
        // render as image button if it is
        if (!writeImageButton(context, component)) {

            if (!isDelay(component))
                // rendering the delayed softkey
                getFormComponentHolder(context, component).addComponent(component);
            else
                // delay the rendering for this softkey
                writeBegin(context, component);
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
        // do nothing if read-only
        if (isReadOnly(component)) return;

        if (isDelay(component)){
            writeEnd(context, component);
            setDelay(component, false);
        } else {
            setDelay(component, true);
        }
    }

    // render a task with <go>
    private void writeTask(FacesContext context, UIComponent component, String actionURL)
    throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(LangConstant.TAG_GO, component);
        writer.writeAttribute(LangConstant.ATT_METHOD, LangConstant.VALUE_POST, "method");
        if (!writeWMLCommandScript(context, component, actionURL))
            writer.writeAttribute(LangConstant.ATT_HREF, actionURL, "href");
        getFormValueHolder(context,component).setFormValue(component.getClientId(context), LangConstant.VALUE_SUBMIT);
        addFormFields( context, (UICommand) component);
        // clear the form submit values in the holder
        getFormValueHolder(context,component).removeFormValue(component.getClientId(context));
        writer.endElement(LangConstant.TAG_GO);
    }

    // get the delay rendering state
    private boolean isDelay(UIComponent component) {
        Object o = component.getAttributes().get(LangConstant.WML_DELAY);
        if (o == null)
            return false;
        else
            return ((Boolean)o).booleanValue();
    }

    // set the delay rendering state
    private void setDelay(UIComponent component, boolean isDelay) {
        component.getAttributes().put(LangConstant.WML_DELAY, new Boolean(isDelay));
    }

    // encode the button as a image
    private boolean writeImageButton(FacesContext context, UIComponent component)
    throws IOException {
        String imageSrc = (String) component.getAttributes().get("image");
        String mediaStrategy = getMediaStrategy(component);
        if (imageSrc != null && mediaStrategy != null)
            imageSrc = MediaAdaptorFactory.doAdapt("image", imageSrc, mediaStrategy);
        if (imageSrc != null && imageSrc.length()>0) {
            ResponseWriter writer = context.getResponseWriter();
            isImage = true;
            writer.startElement(LangConstant.TAG_ANCHOR, component);
            writer.startElement(LangConstant.TAG_IMG, component);
            writer.writeAttribute(LangConstant.ATT_SRC, imageSrc, "value");
            writer.writeAttribute(LangConstant.ATT_ALT, "", "alt");
            writer.endElement(LangConstant.TAG_IMG);
            setDelay(component, true);
            return true;
        } else {
            isImage = false;
            return false;
        }
    }

    // encode the normal button as softkey
    private void writeBegin(FacesContext context, UIComponent component)
    throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String label = (String)((UICommand) component).getValue();
        String type = (String) component.getAttributes().get(LangConstant.ATT_TYPE);
        if (label==null) label = LangConstant.VALUE_SUBMIT;

        if (type == null || type.equalsIgnoreCase(LangConstant.VALUE_SUBMIT)) {
            type = LangConstant.VALUE_ACCEPT;
            component.getAttributes().put(LangConstant.ATT_TYPE, type);
        } else if (type != null && type.equalsIgnoreCase(LangConstant.VALUE_RESET)) {
            isImage = false;
            writer.startElement(LangConstant.TAG_DO, component);
            writer.writeAttribute(LangConstant.ATT_TYPE, LangConstant.VALUE_RESET, "type");
            writer.writeAttribute(LangConstant.ATT_LABEL, label, "label");
            writeID(context, component, true, null);
            writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_WML);
            writer.startElement(LangConstant.TAG_REFRESH, component);
            writer.endElement(LangConstant.TAG_REFRESH);
            writer.endElement(LangConstant.TAG_DO);
            return;
        }
        // get the url
        String viewId = context.getViewRoot().getViewId();
        String actionURL = context.getExternalContext().encodeActionURL(
                context.getApplication().getViewHandler().getActionURL(context, viewId));
        // write the normal button
        writer.startElement(LangConstant.TAG_DO, component);
        writer.writeAttribute(LangConstant.ATT_TYPE, type.toLowerCase(), "type");
        writer.writeAttribute(LangConstant.ATT_LABEL, label, "label");
        writeID(context, component, true, null);
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_WML);
        writeTask(context, component, actionURL);
        writer.endElement(LangConstant.TAG_DO);
    }
    // write the end tag for image button
    private void writeEnd(FacesContext context, UIComponent component)
    throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        if (isImage) {
            String viewId = context.getViewRoot().getViewId();
            String actionURL = context.getExternalContext().encodeActionURL(
                    context.getApplication().getViewHandler().getActionURL(context, viewId));
            // write a task for image button
            writeTask(context, component, actionURL);
            writer.endElement(LangConstant.TAG_ANCHOR);
        }
        writer.writeText("\n", null);
    }
}
