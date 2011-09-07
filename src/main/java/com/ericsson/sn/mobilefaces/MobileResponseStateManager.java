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

import java.util.Map;
import java.io.IOException;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.ResponseStateManager;
import com.ericsson.sn.mobilefaces.device.DeviceDataFactory;
import com.ericsson.sn.mobilefaces.renderkit.share.LangConstant;
import com.ericsson.sn.mobilefaces.util.Base64;
import com.ericsson.sn.mobilefaces.util.Log;

/**
 * <p>
 *      MobileResponseStateManager to track the page state
 *      for most markup language based pages for mobile devices.
 * </p>
 * <p>
 *      MobileResponseStateManager uses only server-side state saving
 *      for WML and XHTML-MP since some mobile devices cannot handle a
 *      very long form field value.
 * </p>
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @author Daning Yang
 * @version 1.2
 */
public class MobileResponseStateManager extends ResponseStateManager {

    /**
     * <p> Create instance of MobileResponseStateManager. </p>
     */
    public MobileResponseStateManager() {
    }

    /**
     * Gets state from request parameter or from session (fallback).
     * @param context Faces context
     * @param viewId viewId
     */
    @Override
    public Object getState(FacesContext context, String viewId) {
        Map<String, String> requestParamsMap = context.getExternalContext().getRequestParameterMap();

        Object[] stateObj;
        stateObj = readState(requestParamsMap.get(VIEW_STATE_PARAM));
        if (stateObj == null){
            stateObj = readState((String)getSession(context).getAttribute(VIEW_STATE_PARAM));
        }
        if (stateObj == null) {
            Log.err("State object could not be restored.", this.getClass());
            stateObj = new Object[2];
        }

        return stateObj;
    }

    /**
     * Writes state to session and to page (if not WML or XHTML-MP).
     * @param context Faces context
     * @param state the state object
     */
    @Override
    public void writeState(FacesContext context, Object state) throws IOException {
        if (!(state instanceof Serializable)) throw new IOException();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(state);
        byte[] serializedState = baos.toByteArray();
        String serializedStateStr = Base64.encode(serializedState);

        getSession(context).setAttribute(VIEW_STATE_PARAM, serializedStateStr);

        String markup = DeviceDataFactory.getCurrentDevice().getMarkup();
        if (markup != null && !markup.equals(LangConstant.MARKUP_WML)) {
            ResponseWriter writer = context.getResponseWriter();
            writer.startElement(LangConstant.TAG_INPUT, null);
            writer.writeAttribute(LangConstant.ATT_TYPE, LangConstant.VALUE_HIDDEN, "type");
            writer.writeAttribute(LangConstant.ATT_NAME, VIEW_STATE_PARAM, "name");
            writer.writeAttribute(LangConstant.ATT_VALUE, markup.equals(LangConstant.MARKUP_XHTML_MP)?"":serializedStateStr, "value");
            writer.endElement(LangConstant.TAG_INPUT);
        }
    }

    /**
     * <p>Returns true if current request is a postback.</p>
     *
     * @param context Faces context
     * @return true if postback, otherwise false
     */
    @Override
    public boolean isPostback(FacesContext context) {
        return context.getExternalContext().getRequestParameterMap().containsKey(VIEW_STATE_PARAM);
    }

    // get the session from FacesContext
    private HttpSession getSession(FacesContext context) {
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return request.getSession(true);
    }

    // decode state object from serialized base64 encoded string, return null on failure
    private Object[] readState(String serializedStateStr) {
        Object[] stateObj = null;
        try {
            byte[] serializedState = Base64.decode(serializedStateStr);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serializedState));
            stateObj = (Object[])ois.readObject();
        }
        catch (Exception ex) {
        }
        return stateObj;
    }

    public static final String VIEW_STATE_PARAM = "com.ericsson.sn.mfaces.FACES_VIEW_STATE";
}
