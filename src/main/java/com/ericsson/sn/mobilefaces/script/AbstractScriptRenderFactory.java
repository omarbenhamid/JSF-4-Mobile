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
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *      ScriptRenderFactory is to generate
 *      {@link com.ericsson.sn.mobilefaces.script.ScriptRenderer}
 *      for current device.
 * </p>
 * <p>
 *      At first, loadScriptRenderer() will load ScriptRenderer and do some initialization works.
 *      Then calling getScriptRenderer() will return the current script renderer.
 * </p>
 * <p>
 *      ScriptFactoryRI is the reference implementation of this abstract class.
 * </p>
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public abstract class AbstractScriptRenderFactory {

    /**
     * <p>
     *      Load ScriptRenderer and do initialization for current device.
     * </p>
     *
     * @param servletContext Servlet Context to get system information
     * @param request HttpServletRequest to get end device information
     */
    public abstract void loadScriptRenderer(ServletContext servletContext, HttpServletRequest request);

    /**
     * <p>
     *      Get script renderer for current request device.
     *      Script renderer is responsible to render the script.
     * </p>
     *
     * @return ScriptRenderer for current device
     */
    public abstract ScriptRenderer getScriptRenderer();
}
