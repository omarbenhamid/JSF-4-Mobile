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

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.util.Log;
import com.ericsson.sn.mobilefaces.util.RenderUtils;

/**
 * <p>
 *      EcmaMPScript renderer for ecmascript mobile profile.
 * </p>
 *
 * <p>
 *      ecmascript-mp should be written in an extern .es file.
 *      There is a file reference with <code>script</code> tag in the head.
 *      Then the function is called by "function()" in the component.
 * </p>
 *
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @author Daning Yang
 * @version 1.0
 */
public class EcmaMPScriptRenderer implements ScriptRenderer {

    /**
     * <p>Write es file reference with <code>script</code> tag.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @return null
     */
    public String encodeScriptHead(FacesContext context, UIComponent component) {
        ResponseWriter writer = context.getResponseWriter();
        // encode the script file path
        String src = (String) component.getAttributes().get("SCRIPT_SRC") + ".es";
        src = context.getApplication().getViewHandler().getResourceURL(context, src);

        if (writer != null && src != null) {
            // Write the script component in the head
            try {
                writer.startElement("script", component);
                writer.writeAttribute("type", TYPE, "type");
                writer.writeAttribute("src", src , "src");
                writer.endElement("script");
            } catch (IOException ex) {
                Log.err("Failed to write script tag!", EcmaMPScriptRenderer.class);
                Log.err(ex, EcmaMPScriptRenderer.class);
            }
        }
        return null;
    }

    /**
     * <p>Write script attributes if need.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @return null
     */
    public String encodeScript(FacesContext context, UIComponent component) {
        ResponseWriter writer = context.getResponseWriter();

        // check all script attributes
        for (int i=0; i<scriptAttributes.length; i++) {
            String value = (String)component.getAttributes().get(scriptAttributes[i]);
            // write it if need
            if (value != null && !value.equals("") && !RenderUtils.isDefaultValue(value)) {
                try {
                    writer.writeAttribute(scriptAttributes[i], value, scriptAttributes[i]);
                } catch (IOException ex) {
                    Log.err("Failed to write script attribute!", EcmaMPScriptRenderer.class);
                    Log.err(ex, EcmaMPScriptRenderer.class);
                }
            }
        }

        return null;
    }

    // script attributes
    private String scriptAttributes[] = {
        "onblur",
        "onchange",
        "onclick",
        "ondblclick",
        "onfocus",
        "onkeydown",
        "onkeypress",
        "onkeyup",
        "onload",
        "onmousedown",
        "onmousemove",
        "onmouseout",
        "onmouseover",
        "onmouseup",
        "onreset",
        "onselect",
        "onsubmit",
        "onunload"
    };

    // script type
    private final static String TYPE = "text/ecmascript";
}
