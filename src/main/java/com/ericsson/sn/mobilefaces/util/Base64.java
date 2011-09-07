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

import java.io.ByteArrayOutputStream;

/**
 * <p>
 * Utility class for encoding/decoding Base64.
 * </p>
 * <p>Company:   Ericsson AB</p>
 *
 */
public class Base64 {
    private static final String symbols =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    /**
     * Decode a Base64 encoded string into a byte array.
     *
     * @param  encodedStr  string to be decoded
     * @return             byte array with decoded data
     */
    public static byte[] decode(String encodedStr) {
        if (encodedStr == null) encodedStr = "";
        while (encodedStr.length()%4 != 0) encodedStr += "=";
        char[] encodedArr = encodedStr.toCharArray();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bitp1, bitp2, bitp3, bitp4;
        for (int i=0; i<encodedArr.length; i+=4) {
            bitp1 = symbols.indexOf(encodedArr[i]);
            bitp2 = symbols.indexOf(encodedArr[i+1]);
            if (bitp1 == -1 || bitp2 == -1) break;
            baos.write((bitp1 << 2) | ((bitp2 & 0x30) >> 4));

            bitp3 = symbols.indexOf(encodedArr[i+2]);
            if (bitp3 == -1) break;
            baos.write(((bitp2 & 0xf) << 4) | ((bitp3 & 0x3c) >> 2));

            bitp4 = symbols.indexOf(encodedArr[i+3]);
            if (bitp4 == -1) break;
            baos.write(((bitp3 & 0x3) << 6) | bitp4);
        }
        return baos.toByteArray();
    }

    /**
     * Encode a byte array to a Base64 string.
     *
     * @param  inArr  array with data to be encoded
     * @return        encoded Base64 string
     */
    public static String encode(byte[] inArr) {
        String outStr = "";
        for (int i=0; i<inArr.length; i+=3) {
            outStr += symbols.charAt((inArr[i]&0xfc)>>2);
            outStr += symbols.charAt(((inArr[i]&0x3)<<4)|((i+1)<inArr.length
            ?((inArr[i+1]&0xf0)>>4):0));
            outStr += ((i+1)<inArr.length)
            ?symbols.charAt(((inArr[i+1]&0x0f)<<2)|((i+2)<inArr.length
            ?((inArr[i+2]&0xc0)>>6):0)):"=";
            outStr += ((i+2)<inArr.length)?symbols.charAt(inArr[i+2]&0x3f):"=";
        }
        return outStr;
    }
}
