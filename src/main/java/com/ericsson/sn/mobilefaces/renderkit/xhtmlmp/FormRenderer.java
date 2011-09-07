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

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.IOException;

import javax.faces.component.UIForm;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.script.ScriptRenderFactory;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicRenderer;

/**
 * <p>
 *      FormRenderer is a class that renders the <code>UIForm</code>
 *      as a form to submit values on XHTML-MP page.
 * </p>
 *
 * <p>
 *      This Form has similar layout as HTML checkbox
 *      with &lt;form&gt tag.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Form"
 *                  renderer-type="javax.faces.Form"
 *                  description="FormRenderer is the renderer for a form on XHTML-MP page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class FormRenderer extends BasicRenderer {

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public FormRenderer() {
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
        Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey(id)) {
            ((UIForm) component).setSubmitted(true);
        } else {
            ((UIForm) component).setSubmitted(false);
        }
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
        // get the form attributes
        String charset = (String)component.getAttributes().get("acceptcharset");
        String viewId = context.getViewRoot().getViewId();
        String actionURL = context.getExternalContext().encodeActionURL(
                context.getApplication().getViewHandler().getActionURL(context, viewId));
        // render the form
        writer.startElement(LangConstant.TAG_FORM, component);
        // write id
        writeID(context, component, false, null);
        // write name attribute
        writeID(context, component, false, true, null);
        // write submit type, should be post
        writer.writeAttribute(LangConstant.ATT_METHOD, "post", null);
        // write the url
        writer.writeAttribute(LangConstant.ATT_ACTION, actionURL, null);
        // write charset
        if (charset != null) {
            writer.writeAttribute(LangConstant.ATT_ACCEPT_CHARSET, charset, "acceptcharset");
        }
        // pass simple attributes
        writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP);
        // write script by script renderer
        ScriptRenderFactory.getScriptRenderer().encodeScript(context, component);
        writer.writeText("\n", null);
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
        // write the view state to the form
        writer.startElement(LangConstant.TAG_DIV, component);
        context.getApplication().getViewHandler().writeState(context);
        writer.endElement(LangConstant.TAG_DIV);
        // write the form id and hidden fields
        writer.startElement(LangConstant.TAG_DIV, component);
        writer.startElement(LangConstant.TAG_INPUT, component);
        writer.writeAttribute(LangConstant.ATT_TYPE, LangConstant.VALUE_HIDDEN, "type");
        writer.writeAttribute(LangConstant.ATT_NAME, component.getClientId(context),"clientId");
        writer.writeAttribute(LangConstant.ATT_VALUE, component.getClientId(context), "value");
        writer.endElement(LangConstant.TAG_INPUT);
        writeHiddenFields(context, component);
        writer.endElement(LangConstant.TAG_DIV);
        writer.endElement(LangConstant.TAG_FORM);
    }

    // write the hidden fields
    private static void writeHiddenFields(FacesContext context,
            UIComponent component)
            throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        // get hte hidden attributes
        Map<String, String> map = getHiddenMap(context, false);
        if (map != null) {
            Iterator entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                if (!Boolean.FALSE.equals(entry.getValue())) {
                    // write the boolean attributes
                    if (Boolean.TRUE.equals(entry.getValue())) {
                        writer.startElement(LangConstant.TAG_DIV, component);
                        writer.startElement(LangConstant.TAG_INPUT, component);
                        writer.writeAttribute(LangConstant.ATT_TYPE, LangConstant.VALUE_HIDDEN, null);
                        writer.writeAttribute(LangConstant.ATT_NAME, entry.getKey(), null);
                        writer.endElement(LangConstant.TAG_INPUT);
                        writer.endElement(LangConstant.TAG_DIV);
                    }else {// write the value attributes
                        writer.startElement(LangConstant.TAG_DIV, component);
                        writer.startElement(LangConstant.TAG_INPUT, component);
                        writer.writeAttribute(LangConstant.ATT_TYPE, LangConstant.VALUE_HIDDEN, null);
                        writer.writeAttribute(LangConstant.ATT_NAME, entry.getKey(), null);
                        writer.writeAttribute(LangConstant.ATT_VALUE, entry.getValue(), null);
                        writer.endElement(LangConstant.TAG_INPUT);
                        writer.endElement(LangConstant.TAG_DIV);
                    }
                }
            }

            // Clear the hidden field map
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            requestMap.put(HIDDEN_FIELD_KEY, null);
        }
    }

    /**
     * <p> add a hidden value field to the hidden map of this form.<p>
     * @param context FacesContext
     * @param clientId field id
     * @param value value field
     */
    public static void addHiddenField(FacesContext context, String clientId, String value) {
        Map<String, String> map = getHiddenMap(context, true);
        if (!map.containsKey(clientId)) {
            map.put(clientId, value);
        }
    }

    /**
     * <p> add a hidden boolean field to the hidden map of this form.<p>
     * @param context FacesContext
     * @param clientId field id
     * @param value boolean field value
     */
    public static void addHiddenField(FacesContext context, String clientId, boolean value) {
        Map<String, String> map = getHiddenMap(context, true);
        if (!map.containsKey(clientId)) {
            map.put(clientId, Boolean.toString(value));
        }
    }

    // get the hidden map in this form
    private static Map<String, String> getHiddenMap(FacesContext context,
            boolean createIfNew) {
        Map<String, Object> requests = context.getExternalContext().getRequestMap();

        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) requests.get(HIDDEN_FIELD_KEY);
        if (map == null) {
            if (createIfNew) {
                map = new HashMap<String, String>();
                requests.put(HIDDEN_FIELD_KEY, map);
            }
        }
        return map;
    }

    private static final String HIDDEN_FIELD_KEY = "FormHiddenField";

}
