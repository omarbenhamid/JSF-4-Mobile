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

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import java.text.MessageFormat;

import javax.faces.component.UIMessage;
import javax.faces.component.UIMessages;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.component.ValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.application.FacesMessage;

/**
 * <p>
 *      BasicMessageRenderer implements encode method for Message renderer.
 *      Using isMulti and hasStyle to control the layout of the message.
 * </p>
 *
 *
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public abstract class BasicMessageRenderer extends BasicRenderer {

    /**
     * <p>Create the instance.</p>
     * @param markup markup language type for constants
     */
    public BasicMessageRenderer(String markup) {
        super(markup);
    }

    /**
     * <p>Control the encode of this renderer to output one message
     *    or multi messages.</p>
     * @return if multi message, return true, else false
     */
    protected abstract boolean isMulti();

    /**
     * <p>Control the layout of this renderer with style class or not.</p>
     * @return if support style class, return true, else false
     */
    protected abstract boolean hasStyle();

    /**
     * <p>Encode for common message component
     * set by isMulti() and hasStyle().</p>
     * @param context FacesContext
     * @param component UIComponent
     * @throws java.io.IOException except if cannot write
     */
    @Override
    public void encodeEnd(FacesContext context, UIComponent component)
    throws IOException {
        super.encodeEnd(context, component);
        // if it is simple message, just output directly
        if (!isMulti() && component instanceof UIOutput) {
            outputEncodeEnd(context, component);
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        String layout = (String) component.getAttributes().get("layout");
        String clientId = null;
        boolean isTable = false;

        // get for component id if not global message
        if (!isMulti()) {
            clientId = ((UIMessage) component).getFor();
            if (clientId == null) {
                return;
            }
        } else {
            if (((UIMessages) component).isGlobalOnly()) {
                // make it so only global messages get displayed.
                clientId = "";
            }
        }

        Iterator messageIter = getMessageIter(context, clientId, component);
        FacesMessage curMessage = null;
        // table layout if need
        if ((layout != null) && (layout.equals("table"))) {
            writer.startElement(LangConstant.TAG_TABLE, component);
            writeID(context, component, false, null);
            isTable = true;
        }

        while (messageIter.hasNext()) {
            curMessage = (FacesMessage) messageIter.next();

            boolean showSummary = true;
            boolean showDetail = true;

            // read summary and detail attributes
            if (isMulti()) {
                showSummary = ((UIMessages) component).isShowSummary();
                showDetail = ((UIMessages) component).isShowDetail();
            } else {
                showSummary = ((UIMessage) component).isShowSummary();
                showDetail = ((UIMessage) component).isShowDetail();
            }

            // make sure we have a non-null value for summary and
            // detail.
            String summary = null;
            String detail = null;
            if (curMessage.getSummary() != null)
                summary = curMessage.getSummary();
            else
                summary = "";
            if (curMessage.getDetail() != null)
                detail = curMessage.getDetail();
            else
                detail = "";

            String severityStyle[] = null;
            String severityStyleClass = null;
            String style = null;
            String styleClass = null;

            // check style class if need and exsit
            if (hasStyle()) {

                if (curMessage.getSeverity() == FacesMessage.SEVERITY_INFO) {
                    severityStyle = getStyle("info", component);
                } else if (curMessage.getSeverity() == FacesMessage.SEVERITY_WARN) {
                    severityStyle = getStyle("warn", component);
                } else if (curMessage.getSeverity() == FacesMessage.SEVERITY_ERROR) {
                    severityStyle = getStyle("error", component);
                } else if (curMessage.getSeverity() == FacesMessage.SEVERITY_FATAL) {
                    severityStyle = getStyle("fatal", component);
                }

                style = (String) component.getAttributes().get("style");
                styleClass = (String) component.getAttributes().get("styleClass");

                style = selectStyle(style, severityStyle[0]);
                styleClass = selectStyle(styleClass, severityStyle[1]);
            }

            if (isTable) {
                writer.startElement(LangConstant.TAG_TR, component);
                writer.startElement(LangConstant.TAG_TD, component);
            }

            boolean wroteSpan = false;

            // write style class if need
            if (hasStyle() && (styleClass != null || style != null)) {
                writer.startElement(LangConstant.TAG_SPAN, component);
                if (!isTable) {
                    writeID(context, component, true, null);
                }
                wroteSpan = true;
                if (styleClass != null) {
                    writer.writeAttribute(LangConstant.ATT_CLASS, styleClass, "styleClass");
                }
                if (style != null) {
                    writer.writeAttribute(LangConstant.ATT_STYLE, style, "style");
                }
            }

            Object tooltip = component.getAttributes().get("tooltip");
            boolean isTooltip = false;
            if (tooltip instanceof Boolean) {
                isTooltip = ((Boolean) tooltip).booleanValue();
            }

            // write summary and detail if need
            boolean wroteTooltip = false;
            if (hasStyle() && showSummary && showDetail && isTooltip) {

                if (!wroteSpan) {
                    writer.startElement(LangConstant.TAG_SPAN, component);
                }
                writer.writeAttribute(LangConstant.ATT_TITLE, summary, "title");
                writer.flush();
                writer.writeText("\t", null);
                wroteTooltip = true;
            } else if (wroteSpan) {
                writer.flush();
            }

            if (!wroteTooltip && showSummary) {
                writer.writeText("\t", null);
                writer.writeText(summary, null);
                writer.writeText(" ", null);
            }
            if (showDetail) {
                writer.writeText(detail, null);
            }

            if (hasStyle() && wroteSpan || wroteTooltip) {
                writer.endElement(LangConstant.TAG_SPAN);
            }

            //close table row if present
            if (isTable) {
                writer.endElement(LangConstant.TAG_TD);
                writer.endElement(LangConstant.TAG_TR);
            }

            if(!isMulti()) {
                break;
            }

        } //messageIter

        //close table if present
        if (isTable) {
            writer.endElement(LangConstant.TAG_TABLE);
        }
    }

    /**
     * <p>Encode simple output message.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @throws java.io.IOException except if cannot encode
     */
    public void outputEncodeEnd(FacesContext context, UIComponent component)
    throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        String currentValue = null;
        String style = (String) component.getAttributes().get("style");
        String styleClass = (String) component.getAttributes().get("styleClass");

        Object currentObj = ((ValueHolder) component).getValue();
        if (currentObj != null) currentValue = currentObj.toString();

        ArrayList<Object> paraMap = new ArrayList<Object>();

        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();

            if (!(kid instanceof UIParameter)) {
                continue;
            }

            paraMap.add(((UIParameter) kid).getValue());
        }

        // prepare the message
        String message = null;
        if (paraMap.size() > 0) {
            message = MessageFormat.format(currentValue, paraMap.toArray(new Object[paraMap.size()]));
        } else {
            message = currentValue;
        }

        boolean wroteSpan = false;
        // write span for style if need
        if (hasStyle() && (null != styleClass || null != style || needWriteID(component))) {
            writer.startElement(LangConstant.TAG_SPAN, component);
            wroteSpan = true;
            writePassAttributes(writer, component, LangConstant.PASS_THROUGH_ATTR_XHTML_MP);
        }
        boolean escape = getBooleanValue(component.getAttributes().get("escape"), "true");
        // write message content
        if (escape) {
            writer.writeText(message, "value");
        } else {
            writer.write(message);
        }

        if (wroteSpan) {
            writer.endElement(LangConstant.TAG_SPAN);
        }
    }

    // get severity style
    private String[] getStyle(String name, UIComponent component) {
        String style[] = new String[2];
        style[0] = (String) component.getAttributes().get(name + "Style");
        style[1] = (String) component.getAttributes().get(name + "Class");
        return style;
    }

    // select style
    private String selectStyle(String style1, String style2) {
        if(style1 != null) return style1;
        else return style2;
    }

    // get message interator with responsiable messages
    private Iterator getMessageIter(FacesContext context, String forComponent, UIComponent component) {
        Iterator messageIter = null;
        if (null != forComponent) {
            if (forComponent.length() == 0) {
                messageIter = context.getMessages(null);
            } else {
                UIComponent result = getForComponent(context, forComponent, component);
                if (result == null) {
                    messageIter = Collections.EMPTY_LIST.iterator();
                } else {
                    messageIter = context.getMessages(result.getClientId(context));
                }
            }
        } else {
            messageIter = context.getMessages();
        }
        return messageIter;
    }
}
