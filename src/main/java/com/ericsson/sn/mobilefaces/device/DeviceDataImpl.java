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

import com.ericsson.sn.mobilefaces.util.Log;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;

/**
 * <p>
 *      DeviceDataImpl is the implementation of
 *      {@link com.ericsson.sn.mobilefaces.device.DeviceData}
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 *
 * @author Daning Yang
 * @version 1.1
 */
public class DeviceDataImpl extends DeviceData {
    /**
     * <p>Creates a new instance of DeviceData object with empty table.</p>
     */
    public DeviceDataImpl() {
        super();
    }

    /**
     * <p>Creates a new instance of DeviceData object with initialization property table.</p>
     * @param property initialization property table
     */
    public DeviceDataImpl(Hashtable<String, String> property ) {
        super(property);
    }

    /**
     * <p>Get device name.</p>
     * @return device name
     */
    @Override
    public String getName() {
        return super.getProperty(DEVICE_NAME);
    }

    /**
     * <p>Get support markup language.</p>
     * @return support markup language
     */
    @Override
    public String getMarkup() {
        return super.getProperty(MARKUP);
    }

    /**
     * <p>Get user agent.</p>
     * @return user agent
     */
    @Override
    public String getAgent() {
        return super.getProperty(USER_AGENT);
    }

    /**
     * <p>Get screen level.</p>
     * @return screen level
     */
    @Override
    public String getScreen() {
        return super.getProperty(SCREEN_LEVEL);
    }

    /**
     * <p>Get support script.</p>
     * @return support script
     */
    @Override
    public String getScript() {
        return super.getProperty(SCRIPT);
    }

    /**
     * <p>Get support image list.</p>
     * @return support image list
     */
    @Override
    public String getImages() {
        return super.getProperty(IMAGE);
    }

    // Test case
    public static void main(String[] arg) {
        // Initialize basic device properties.
        DeviceData device = new DeviceDataImpl();
        device.setProperty(DeviceData.DEVICE_NAME, "Sony Ericsson K700");
        device.setProperty(DeviceData.MARKUP, LangConstant.MARKUP_XHTML_MP);
        device.setProperty(DeviceData.USER_AGENT, "SEK700");
        device.setProperty(DeviceData.SCREEN_LEVEL, "5");
        device.setProperty(DeviceData.SCRIPT, LangConstant.JAVASCRIPT);
        device.setProperty(DeviceData.IMAGE, "jpg;gif;png");

        // Load device properties.
        Log.info("===DeviceData Demo===", DeviceData.class);
        Log.info("Device name: " + device.getName(), DeviceData.class);
        Log.info("Device markup support: " + device.getMarkup(), DeviceData.class);
        Log.info("Device user-agent: " + device.getAgent(), DeviceData.class);
        Log.info("Device screen level: " + device.getScreen(), DeviceData.class);
        Log.info("Device support script: " + device.getScript(), DeviceData.class);
        Log.info("Device support images: " + device.getImages(), DeviceData.class);
    }
}
