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
package com.ericsson.sn.mobilefaces.taglib;

import java.util.Hashtable;
import java.util.Enumeration;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;
import com.ericsson.sn.mobilefaces.util.Log;
import com.ericsson.sn.mobilefaces.taglib.TagConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;

/**
 * <p>
 *      CardTag is a JSF UIComponentTag
 *      for generate card tag for WML page like <card>.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @author Daning Yang
 * @version 1.1
 */
public class CardTag extends UIComponentELTag {

    @Override
    public String getComponentType() { return TagConstant.COMPONENT_TYPE_OUTPUT; }

    @Override
    public String getRendererType() { return TagConstant.RENDERER_TYPE_CARD; }

    private Hashtable<String, ValueExpression> attributes = new Hashtable<String, ValueExpression>();

    public void setCardID(ValueExpression cardId) {
        attributes.put(LangConstant.WML_CARD_ID, cardId);
    }

    public void setNext(ValueExpression next) {
        attributes.put(LangConstant.WML_NEXT, next);
    }

    public void setNewcontext(ValueExpression newcontext) {
        attributes.put(LangConstant.WML_NEXCONTEXT, newcontext);
    }

    public void setTitle(ValueExpression title) {
        attributes.put(LangConstant.ATT_TITLE, title);
    }

    public void setOnenterbackward(ValueExpression onenterbackward) {
        attributes.put("onenterbackward", onenterbackward);
    }

    public void setOnenterforward(ValueExpression onenterforward) {
        attributes.put("onenterforward", onenterforward);
    }

    public void setOntimer(ValueExpression ontimer) {
        attributes.put("ontimer", ontimer);
    }

    public void setClass(ValueExpression classs) {
        attributes.put(LangConstant.ATT_CLASS, classs);
    }

    public void setOrdered(ValueExpression ordered) {
        attributes.put("ordered", ordered);
    }

    public void setLang(ValueExpression lang) {
        attributes.put("lang", lang);
    }

    public void setCommandtype(ValueExpression command) {
        attributes.put(LangConstant.WML_COMMAND_TYPE, command);
    }

    @Override
    public void setProperties(UIComponent component) {
        super.setProperties(component);

        if (!Log.doAssert(component, "Cannot find UIComponent!", CardTag.class))
            return;

        // set attributes to the component
        Enumeration e = attributes.keys();
        while (e.hasMoreElements()) {
            String key = (String)e.nextElement();
            ValueExpression value = attributes.get(key);

            component.setValueExpression(key, value);
        }
    }
}
