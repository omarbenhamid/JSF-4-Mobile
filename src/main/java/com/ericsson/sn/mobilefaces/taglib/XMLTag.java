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

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;
import com.ericsson.sn.mobilefaces.taglib.TagConstant;

/**
 * <p>
 *      XMLTag is a JSF UIComponentTag for
 *      generating page head tag like <html>.
 *      In WML page, it will be rendered as <wml>.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public class XMLTag extends UIComponentELTag {

    @Override
    public String getComponentType() { return TagConstant.COMPONENT_TYPE_OUTPUT; }

    @Override
    public String getRendererType() { return TagConstant.RENDERER_TYPE_PAGETYPE; }

    @Override
    public void setProperties(UIComponent component) {
        super.setProperties(component);
    }
}
