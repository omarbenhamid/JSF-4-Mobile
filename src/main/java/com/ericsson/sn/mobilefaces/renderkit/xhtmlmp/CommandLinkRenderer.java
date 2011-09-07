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

import com.ericsson.sn.mobilefaces.device.DeviceDataFactory;
import java.util.Map;
import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.script.ScriptRenderFactory;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicRenderer;

/**
 * <p>
 *      CommandLinkRenderer is a renderer to render the <code>UICommand</code>
 *      as a link on an XHTML-MP page.
 * </p>
 *
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Command"
 *                  renderer-type="javax.faces.Link"
 *                  description="CommandLinkRenderer is the renderer for the UICommand component on an XHTML-MP page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class CommandLinkRenderer extends BasicRenderer {

    private String value;

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public CommandLinkRenderer() {
        super(LangConstant.XHTML_MP);
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
        if (requestParam.get(id) == null || requestParam.get(id).equals(""))
            return;
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
        value = (String)((UICommand) component).getValue();
        if (value == null) value = "";
        if (encodeDisableComponent(context, component, value, LangConstant.PASS_THROUGH_ATTR_XHTML_MP, true))
            return;

        // if script is supported then render hidden form and link, otherwise render button
        if (hasJavascriptSupport()){
            // render hidden input
            writer.startElement(LangConstant.TAG_INPUT, component);
            writeID(context, component, true, null);
            writer.writeAttribute(LangConstant.ATT_TYPE, LangConstant.VALUE_HIDDEN, "type");
            writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP);
            ScriptRenderFactory.getScriptRenderer().encodeScript(context, component);
            writer.endElement(LangConstant.TAG_INPUT);
            // render link that is using javascript
            writer.startElement(LangConstant.TAG_A, component);
            writer.writeAttribute(LangConstant.ATT_ID, component.getClientId(context), "id");
            writer.writeAttribute(LangConstant.ATT_HREF, "#", "src");
            UIComponent form;
            do { form = component.getParent(); }
            while (form != null && !(form instanceof UIForm));
            String formName = (form != null) ? form.getClientId(context) : "";
            String inputName = component.getClientId(context);
            writer.writeAttribute(LangConstant.ATT_ONCLICK, "document.forms['" +
                formName + "']['" + inputName + "'].value = '" + inputName + "'; " +
                "document.forms['" + formName + "'].submit(); return false;", "onclick");
            writer.write(value);
        }
        else {
            // render button
            Iterator kids = component.getChildren().iterator();
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                if(kid instanceof UIOutput && !(kid instanceof UIInput)) {
                    value += (String)((UIOutput)kid).getValue();
                }
            }
            writer.startElement(LangConstant.TAG_INPUT, component);
            writeID(context, component, true, null);
            writer.writeAttribute(LangConstant.ATT_TYPE, LangConstant.VALUE_SUBMIT, "type");
            writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP);
            writer.writeAttribute(LangConstant.ATT_VALUE, value, "value");
            writer.endElement(LangConstant.TAG_INPUT);
        }
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

            if(hasJavascriptSupport() || !(kid instanceof UIOutput) || kid instanceof UIInput) {
                kid.encodeBegin(context);
                if (kid.getRendersChildren()) {
                    kid.encodeChildren(context);
                }
                kid.encodeEnd(context);
            }
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
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
        // render </a> only if <a> was renderered in encodeBegin
        if (hasJavascriptSupport()){
            writer.endElement(LangConstant.TAG_A);
        }
    }

    private boolean hasJavascriptSupport() {
        return DeviceDataFactory.getCurrentDevice().getMarkup().equals(LangConstant.MARKUP_HTML_BASIC)
            || (DeviceDataFactory.getCurrentDevice().getScript() != null
                && DeviceDataFactory.getCurrentDevice().getScript().equals(LangConstant.ECMASCRIPTMP));
    }
}
