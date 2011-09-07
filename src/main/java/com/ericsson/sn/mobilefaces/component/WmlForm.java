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
package com.ericsson.sn.mobilefaces.component;

import java.util.Vector;
import java.util.Hashtable;
import java.io.IOException;
import java.util.Enumeration;

import javax.faces.context.FacesContext;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.UIComponent;

import com.ericsson.sn.mobilefaces.util.Log;
import com.ericsson.sn.mobilefaces.util.FormValueHolder;
import com.ericsson.sn.mobilefaces.util.FormComponentHolder;

/**
 * <p>
 *      WMLForm implements the FormValueHolder and FormComponentHolder
 *      to contain the form parameters and commandbutton for the WML page.
 * </p>
 */
public class WmlForm extends HtmlForm implements FormValueHolder, FormComponentHolder {

    private Hashtable<String, String> valueHolder = new Hashtable<String, String>();
    private Vector<UIComponent> componentHolder = new Vector<UIComponent>();

    /**
     * @see com.ericsson.sn.mobilefaces.util.FormValueHolder#clear()
     */
    public void clear() {
        valueHolder.clear();
        componentHolder.clear();
    }

    /**
     * @see com.ericsson.sn.mobilefaces.util.FormValueHolder#setFormValue(String name, String value)
     */
    public void setFormValue(String name, String value) {
        if (name == null)
            return;
        else {
            if (value == null)
                value = "";
            valueHolder.put(name, value);
        }
    }

    /**
     * @see com.ericsson.sn.mobilefaces.util.FormValueHolder#getValueTable()
     */
    public Hashtable getValueTable() {
        return valueHolder;
    }

    /**
     * @see com.ericsson.sn.mobilefaces.util.FormValueHolder#removeFormValue(String name)
     */
    public void removeFormValue(String name) {
        valueHolder.remove(name);
    }

    /**
     * @see com.ericsson.sn.mobilefaces.util.FormComponentHolder#addComponent(UIComponent component)
     */
    public void addComponent(UIComponent component) {
        componentHolder.add(component);
    }

    /**
     * @see com.ericsson.sn.mobilefaces.util.FormComponentHolder#encodeComponents(FacesContext context)
     */
    public void encodeComponents(FacesContext context) {
        Enumeration e = componentHolder.elements();
        try {
            while (e.hasMoreElements()) {
                UIComponent component = (UIComponent)e.nextElement();
                component.encodeBegin(context);
                component.encodeChildren(context);
                component.encodeEnd(context);
            }
        } catch (IOException ex) {
            Log.err("Failed to encode components!", WmlForm.class);
            Log.err(ex, WmlForm.class);
        }
    }
}
