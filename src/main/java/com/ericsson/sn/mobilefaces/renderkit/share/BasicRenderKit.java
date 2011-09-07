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
package com.ericsson.sn.mobilefaces.renderkit.share;

import java.util.HashMap;
import java.io.Writer;
import java.io.OutputStream;

import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.MobileResponseWriter;
import com.ericsson.sn.mobilefaces.MobileResponseStateManager;

/**
 * <p>
 *      Basic renderkit implements basic methods for
 *      different markup langugae renderkit.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public abstract class BasicRenderKit extends RenderKit {

    /**
     * <p>renderer kit.</p>
     */
    protected HashMap<String, Renderer> renderKit;
    /**
     * <p>Response State Manager</p>
     */
    protected ResponseStateManager responseStateManager = null;

    /**
     * <p>Create a instance of BasicRenderKit.</p>
     */
    public BasicRenderKit() {
        super();
        responseStateManager = new MobileResponseStateManager();
        renderKit = new HashMap<String, Renderer>();
    }

    /**
     * <p>Return the content type</p>
     * @return content type
     */
    protected abstract String getContentType();

    /**
     * <p>Return the encoding</p>
     * @return encoding
     */
    protected abstract String getEncoding();

    /**
     * <p>Add a renderer to the manager</p>
     * @param family renderer family
     * @param rendererType renderer type
     * @param renderer renderer
     */
    @Override
    public void addRenderer(String family, String rendererType, Renderer renderer) {
        synchronized (renderKit) {
            renderKit.put(family + "." + rendererType, renderer);
        }
    }

    /**
     * <p>Get a renderer from the manager</p>
     * @param family renderer family
     * @param rendererType renderer type
     * @return renderer
     */
    @Override
    public Renderer getRenderer(String family, String rendererType) {
        Renderer renderer = null;
        StringBuffer sb = new StringBuffer(family);
        sb.append(".");
        sb.append(rendererType);
        synchronized (renderKit) {
            renderer = renderKit.get(sb.toString());
        }
        return renderer;
    }

    /**
     * <p>Get a Response State Manager</p>
     * @return a Response State Manager
     */
    @Override
    public ResponseStateManager getResponseStateManager() {
        return responseStateManager;
    }

    /**
     * <p>Get a response writer instance.</p>
     * @param writer original writer
     * @param contentTypeList content type list
     * @param characterEncoding character encoding
     * @return response writer instance
     */
    @Override
    public ResponseWriter createResponseWriter(Writer writer, String contentTypeList,
            String characterEncoding) {
        if (writer == null) {
            return null;
        }
        // Set content type and encoding
        String contentType = getContentType();
        if (characterEncoding == null) {
            characterEncoding = getEncoding();
        }
        // Create a instance
        return new MobileResponseWriter(writer, contentType, characterEncoding);
    }

    /**
     * <p>Get a Response Stream instance.</p>
     * @param out original output stream
     * @return a Response Stream instance
     */
    @Override
    public ResponseStream createResponseStream(OutputStream out) {
        return null;
    }
}
