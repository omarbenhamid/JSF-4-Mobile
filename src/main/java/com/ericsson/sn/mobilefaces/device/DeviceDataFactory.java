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
package com.ericsson.sn.mobilefaces.device;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.ericsson.sn.mobilefaces.util.Log;
import com.ericsson.sn.mobilefaces.util.SimpleXMLReader;

/**
 * <p>
 *      DeviceDataFactory Proxy.
 *      This is the agent of DeviceDataFactory. It will generate a device factory
 *      based on config file. If user defines a custom device factory, this agent will use the
 *      custom device factory to create DeviceData object. If no custom device factory,
 *      this agent will use
 *      {@link com.ericsson.sn.mobilefaces.device.DeviceDataFactoryRI}
 *      as default factory to create device object.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @author Daning Yang
 * 
 * @version 1.1
 */
public class DeviceDataFactory {

    private static AbstractDeviceDataFactory deviceFactory;

    // Singleton, it is not allowed to create instantce.
    private DeviceDataFactory() {}


    /**
     * <p>
     *      Initialization the concrete DeviceDataFactory.
     *      Calling concrete DeviceDataFactory initialization.
     * </p>
     * @param servletContext Servlet Context to get system information
     * @param request HttpServletRequest to get end device information
     */
    public static void detectDevice(ServletContext servletContext, HttpServletRequest request) {
        // Create concrete device factory
        createFactory(servletContext);
        // Initialize concrete device factory
        deviceFactory.detectDevice( servletContext, request);
    }

    /**
     * <p>
     *      Get DeviceData object.
     *      DeviceData object contains the device feature information.
     * </p>
     *
     * @return current DeviceData object
     */
    public static DeviceData getCurrentDevice() {
        // Use concrete factory to get current DeviceData.
        DeviceData d = deviceFactory.getCurrentDevice();
        Log.doAssert(d, "Cannot find current device!", DeviceDataFactory.class);
        return d;
    }

    /**
     * <p>
     *      Get device object from specific request for current device.
     * </p>
     * @return current DeviceData object
     * @param request HttpServletRequest to get end device information
     */
    public static DeviceData getCurrentDevice(HttpServletRequest request) {
        // Use concrete factory to get current device.
        DeviceData d = deviceFactory.getCurrentDevice(request);
        Log.doAssert(d, "Cannot find current device!", DeviceDataFactory.class);
        return d;
    }

    // Create device factory by reference implementation or custom factory.
    private static void createFactory(ServletContext servletContext) {
        // Only create once
        if (deviceFactory == null) {
            // Get config file path from context
            if (Log.doAssert(servletContext, "Cannot find Servlet Context!", DeviceDataFactory.class)) {
                // Read the config file
                SimpleXMLReader config = new SimpleXMLReader(servletContext.getRealPath("/") + servletContext.getInitParameter(MCONFIG_NAME));
                // Get custom device factory class name
                String classname = config.getString(DEVICE_FACTORY_PATH);
                if (classname ==  null) {
                    // if no custom factory, use DeviceDataFactoryRI as default
                    deviceFactory = new DeviceDataFactoryRI();
                    Log.info("Initialize default device factory.", DeviceDataFactory.class);
                } else {
                    // if has custom factory, load custom DeviceDataFactory
                    try {
                        classname = classname.trim();
                        Class c = Class.forName(classname);
                        deviceFactory =  (AbstractDeviceDataFactory)c.newInstance();
                        Log.info("Initialize custom device factory: " + classname, DeviceDataFactory.class);
                    } catch ( Exception e ) {
                        Log.err("Cannot find custom device factory.", DeviceDataFactory.class);
                        Log.err(e, DeviceDataFactory.class);
                    }
                }
            }
        }
    }

    // Config file path reference in context
    private static final String MCONFIG_NAME = "ericsson.mobilefaces.CONFIG_FILE";
    // Custom device factory xml path in config file
    private static final String DEVICE_FACTORY_PATH = "mfaces-config/factory/device-data-factory";
}
