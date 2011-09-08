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

import java.util.Collection;
import java.util.Iterator;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ericsson.sn.mobilefaces.util.Log;
import com.ericsson.sn.mobilefaces.util.PropLoader;
import com.ericsson.sn.mobilefaces.util.PropertyContainer;
import com.ericsson.sn.mobilefaces.util.SimpleXMLReader;

/**
 * <p>
 * Reference implementation of AbstractDeviceDataFactory. This factory will read
 * device feature information from a xml file, select current device based on
 * user-agent and save current device object in the session. Return current
 * device object from session when calling getCurrentDevice().
 * </p>
 * 
 * <p>
 * Company: Ericsson AB
 * </p>
 * 
 * 
 * @author Daning Yang
 * 
 * @version 1.1
 */
public class DeviceDataFactoryRI extends AbstractDeviceDataFactory {

	private static final String DEVICE_SESSION_KEY = "com.ericsson.sn.mobilefaces.device.DeviceDataFactoryRI.Device";

	/**
	 * @see com.ericsson.sn.mobilefaces.device.AbstractDeviceDataFactory#detectDevice(ServletContext
	 *      servletContext, HttpServletRequest request)
	 */
	@Override
	public void detectDevice(ServletContext servletContext,
			HttpServletRequest request) {
		if (Log.doAssert(servletContext,
				"Cannot find Servlet Context when initDevice()",
				DeviceDataFactoryRI.class)) {
			initSessionDevice(servletContext, request);
		}
	}

	/**
	 * @see com.ericsson.sn.mobilefaces.device.AbstractDeviceDataFactory#getCurrentDevice()
	 */
	@Override
	public DeviceData getCurrentDevice() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();

		return getCurrentDevice(request);
	}

	/**
	 * 
	 * @see com.ericsson.sn.mobilefaces.device.AbstractDeviceDataFactory#getCurrentDevice(HttpServletRequest
	 *      request)
	 * 
	 */
	@Override
	public DeviceData getCurrentDevice(HttpServletRequest request) {
		return initSessionDevice(request.getServletContext(), request);
	}

	// Load current device object to session
	private DeviceData initSessionDevice(ServletContext servletContext,
			HttpServletRequest request) {
		HttpSession session = request.getSession();

		DeviceData sessionDevice = null;
		try {
			sessionDevice = (DeviceData) session
					.getAttribute(DEVICE_SESSION_KEY);
			if (Log.isDebug()) {
				// Check for user agent change !
				String ua = request.getHeader("user-agent");
				if (sessionDevice != null && ua != null
						&& !ua.contains(sessionDevice.getAgent()))
					sessionDevice = null;
			}
		} catch (Exception e) {
		}

		// Initialize device only when no device object in session
		if (sessionDevice == null) {
			String useragent = request.getHeader("user-agent");
			// Find current device based on user-agent
			sessionDevice = findDevice(servletContext, request);
			if (Log.doAssert(sessionDevice, "Cannot find device for: "
					+ useragent, DeviceDataFactoryRI.class)) {
				Log.info("Init device for: " + useragent,
						DeviceDataFactoryRI.class);
				session.setAttribute(DEVICE_SESSION_KEY, sessionDevice);
			}
		}
		return sessionDevice;
	}

	// Find current device based on user-agent
	private DeviceData findDevice(ServletContext servletContext,
			HttpServletRequest request) {
		// Read the user-agent from request
		String useragent = request.getHeader("user-agent");

		// Find device define file path from mfaces-config
		SimpleXMLReader config = new SimpleXMLReader(
				servletContext.getRealPath("/")
						+ servletContext.getInitParameter(MCONFIG_NAME));
		String define_path = config.getString(DEVICE_DEFINE_PATH);

		// Load device feature xml file
		PropLoader propLoader = new PropLoader(DEVICE_FEATURE_XML_FLAG);
		Collection deviceCollection = propLoader.parse(servletContext
				.getRealPath("/") + define_path);
		Iterator iterator = deviceCollection.iterator();

		DeviceData result = null;
		DeviceData def = null;

		// Find required device object and default device object
		while (iterator.hasNext()) {
			PropertyContainer d = (PropertyContainer) iterator.next();

			if (useragent.indexOf(d.getProperty(DeviceData.USER_AGENT)) != -1) {
				result = new DeviceDataImpl(d.getTable());
				break;
			} else if (d.getProperty(DeviceData.USER_AGENT).equals("default")) {
				def = new DeviceDataImpl(d.getTable());
			}
		}

		// If find required device, return it. Or return default device.
		if (result == null) {
			Log.info("Cannot find device object, using default device!",
					DeviceDataFactoryRI.class);
			return def;
		} else {
			Log.info(
					"Find device object: "
							+ result.getProperty(DeviceData.DEVICE_NAME),
					DeviceDataFactoryRI.class);
			return result;
		}
	}

	// DeviceData feature xml file element tag.
	private static final String DEVICE_FEATURE_XML_FLAG = "device";
	// MobileFaces config name
	private static final String MCONFIG_NAME = "ericsson.mobilefaces.CONFIG_FILE";
	// DeviceData feature xml file path in config xml file
	private static final String DEVICE_DEFINE_PATH = "mfaces-config/configuration-file/devices";
}
