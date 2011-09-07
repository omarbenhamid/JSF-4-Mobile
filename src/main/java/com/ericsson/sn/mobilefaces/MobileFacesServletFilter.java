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
package com.ericsson.sn.mobilefaces;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.ericsson.sn.mobilefaces.util.Log;
import com.ericsson.sn.mobilefaces.util.GetToPostRequestWrapper;
import com.ericsson.sn.mobilefaces.device.DeviceDataFactory;
import com.ericsson.sn.mobilefaces.media.MediaAdaptorFactory;
import com.ericsson.sn.mobilefaces.script.ScriptRenderFactory;

/**
 * <p>
 *      MobileFacesServletFilter is to initialize MobileFaces
 *      components before JSF lifecycle.
 * </p>
 * <p>
 *      This servlet filter should be setting
 *      the filter chain of container for MobileFaces
 *      entry  in web.xml.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @author       Daning Yang
 * @version      1.0
 */
public class MobileFacesServletFilter implements Filter {

    private ServletContext context = null;

    /**
     * <p>
     *     Initializes filter context and debug mode.
     *     This method decides using the debug mode
     *     or not via the FilterConfig parameter.
     * </p>
     * @param  fConfig the FilterConfig to get servlet context
     */
    public void init(FilterConfig fConfig) {
        // load the context
        context = fConfig.getServletContext();

        String debug = fConfig.getInitParameter("Debug");
        if (debug != null && debug.equalsIgnoreCase("true")) {
            Log.setDebug(true);
        } else {
            Log.setDebug(false);
        }
    }

    /**
     * <p> Initialize MobileFaces components.</p>
     * @param servletRequest ServletRequest
     * @param servletResponse ServletResponse
     * @param chain FilterChain
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = GetToPostRequestWrapper.transform((HttpServletRequest)servletRequest);
        DeviceDataFactory.detectDevice(context, httpServletRequest);
        MediaAdaptorFactory.loadAdaptor(context, httpServletRequest);
        ScriptRenderFactory.loadScriptRenderer(context, httpServletRequest);

        chain.doFilter(servletRequest, servletResponse );
    }

    // destroy this filter
    public void destroy() {
        // todo: e.g close all database connection.
    }
}
