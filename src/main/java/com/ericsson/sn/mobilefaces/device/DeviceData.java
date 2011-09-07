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

import java.util.Hashtable;

import com.ericsson.sn.mobilefaces.util.PropertyContainer;

/**
 * <p>
 *      DeviceData object contains device feature information.
 *      DeviceData object with current device feature can be generated by DeviceFactory.
 *      Using setProperty() to set the parameter and using getProperty() to get the parameter.
 *      The basic features can be read by getter directly.
 *  </p>
 *  <p>
 *      Every device object should be initialized for at least five properties: name,
 *      support markup language, user-agent, script , images and screen level. The name of these six
 *      Propertys should be referenced from the Constant fields in this class.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @author Daning Yang
 * @version 1.1
 */
public abstract class DeviceData extends PropertyContainer {

    /**
     * <p>Creates a new instance of DeviceData object with empty table.</p>
     */
    public DeviceData() {
        super();
    }

    /**
     * <p>Creates a new instance of DeviceData object with initialization property table.</p>
     * @param property initialization property table
     */
    public DeviceData(Hashtable<String, String> property ) {
        super.property = property;
    }

    /**
     * <p>Get device name.</p>
     * @return device name
     */
    public abstract String getName();

    /**
     * <p>Get support markup language.</p>
     * @return support markup language
     */
    public abstract String getMarkup();

    /**
     * <p>Get user agent.</p>
     * @return user agent
     */
    public abstract String getAgent();

    /**
     * <p>Get screen level.</p>
     * @return screen level
     */
    public abstract String getScreen();

    /**
     * <p>Get support script.</p>
     * @return support script
     */
    public abstract String getScript();

    /**
     * <p>Get support image list.</p>
     * @return support image list
     */
    public abstract String getImages();

    /**
     * <p> name </p>
     */
    public static final String DEVICE_NAME = "DEVICE_NAME";

    /**
     * <p> markup language support </p>
     */
    public static final String MARKUP = "MARKUP";

    /**
     * <p> user-agent </p>
     */
    public static final String USER_AGENT = "USER_AGENT";

    /**
     * <p> screen level </p>
     */
    public static final String SCREEN_LEVEL = "SCREEN_LEVEL";

    /**
     * <p> script support </p>
     */
    public static final String SCRIPT = "SCRIPT";

    /**
     * <p> image support </p>
     */
    public static final String IMAGE = "IMAGE_SUPPORT";
}
