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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * <p>
 *  Design pattern: Facade
 *
 *  This class is a Facade that hides the complexity to load property from an XML file.
 *  This class uses Vector as Iterator pattern and return Collection/aggregate interface.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @author Li Ping
 * @version 1.1
 */
public class PropLoader extends DefaultHandler {

    // XML validation
    public boolean validating =true;

    // use XML namespace
    public boolean namespaceAware =false;

    // The XML file Name
    private String XmlFileName;

    // The content object to return which implements the Collection interface
    private Collection<Object> content;

    // The object tobe in the Collection
    private PropertyContainer prop;

    // The character textnode string from the XML
    private String textString = "";

    // The startElement tag name
    private String startElement = "";

    // The endElement tag name
    private String endElement = "";

    // The begin parse
    private boolean beginParse = false;

    // The xmlElement tag name
    private String xmlElement = "";

    // Test case
    public static void main(String args[]) {

        String fileName = "";
        String xmlTag = "";
        // If there is only two argument
        if (args.length == 2) {
            // matar in arg som fil namn
            fileName= args[0];
            xmlTag = args[1];
            Log.info(" Validate XML with SAX2\n", PropLoader.class);
            Log.info(" To load", PropLoader.class);
            Log.info(" fileName = "+ fileName +"\n", PropLoader.class);

        } else {
            Log.info("\nValid XML \n", PropLoader.class);
            Log.info(" java PropLoader <fileName> <xmlTag>", PropLoader.class);
            Log.info(" Example:", PropLoader.class);
            Log.info(" java PropLoader valid.xml beginTag\n", PropLoader.class);
            System.exit(0);
        }

        // Prepare the loader
        PropLoader saxFacade = new PropLoader(xmlTag);
        Collection collection = saxFacade.parse(fileName);
        // Load xml file content
        Iterator iterator = collection.iterator();
        PropertyContainer link;

        // Display all
        while (iterator.hasNext()) {
            link = (PropertyContainer)iterator.next();
            Log.info(link, PropLoader.class);
        }
    }

    /**
     * Initialize loader with xml element tag.
     * @param xmlElement xml element tag
     */
    public PropLoader(String xmlElement) {
        super();
        this.xmlElement = xmlElement;
    }

    /**
     * Parse
     * @param XmlFileName  A variable of type <code>String</code> representing XML file for parsing
     * @return Collection List of links
     *
     */
    public Collection parse(String XmlFileName) {
        this.XmlFileName = XmlFileName;
/*
        Log.debug("Loading XML file: " + XmlFileName, PropLoader.class);
*/
        // Use an instance of itself as the SAX event handler
        DefaultHandler handler = this; // new saxFacade();

        // Use the validating parser
        SAXParserFactory factory = SAXParserFactory.newInstance();

        // validating towards xml schema defined in the xml file
        factory.setValidating(validating);

        factory.setNamespaceAware(namespaceAware);

        try {
            // Parse the input
            SAXParser saxParser = factory.newSAXParser();

            // To unix file path format e.g. c:/tomcat/ .... insted of c:\tomcat\....
            XmlFileName = XmlFileName.replace('\\','/');

            // 2 ways to parse a xml file depending if the xmlfile is located inside a Jar file or not.
            // inputstream for JAR is used if the xmlfile is located in the Jar file.

            // For reading XML file from a Jar arcive
            InputStream inputStream = this.getClass().getResourceAsStream(XmlFileName);

            // For directly read XML file from a folder
            File file = new File(XmlFileName);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            InputSource inputSource = new InputSource(bufferedReader);

            // If it is not possible to read from Jar archive via inputStream then read it from inputSource
            if (inputStream == null) {
                saxParser.parse( inputSource, handler);
            } else {
                saxParser.parse( inputStream, handler);
            }
            fileReader.close();
            bufferedReader.close();
        } catch (SAXParseException spe) {
            // Error generated by the parser
            Log.err("\n** Parsing error"
                    + ", line " + spe.getLineNumber()
                    + ", uri " + spe.getSystemId(), PropLoader.class);
            Log.err("   " + spe.getMessage() , PropLoader.class);

            // Use the contained exception, if any
            Exception  x = spe;
            if (spe.getException() != null)
                x = spe.getException();
            x.printStackTrace();
        } catch (SAXException sxe) {
            // Error generated by this application
            // (or a parser-initialization error)
            Exception  x = sxe;
            if (sxe.getException() != null)
                x = sxe.getException();
            x.printStackTrace();
            Log.err("\n**   " + x.getMessage(), PropLoader.class );
        } catch (ParserConfigurationException pce) {
            // Parser with specified options can't be built
            pce.printStackTrace();
            Log.err("\n**   " + pce.getMessage(), PropLoader.class );
        } catch (IOException ioe) {
            // I/O error
            ioe.printStackTrace();
            Log.err("\n**   " + ioe.getMessage(), PropLoader.class );
        }
        return content;
    }

    /**
     * Receive notification of the beginning of the document.
     */
    @Override
    public void startDocument() throws SAXException {
        content = new Vector<Object>();
    }

    /**
     *  Receieve notification of the end of the document.
     *
     */
    @Override
    public void endDocument() throws SAXException {
    }

    /**
     *  Receieve notification of the start of an element
     *  @param namespaceURI A variable of type <code>String</code> representing the namespaceURI
     *  @param lName A variable of type <code>String</code> representing the local name
     *  @param qName A variable of type <code>String</code> representing the qualified name
     *  @param attrs variable of type <code>Attributes</code> representing the attrs
     */
    @Override
    public void startElement(String namespaceURI,
            String lName,
            String qName,
            Attributes attrs) throws SAXException {

        // save the startElement to be used in the characters node
        startElement = qName;

        // match the link tag
        if (qName.equals(xmlElement) ) {
            // Begin parsing
            beginParse = true;
            prop = new PropertyContainer();
        }
    }

    /**
     *  Receieve notification of the end of an element
     *  @param namespaceURI A variable of type <code>String</code> representing the namespaceURI
     *  @param sName simple name A variable of type <code>String</code> representing the start name
     *  @param qName qualified name A variable of type <code>String</code> representing the qualified name
     *
     */
    @Override
    public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
        endElement = qName;

        if (qName.compareTo(xmlElement)==0) {
            // insert the link into the Collection object "content"
            beginParse = false;
            content.add(prop);
        }
    }

    /**
     *  SAX callback method for the character nod
     *  @param buf
     *  @param offset
     *  @param len
     *  @throws SAXException
     */
    @Override
    public void characters(char buf[], int offset, int len) throws SAXException {
        textString = new String(buf, offset, len);

        // trim() is important for get Integer.parseInt to work
        textString = textString.trim();

        // avoide ascii 9 or 10 comes in into the compare of the startElement
        if (textString.equals("")) {
        } else if (beginParse) {
            prop.setProperty( startElement, textString);
        } else {
        }
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {

    }
}
