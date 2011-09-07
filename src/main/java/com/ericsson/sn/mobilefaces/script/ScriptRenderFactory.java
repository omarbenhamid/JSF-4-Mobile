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

import javax.servlet.ServletContext;

import com.ericsson.sn.mobilefaces.util.Log;
import com.ericsson.sn.mobilefaces.util.SimpleXMLReader;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *      ScriptRenderFactory Proxy.
 *      This is the proxy of AbstractScriptRenderFactory to generate a ScriptRenderer
 * </p>
 * <p>
 *      If user defines a custom ScriptRenderFactory, this proxy will use the
 *      custom Script factory to create Script. If no custom Script factory, this agent will use
 *      ScriptRenderFactoryRI as default factory to create Script object.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public class ScriptRenderFactory {

    private static AbstractScriptRenderFactory scrfact;

    // Singleton, it is not allowed to create instantce.
    private ScriptRenderFactory() {}

    /**
     * <p>
     *      Create the concrete script factory.
     *      Calling concrete script factory initialization.
     * </p>
     * @param servletContext Servlet Context to get system information
     * @param request HttpServletRequest to get end device information
     */
    public static void loadScriptRenderer(ServletContext servletContext, HttpServletRequest request) {
        // Create concrete Script factory
        createFactory(servletContext);
        // Initialize concrete Script factory
        scrfact.loadScriptRenderer( servletContext, request);
    }

    /**
     * <p>
     *      Get script renderer for current device.
     * </p>
     *
     * @return current script renderer
     */
    public static ScriptRenderer getScriptRenderer() {
        // Use concrete factory to get current script renderer.
        ScriptRenderer s = scrfact.getScriptRenderer();
        Log.doAssert(s, "Cannot find current ScriptRenderer!", ScriptRenderFactory.class);
        return s;
    }

    // Create script factory by reference implementation or custom factory.
    private static void createFactory(ServletContext servletContext) {
        // Only create once
        if (scrfact == null) {
            // Get config file path from context
            if (Log.doAssert(servletContext, "Cannot find Servlet Context!", ScriptRenderFactory.class)) {
                // Read the config file
                SimpleXMLReader config = new SimpleXMLReader(servletContext.getRealPath("/") + servletContext.getInitParameter(MCONFIG_NAME));
                // Get custom script factory class name
                String classname = config.getString(SCRIPT_FACTORY_PATH);
                if (classname == null) {
                    // if no custom factory, use ScriptRenderFactoryRI as default
                    scrfact = new ScriptRenderFactoryRI();
                    Log.info("Initialize default Script factory.", ScriptRenderFactory.class);
                } else {
                    // if has custom factory, load custom ScriptRenderFactory
                    try {
                        Class c = Class.forName(classname);
                        scrfact =  (AbstractScriptRenderFactory)c.newInstance();
                        Log.info("Initialize custom Script factory: " + classname, ScriptRenderFactory.class);
                    } catch ( Exception e ) {
                        Log.err("Cannot find custom Script factory.", ScriptRenderFactory.class);
                        Log.err(e, ScriptRenderFactory.class);
                    }
                }
            }
        }
    }

    // Config file path reference in context
    private static final String MCONFIG_NAME = "ericsson.mobilefaces.CONFIG_FILE";
    // Custom script factory xml path in config file
    private static final String SCRIPT_FACTORY_PATH = "mfaces-config/factory/script-render-factory";
}
