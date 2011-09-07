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
package com.ericsson.sn.mobilefaces.renderkit.html;

import com.ericsson.sn.mobilefaces.renderkit.share.BasicPageTypeRenderer;

/**
 * <p>
 *      PageTypeRenderer is a renderer to render page tag
 *      as &lt;html&gt; on HTML page.
 * </p>
 *
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @jsf.render-kit  component-family="javax.faces.Output"
 *                  renderer-type="com.ericsson.sn.mobilefaces.PageType"
 *                  description="PageTypeRenderer is the renderer for page tag on HTML page"
 *
 * @author Daning Yang
 * @version 1.0
 */
public class PageTypeRenderer extends BasicPageTypeRenderer {

    /**
     * @see com.ericsson.sn.mobilefaces.renderkit.share.BasicPageTypeRenderer#getPageTag()
     */
    @Override
    protected String getPageTag() {
        return "html";
    }
}
