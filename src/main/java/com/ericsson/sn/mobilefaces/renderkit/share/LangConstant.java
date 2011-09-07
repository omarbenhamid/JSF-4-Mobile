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
package com.ericsson.sn.mobilefaces.renderkit.share;

import java.util.HashMap;

/**
 * <p>
 *      LangConstant is the class to contain
 *      constants of markup language tags.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @author Daning Yang
 * @version 1.1
 */
public class LangConstant {
    // tag names for html/xhtml-mp
    public static final String TAG_A =  "a";
    public static final String TAG_BODY = "body";
    public static final String TAG_BR =  "br";
    public static final String TAG_DIV =  "div";
    public static final String TAG_FORM =  "form";
    public static final String TAG_IMG =  "img";
    public static final String TAG_INPUT = "input";
    public static final String TAG_LABEL =  "label";
    public static final String TAG_LINK =  "link";
    public static final String TAG_META = "meta";
    public static final String TAG_OPTION =  "option";
    public static final String TAG_OPTION_GROUP =  "optgroup";
    public static final String TAG_SCRIPT =  "script";
    public static final String TAG_SELECT =  "select";
    public static final String TAG_SMALL =  "small";
    public static final String TAG_SPAN = "span";
    public static final String TAG_TABLE =  "table";
    public static final String TAG_TEXTAREA =  "textarea";
    public static final String TAG_TITLE = "title";
    public static final String TAG_TD =  "td";
    public static final String TAG_TH =  "th";
    public static final String TAG_TR =  "tr";

    // tag names for wml
    public static final String TAG_ANCHOR =  "anchor";
    public static final String TAG_B =  "b";
    public static final String TAG_BIG =  "big";
    public static final String TAG_CARD =  "card";
    public static final String TAG_DO =  "do";
    public static final String TAG_EM =  "em";
    public static final String TAG_FIELDSET =  "fieldset";
    public static final String TAG_GO =  "go";
    public static final String TAG_I =  "i";
    public static final String TAG_P =  "p";
    public static final String TAG_POSTFIELD =  "postfield";
    public static final String TAG_PREV =  "prev";
    public static final String TAG_REFRESH =  "refresh";
    public static final String TAG_SETVAR =  "setvar";
    public static final String TAG_SKIP =  "skip";
    public static final String TAG_STRONG =  "strong";
    public static final String TAG_U =  "u";

    public static final String TAG_WML_CARDS =  "WML.CARDS";
    public static final String TAG_WML_TITLE =  "WML.TITLE";

