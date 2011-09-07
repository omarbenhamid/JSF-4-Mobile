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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.faces.context.FacesContext;
import com.ericsson.sn.mobilefaces.util.Log;
import com.ericsson.sn.mobilefaces.util.RenderUtils;
import com.ericsson.sn.mobilefaces.util.PropertyContainer;
import com.ericsson.sn.mobilefaces.device.DeviceDataFactory;
import com.ericsson.sn.mobilefaces.media.MediaAdaptor;

/**
 * <p>
 *      Image Convertor is a MediaAdaptor for images based on "convertor" strategy.
 *      At first image convertor checks whether the image has been converted before.
 *      If there is converted image, adaptor will just return the image url.
 * </p>
 * <p>
 *      Secondly if no existing converted file, image convertor converts the size
 *      based on current screen level and format based on current device support format list.
 * </p>
 * <p>
 *      Finally return the converted image url.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @author Daning Yang
 * 
 * @version 1.1
 */
public class ImageConvertor implements MediaAdaptor {

    // supported format list for current device
    private String[] support_image;
    // helper for parse filepath
    private ImageAdaptorHelper helper;

    private boolean adaptSuccess;

    /**
     * <p>Creates a new instance of ImageFinder</p>
     */
    public ImageConvertor() {
        helper = new ImageAdaptorHelper();
    }

    /**
     * <p>Do the convert work.</p>
     * @param imgPath original image path
     * @return converted image path
     */
    public String doAdapt(String imgPath) {
        adaptSuccess = false;
        float rate;

        // Create context and session
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession session = request.getSession();

        // Make sure the image path is correct
        String imagePath = helper.getImagePathFromServletContext(request, imgPath);

        // Check original file is exsit
        if (!helper.checkFileExist(servletContext.getRealPath("/") + imagePath)) {
            Log.err("Cannot find image: " + imagePath, ImageConvertor.class);
            return null;
        }

        // Load ImageConvertIndex object from context
        String screenId = DeviceDataFactory.getCurrentDevice().getScreen();
        HashMap adaptorMap = (HashMap) servletContext.getAttribute("media.image.convertor.index");
        PropertyContainer adaptorLinks = (PropertyContainer) adaptorMap.get(screenId);
        // Read screen convert rate from index file
        if (Log.doAssert(adaptorLinks, "Cannot find rate for screen level " + screenId, ImageConvertor.class)) {
            rate = (new Float(adaptorLinks.getProperty("RATE"))).floatValue();
        } else {
            return null;
        }

        // Parse support image formats for current device
        support_image = RenderUtils.parseStrings(DeviceDataFactory.getCurrentDevice().getImages(), ";");

        String dist_img = TEMP_FOLDER;
        // Check the file format, finding support format
        if (!checkFileFormat(helper.getImgType(helper.getImgFile(imagePath)))) {
            dist_img += helper.folderFormat(helper.getImgPath(imagePath)) + adaptorLinks.getProperty("RATE") + "/" + helper.getImgName(helper.getImgFile(imagePath)) + "." + support_image[0];
        } else {
            dist_img += helper.folderFormat(helper.getImgPath(imagePath)) + adaptorLinks.getProperty("RATE") + "/" + helper.getImgFile(imagePath);
        }
        // Prepare original image path
        imagePath = helper.folderFormat(helper.getImgPath(imagePath)) + helper.getImgFile(imagePath);
        // Check if converted image exists
        if (!helper.checkFileExist(servletContext.getRealPath("/") + dist_img)) {
            // Make folder
            File file = new File(servletContext.getRealPath("/") + helper.getImgPath(dist_img));
            file.mkdirs();
            // Do convert
            convertImage(servletContext.getRealPath("/") + imagePath, servletContext.getRealPath("/") + dist_img, helper.getImgType(helper.getImgFile(dist_img)), rate);
        }

        adaptSuccess = true;

        dist_img = helper.getPrefixForRequestPathToServletContext(request) + dist_img;

        // return final image path
        Log.debug("Load image: " + dist_img, ImageConvertor.class);
        return dist_img;
    }

    public boolean isSuitable() {
        return adaptSuccess;
    }

    // Check the file format is supported or not
    private boolean checkFileFormat(String type) {
        // Search the format in the support list
        for (int i = 0; i < support_image.length; i++) {
            if (type.equalsIgnoreCase(support_image[i])) {
                return true;
            }
        }
        return false;
    }

    // Do convert image based on java.awt.Image for scaling size and javax.imageio.ImageIO for converting format
    private boolean convertImage(String source, String dist, String format, float rate) {
        boolean convertSuccess = true;

        // Load image from file
        File inFile = new File(source);
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(inFile);
        } catch (IOException ex) {
            Log.err("Cannot read original image file!", ImageConvertor.class);
            Log.err(ex, ImageConvertor.class);
            convertSuccess = false;
        }
        if (bi != null) {
            // Convert image size for the rate
            int width = (int) (bi.getWidth(null) * rate);
            int height = (int) (bi.getHeight(null) * rate);
            Image img = null;
            BufferedImage newbi = null;
            if (Log.doAssert(bi, "Cannot read BufferedImage!", ImageConvertor.class)) {
                img = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                newbi = toBufferedImage(img, format.equalsIgnoreCase("wbmp") ? BufferedImage.TYPE_BYTE_BINARY : BufferedImage.TYPE_INT_RGB);
            }
            // Convert format and save the image
            File outFile = new File(dist);
            try {
                convertSuccess = ImageIO.write(newbi, format, outFile);
            } catch (IOException ex) {
                Log.err("Cannot write image file!", ImageConvertor.class);
                Log.err(ex, ImageConvertor.class);
            }
        }
        return convertSuccess;
    }

    // Convert Image to BufferedImage
    private BufferedImage toBufferedImage(Image im, int type) {
        BufferedImage bi = new BufferedImage(im.getWidth(null), im.getHeight(null), type);
        Graphics bg = bi.getGraphics();
        bg.drawImage(im, 0, 0, null);
        bg.dispose();
        return bi;
    }

    // Test case
    public static void main(String[] args) {
        if (args.length == 4) {
            String source = args[0];
            String target = args[1];
            String format = args[2];
            String rate = args[3];
            Log.info("*****Test for converting image*****", ImageConvertor.class);
            ImageConvertor ic = new ImageConvertor();
            // Do convert
            boolean suc = ic.convertImage(source, target, format, new Float(rate).floatValue());
            if (suc) {
                Log.info("Converted successfully!", ImageConvertor.class);
            } else {
                Log.info("Converted failed!", ImageConvertor.class);
            }
        } else {
            Log.info(" java ImageConvertor <sourceFile> <targetFile> <targetFormat> <convertRate>", ImageConvertor.class);
        }
    }

    /**
     * <p>Temporary folder for converting image</p>
     */
    public static String TEMP_FOLDER = "mfaces_tmp_img/";
}
