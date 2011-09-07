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

/**
 * <p>
 *      MediaAdaptorFactory is to generate the MediaAdaptor
 *      {@link com.ericsson.sn.mobilefaces.media.MediaAdaptor}
 * <p>
 * </p>
 *      MediaAdaptorFactoryRI is the reference implementation of this abstract class.
 * </p>
 *
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public abstract class AbstractMediaAdaptorFactory {

    /**
     * <p>
     *       Load adaptor list and do initialization.
     * </p>
     * @param servletContext ServletContext to get system information
     * @param request HttpServletRequest to get end device information
     */
    public abstract void loadAdaptor(ServletContext servletContext, HttpServletRequest request);

    /**
     * <p>
     *      Get a suitable adaptor for current media and adapt strategy.
     * </p>
     *
     * @return suitable adaptor
     * @param mediaType the media type
     * @param strategy single strategy name
     */
    public abstract MediaAdaptor getAdaptor(String mediaType, String strategy);

    /**
     * <p>
     *      Try different strategies to find a suitable adaptor
     *      and do adapt for input media file by it.
     * </p>
     * @return adapted media file path
     * @param mediaType the media type
     * @param strategyStr strategy names string
     * @param inFile original media file path
     */
    public abstract String doAdapt(String mediaType, String inFile, String strategyStr);
}
