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
package com.ericsson.sn.mobilefaces.taglib;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.HttpServletRequest;
import javax.faces.context.FacesContext;
import javax.el.ValueExpression;

import com.ericsson.sn.mobilefaces.util.Log;
import com.ericsson.sn.mobilefaces.device.DeviceData;
import com.ericsson.sn.mobilefaces.device.DeviceDataFactory;

/**
 * <p>
 *      FilterTag is a standard JSP custom tag
 *      for filtering content to adapt different end device.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @author Daning Yang
 * 
 * @version 1.1
 */
public class FilterTag extends TagSupport {

    private ValueExpression markup = null;
    private ValueExpression screen = null;
    private ValueExpression device = null;
    private ValueExpression script = null;
    private ValueExpression skip = null;

    /**
     * <p>Set markup language condition.</p>
     * @param markup markup language
     */
    public void setMarkup(ValueExpression markup) {
        this.markup = markup;
    }

    /**
     * <p>Set screen level condition.</p>
     * @param screen screen level
     */
    public void setScreen(ValueExpression screen) {
        this.screen = screen;
    }

    /**
     * <p>Set device name condition.</p>
     * @param device device name
     */
    public void setDevice(ValueExpression device) {
        this.device = device;
    }

    /**
     * <p>Set script condition.</p>
     * @param script script
     */
    public void setScript(ValueExpression script){
        this.script = script;
    }

    /**
     * <p>Set skip or not when satisfied the conditions.</p>
     * @param skip skip or not
     */
    public void setSkip(ValueExpression skip) {
        this.skip = skip;
    }

    // Do start tag
    @Override
    public int doStartTag() {
        if (isSkip()) {
            // Skip the body within the tag
            return(SKIP_BODY);

        } else {
            // NOT Skip the body within the tag
            return(EVAL_BODY_INCLUDE);
        }
    }

    // Estimate the conditions
    private boolean isSkip() {
        boolean doSkip = false;
        boolean checkConditions = false;

        // Get device object
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        DeviceData internalDevice = DeviceDataFactory.getCurrentDevice();
        if (!Log.doAssert(internalDevice, "Cannot find device object!", FilterTag.class)) {
            return false;
        }

        checkConditions = (checkMarkup(internalDevice) || checkDevice(internalDevice) ||
                           checkScreen(internalDevice) || checkScript(internalDevice));

        Object skipObject = skip.getValue(FacesContext.getCurrentInstance().getELContext());
        if (skipObject != null){
            doSkip = (Boolean)skipObject;
        }

        // Estimate the conditions
        if (checkConditions) {
            return doSkip;
        } else {
            return !doSkip;
        }
    }

    // Check markup language condition
    private boolean checkMarkup(DeviceData deviceData) {
        if (markup != null){
            Object markupObject = markup.getValue(FacesContext.getCurrentInstance().getELContext());
            if (markupObject != null) {
                return (((String)markupObject).indexOf(deviceData.getMarkup()) != -1);
            }
        }
        return false;
    }

    // Check device name condition
    private boolean checkDevice(DeviceData deviceData) {
        if (device != null){
            Object deviceObject = device.getValue(FacesContext.getCurrentInstance().getELContext());
            if (deviceObject != null) {
                return (((String)deviceObject).indexOf(deviceData.getName()) != -1);
            }
        }
        return false;
    }

    // Check screen level condition
    private boolean checkScreen(DeviceData deviceData) {
        if (screen != null){
            Object screenObject = screen.getValue(FacesContext.getCurrentInstance().getELContext());
            if (screenObject != null) {
                return (((String)screenObject).indexOf(deviceData.getScreen()) != -1);
            }
        }
        return false;
    }

    // Check script condition
    private boolean checkScript(DeviceData deviceData) {
        if (script != null){
            Object scriptObject = script.getValue(FacesContext.getCurrentInstance().getELContext());
            if (scriptObject != null) {
                return (((String)scriptObject).indexOf(deviceData.getScript()) != -1);
            }
        }
        return false;
    }
}
