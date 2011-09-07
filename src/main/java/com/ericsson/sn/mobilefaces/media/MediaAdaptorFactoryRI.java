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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.ericsson.sn.mobilefaces.util.Log;
import com.ericsson.sn.mobilefaces.util.PropLoader;
import com.ericsson.sn.mobilefaces.util.SimpleXMLReader;
import com.ericsson.sn.mobilefaces.util.PropertyContainer;
import com.ericsson.sn.mobilefaces.util.RenderUtils;

/**
 * <p>
 *      Reference implementation of AbstractMediaAdaptorFactory.
 * </p>
 *
 * <p>
 *      In this factory, it will load all adaptors to the application scope.
 *      And then search a suitable one based on mediaType and strategy for
 *      current media source.
 * </p>
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public class MediaAdaptorFactoryRI extends AbstractMediaAdaptorFactory {

    private ServletContext context;

    /**
     * @see com.ericsson.sn.mobilefaces.media.AbstractMediaAdaptorFactory#loadAdaptor(ServletContext servletContext, HttpServletRequest request)
     */
    @Override
    public void loadAdaptor(ServletContext servletContext, HttpServletRequest request) {
        if (Log.doAssert(servletContext, "Cannot find Servlet Context when loadAdaptor()", MediaAdaptorFactoryRI.class)) {
            context = servletContext;
            // Load MediaAdaptor class name
            loadAdaptor(servletContext);
        }
    }

    /**
     * @see com.ericsson.sn.mobilefaces.media.AbstractMediaAdaptorFactory#getAdaptor(String mediaType, String strategy)
     */
    @Override
    public MediaAdaptor getAdaptor(String mediaType, String strategy) {
        // Load index file if need
        loadIndexFile( context, mediaType, strategy);
        // Load MediaAdaptor class name
        HashMap adaptorPool = (HashMap)context.getAttribute("MediaAdaptor");
        String classname = (String)adaptorPool.get(mediaType + "|" + strategy);
        // Load the class
        MediaAdaptor rm = null;
        try {
            Class c = Class.forName(classname);
            rm =  (MediaAdaptor)c.newInstance();
        } catch ( Exception e ) {
            Log.err("Cannot find custom MediaAdaptor.", MediaAdaptorFactoryRI.class);
            Log.err(e, MediaAdaptorFactoryRI.class);
        }
        return rm;
    }

    /**
     * @see com.ericsson.sn.mobilefaces.media.AbstractMediaAdaptorFactory#doAdapt(String mediaType, String inFile, String strategyStr)
     */
    @Override
    public String doAdapt(String mediaType, String inFile, String strategyStr) {
        // Parse strategy candidate
        String strategy[] =  RenderUtils.parseStrings(strategyStr, ";");
        // Try every strategy until find a successful one.
        for (int i=0; i<strategy.length; i++) {
            MediaAdaptor rm = getAdaptor( mediaType, strategy[i]);
            if (rm != null) {
                String result = rm.doAdapt(inFile);
                if (rm.isSuitable()) {
                    Log.debug("Load media adaptor: " + rm, MediaAdaptorFactoryRI.class);
                    return result;
                }
            }
        }

        Log.debug("Cannot find successful strategy for " + mediaType, MediaAdaptorFactoryRI.class);
        // If cannot find a successful one, return a UnknownMediaAdaptor
        MediaAdaptor rm = new UnknownMediaAdaptor();
        return rm.doAdapt(inFile);
    }

    // Load all MediaAdaptor class name to a hashmap and
    // initialize image convertor and finder as default.
    // ==================================================
    // Format in the hashmap
    // key: "type|strategy"  ->  value: "classname"
    // example:   key: "image|finder"  ->  value: "com.ericsson.sn.mobilefaces.media.image.ImageAdaptorFinder"
    // ==================================================
    private void loadAdaptor(ServletContext servletContext) {
        // Only load onces
        if (servletContext.getAttribute("MediaAdaptor") == null) {
            // initialize map and image convertor, finder.
            HashMap<String, String> adaptorPool = new HashMap<String, String>();
            adaptorPool.put("image|convertor","com.ericsson.sn.mobilefaces.media.image.ImageConvertor");
            adaptorPool.put("image|finder","com.ericsson.sn.mobilefaces.media.image.ImageFinder");
            // load the config file
            PropLoader adaptorLoader = new PropLoader(MEDIA_ADAPTOR_NAME);
            Collection adaptorCollection = adaptorLoader.parse(servletContext.getRealPath("/") + servletContext.getInitParameter(MCONFIG_NAME) );
            Iterator iterator = adaptorCollection.iterator() ;
            // save classname and type|strategy pairs to the hashmap
            while (iterator.hasNext()) {
                PropertyContainer r = (PropertyContainer)iterator.next();
                String name = r.getProperty("type") + "|" + r.getProperty("strategy");
                String classname = r.getProperty("class");
                adaptorPool.put(name, classname);
            }
            // save the hashmap to the context
            servletContext.setAttribute("MediaAdaptor", adaptorPool);
        }
    }

    // Load MediaAdaptor index define file
    // ==================================================
    // Attribute name of media index in context format:
    // media.type.strategy.index
    // example: media.image.finder.index
    // ==================================================
    private void loadIndexFile(ServletContext servletContext, String type, String strategy) {
        // Only load index file onces
        if (servletContext.getAttribute("media." + type + "." + strategy +".index") == null) {
            // Read the config file
            SimpleXMLReader config = new SimpleXMLReader(servletContext.getRealPath("/") + servletContext.getInitParameter(MCONFIG_NAME));
            // Get index define file path
            String indexfile = config.getString(MEDIA_ADAPTOR_PATH);
            // Load the file
            HashMap<String, Object> adaptorMap = new HashMap<String, Object>();
            PropLoader adaptorLoader = new PropLoader(type + "-" + strategy);
            Collection adaptorCollection = adaptorLoader.parse(servletContext.getRealPath("/") + indexfile );
            Iterator iterator = adaptorCollection.iterator() ;
            // Load image index to hashmap
            while (iterator.hasNext()) {
                PropertyContainer pc = (PropertyContainer)iterator.next();
                Log.info("Add " + type + "|" + strategy + " media adaptor: " + pc.getProperty("NAME"), MediaAdaptorFactoryRI.class);
                adaptorMap.put(pc.getProperty("SCREEN"), pc);
            }
            // Save the hashmap to application scope
            servletContext.setAttribute("media." + type + "." + strategy +".index" , adaptorMap);
        }
    }

    // MobileFaces config name
    private static final String MCONFIG_NAME = "ericsson.mobilefaces.CONFIG_FILE";
    // Media Adaptor name in xml define file
    private static final String MEDIA_ADAPTOR_NAME = "media-adaptor";
    // Media Adaptor define file path in config xml file
    private static final String MEDIA_ADAPTOR_PATH = "mfaces-config/configuration-file/media-adaptors";
}
