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
package com.ericsson.sn.mobilefaces.util;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

/**
 * <p>
 *      In the WML page, all buttons will be displayed as soft keys.
 *      So we hold some components to be rendered at the end of form.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public interface FormComponentHolder {

    /**
     * <p>Clear the hashtable in the holder.</p>
     */
    public void clear();

    /**
     * <p>Add the component to holder for delay rendering.</p>
     * @param component component for delay rendering
     */
    public void addComponent(UIComponent component);

    /**
     * <p>Render all the components in the holder.</p>
     * @param context FacesContext to get writter
     */
    public void encodeComponents(FacesContext context);
}
