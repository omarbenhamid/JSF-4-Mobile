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

/**
 * <p>
 *      AbstractDeviceDataFactory is to generate the
 *      {@link com.ericsson.sn.mobilefaces.device.DeviceData}.
 * <p>
 * </p>
 *      At first, call detectDevice() to do some initialization works.
 *      Then calling getCurrentDevice() will return the current device object.
 * <p>
 * </p>
 *      DeviceFactoryRI is the reference implementation of this abstract class.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @author Daning Yang
 * @version 1.0
 */
public abstract class AbstractDeviceDataFactory {

    /**
     * <p>
     *      Detect current device and initialize DeviceData object.
     * </p>
     *
     * @param servletContext Servlet Context to get system information
     * @param request HttpServletRequest to get end device information
     */
    public abstract void detectDevice(ServletContext servletContext, HttpServletRequest request);

    /**
     * <p>
     *      Get device object for current request device.
     *      DeviceData object contains the device feature information.
     * </p>
     *
     *
     * @return current DeviceData object
     */
    public abstract DeviceData getCurrentDevice();

    /**
     * <p>
     *      Get DeviceData object for current request device from specific request.
     * </p>
     * @return current DeviceData object
     * @param request HttpServletRequest to get end device information
     */
    public abstract DeviceData getCurrentDevice(HttpServletRequest request);
}
