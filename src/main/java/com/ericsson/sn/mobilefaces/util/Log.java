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

/**
 * <p>Log facade to show message and record the error information.</p>
 *
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @author       Daning Yang
 * @version      1.0
 */
public class Log {

    private static boolean debug = false;

    // All methods are static, so it is not allowed to create instantce.
    private Log() {

    }

    /**
     * <p> Set flag to show debug message or not. </p>
     * @param isDebug display debug or not
     */
    public static void setDebug(boolean isDebug) {
        debug = isDebug;
    }

    /**
     * <p> Print out the key message with the highest priority.</p>
     *
     * @param master master class for tracking
     * @param msg message
     */
    public static void keyMsg(Object msg, Class master) {
        System.out.println("==MobileFaces: " + master.getName());
        System.out.println(msg.toString());
    }

    /**
     * <p>Print out the error message with the higher priority.</p>
     *
     * @param master master class for tracking
     * @param msg message
     */
    public static void err(Object msg, Class master) {
        System.err.println("==MobileFaces Error: " + master.getName());
        System.err.println(msg.toString());
    }

    /**
     * <p>Print out the information message with the normal priority.</p>
     *
     * @param master master class for tracking
     * @param msg message
     */
    public static void info(Object msg, Class master) {
        if (debug) {
            System.out.println("==MobileFaces Info: " + master.getName());
            System.out.println(msg.toString());
        }
    }

    /**
     * <p>Print out the debug message with the low priority.
     * It will not be displayed in default JRE setting.</p>
     *
     * @param master master class for tracking
     * @param msg message
     */
    public static void debug(Object msg, Class master) {
        if (debug) {
            System.out.println("==MobileFaces Debug: " + master.getName());
            System.out.println(msg.toString());
        }
    }

    /**
     * <p>Estimate the condition expression is true. If false, throw out the warning.</p>
     *
     * @return condition state
     * @param master master class for tracking
     * @param condition condition equation
     * @param msg warning message
     */
    public static boolean doAssert(boolean condition, String msg, Class master) {
        if (!condition) {
            System.err.println("==MobileFaces False Condition Found in " + master.getName());
            System.err.println(msg.toString());
        }
        return condition;
    }

    /**
     * <p>Estimate the object is not null. If null, throw out the warning.</p>
     *
     * @return if object is null, return false, else true
     * @param master master class for tracking
     * @param object object need be checked
     * @param msg warning message
     */
    public static boolean doAssert(Object object, String msg, Class master) {
        if (object == null) {
            System.err.println("==MobileFaces Null Object Found in " + master.getName());
            System.err.println(msg.toString());
            return false;
        } else {
            return true;
        }
    }

    // Test case
    public static void main(String args[]) {
        Log.keyMsg("This is a test for Log.class", Log.class);
    }
}