    // Tag attributes
    public static final String ATT_ACCEPT = "accept";
    public static final String ATT_ACCEPT_CHARSET = "accept-charset";
    public static final String ATT_ACCESSKEY =  "accesskey";
    public static final String ATT_ACTION = "action";
    public static final String ATT_ALIGN = "align";
    public static final String ATT_ALT = "alt";
    public static final String ATT_BORDER = "border";
    public static final String ATT_CELLPADDING = "cellpadding";
    public static final String ATT_CELLSPACING = "cellspacing";
    public static final String ATT_CHARSET = "charset";
    public static final String ATT_CHECKED = "checked";
    public static final String ATT_CITE = "cite";
    public static final String ATT_CLASS = "class";
    public static final String ATT_CLEAR = "clear";
    public static final String ATT_COLS = "cols";
    public static final String ATT_COLSPAN = "colspan";
    public static final String ATT_COLUMNS = "columns";
    public static final String ATT_CONTENT = "content";
    public static final String ATT_COORDS = "coords";
    public static final String ATT_DIR = "dir";
    public static final String ATT_DISABLED = "disabled";
    public static final String ATT_DOMAIN = "domain";
    public static final String ATT_EMPTYOK = "emptyok";
    public static final String ATT_ENCTYPE = "enctype";
    public static final String ATT_FOR = "for";
    public static final String ATT_FORMAT = "format";
    public static final String ATT_FORUA = "forua";
    public static final String ATT_HEIGHT = "height";
    public static final String ATT_HREF = "href";
    public static final String ATT_HREFLANG = "hreflang";
    public static final String ATT_HSPACE = "hspace";
    public static final String ATT_ID = "id";
    public static final String ATT_INAME = "iname";
    public static final String ATT_ISTYLE = "istyle"; 
    public static final String ATT_IVALUE = "ivalue";
    public static final String ATT_LABEL =  "label";
    public static final String ATT_LANG = "lang";
    public static final String ATT_LANGUAGE = "language";
    public static final String ATT_LOCALSRC = "localsrc";
    public static final String ATT_LONGDESC = "longdesc";
    public static final String ATT_MAXLENGTH = "maxlength";
    public static final String ATT_MEDIA = "media";
    public static final String ATT_METHOD = "method";
    public static final String ATT_MODE = "mode";
    public static final String ATT_MULTIPLE = "multiple";
    public static final String ATT_NAME = "name";
    public static final String ATT_NEWCONTEXT = "newcontext";
    public static final String ATT_ONCHANGE = "onchange";
    public static final String ATT_ONCLICK = "onclick";
    public static final String ATT_ONENTERBACKWARD = "onenterbackward";
    public static final String ATT_ONENTERFORWARD = "onenterforward";
    public static final String ATT_ONEXIT = "onexit";
    public static final String ATT_ONPICK = "onpick";
    public static final String ATT_ONTHROW = "onthrow";
    public static final String ATT_ONTIMER = "ontimer";
    public static final String ATT_OPTIONAL = "optional";
    public static final String ATT_ORDERED = "ordered";
    public static final String ATT_PATH = "path";
    public static final String ATT_PROFILE = "profile";
    public static final String ATT_PROPERTY = "property";
    public static final String ATT_READONLY = "readonly";
    public static final String ATT_REL = "rel";
    public static final String ATT_REV = "rev";
    public static final String ATT_ROWS = "rows";
    public static final String ATT_ROWSPAN = "rowspan";
    public static final String ATT_SCOPE = "scope";
    public static final String ATT_SELECTED = "selected";
    public static final String ATT_SENDREFERER = "sendreferer";
    public static final String ATT_SHAPE = "shape";
    public static final String ATT_SIZE = "size";
    public static final String ATT_SOURCE = "source";
    public static final String ATT_SRC = "src";
    public static final String ATT_STYLE = "style";
    public static final String ATT_STYLE_CLASS = "styleClass";
    public static final String ATT_TABINDEX = "tabindex";
    public static final String ATT_TARGET = "target";
    public static final String ATT_TITLE = "title";
    public static final String ATT_TYPE = "type";
    public static final String ATT_USEMAP = "usemap";
    public static final String ATT_VALIGN = "valign";
    public static final String ATT_VALUE = "value";
    public static final String ATT_VSPACE = "vspace";
    public static final String ATT_XMLLANG = "xml:lang";
    public static final String ATT_XMLNS =  "xmlns";
    public static final String ATT_XMLSPACE = "xml:space";
    public static final String ATT_WIDTH = "width";

    public static final String ATT_CACHE = "CACHE";
    public static final String ATT_DTD = "DTD";


    public static final String VALUE_HIDDEN = "hidden";
    public static final String VALUE_SUBMIT = "submit";
    public static final String VALUE_RESET = "reset";
    public static final String VALUE_PASSWORD = "password";
    public static final String VALUE_TEXT = "text";
    public static final String VALUE_FILE = "file";
    public static final String VALUE_POST = "post";
    public static final String VALUE_TEXT_CSS = "text/css";
    public static final String VALUE_STYLESHEET = "stylesheet";
    public static final String VALUE_ACCEPT = "accept";

    public static final String WML_FIRST_CARD = "FIRST_CARD";
    public static final String WML_DELAY = "WML.DELAY";
    public static final String WML_CARD_ID = "CARD_ID";
    public static final String WML_NEXT = "next";
    public static final String WML_NEXCONTEXT = "newcontext";
    public static final String WML_COMMAND_TYPE = "commandType";
    public static final String WML_COMMAND_BUTTON = "button";
    public static final String WML_COMMAND_LINK = "link";

    public static final String MEDIA_ADAPTER_STRATEGY = "STRATEGY";
    public static final String MEDIA_ADAPTER_RESOURCE = "RESOURCE";
    public static final String MEDIA_ADAPTER_MANAGED = "MANAGED";

