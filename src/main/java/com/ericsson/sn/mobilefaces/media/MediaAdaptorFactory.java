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
package com.ericsson.sn.mobilefaces.media;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.ericsson.sn.mobilefaces.util.Log;
import com.ericsson.sn.mobilefaces.util.SimpleXMLReader;

/**
 * <p>
 *      MediaAdaptorFactory Proxy.
 *      This is the proxy of AbstractMediaAdaptorFactory.
 * </p>
 * <p>
 *      It will generate a MediaAdaptorFactory based on config file.
 *      If user defines a custom MediaAdaptorFactory, this proxy will use the
 *      custom MediaAdaptorFactory to create MediaAdaptor.
 *      If no custom MediaAdaptorFactory, this agent will use
 *      MediaAdaptorFactoryRI as default factory to create MediaAdaptor.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @author Daning Yang
 * 
 * @version 1.1
 */
public class MediaAdaptorFactory {

    // Singleton concrete factory instance
    private static AbstractMediaAdaptorFactory adaptorFactory;

    // Singleton, it is not allowed to create multiple instances.
    private MediaAdaptorFactory() {}

    /**
     * <p>
     *      Create the concrete MediaAdaptorFactory based on config file and initializate it.
     * </p>
     *
     * @param servletContext Servlet Context to get system information
     * @param request HttpServletRequest to get end device information
     */
    public static void loadAdaptor(ServletContext servletContext, HttpServletRequest request) {
        // Create the concrete MediaAdaptorFactory
        initFactory(servletContext);
        // Initialize the MediaAdaptorFactory
        adaptorFactory.loadAdaptor( servletContext, request);
    }

    /**
     * <p>
     *      Use concrete MediaAdaptorFactory to generate the MediaAdaptor
     *      based on media type and strategy.
     * </p>
     * @return suitable MediaAdaptor object
     * @param strategy strategy name
     * @param mediaType media type
     */
    public static MediaAdaptor getAdaptor(String mediaType, String strategy) {
        // Use concrete factory to get current MediaAdaptor.
        MediaAdaptor d = adaptorFactory.getAdaptor(mediaType, strategy);
        Log.doAssert(d, "Cannot find MediaAdaptor for " + mediaType, MediaAdaptorFactory.class);
        return d;
    }

    /**
     * <p>
     *      Use concrete MediaAdaptorFactory to generate the MediaAdaptor
     *      and do the adaptation.
     * </p>
     *
     *
     * @return adapted media file path
     * @param strategyStr candidate strategy string
     * @param mediaType media type
     * @param inFile original media file path
     */
    public static String doAdapt(String mediaType, String inFile, String strategyStr) {
        return adaptorFactory.doAdapt(mediaType, inFile, strategyStr);
    }

    // Create media factory by reference implementation or custom factory.
    private static void initFactory(ServletContext servletContext) {
        // Only initialize once
        if (adaptorFactory == null) {
            // Get config file path from context
            if (Log.doAssert(servletContext, "Cannot find Servlet Context!", MediaAdaptorFactory.class)) {
                // Read the config file
                SimpleXMLReader config = new SimpleXMLReader(servletContext.getRealPath("/") + servletContext.getInitParameter(MCONFIG_NAME));
                // Get custom MediaAdaptorFactory class name
                String classname = config.getString(DEVICE_FACTORY_PATH);
                if (classname ==  null) {
                    // if no custom factory, use MediaAdaptorFactoryRI as default
                    adaptorFactory = new MediaAdaptorFactoryRI();
                    Log.info("Initialize default MediaAdaptorFactory.", MediaAdaptorFactory.class);
                } else {
                    // if has custom factory, load custom MediaAdaptorFactory
                    try {
                        Class c = Class.forName(classname);
                        adaptorFactory =  (AbstractMediaAdaptorFactory)c.newInstance();
                        Log.info("Initialize custom MediaAdaptorFactory: " + classname, MediaAdaptorFactory.class);
                    } catch ( Exception e ) {
                        Log.err("Cannot find custom MediaAdaptorFactory.", MediaAdaptorFactory.class);
                        Log.err(e, MediaAdaptorFactory.class);
                    }
                }
            }
        }
    }

    // Config file path reference in context
    private static final String MCONFIG_NAME = "ericsson.mobilefaces.CONFIG_FILE";
    // Custom MediaAdaptorFactory xml path in config file
    private static final String DEVICE_FACTORY_PATH = "mfaces-config/factory/media-adaptor-factory";
}
