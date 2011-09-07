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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.faces.context.FacesContext;

import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.util.Log;
import com.ericsson.sn.mobilefaces.util.PropLoader;
import com.ericsson.sn.mobilefaces.util.PropertyContainer;
import com.ericsson.sn.mobilefaces.device.DeviceDataFactory;

/**
 * <p>
 *      Reference implementation of AbstractScriptRenderFactory.
 * </p>
 *
 * <p>
 *      In the initScriptRenderer(), this factory will load script renderer from
 *      a custom class or default class and save it in the session.
 *      Then return the script renderer from session when calling getScriptRenderer();
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 *
 * @author Daning Yang
 * 
 * @version 1.1
 */
public class ScriptRenderFactoryRI extends AbstractScriptRenderFactory{

    /**
     * @see com.ericsson.sn.mobilefaces.script.AbstractScriptRenderFactory#loadScriptRenderer(ServletContext servletContext, HttpServletRequest request)
     */
    @Override
    public void loadScriptRenderer(ServletContext servletContext, HttpServletRequest request) {
        if(Log.doAssert(servletContext, "Cannot find Servlet Context when initScriptRenderer()", ScriptRenderFactoryRI.class)) {
            initSession(servletContext, request);
        }
    }

    /**
     * @see com.ericsson.sn.mobilefaces.script.AbstractScriptRenderFactory#getScriptRenderer()
     */
    @Override
    public ScriptRenderer getScriptRenderer() {
        // Get session
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        HttpSession session = request.getSession();

        // Read current script renderer from session
        ScriptRenderer sr = null;
        try {
            sr = (ScriptRenderer)session.getAttribute("ScriptRenderer");
        } catch (Exception e){ }
        if (sr == null){
            ServletContext servletContext = (ServletContext)context.getExternalContext().getContext();
            initSession(servletContext, request);
            sr = (ScriptRenderer)session.getAttribute("ScriptRenderer");
        }
        Log.doAssert(sr, "Cannot find Script Renderer!", ScriptRenderFactoryRI.class);
        return sr;
    }

    // Load script renderer for current device
    private void initSession(ServletContext servletContext, HttpServletRequest request) {
        // Get session
        HttpSession session = request.getSession();

        Object sessionScriptRenderer = null;
        try {
            sessionScriptRenderer = session.getAttribute("ScriptRenderer");
        } catch (Exception e){ }

        // Initialize script renderer only when no renderer in session
        if (sessionScriptRenderer == null) {
            // Get support script type from Device object.
            String script = DeviceDataFactory.getCurrentDevice(request).getScript();
            // Find the renderer for the script
            ScriptRenderer renderer = findRenderer(servletContext, script);
            // If cannot find the renderer, use the "No Operation" Renderer.
            if (renderer == null) {
                Log.debug("Use the NoopScriptRenderer.", ScriptRenderFactoryRI.class);
                renderer = new NoopScriptRenderer();
            }
            // Save to the session
            session.setAttribute("ScriptRenderer", renderer);
        }
    }

    // Find a renderer from custom class or default class
    private ScriptRenderer findRenderer(ServletContext servletContext, String script) {
        String classname = null;
        ScriptRenderer sr = null;
        // Set default script renderer classes
        HashMap<String, String> rendererPool = new HashMap<String, String>();
        rendererPool.put(LangConstant.JAVASCRIPT, "com.ericsson.sn.mobilefaces.script.JavaScriptRenderer");
        rendererPool.put(LangConstant.ECMASCRIPTMP, "com.ericsson.sn.mobilefaces.script.EcmaMPScriptRenderer");
        rendererPool.put(LangConstant.WMLSCRIPT, "com.ericsson.sn.mobilefaces.script.WmlScriptRenderer");
        rendererPool.put(LangConstant.WMLSCRIPTC, "com.ericsson.sn.mobilefaces.script.WmlScriptRenderer");
        // load the config file
        PropLoader rendererLoader = new PropLoader(SCRIPT_RENDERER_NAME);
        Collection rendererCollection = rendererLoader.parse(servletContext.getRealPath("/") + servletContext.getInitParameter(MCONFIG_NAME) );
        Iterator iterator = rendererCollection.iterator() ;
        // find the custom script renderer for current script
        while(iterator.hasNext()) {
            PropertyContainer r = (PropertyContainer)iterator.next();
            String type = r.getProperty("type");
            if (type.equalsIgnoreCase(script)) {
                classname = r.getProperty("class");
                break;
            }
        }
        // if there is no custom renderer, try to use default renderer
        if (classname == null)
            classname = rendererPool.get(script);

        // if cannot use default renderer, return null
        if (classname == null)
            return null;
        else {
            try {
                Log.debug("Load the ScriptRenderer: " + classname + " for " + script + ".", ScriptRenderFactoryRI.class);
                Class c = Class.forName(classname);
                sr =  (ScriptRenderer)c.newInstance();
                // add wmlscriptc attribute if the device only support compiled wmlscript.
                if (script.equals("wmlscriptc"))
                    servletContext.setAttribute("wmlscriptc","true");

            } catch ( Exception e ) {
                Log.info("Cannot find Script Renderer for " + script + ".", ScriptRenderFactoryRI.class);
                Log.info(e, ScriptRenderFactoryRI.class);
            }
        }
        return sr;
    }

    // MobileFaces config name
    private static final String MCONFIG_NAME = "ericsson.mobilefaces.CONFIG_FILE";
    // Script renderer config name
    private static final String SCRIPT_RENDERER_NAME = "script-renderer";
}
