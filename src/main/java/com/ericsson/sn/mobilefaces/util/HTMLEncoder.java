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
 *      Simply encode the text for HTML page.
 *      Some special characters will be replaced so
 *      that they can be displayed correctly on
 *      HTML page. This class is the help class for
 *      {@link com.ericsson.sn.mobilefaces.MobileResponseWriter}
 * </p>
 *
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @author       Daning Yang
 * @version      1.0
 */
public class HTMLEncoder {

    /** Creates a new instance of HTMLEncoder */
    public HTMLEncoder() {
        if (SPECIAL_CHARS.isEmpty()) {
            SPECIAL_CHARS.put(new Character('"'), "&quot;");
            SPECIAL_CHARS.put(new Character('&'), "&amp;");
            SPECIAL_CHARS.put(new Character('<'), "&lt;");
            SPECIAL_CHARS.put(new Character('"'), "&quot;");
            SPECIAL_CHARS.put(new Character('>'), "&gt;");
            SPECIAL_CHARS.put(new Character('"'), "&quot;");

            char ch = 0xA1;

            while (ch <= 0xff) {
                SPECIAL_CHARS.put(new Character(ch), SPECIAL_LANG_CHARS[ch - 0xA1]);
                ch++;
            }
        }
    }

    /**
     * <p>Encodes the given string, so that it can be used within a html page.</p>
     * @param string the string to convert
     * @return the encoded string
     */
    public String encode(String string) {
        if (string == null) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        String replace;
        char c;
        // check every char
        for (int i=0; i<string.length(); i++) {
            replace = null;
            c = string.charAt(i);
            // replace the space if need
            if (c == ' ' && (i - 1 >= 0 && string.charAt(i - 1) == ' ')) {
//                replace = "&#160;";
            } else if (c >= 0x3f) {
                // replace the special characters
                replace = SPECIAL_CHARS.get(new java.lang.Character(c));
            }
            // add to buffer
            if (replace == null)
                sb.append(c);
            else
                sb.append("&" + replace + ";");
        }
        return sb.toString();
    }

    /**
     * <p>Encodes the url, so that it can be used within a html page.</p>
     * @param url the utl to convert
     * @return the encoded url
     */
    public String encodeURL(String url) {
        StringBuffer sb = new StringBuffer();
        // check all characters
        for (int i = 0; i < url.length(); i++) {
            char ch = url.charAt(i);
            // replace space
            if (ch == ' ') sb.append('+');
            // replace "
            else if (ch == '"') sb.append("%22");
            else sb.append(ch);
        }
        return sb.toString();
    }

    private static Hashtable<Character, String> SPECIAL_CHARS = new Hashtable<Character, String>();

    // special language characters set
    private final static String[] SPECIAL_LANG_CHARS = new String[]{
        "iexcl",
        "cent",
        "pound",
        "curren",
        "yen",
        "brvbar",
        "sect",
        "uml",
        "copy",
        "ordf",
        "laquo",
        "not",
        "shy",
        "reg",
        "macr",
        "deg",
        "plusmn",
        "sup2",
        "sup3",
        "acute",
        "micro",
        "para",
        "middot",
        "cedil",
        "sup1",
        "ordm",
        "raquo",
        "frac14",
        "frac12",
        "frac34",
        "iquest",
        "Agrave",
        "Aacute",
        "Acirc",
        "Atilde",
        "Auml",
        "Aring",
        "AElig",
        "Ccedil",
        "Egrave",
        "Eacute",
        "Ecirc",
        "Euml",
        "Igrave",
        "Iacute",
        "Icirc",
        "Iuml",
        "ETH",
        "Ntilde",
        "Ograve",
        "Oacute",
        "Ocirc",
        "Otilde",
        "Ouml",
        "times",
        "Oslash",
        "Ugrave",
        "Uacute",
        "Ucirc",
        "Uuml",
        "Yacute",
        "THORN",
        "szlig",
        "agrave",
        "aacute",
        "acirc",
        "atilde",
        "auml",
        "aring",
        "aelig",
        "ccedil",
        "egrave",
        "eacute",
        "ecirc",
        "euml",
        "igrave",
        "iacute",
        "icirc",
        "iuml",
        "eth",
        "ntilde",
        "ograve",
        "oacute",
        "ocirc",
        "otilde",
        "ouml",
        "divide",
        "oslash",
        "ugrave",
        "uacute",
        "ucirc",
        "uuml",
        "yacute",
        "thorn",
        "yuml"
    };
}
