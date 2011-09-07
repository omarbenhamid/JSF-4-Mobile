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
package com.ericsson.sn.mobilefaces.util;

import java.util.Hashtable;

/**
 * <p>
 *      In the WML page, there is no form tag to submit parameters.
 *      Instead of it, WML adds all parameters to the post fields in
 *      every button. So we need a holder to save the input fields and
 *      return them when render the button.
 * </p>
 *
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public interface FormValueHolder {

    /**
     * <p>Set a parameter pair to the form holder.</p>
     * @param name parameter name
     * @param value parameter value
     */
    public void setFormValue(String name, String value);

    /**
     * <p>Remove a parameter pair from the form holder.</p>
     * @param name parameter name
     */
    public void removeFormValue(String name);

    /**
     * <p>Return the hashtable which contains all parameters.</p>
     * @return parameter hashtable
     */
    public Hashtable getValueTable();

    /**
     * <p>Clear the hashtable in the holder.</p>
     */
    public void clear();
}
