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

import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.io.IOException;

import javax.faces.FactoryFinder;
import javax.faces.render.Renderer;
import javax.faces.event.ActionEvent;
import javax.faces.convert.Converter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.component.NamingContainer;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;

import com.ericsson.sn.mobilefaces.util.Log;
import com.ericsson.sn.mobilefaces.util.RenderUtils;
import com.ericsson.sn.mobilefaces.script.ScriptRenderFactory;
import com.ericsson.sn.mobilefaces.util.FormComponentHolder;
import com.ericsson.sn.mobilefaces.util.FormValueHolder;

/**
 * <p>
 *      Basic renderer implements the common methods needed for all renderers.
 * </p>
 *
 * <p>
 *      In the decode and encode methods, BasicRenderer will check the context, component
 *      readonly and isRendered attr�butes to decide doing decode/encode or not.
 * </p>
 *
 * <p>
 *      Some methods such as writeID, writePassAttributes and encodeRecursive are needed for most
 *      renderers.
 * </p>
 *
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public abstract class BasicRenderer extends Renderer {

    /**
     * <p>
     *      LangConstant is value container which contains
     *      all the Markup language tag and attributes strings.
     * </p>
     * <p>
     *      In the constructor, BasicRenderer will initialize a
     *      LangConstant instance based on device markup language.
     * </p>
     * <p>
     *      For example, in the renderer, you can call "lang.TAG_TABLE" to get
     *      the table tag for current markup language.
     * </p>
     */
    protected LangConstant lang;

    /**
     * <p> SLOT string for rebuild the WML ID. </p>
     */
    protected static String WML_ID_SLOT = "a";

    /**
     * <p> Construct the renderer and initialize a
     *      LangConstant instance </p>
     * @param markup current markup language
     */
    public BasicRenderer(String markup) {
        super();

        // We can initialize LangConstant instance based on "markup" attribute.
        // But now we simplify it by initializing a common LangConstant instance.
        lang = new LangConstant();
    }

    /**
     * <p> PreDecode to check the component and context are not null and the component is not readonly.
     *     If the component is readonly, skip the decode.</p>
     * @param context FacesContext
     * @param component UIComponent
     */
    @Override
    public void decode(FacesContext context, UIComponent component) {
        if (component == null || context == null || isReadOnly(component)) {
            return;
        }
    }

    /**
     * <p> PreEncode to check the component and context are not null and the component need be rendered.
     *     If not, skip the encode.</p>
     *
     * @param context FacesContext
     * @param component UIComponent
     * @throws java.io.IOException exception if cannot writing
     */
    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
    throws IOException {
        if (context == null || component == null || !component.isRendered()) {
            return;
        }
    }

    /**
     * <p> PreEncode to check the component and context are not null.</p>
     *
     * @param context FacesContext
     * @param component UIComponent
     * @throws java.io.IOException exception if cannot writing
     */
    @Override
    public void encodeChildren(FacesContext context, UIComponent component)
    throws IOException {
        if (context == null || component == null) {
            return;
        }
    }

    /**
     * <p> PreEncode to check the component and context are not null and the component need be rendered.
     *     If not, skip the encode.</p>
     *
     * @param context FacesContext
     * @param component UIComponent
     * @throws java.io.IOException exception if cannot writing
     */
    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
    throws IOException {
        if (context == null || component == null || !component.isRendered()) {
            return;
        }
    }

    /**
     * <p> Check whether this component need write ID.</p>
     * @param component UIComponent
     * @return if need write ID, return true. Else false.
     */
    protected boolean needWriteID(UIComponent component) {
        String id = component.getId();
        return (id != null && !id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX));
    }

    /**
     * <p> Write id for the component if need.</p>
     * @param context Faces Context
     * @param component UIComponent
     * @param name need write the name attribute or not
     * @param id_str id string. If null, system will use the default one
     * @throws java.io.IOException exception if cannot write the id
     */
    protected void writeID(FacesContext context, UIComponent component, boolean name, String id_str)
    throws IOException {
        writeID(context, component, true, name, id_str);
    }

    /**
     * <p> Write id for attribute id = true.  Write name for attribute name = true.</p>
     * @param context Faces Context
     * @param component UIComponent
     * @param id need write the id attribute or not
     * @param name need write the name attribute or not
     * @param id_str id string. If null, system will use the default one
     * @throws java.io.IOException exception if cannot write the id
     */
    protected void writeID(FacesContext context, UIComponent component, boolean id, boolean name, String id_str)
    throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String idstr = null;
        if (id_str != null)
            idstr = id_str;
        else
            idstr = component.getClientId(context);
        if (needWriteID(component) && id)
            writer.writeAttribute(LangConstant.ATT_ID, idstr, "id");
        if (name)
            writer.writeAttribute(LangConstant.ATT_NAME,  idstr, "clientId");
    }

    /**
     * <p> Check the component disable and readonly attributes and return the state.</p>
     * @param component UIComponent
     * @return if the component is readonly, return true. Esle return false.
     */
    protected boolean isReadOnly(UIComponent component) {
        Object disable = component.getAttributes().get("disabled");
        Object readonly = component.getAttributes().get("readonly");

        if (disable != null && getBooleanValue(disable, "true"))
            return true;
        if (readonly != null && getBooleanValue(readonly, "true"))
            return true;

        return false;
    }

    /**
     * <p> Encode the disable component with the lable string
     *     for markup language which doesn't support disabled attribute.</p>
     * @param context Faces Context
     * @param component UIComponent
     * @param label label for replace the component
     * @param passAttributes pass attributes for label string
     * @param hasSpan use the span tag for label string or not
     * @throws java.io.IOException except if cannot write label
     * @return if the component is dsiable, return true. Else return false.
     */
    protected boolean encodeDisableComponent(FacesContext context, UIComponent component,
            String label, String[] passAttributes, boolean hasSpan) throws IOException {
        if (isReadOnly(component)) {
            ResponseWriter writer = context.getResponseWriter();
            if (hasSpan) {
                writer.startElement(LangConstant.TAG_SPAN, component);
                writeID(context, component, false, null);
                writePassAttributes(writer, component, passAttributes);
            }
            writer.writeText(label, "value");
            if (hasSpan)
                writer.endElement(LangConstant.TAG_SPAN);
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p> Convert object to boolean. If the object is Boolean, return the value directly.
     *     If the object is a String, compare it with trueValue and return the result.</p>
     * @param value Object for checking
     * @param trueString true string
     * @return convert result
     */
    protected boolean getBooleanValue(Object value, String trueString) {
        if (value == null)
            return false;
        if (value instanceof String) {
            return ((String) value).equalsIgnoreCase(trueString);
        } else {
            return value.equals(Boolean.TRUE);
        }
    }

    /**
     * <p> Some attributs are not generated by JSF components.
     *     So JSF just pass them to the page directly without any modification.</p>
     * @param writer Response Writer
     * @param component UIComponent
     * @param attributes pass attributes name
     * @throws java.io.IOException except if cannot write the attributes
     */
    protected void writePassAttributes(ResponseWriter writer, UIComponent component, String[] attributes)
    throws IOException {
        if (component == null)
            return;

        for (String attribute : attributes) {
            String[] attrSplit = splitAttribute(attribute);
            Object value = component.getAttributes().get(attrSplit[0]);
            String str = null;
            if (value != null)
                str = value.toString();
            if (str != null && str.equalsIgnoreCase("true")) {
                writer.writeAttribute(attrSplit[1], attrSplit[1], attrSplit[0]);
            }
            if (value != null && !value.equals("") && !RenderUtils.isDefaultValue(value)) {
                writer.writeAttribute(attrSplit[1], value, attrSplit[0]);
            }
        }
    }

    public static boolean hasPassAttributes(UIComponent component, String[] attributes) {
        if (component == null) {
            return false;
        }
        Map attrs = component.getAttributes();
        if (attrs == null) {
            return false;
        }
        Object attrVal;
        for (String attribute : attributes) {
            if (((attrVal = attrs.get(splitAttribute(attribute)[0])) != null) && !attrVal.equals("")) {
                return true;
            }
        }
        return false;
    }

    private static String[] splitAttribute(String attr) {
        String[] attrSplit = attr.split(";");
        return (attrSplit.length > 1) ? attrSplit : new String[]{attrSplit[0], attrSplit[0]};
    }

    /**
     * <p> Create a converter by the class.</p>
     * @param convertClass convert class
     * @return converter
     */
    protected Converter getConverter(Class convertClass){
        if (convertClass == null) {
            return null;
        }
        try {
            // get application from the factory
            ApplicationFactory factory =
                    (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
            Application application = factory.getApplication();
            // create the converter
            return (application.createConverter(convertClass));
        } catch (Exception e) {
            Log.err("Cannot find converter for " + convertClass.getName(), BasicRenderer.class);
            Log.err(e, BasicRenderer.class);
            return null;
        }
    }

    /**
     * <p> Add a action event to the event queue.</p>
     * @param component component to generate the event
     */
    protected void addActionEvent(UIComponent component) {
        ActionEvent actionEvent = new ActionEvent(component);
        component.queueEvent(actionEvent);
    }

    /**
     * <p> Try to get the media strategy from the children component.</p>
     *
     * @return media strategy string
     * @param component UIComponent
     */
    protected String getMediaStrategy(UIComponent component) {
        // check all the children
        Iterator kids = getChildren(component);
        while (kids.hasNext()) {
            // find resource managed component
            UIComponent c = (UIComponent)kids.next();
            String managed = (String)c.getAttributes().get(LangConstant.MEDIA_ADAPTER_RESOURCE);
            if (managed!=null && managed.equalsIgnoreCase(LangConstant.MEDIA_ADAPTER_MANAGED)) {
                // read the strategy
                String strategy = (String)c.getAttributes().get(LangConstant.MEDIA_ADAPTER_STRATEGY);
                if (strategy != null && !strategy.equals("null"))
                    return strategy;
            }
        }
        return null;
    }

    /**
     * <p> Find the facet with the "name" for current component. </p>
     * @param component UIComponent
     * @param name name of the facet
     * @return the facet
     */
    protected UIComponent getFacet(UIComponent component, String name) {
        // get the unrendered facet with the name
        UIComponent facet = component.getFacet(name);
        if ((facet != null) && !facet.isRendered()) {
            facet = null;
        }
        return (facet);
    }

    /**
     * <p> Some components has the "for" attributes such as label.</p>
     * @param context FacesContext
     * @param forComponent for component name
     * @param component current component
     * @return the for component
     */
    protected UIComponent getForComponent(FacesContext context,
            String forComponent, UIComponent component) {
        if (forComponent == null || forComponent.length() == 0) {
            return null;
        }

        UIComponent result = null;
        UIComponent parent = component;
        while (parent != null) {
            result = parent.findComponent(forComponent);
            if (result != null)
                break;
            parent = parent.getParent();
        }
        if (result == null) {
            result =
                    findComponent(context.getViewRoot(), forComponent);
        }
        return result;
    }

    /**
     * Find NamingContainers for forComponent from the
     * start point
     */
    private UIComponent findComponent(UIComponent baseComponent, String forComponent) {
        UIComponent result = null;
        Iterator kids = baseComponent.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (kid instanceof NamingContainer) {
                result = kid.findComponent(forComponent);
            }
            if (result == null) {
                if (kid.getChildCount() > 0) {
                    result = findComponent(kid, forComponent);
                }
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * <p>Get the forComponent id from parent component.<p>
     * @param context Faces Context
     * @param component UIComponent
     * @param forValue forComponent value
     * @return component id
     */
    protected String getForComponentId(FacesContext context,
            UIComponent component, String forValue) {
        String result = null;
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof NamingContainer) {
                break;
            }
            parent = parent.getParent();
        }
        if (parent == null) {

            return result;
        }
        String parentId = parent.getClientId(context);
        result = parentId + NamingContainer.SEPARATOR_CHAR + forValue;
        return result;
    }

    /**
     * <p>Render nested child components by invoking the encode methods
     * on those components.</p>
     * @param context Faces Context
     * @param component UIComponent
     * @throws java.io.IOException except if cannot write the component
     */
    protected void encodeRecursive(FacesContext context, UIComponent component)
    throws IOException {
        if (!component.isRendered()) {
            return;
        }

        // Render this component and its children recursively
        component.encodeBegin(context);
        if (component.getRendersChildren()) {
            component.encodeChildren(context);
        } else {
            Iterator kids = getChildren(component);
            while (kids.hasNext()) {
                UIComponent kid = (UIComponent) kids.next();
                encodeRecursive(context, kid);
            }
        }
        component.encodeEnd(context);
    }

    /**
     * <p>Get the children components of current parent component.</p>
     *
     * @param component UIComponent
     * @return children interator
     */
    protected Iterator getChildren(UIComponent component) {
        List<UIComponent> results = new ArrayList<UIComponent>();
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (kid.isRendered()) {
                results.add(kid);
            }
        }
        return (results.iterator());
    }

    /**
     * <p>Get the parameter children as a hashtable.</p>
     * @param context Faces Context
     * @param component UIComponent
     * @return parameter hashtable
     */
    protected Hashtable getParamList(FacesContext context, UIComponent component) {
        Hashtable<String, String> table = new Hashtable<String, String>();
        // get the children iterator
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (kid instanceof UIParameter) {
                UIParameter uiParam = (UIParameter) kid;
                Object value = uiParam.getValue();
                table.put(uiParam.getName(), (value == null ? null : value.toString()));
            }
        }
        return table;
    }

    /**
     * <p> Translate ID by replacing ":" with WML_ID_SLOT for some WML component.</p>
     *
     * <p> The names of some WML components are used as variable such as $(input_name).
     *     So these names cannot contain the ":". We replace the id without ":".</p>
     *
     * @param id origirnal id
     * @return translated id
     */
    protected String translateID(String id) {
        if (id.indexOf(":")!=-1)
            id = id.substring(0,id.indexOf(":")) + WML_ID_SLOT + id.substring(id.indexOf(":")+1);
        return id;
    }

    /**
     * <p> Add attribute pairs in the Form holder to the post field of command component.</p>
     * @param context Faces Context
     * @param command UICommand
     * @throws java.io.IOException except if cannot write attributes
     */
    protected void addFormFields(FacesContext context, UICommand command)
    throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        // get form holder
        FormValueHolder holder = getFormValueHolder( context, command);
        Hashtable hash = holder.getValueTable();
        // read attribute pairs from hastable of form holder
        // and write it to as post field of command
        Enumeration e = hash.keys();
        while (e.hasMoreElements()) {
            writer.startElement(LangConstant.TAG_POSTFIELD, command);
            String name = (String)e.nextElement();
            writer.writeAttribute(LangConstant.ATT_NAME, name, LangConstant.ATT_NAME);
            writer.writeAttribute(LangConstant.ATT_VALUE, hash.get(name), LangConstant.ATT_VALUE);
            writer.endElement(LangConstant.TAG_POSTFIELD);
        }
    }

    /**
     * <p> Get form value holder of current component.
     *     Search from the parents to find the FormValueHolder.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @return the form value holder for current component
     */
    protected FormValueHolder getFormValueHolder(FacesContext context, UIComponent component) {
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof FormValueHolder) {
                break;
            }
            parent = parent.getParent();
        }
        if (parent != null)
            return (FormValueHolder) parent;
        else
            return null;
    }

    /**
     * <p> Get form component holder of current component.
     *     Search from the parents to find the FormComponentHolder.</p>
     * @param context Faces Context
     * @param component UIComponent
     * @return the form component holder for current component
     */
    protected FormComponentHolder getFormComponentHolder(FacesContext context, UIComponent component) {
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof FormComponentHolder) {
                break;
            }
            parent = parent.getParent();
        }
        if (parent != null)
            return (FormComponentHolder) parent;
        else
            return null;
    }

    /**
     * <p> In the WML page, we use a script by replacing the real url href.</p>
     *
     * <p>
     *   <pre>
     *     For example, an command component is
     *     <h:commandButton onclick="validation()"/> <br/>
     *     will be translated to
     *     <do>
     *          <go href="script.wmls#validation()">
     *              <setvar name="commandurl" value="url"/>
     *          </go>
     *     </do>
     *   </pre>
     * </p>
     *
     * @param context Faces Context
     * @param component UIComponent
     * @param realURL the real URL
     * @throws java.io.IOException except if cannot write the command script
     * @return if there is script for this component, return true. else return false
     */
    protected boolean writeWMLCommandScript(FacesContext context, UIComponent component, String realURL)
    throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String script = ScriptRenderFactory.getScriptRenderer().encodeScript(context, component);
        if (script == null) {
            return false;
        } else {
            writer.writeAttribute(LangConstant.ATT_HREF, script, "href");
            writer.startElement(LangConstant.TAG_SETVAR, component);
            writer.writeAttribute(LangConstant.ATT_NAME, "commandurl", "name");
            writer.writeAttribute(LangConstant.ATT_VALUE, realURL, "value");
            writer.endElement(LangConstant.TAG_SETVAR);
            return true;
        }
    }

    /**
     * <p> Add "onpick" script if need.</p>
     * @param context Faces Context
     * @param component UIComponent
     * @throws java.io.IOException except if cannot write the menu script
     * @return if there is script for this component, return true. else return false
     */
    protected boolean writeWMLMenuScript(FacesContext context, UIComponent component)
    throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        component.getAttributes().put("onpick", "true");
        String script = ScriptRenderFactory.getScriptRenderer().encodeScript(context, component);
        if (script == null) {
            return false;
        } else {
            writer.writeAttribute(LangConstant.ATT_ONPICK, script, "onpick");
            return true;
        }
    }
}
