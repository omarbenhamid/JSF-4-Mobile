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
 *      DocTypeTag is a JSF UIComponentTag for
 *      current markup language DOCTYPE head.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 *
 * @author Daning Yang
 * @version 1.1
 */
public class DocTypeTag extends UIComponentELTag {

    private ValueExpression version = null;
    private ValueExpression cache = null;

    @Override
    public String getComponentType() { return TagConstant.COMPONENT_TYPE_OUTPUT; }

    @Override
    public String getRendererType() { return TagConstant.RENDERER_TYPE_DOCTYPE; }

    public void setVersion(ValueExpression value) {
        version = value;
    }

    public void setCache(ValueExpression value) {
        cache = value;
    }

    @Override
    public void setProperties(UIComponent component) {
        super.setProperties(component);

        if (!Log.doAssert(component, "Cannot find UIComponent!", DocTypeTag.class)) return;

        component.setValueExpression(LangConstant.ATT_DTD, version);
        component.setValueExpression(LangConstant.ATT_CACHE, cache);
    }
}
