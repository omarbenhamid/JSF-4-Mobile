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

import java.util.HashMap;
import java.util.Iterator;

import javax.faces.render.RenderKit;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;

import com.ericsson.sn.mobilefaces.util.Log;
import com.ericsson.sn.mobilefaces.device.DeviceDataFactory;
import com.ericsson.sn.mobilefaces.renderkit.html.HTMLRenderKit;

/**
 * <p>
 *      MobileRenderKitFactory is an implementation of RenderKitFactory
 *      for generating RenderKit for mobile devices based on
 *      {@link com.ericsson.sn.mobilefaces.device.DeviceData} feature.
 * </p>
 *
 * <p>
 *      MobileRenderKitFactory contains HTML_BASIC/XHTML-MP/WML renderkits
 *      for mobile device. New renderkits can be defined in faces-config as
 *      standard JSF custom renderkit.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public class MobileRenderKitFactory extends RenderKitFactory {

    protected String renderKitId = null;
    protected HashMap<String, RenderKit> renderKits = null;

    /**
     * <p> Initialize renderkit pool and default HTML renderkit. </p>
     */
    public MobileRenderKitFactory() {
        super();
        renderKits = new HashMap<String, RenderKit>();
        addRenderKit(HTML_BASIC_RENDER_KIT, new HTMLRenderKit());
    }

    /**
     * <p> Add new renderkit to pool </p>
     * @param renderKitId new renderkit ID
     * @param renderKit new renderkit
     */
    @Override
    public void addRenderKit(String renderKitId, RenderKit renderKit) {
        synchronized (renderKits) {
            Log.debug("Add renderkit: " + renderKitId, MobileRenderKitFactory.class);
            renderKits.put(renderKitId, renderKit);
        }
    }

    /**
     * <p> Get renderkit from pool based on current device support markup language. </p>
     * @param context FacesContext to get device feature
     * @param renderKitId default renderKit Id
     * @return renderkit
     */
    @Override
    public RenderKit getRenderKit(FacesContext context, String renderKitId) {
        if(context != null) {
            renderKitId = DeviceDataFactory.getCurrentDevice().getMarkup();
        }
        RenderKit renderKit = null;
        synchronized (renderKits) {
            if (renderKits.containsKey(renderKitId)) {
                renderKit = renderKits.get(renderKitId);
            }
        }
        if (context != null)
            Log.doAssert( renderKit, "Cannot find renderkit " + renderKitId, MobileRenderKitFactory.class);

        return renderKit;
    }

    /**
     * <p> Get iterator for all renderkits in the pool. </p>
     * @return iterator of all renderkits
     */
    @Override
    public Iterator<String> getRenderKitIds() {
        return renderKits.keySet().iterator();
    }
}
