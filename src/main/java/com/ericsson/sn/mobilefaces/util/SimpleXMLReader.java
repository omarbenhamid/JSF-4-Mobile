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

import java.io.InputStream;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>
 *      Easy reader for xml file. Define xml file path by a file path or a http url.
 *      Read a value by a path like "db/query/search/".
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public class SimpleXMLReader {

    private Document document;

    /**
     * <p>Initialize SimpleXMLReader to get a XML file.</p>
     *
     * @param fileurl A http URL or a file path of the XML file.
     */
    public SimpleXMLReader(String fileurl) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            // Get document instance from a xml file.
            document = getDocument( db, fileurl );
        } catch ( Exception e ) {
            Log.err("Cannot open xml file from " + fileurl, SimpleXMLReader.class);
            Log.err(e, SimpleXMLReader.class);
        }
    }

    // Get document instance from a xml file.
    private Document getDocument( DocumentBuilder db, String urlString ) throws Exception {
        InputStream in;
        // Estimate read from remote or local.
        if ((urlString.substring(0,4)).equals("http")) {
            // Get url
            URL url = new URL( urlString );
            URLConnection URLconnection = url.openConnection( ) ;
            // Get http connection
            HttpURLConnection httpConnection = (HttpURLConnection)URLconnection;
            in = httpConnection.getInputStream( ) ;
        } else {
            // Read from file
            in = new FileInputStream(urlString);
        }
        // Read document
        Document doc = db.parse( in );
        return doc;
    }

    /**
     * Get the root node by elementName
     */
    private Node getNode(String elementName) {
        NodeList nodes = document.getElementsByTagName( elementName );
        // Get first node
        Node firstElement = nodes.item( 0 );
        return firstElement;
    }

    /**
     * Get the sub node by node name from parent node
     */
    private Node getNode(Node baseNode, String nodename) {
        Node node = null;
        if (baseNode.getNodeType() == baseNode.ELEMENT_NODE) {
            NodeList nodes = baseNode.getChildNodes();
            // Search nodes to find required sub node
            for (int i=0;i<nodes.getLength();i++) {
                if (nodes.item( i ).getNodeName().equals(nodename)) {
                    node = nodes.item( i );
                    break;
                }
            }
        }
        return node;
    }

    /**
     * Get the node value from a node
     */
    private String getString(Node node) {
        String str = null;
        if (node != null) {
            Node child = node.getFirstChild();
            if (child != null) {
                if (child.getNodeType() == child.TEXT_NODE) {
                    str =  child.getNodeValue();
                }
            }
        }
        return str;
    }

    /**
     * <p>Get a value by a path like "db/query/search/"</p>
     * <p>
     *      Example: <br />
     *      Read value from:
     *      <db><br />
     *          <query><br />
     *              <search><br />
     *                  value<br />
     *              </search><br />
     *          </query><br />
     *      </db><br />
     *      path = "db/query/search/"
     * </p>
     * @param Path path string
     * @return node value
     */
    public String getString(String Path) {
        // Prepare the path
        String path = parseStr(Path);
        int k = path.indexOf("/");
        String rootname = path.substring(0,k);

        // Prepare the node
        Node root = getNode(rootname);

        // Read value from node
        while (path.indexOf("/",k+1) != -1) {
            rootname = path.substring(k+1,path.indexOf("/",k+1));
            k = path.indexOf("/",k+1);
            if (root != null) {
                root = getNode(root, rootname);
            } else {
                return null;
            }
        }
        return getString(root);
    }

    // Correct the path expression.
    private String parseStr(String base) {
        if (base.charAt(0) == '/') {
            base = base.substring(1,base.length());
        }
        if (base.charAt(base.length()-1) != '/') {
            base = base + "/";
        }
        return base;
    }

    // Test for user case
    public static void main(String args[]) {

        String fileName = "";
        String xmlPath = "";
        // If there is only two argument
        if (args.length == 2) {
            // matar in arg som fil namn
            fileName = args[0];
            xmlPath = args[1];
            Log.info(" Read value from XML file\n", SimpleXMLReader.class);
            Log.info(" To read from", SimpleXMLReader.class);
            Log.info(" fileName = "+ fileName +"\n", SimpleXMLReader.class);
        } else {
            Log.info("\nRead XML file \n", SimpleXMLReader.class);
            Log.info(" java EasyXReader <fileName> <xmlPath>", SimpleXMLReader.class);
            Log.info(" Example:", SimpleXMLReader.class);
            Log.info(" java PropLoader valid.xml beginTag\n", SimpleXMLReader.class);
            System.exit(0);
        }

        // Prepare the reader
        SimpleXMLReader reader = new SimpleXMLReader(fileName);
        Log.info("Read value: " + reader.getString(xmlPath), SimpleXMLReader.class);
    }
}
