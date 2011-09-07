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

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;
import com.ericsson.sn.mobilefaces.taglib.TagConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.util.Log;

/**
 * <p>
 *      CssRefTag is a JSF UIComponentTag for rendering
 *      a Cascading Style Sheet reference. In XHMTL-MP
 *      and HTML, this tag will be rendered as <link>.
 *      In WML, this tag will not be rendered.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * 
 * @version 1.0
 */
public class CssRefTag extends UIComponentELTag {

    private ValueExpression href = null;

    @Override
    public String getComponentType() { return TagConstant.COMPONENT_TYPE_OUTPUT; }

    @Override
    public String getRendererType() { return TagConstant.RENDERER_TYPE_CSSREF; }

    public void setHref(ValueExpression href) { this.href = href; }

    @Override
    public void setProperties(UIComponent component) {
        super.setProperties(component);

        if (!Log.doAssert(component, "Cannot find UIComponent!", CssRefTag.class))
            return;

        component.setValueExpression(LangConstant.ATT_HREF, href);
    }
}
