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

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * <p>
 *      PropertyContainer contains property pairs.
 *      Normally container will be used with PropLoader
 *      to load property pairs from a xml file.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public class PropertyContainer implements Serializable {

    protected Hashtable<String, String> property = new Hashtable<String, String>();

    /**
     *  <p> Get property value from hashtable. </p>
     * @param name property name
     * @return property value
     */
    public String getProperty(String name) {
        String value = property.get(name);
        Log.doAssert(value, "Cannot find property " + name + "!", PropertyContainer.class);
        return value;
    }

    /**
     *  <p> Set property pair. </p>
     * @param name property name
     * @param value property value
     */
    public void setProperty(String name, String value) {
        property.put(name, value);
    }

    /**
     * <p> Initialize the property table.</p>
     * @param property new property table
  c   */
    public void setTable(Hashtable<String, String> property) {
        this.property = property;
    }

    /**
     * <p> Get the property table.</p>
     *
     * @return the property hashtable in the container
     */
    public Hashtable<String, String> getTable() {
        return property;
    }

    /**
     * Output property pairs in this container
     * @return Content of this container
     */
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("**PropertyContainer** \n");
        // Output all pairs
        Enumeration e = property.keys();
        while (e.hasMoreElements()) {
            String name = (String)e.nextElement();
            String value = property.get(name);
            buf.append(name + " = " + value + "\n");
        }
        return buf.toString();
    }

    // Test case
    public static void main(String args[]) {
        Log.info("*************Test for Property Container**************", PropertyContainer.class);
        // Get a PropertyContainer
        PropertyContainer pc = new PropertyContainer();
        // Put property pairs to it
        pc.setProperty("Property1", "Value1");
        pc.setProperty("Property2", "Value2");
        pc.setProperty("Property3", "Value3");
        // Output all property pairs
        Log.info(pc, PropertyContainer.class);
    }
}
