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

/**
 * <p>
 *      TagConstant is a class with constants
 *      for MobileFaces taglib.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @author Daning Yang
 * @version 1.1
 */
public class TagConstant {
    public static final String COMPONENT_TYPE_OUTPUT = "javax.faces.Output";

    public static final String RENDERER_TYPE_BODY = "com.ericsson.sn.mobilefaces.Body";
    public static final String RENDERER_TYPE_HEADING = "com.ericsson.sn.mobilefaces.Heading";
    public static final String RENDERER_TYPE_CARD = "com.ericsson.sn.mobilefaces.Card";
    public static final String RENDERER_TYPE_CSSREF = "com.ericsson.sn.mobilefaces.CssRef";
    public static final String RENDERER_TYPE_DOCTYPE = "com.ericsson.sn.mobilefaces.DocType";
    public static final String RENDERER_TYPE_SCRIPTREF = "com.ericsson.sn.mobilefaces.ScriptRef";
    public static final String RENDERER_TYPE_TITLE = "com.ericsson.sn.mobilefaces.Title";
    public static final String RENDERER_TYPE_PAGETYPE = "com.ericsson.sn.mobilefaces.PageType";
    public static final String RENDERER_TYPE_MEDIAADAPTOR = "com.ericsson.sn.mobilefaces.MediaAdaptor";

    public static final String COMPONENT_ATTR_HEADING = "HEADING";
    public static final String[] COMPONENT_ATTR_HEADING_NO = {null, "1", "2", "3", "4", "5", "6"};
}
