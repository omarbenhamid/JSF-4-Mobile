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
package com.ericsson.sn.mobilefaces.media.image;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.faces.context.FacesContext;

import com.ericsson.sn.mobilefaces.util.Log;
import com.ericsson.sn.mobilefaces.device.DeviceDataFactory;
import com.ericsson.sn.mobilefaces.media.MediaAdaptor;
import com.ericsson.sn.mobilefaces.util.PropertyContainer;
import com.ericsson.sn.mobilefaces.util.RenderUtils;

/**
 * <p>
 *      Image Finder is a MediaAdaptor for images based on "finder" strategy.
 * </p>
 * <p>
 *      At first image finder finds correspond folder for different screen level.
 * </p>
 * <p>
 *      Secondly image finder checks the image format and finds supported format image
 *      for current device.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @author       Daning Yang
 * @version      1.0
 */
public class ImageFinder implements MediaAdaptor {

    // supported format list for current device
    private String[] support_image;
    // helper for parse filepath
    private ImageAdaptorHelper helper;

    private boolean adaptSuccess;

    /**
     * <p> Creates a new instance of ImageFinder </p>
     */
    public ImageFinder() {
        helper = new ImageAdaptorHelper();
    }

    /**
     * <p> Find correspond folder for current screen level and supported format image. </p>
     * @param imgPath original image path
     * @return new image path. if cannot find folder, return original path. if cannot find supported format image, return null.
     */
    public String doAdapt(String imgPath){
        adaptSuccess = false;
        String value = imgPath;
        // Create context and session
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext)context.getExternalContext().getContext();
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        HttpSession session = request.getSession();
        // Load ImageFinderIndex object from context
        String screenId = DeviceDataFactory.getCurrentDevice().getScreen();
        HashMap adaptorMap = (HashMap)servletContext.getAttribute("media.image.finder.index");
        PropertyContainer adaptorLinks = (PropertyContainer)adaptorMap.get(screenId);

        if (!Log.doAssert(adaptorLinks, "Cannot find ImageFinderIndex for screen level " + screenId, ImageFinder.class)) {
            return null;
        }
        // Parse support image formats for current device
        support_image = RenderUtils.parseStrings(DeviceDataFactory.getCurrentDevice().getImages(), ";");
        // search folder for current screen level
        value = parseLinkValue(adaptorLinks.getProperty("FOLDER"), value);
        // search support format file for current device
        if (Log.doAssert(value, "Cannot find folder for screen level " + screenId, ImageFinder.class)) {
            value = findImageType(value, servletContext);
        } else {
            return null;
        }
        // encode image path
        if (Log.doAssert(value, "Cannot find support format image!", ImageFinder.class)) {
            value = context.getApplication().getViewHandler().getResourceURL(context, value);
            //value = context.getExternalContext().encodemediaURL(value);
        } else {
            return null;
        }
        adaptSuccess = true;
        Log.debug("Find the image file: " + value, ImageFinder.class);
        return value;
    }

    public boolean isSuitable() {
        return adaptSuccess;
    }

    // Search folder for current screen level
    private String parseLinkValue(String folder, String value) {
        String result = null;
        if (folder == null || folder.length() == 0) {
            result = value;
        } else {
            result = helper.getImgPath(value) + helper.folderFormat(folder) + helper.getImgFile(value);
        }
        Log.debug("Find image path: " + result, ImageFinder.class);
        return result;
    }

    // Search support format file for current device
    private String findImageType(String filepath, ServletContext servletContext) {
        // Get file type
        String filetype = helper.getImgType(helper.getImgFile(filepath));
        String oldfile = servletContext.getRealPath("/") + filepath;

        if (checkFileFormat(filetype) && helper.checkFileExist(oldfile)) {
            // If original image is supported by device and exist, then return original path directly
            return filepath;
        } else {
            // If original image is NOT supported by device, then search support image file
            for (int i=0; i<support_image.length; i++) {
                // Assume a new format file
                String newfile = helper.getImgPath(filepath) + helper.getImgName(helper.getImgFile(filepath)) + "." + support_image[i];
                String realfile = servletContext.getRealPath("/") + newfile;
                // Check the file existing
                if (helper.checkFileExist(realfile)) {
                    // Find the file and return it.
                    return newfile;
                }
            }
            // Cannot find support file.
            return null;
        }
    }

    // Check the file format is supported or not
    private boolean checkFileFormat(String type) {
        // Search the format in the support list
        for (int i=0; i<support_image.length; i++) {
            if (type.equalsIgnoreCase(support_image[i])) {
                return true;
            }
        }
        return false;
    }
}