    public static final String MARKUP_HTML_BASIC = "HTML_BASIC";
    public static final String MARKUP_XHTML_MP = "XHTML_MP";
    public static final String MARKUP_WML = "WML";

    public static final String ATT_SCRIPT_SRC = "SCRIPT_SRC";
    public static final String JAVASCRIPT = "javascript";
    public static final String ECMASCRIPTMP = "ecmascriptmp";
    public static final String WMLSCRIPT = "wmlscript";
    public static final String WMLSCRIPTC = "wmlscriptc";

    public static final String[] PASS_THROUGH_ATTR_XHTML_MP = {
        ATT_ACCEPT,
        ATT_ACCESSKEY,
        "acceptCharset;" + ATT_ACCEPT_CHARSET,
        ATT_ALIGN,
        ATT_ALT,
        ATT_BORDER,
        ATT_CELLPADDING,
        ATT_CELLSPACING,
        ATT_CHARSET,
        ATT_CITE,
        ATT_STYLE_CLASS + ";" + ATT_CLASS,
        ATT_CLEAR,
        ATT_COLS,
        ATT_COLSPAN,
        ATT_COORDS,
        ATT_DIR,
        ATT_EMPTYOK,
        ATT_ENCTYPE,
        ATT_FOR,
        ATT_FORMAT,
        ATT_HEIGHT,
        ATT_HREFLANG,
        ATT_HSPACE,
        ATT_ISTYLE,
        ATT_LABEL,
        ATT_LOCALSRC,
        ATT_LONGDESC,
        ATT_MAXLENGTH,
        ATT_MEDIA,
        ATT_MODE,
        ATT_PROFILE,
        ATT_READONLY,
        ATT_REL,
        ATT_REV,
        ATT_ROWS,
        ATT_ROWSPAN,
        ATT_SHAPE,
        ATT_SIZE,
        ATT_STYLE,
        ATT_TABINDEX,
        ATT_TARGET,
        ATT_TITLE,
        ATT_USEMAP,
        ATT_VALIGN,
        ATT_VSPACE,
        "xmlLang;" + ATT_XMLLANG,
        "xmlSpace;" + ATT_XMLSPACE,
        ATT_WIDTH
    };

    public static final String[] PASS_THROUGH_ATTR_WML = {
        "acceptCharset;" + ATT_ACCEPT_CHARSET,
        ATT_ACCESSKEY,
        ATT_ALIGN,
        ATT_ALT,
        ATT_COLUMNS,
        ATT_DOMAIN,
        ATT_EMPTYOK,
        ATT_FORMAT,
        ATT_FORUA,
        ATT_HEIGHT,
        ATT_HSPACE,
        ATT_INAME,
        ATT_IVALUE,
        ATT_LABEL,
        ATT_LOCALSRC,
        ATT_MAXLENGTH,
        ATT_MODE,
        ATT_MULTIPLE,
        ATT_NEWCONTEXT,
        ATT_ONENTERBACKWARD,
        ATT_ONENTERFORWARD,
        ATT_ONEXIT,
        ATT_ONPICK,
        ATT_ONTHROW,
        ATT_ONTIMER,
        ATT_OPTIONAL,
        ATT_ORDERED,
        ATT_PATH,
        ATT_PROPERTY,
        ATT_REL,
        ATT_SENDREFERER,
        ATT_SIZE,
        ATT_SOURCE,
        ATT_TABINDEX,
        ATT_TITLE,
        ATT_VSPACE,
        ATT_WIDTH,
        "lang;" + ATT_XMLLANG
    };

    // extension tag library
    protected static HashMap<String, String> lib = new HashMap<String, String>();

    /**
     * <p>put extension tag to library</p>
     * @param key extension tag name
     * @param value extension tag value
     */
    public static void put(String key, String value) {
        lib.put(key, value);
    }

    /**
     * <p>get extension tag from library</p>
     * @param key extension tag name
     * @return extension tag value
     */
    public static final String get(String key) {
        return lib.get(key);
    }

    /**
     * HTML tag library
     */
    public static final String HTML = "html";
    /**
     * WML tag library
     */
    public static final String WML = "wml";
    /**
     * XHTML_MP tag library
     */
    public static final String XHTML_MP = "xhtml-mp";
    /**
     * CHTML tag library
     */
    public static final String CHTML = "chtml";
}
