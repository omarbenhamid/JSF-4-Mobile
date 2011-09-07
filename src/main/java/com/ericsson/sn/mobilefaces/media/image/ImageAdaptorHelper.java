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
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *      ImageAdaptorHelper is helper utility for image adaptors.
 *      Helper provides methods for parsing image path, convert image format and so on.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @author Daning Yang
 * 
 * @version 1.1
 */
public class ImageAdaptorHelper {
    /**
     * <p>
     *      Get image path. <br />
     *      Example: <br />
     *      input "res/img/image.jpg"<br />
     *      output "res/img/"
     * </p>
     *
     * @param base input image full name
     * @return path part of the full name
     */
    public String getImgPath(String base) {
        if (base.indexOf("/") != -1) {
            int end = base.lastIndexOf("/");
            return base.substring(0, end+1);
        } else {
            return "";
        }
    }

    /**
     * <p>
     *      Get image file name. <br />
     *      Example: <br />
     *      input "res/img/image.jpg"<br />
     *      output "image.jpg"
     * </p>
     *
     * @param base input image full name
     * @return file name part of the full name
     */
    public String getImgFile(String base) {
        if (base.indexOf("/") != -1) {
            int end = base.lastIndexOf("/");
            return base.substring(end+1, base.length());
        } else {
            return base;
        }
    }

    /**
     * <p>
     *      Get image type. <br />
     *      Example: <br />
     *      input "image.jpg"<br />
     *      output "jpg"
     * </p>
     *
     * @param filename input image file name
     * @return file format part of the file name
     */
    public String getImgType(String filename) {
        if (filename.indexOf(".") != -1) {
            int end = filename.lastIndexOf(".");
            return filename.substring(end+1, filename.length());
        } else {
            return null;
        }
    }

    /**
     * <p>
     *      Get image name. <br />
     *      Example: <br />
     *      input "image.jpg"<br />
     *      output "image"
     * </p>
     *
     * @param filename input image file name
     * @return file name part of the file name
     */
    public String getImgName(String filename) {
        if (filename.indexOf(".") != -1) {
            int end = filename.lastIndexOf(".");
            return filename.substring(0, end);
        } else {
            return null;
        }
    }

    /**
     * <p>
     *      Format folder name. <br />
     *      Example: <br />
     *      input "/img"<br />
     *      output "img/"
     * </p>
     *
     * @param base input folder name
     * @return formated folder name
     */
    public String folderFormat(String base) {
        if (base.charAt(0) == '/') {
            base = base.substring(1,base.length());
        }
        if (base.charAt(base.length()-1) != '/') {
            base = base + "/";
        }
        return base;
    }

    /**
     * <p>Check the file existing </p>
     * @param filename file name with path
     * @return if exists, return true. else, return false
     */
    public boolean checkFileExist(String filename) {
        File file=new File(filename);
        return file.exists();
    }

    /**
     * <p> Given a http servlet request and a relative image path,
     *     gets the image path from the servlet context root.</p>
     * <p>
     *     <strong>Example:</strong><br/>
     *     Request from: <code>http://localhost:8080/WebApp/subdir/index.jsp</code><br/>
     *     Calling <code>getImagePathFromServletContext(request, "../images/image.jpg")</code><br/>
     *     returns <code>"subdir/images/image.jpg"</code>
     * </p>
     *
     * @param request the http servlet request
     * @param relativeImagePath the relative image path
     * @return the image path from the servlet context root
     *
     */
    public String getImagePathFromServletContext(HttpServletRequest request, String relativeImagePath){
        String relativeImagePathValue = relativeImagePath;

        // Remove existing "../" that is first in relativeImagePath and count
        // how many subdirectories that exist.
        int subDirCountInRelativeImagePath = 0;
        while (relativeImagePathValue.startsWith("../")){
            relativeImagePathValue = relativeImagePathValue.replace("../", "");
            subDirCountInRelativeImagePath++;
        }

        // If no subdirectory existed, return the relativeImagePath
        if (subDirCountInRelativeImagePath <= 0)
            return relativeImagePath;

        String servletPath = request.getServletPath();
        if (servletPath.startsWith("/"))
            servletPath = servletPath.replaceFirst("/", "");
        String[] subDirsInRequest = servletPath.split("/");
        int subDirCountInRequest = subDirsInRequest.length - 1; // -1 since the filename of the servlet is included.
        int subDirectoriesToInclude = subDirCountInRequest - subDirCountInRelativeImagePath;

        // If too many subdirectories in the image path, return original relativeImagePath
        if (subDirectoriesToInclude < 0)
            return relativeImagePath;

        String subDirectories = "";
        for (int i=0; i<subDirectoriesToInclude; i++){
            subDirectories = subDirsInRequest[i] + "/";
        }

        return subDirectories + relativeImagePathValue;
    }

    /**
     * <p>Given a http servlet request, returns the prefix that is necessary to
     * step down to the servlet context root.</p>
     *
     * <p>
     *     <strong>Example:</strong><br/>
     *     Request from: <code>http://localhost:8080/subdir/subdir/index.jsp</code><br/>
     *     Calling <code>getPrefixForRequestPathToServletContext(request)</code><br/>
     *     returns <code>"../../"</code>
     * </p>
     *
     * @param request the http servlet request
     * @return the prefix
     */
    public String getPrefixForRequestPathToServletContext(HttpServletRequest request){
        String servletPath = request.getServletPath();
        if (servletPath.startsWith("/"))
            servletPath = servletPath.replaceFirst("/", "");
        String[] subDirsInRequest = servletPath.split("/");
        int subDirCountInRequest = subDirsInRequest.length - 1; // -1 since the filename of the servlet is included.
        String prefixPath = "";
        for (int i=0; i<subDirCountInRequest; i++){
            prefixPath += "../";
        }
        return prefixPath;
    }
}
