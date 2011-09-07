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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * <p>
 *      A "No Operation" script renderer for unknown script type.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @author Daning Yang
 * @version 1.0
 */
public class NoopScriptRenderer implements ScriptRenderer {

    /**
     * <p>Write nothing.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @return null
     */
    public String encodeScriptHead(FacesContext context, UIComponent component) {
        return null;
    }

    /**
     * <p>Do nothing.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @return null
     */
    public String encodeScript(FacesContext context, UIComponent component) {
        return null;
    }
}
