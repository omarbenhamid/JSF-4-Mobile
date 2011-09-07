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

import com.ericsson.sn.mobilefaces.MobileResponseStateManager;
import java.util.Hashtable;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * <p>
 *      HttpRequestWrapper for transforming GET to POST requests.
 * </p>
 *
 * <p>Company:   Ericsson AB</p>
 *
 * @version      1.0
 */
public class GetToPostRequestWrapper extends HttpServletRequestWrapper {

    public Hashtable<String, String> headers = null;

    public GetToPostRequestWrapper(HttpServletRequest servletRequest, Hashtable<String, String> headers) {
        super(servletRequest);

        Enumeration e = servletRequest.getHeaderNames();
        while (e.hasMoreElements()) {
            String hName = (String)e.nextElement();
            String hValue = servletRequest.getHeader(hName);
            headers.put(hName, hValue);
        }

        if (headers != null) {
            this.headers = headers;
        }
    }

    @Override
    public String getMethod(){
        return "POST";
    }

    @Override
    public Enumeration getHeaderNames() {
        return headers != null ? headers.keys() : null;
    }

    @Override
    public Enumeration getHeaders(String name) {
        Hashtable headerCopy = (Hashtable)headers.clone();

        Object elem;
        Enumeration e = headerCopy.elements();
        while(e.hasMoreElements()) {
            elem = e.nextElement();
            if (!elem.equals(name)) {
                headerCopy.remove(elem);
            }
        }
        return headerCopy.elements();
    }

    @Override
    public String getHeader(String name) {
        return headers != null ? headers.get(name) : null;
    }

    @Override
    public int getIntHeader(String name) {
        return headers != null ? new Integer(name).intValue() : -1;
    }

    @Override
    public String getQueryString() {
        return null;
    }

    /**
     * Transforms a GET request to POST request by wrapping the old request
     * with a new one, adding the query parameters to the request header. Note
     * that this is only done if <code>VIEW_STATE_PARAM</code> is a key in the
     * GET request.
     *
     * @param request The HttpServletRequest
     * @return The HttpServletRequest, whether it is transformed or not.
     */
    public static HttpServletRequest transform(HttpServletRequest request) {
        if (request != null && request.getQueryString() != null) {
            String queryString = request.getQueryString();
            Hashtable<String, String> headers = new Hashtable<String, String>();
            String[] queries = queryString.split("&");
            for (int i = 0; i < queries.length; i++) {
                String query = queries[i];
                String[] queryInfo = query.split("=");
                String qName = queryInfo[0];
                String qValue = queryInfo.length > 1 ? queryInfo[1] : "";
                headers.put(qName, qValue);
            }

            if (headers.containsKey(MobileResponseStateManager.VIEW_STATE_PARAM)) {
                return new GetToPostRequestWrapper(request, headers);
            }
        }

        return request;
    }
}
