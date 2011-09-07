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
import java.util.HashMap;
import java.util.Iterator;
import java.io.IOException;

import javax.faces.component.UIForm;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.util.FormValueHolder;
import com.ericsson.sn.mobilefaces.util.FormComponentHolder;
import com.ericsson.sn.mobilefaces.MobileResponseStateManager;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.BasicRenderer;

/**
 * <p>
 *      FormRenderer is a class that renders the <code>UIForm</code>
 *      as a simulate form to submit values on WML page.
 * </p>
 *
 * <p>
 *      This Form is rendered as nothing but just a holder to hold
 *      values.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Form"
 *                  renderer-type="javax.faces.Form"
 *                  description="FormRenderer is the renderer for simulating a form on WML page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class FormRenderer extends BasicRenderer{

    /**
     * <p>Generate instance and initialize with markup language type.</p>
     */
    public FormRenderer() {
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
        Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
        // set submitted if this form is submitted
        ((UIForm) component).setSubmitted(requestParameterMap.containsKey(id));
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
        if (component instanceof FormValueHolder) {
            ((FormValueHolder)component).clear();
            context.getApplication().getViewHandler().writeState(context);
            // write the form attributes to the
            // holder for submitting with all buttons
            writeFormParam(component, MobileResponseStateManager.VIEW_STATE_PARAM, "");
            writeFormParam(component, component.getClientId(context), component.getClientId(context));
            writeHiddenFields(context, component);
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
        if (component instanceof FormComponentHolder) {
            ResponseWriter writer = context.getResponseWriter();
            // write all delayed buttons in this form
            writer.startElement(LangConstant.TAG_P, component);
            ((FormComponentHolder)component).encodeComponents(context);
            writer.endElement(LangConstant.TAG_P);
        }
    }

    // write a parameter to holder
    private void writeFormParam(UIComponent component, String name, String value) {
        ((FormValueHolder)component).setFormValue(name, value);
    }

    // write the hidden fields
    private void writeHiddenFields(FacesContext context, UIComponent component)
    throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Map map = getHiddenMap(context, false);
        if (map != null) {
            Iterator entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                if (!Boolean.FALSE.equals(entry.getValue())) {
                    // write all the parameters to holder
                    writeFormParam(component, (String)entry.getKey(), "true");
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

    // get the hidden field map
    private static Map<String, String> getHiddenMap(FacesContext context, boolean createIfNew) {
        Map<String, Object> requests = context.getExternalContext().getRequestMap();

        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>)requests.get(HIDDEN_FIELD_KEY);
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
