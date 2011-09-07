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

import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import javax.servlet.ServletContext;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

import com.ericsson.sn.mobilefaces.util.RenderUtils;

/**
 * <p>
 *      WMLScript renderer for wmlscript.
 * </p>
 *
 * <p>
 *      WMLScript should be written in an extern wmls file.
 *      Then the function is called by "filename.wmls#function()"
 *      in the component.
 * </p>
 *
 * <p>
 *      WMLScript renderer renders the reference in two kind modes. <br />
 *      One is to replace the "href" attribute with the script reference
 *      for "button" and "link" components. "onclick", "ondblclick", "onkeydown",
 *      "onkeypress", "onsubmit" will be rendered as this mode. <br />
 *      The other is to add "onpick" attribute for the "select" component. "onchange"
 *      will be rendered as this mode.
 * </p>
 *
 * <p>
 *      There are two kind wmlscript source file: .wmls for source code
 *      and .wmlscriptc for compiled script file.
 * </p>
 * <p>Company:   Ericsson AB</p>
 *
 * @author Daning Yang
 * @version 1.0
 */
public class WmlScriptRenderer implements ScriptRenderer {

    private String file = null;

    /**
     * <p>Write nothing. Just save the file path for further render.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @return null
     */
    public String encodeScriptHead(FacesContext context, UIComponent component) {
        String src = null;

        ServletContext servletContext = ((ServletContext)context.getExternalContext().getContext());
        String wsc = (String)servletContext.getAttribute(LangConstant.WMLSCRIPTC);

        // check the wmlscriptc attribute to decide using wmls file or wmlscriptc file
        if (wsc != null && wsc.equals("true"))
            src = (String) component.getAttributes().get("SCRIPT_SRC") + ".wmls";
        else
            src = (String) component.getAttributes().get("SCRIPT_SRC") + ".wmlscriptc";
        // clear the wmlscriptc attribute
        servletContext.setAttribute(LangConstant.WMLSCRIPTC, "false");

        src = context.getApplication().getViewHandler().getResourceURL(context, src);
        file = src;
        return null;
    }

    /**
     * <p>Check the render mode by finding "onpick" attribute from the component.
     *    Return the script reference without writting it directly.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @return if there is script, return the reference. Or return null.
     */
    public String encodeScript(FacesContext context, UIComponent component) {
        String result = null;

        // Get "onpick" attribute from the component
        String isOnPick = (String) component.getAttributes().get(LangConstant.ATT_ONPICK);

        // The command mode
        if (isOnPick != null && isOnPick.equalsIgnoreCase("true")) {
            // Check onchange attribute
            String value = (String)component.getAttributes().get(LangConstant.ATT_ONCHANGE);
            if (value != null && !value.equals("") && !RenderUtils.isDefaultValue(value)) {
                // construct the url with script file name and method name
                result = file + "#" + convertMethod(value);
            } else {
                result = null;
            }

        } else { // The select mode
            // Check available attributes
            for (int i=0; i<scriptAttributes.length; i++) {
                String value = (String)component.getAttributes().get(scriptAttributes[i]);

                if (value != null && !value.equals("") && !RenderUtils.isDefaultValue(value)) {
                    // construct the url with script file name and method name
                    result = file + "#" + convertMethod(value);
                }
            }
        }
        return result;
    }

    // Remove the exclude string different from javascript
    private String convertMethod(String base) {
        if (base == null)
            return null;
        // check all exclude strings
        for (int i=0; i<excludeStr.length; i++) {
            if (base.indexOf(excludeStr[i]) != -1) {
                // remove it
                int begin = base.indexOf(excludeStr[i]);
                int end = base.indexOf(excludeStr[i]) + excludeStr[i].length();
                base = base.substring(0,begin) + base.substring(end+1);
                base = base.trim();
            }
        }
        return base;
    }

    // exclude string
    private String excludeStr[] = {
        "return"
    };

    // command script attributes
    private String scriptAttributes[] = {
        "onclick",
        "ondblclick",
        "onkeydown",
        "onkeypress",
        "onsubmit"
    };

    // script type
    private final static String TYPE = "text/wmlscript";
}
