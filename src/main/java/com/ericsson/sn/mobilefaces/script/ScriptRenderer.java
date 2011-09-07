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
package com.ericsson.sn.mobilefaces.script;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

/**
 * <p>
 *      ScriptRenderer does the work for adding script reference on the page
 *      and binding event to the component.
 *      To compatible with different scripts, script codes should be written in extra
 *      script file.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public interface ScriptRenderer{

    /**
     * <p>Write script reference of the source file in the head for this page.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @return the reference path
     */
    public String encodeScriptHead(FacesContext context, UIComponent component);

    /**
     * <p>Binding the event to the component.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @return the script binding string
     */
    public String encodeScript(FacesContext context, UIComponent component);
}
