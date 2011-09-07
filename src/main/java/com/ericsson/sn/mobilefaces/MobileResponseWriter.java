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
package com.ericsson.sn.mobilefaces;

import java.io.Writer;
import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import com.ericsson.sn.mobilefaces.util.HTMLEncoder;

/**
 * <p>
 *      MobileResponseWriter is an implementation of ResponseWriter
 *      for writing response HTML/XHTML-MP/WML pages.
 * </p>
 *
 * <p>
 *      MobileResponseWriter implements basic writer functions to
 *      write tag, content and attributes. It uses {@link com.ericsson.sn.mobilefaces.util.HTMLEncoder}
 *      to encode input strings for HTML page.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public class MobileResponseWriter extends ResponseWriter {

    private Writer writer = null;
    private static HTMLEncoder encoder;

    private String contentType = "text/html";
    private String encoding = null;

    private char[] buffer = new char[1028];
    private char[] charHolder = new char[1];

    private boolean isEmpty;
    private boolean needClose;
    private boolean notEsp;

    /**
     * <p> create a instance with initialization</p>
     *
     * @param writer      the writer for this class to output
     * @param contentType the content type.
     * @param encoding    the character encoding.
     * @throws FacesException cannot create the instance.
     */
    public MobileResponseWriter(Writer writer, String contentType, String encoding)
    throws FacesException {
        this.writer = writer;
        if (null != contentType) {
            this.contentType = contentType;
        }
        this.encoding = encoding;
        if(encoder == null)
            encoder = new HTMLEncoder();
    }

    /**
     * <p> page content type </p>
     * @return the content type for this ResponseWriter.
     */
    @Override
    public String getContentType() {
        return contentType;
    }

    /**
     * <p> character encoding </p>
     * @return the character encoding for this ResponseWriter.
     */
    @Override
    public String getCharacterEncoding() {
        return encoding;
    }

    /**
     * <p> begin a response.</p>
     *
     * @throws IOException fail to write the document
     */
    @Override
    public void startDocument() throws IOException {
    }

    /**
     * <p> end writing a response.</p>
     * @throws IOException fail to write the document
     */
    @Override
    public void endDocument() throws IOException {
        writer.flush();
    }

    /**
     * <p> update the buffer string.</p>
     *
     * @throws IOException if an input/output error occurs.
     */
    @Override
    public void flush() throws IOException {
        closeStartTag();
    }

    /**
     * <p>write the start tag of an element for the component.</p>
     *
     * @param name the start tag of element
     * @param component the component responsible for this tag
     * @throws IOException failed to write the tag
     */
    @Override
    public void startElement(String name, UIComponent component)
    throws IOException {
        if (name == null) {
            return;
        }
        closeStartTag();

        char firstChar = name.charAt(0);
        if (firstChar == 's' || firstChar == 'S') {
            if ("script".equalsIgnoreCase(name) ||
                    "skip".equalsIgnoreCase(name) ||
                    "style".equalsIgnoreCase(name)) {
                notEsp = true;
            }
        }

        if (!"skip".equalsIgnoreCase(name)) {
            writer.write("<");
            writer.write(name);
            isEmpty = true;
            needClose = true;
        }
    }

    /**
     * <p>write the end tag of an element for the component.</p>
     *
     * @param name the end tag of element
     * @throws IOException failed to write the tag
     */
    @Override
    public void endElement(String name) throws IOException {
        if (name == null) {
            return;
        }

        notEsp = false;
        if (needClose) {
            if (isEmpty && !"script".equalsIgnoreCase(name)) {
                writer.write(" />");
                needClose = false;
                return;
            }

            writer.write(">");
            needClose = false;
        }
        // write the end tag
        if(!"skip".equalsIgnoreCase(name)) {
            writer.write("</");
            writer.write(name);
            writer.write(">");
        }
    }

    /**
     * <p>write the attribute for the component.</p>
     * @param name attribute name
     * @param value attribute value
     * @param componentName the component responsible for this tag
     * @throws IOException failed to write the tag
     */
    @Override
    public void writeAttribute(String name, Object value, String componentName)
    throws IOException {
        if (name == null || value == null) {
            return;
        }
        Class valueClass = value.getClass();

        if (valueClass == Boolean.class) {
            if (Boolean.TRUE.equals(value)) {
                writer.write(" ");
                writer.write(name);
                writer.write("=\"");
                writer.write(name);
                writer.write("\"");
            } else {
                // Don't write anything for "false" booleans
            }
        } else {
            writer.write(" ");
            writer.write(name);
            writer.write("=\"");
            writer.write(encoder.encode(value.toString()));
            writer.write("\"");
        }
    }

    /**
     * <p>write the uri attribute for the component.</p>
     * @param name attribute name
     * @param value attribute value
     * @param componentName the component responsible for this tag
     * @throws IOException failed to write the tag
     */
    @Override
    public void writeURIAttribute(String name, Object value, String componentName)
    throws IOException {
        if (name == null || value == null) {
            throw new NullPointerException();
        }
        writer.write(" ");
        writer.write(name);
        writer.write("=\"");
        String stringValue = value.toString();

        if (stringValue.startsWith("javascript:")) {
            writer.write(stringValue);
        } else {
            writer.write(encoder.encodeURL(stringValue));
        }
        writer.write("\"");
    }

    /**
     * <p>write a comment string</p>
     *
     * @param comment content of the comment
     * @throws IOException failed to write the tag
     */
    @Override
    public void writeComment(Object comment) throws IOException {
        if (comment == null) {
            return;
        }
        closeStartTag();
        writer.write("<!-- ");
        writer.write(comment.toString());
        writer.write(" -->");
    }

    /**
     * <p>write a text with encoding if need.</p>
     *
     * @param text text to be written
     * @param componentName the component responsible for this tag
     * @throws IOException failed to write the tag
     */
    @Override
    public void writeText(Object text, String componentName)
    throws IOException {
        if (text == null) {
            return;
        }
        closeStartTag();
        if (notEsp) {
            writer.write(text.toString());
        } else {
            String tmpStr = text.toString();
            if(encoder != null)
                tmpStr   = encoder.encode(tmpStr);
            writer.write(tmpStr);
        }
    }

    /**
     * <p>write a text with encoding if need.</p>
     *
     * @param text text to be written
     * @param off  starting offset
     * @param len  length of text to be written
     * @throws IOException failed to write the tag
     */
    @Override
    public void writeText(char text[], int off, int len)
    throws IOException {
        if (text == null) {
            return;
        }
        // check that the offset is correct
        if (off < 0 || off > text.length || len < 0 || len > text.length) {
            throw new IndexOutOfBoundsException();
        }
        closeStartTag();
        if (notEsp) {
            writer.write(text, off, len);
        } else {
            String tmpStr = new String(text, off, len);
            if(encoder != null)
                tmpStr = encoder.encode(tmpStr);
            writer.write(tmpStr);
        }
    }

    /**
     * <p>create a new instance of this <code>ResponseWriter</code>.</p>
     *
     *
     * @param writer the writer to create another <code>ResponseWriter</code>.
     * @return response writer
     */
    @Override
    public ResponseWriter cloneWithWriter(Writer writer) {
        try {
            return new MobileResponseWriter(writer, getContentType(),
                    getCharacterEncoding());
        } catch (FacesException e) {
            throw new IllegalStateException();
        }
    }

    /**
     * This method automatically closes a previous element (if not
     * already closed).
     */
    private void closeStartTag() throws IOException {
        if (needClose) {
            writer.write(">");
            isEmpty = false;
            needClose = false;
        }
    }

    /**
     * <p>close the writer</p>
     * @throws java.io.IOException fail to close the writer
     */
    @Override
    public void close() throws IOException {
        closeStartTag();
        writer.close();
    }

    // other write text methods
    public void writeText(char text) throws IOException {
        charHolder[0] = text;
        writeText( charHolder, 0, 1);
    }

    public void writeText(char text[]) throws IOException {
        writeText( text, 0, text.length);
    }

    public void write(char cbuf) throws IOException {
        closeStartTag();
        writer.write(cbuf);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        closeStartTag();
        writer.write(cbuf, off, len);
    }

    @Override
    public void write(int c) throws IOException {
        closeStartTag();
        writer.write(c);
    }

    @Override
    public void write(String str) throws IOException {
        closeStartTag();
        writer.write(str);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        closeStartTag();
        writer.write(str, off, len);
    }
}
