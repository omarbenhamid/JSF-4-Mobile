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
import javax.faces.webapp.UIComponentELTag;
import javax.faces.component.UIComponent;
import com.ericsson.sn.mobilefaces.util.Log;
import com.ericsson.sn.mobilefaces.taglib.TagConstant;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;

/**
 * <p>
 *      BodyTag is a JSF UIComponentTag for
 *      generating page body tag like <body>.
 *      In WML page, it will be rendered as <card>.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @author Daning Yang
 * @version 1.1
 */
public class BodyTag extends UIComponentELTag {

    private ValueExpression cards = null;
    private ValueExpression cardTitle = null;

    private ValueExpression dir = null;
    private ValueExpression lang = null;
    private ValueExpression style = null;
    private ValueExpression styleClass = null;
    private ValueExpression title = null;

    @Override
    public String getComponentType() {
        return TagConstant.COMPONENT_TYPE_OUTPUT;
    }

    @Override
    public String getRendererType() {
        return TagConstant.RENDERER_TYPE_BODY;
    }

    public void setCards(ValueExpression cards) {
        this.cards = cards;
    }

    public void setCardTitle(ValueExpression cardTitle) {
        this.cardTitle = cardTitle;
    }

    public void setDir(ValueExpression dir) {
        this.dir = dir;
    }

    public void setLang(ValueExpression lang) {
        this.lang = lang;
    }

    public void setStyleClass(ValueExpression styleClass) {
        this.styleClass = styleClass;
    }

    public void setStyle(ValueExpression style) {
        this.style = style;
    }

    public void setTitle(ValueExpression title) {
        this.title = title;
    }

    @Override
    public void setProperties(UIComponent component) {
        super.setProperties(component);

        if (!Log.doAssert(component, "Cannot find UIComponent!", BodyTag.class)) {
            return;
        }

        component.setValueExpression(LangConstant.TAG_WML_CARDS, cards);
        component.setValueExpression(LangConstant.TAG_WML_TITLE, cardTitle);
        component.setValueExpression(LangConstant.ATT_DIR, dir);
        component.setValueExpression(LangConstant.ATT_LANG, lang);
        component.setValueExpression(LangConstant.ATT_STYLE, style);
        component.setValueExpression(LangConstant.ATT_STYLE_CLASS, styleClass);
        component.setValueExpression(LangConstant.ATT_TITLE, title);
    }
}
