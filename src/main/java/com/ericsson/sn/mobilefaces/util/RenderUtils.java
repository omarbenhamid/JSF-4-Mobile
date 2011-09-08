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

import java.util.ArrayList;

import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * <p>
 * Util group to support rendering work.
 * </p>
 * 
 * <p>
 * Company: Ericsson AB
 * </p>
 * 
 * 
 * @author Daning Yang
 * @version 1.1
 */
public class RenderUtils {

	/**
	 * <p>
	 * Get the string array by parseing a long string.
	 * </p>
	 * <p>
	 * For example:<br/>
	 * Input: str="abc;adc;adc" slot=";"<br/>
	 * Output: string[0] = "abc" string[1] = "adc" string[2] = "adc"
	 * </p>
	 * 
	 * @param str
	 *            origianl string
	 * @param slot
	 *            slot string
	 * @return result string array
	 */
	public static String[] parseStrings(String str, String slot) {
		if (str == null) {
			return (new String[0]);
		}

		str = str.trim();
		ArrayList<String> list = new ArrayList<String>();
		while (str.length() > 0) {
			int comma = str.indexOf(slot);
			if (comma >= 0) {
				list.add(str.substring(0, comma).trim());
				str = str.substring(comma + 1);
			} else {
				list.add(str.trim());
				str = "";
			}
		}
		String results[] = new String[list.size()];
		return list.toArray(results);
	}

	/**
	 * <p>
	 * Check the value is default or not.
	 * </p>
	 * 
	 * @param value
	 *            value is an instance of a wrapper
	 * @return true if and only if the value is default
	 */
	public static boolean isDefaultValue(Object value) {
		if (value == null) {
			return true;
		}

		if (value instanceof Boolean
				&& (Boolean) value == Boolean.FALSE.booleanValue()) {
			return true;
		} else if (value instanceof Double
				&& (Double) value == Double.MIN_VALUE) {
			return true;
		} else if (value instanceof Integer
				&& (Integer) value == Integer.MIN_VALUE) {
			return true;
		} else if (value instanceof Character
				&& (Character) value == Character.MIN_VALUE) {
			return true;
		} else if (value instanceof Float && (Float) value == Float.MIN_VALUE) {
			return true;
		} else if (value instanceof Short && (Short) value == Short.MIN_VALUE) {
			return true;
		} else if (value instanceof Long && (Long) value == Long.MIN_VALUE) {
			return true;
		} else if (value instanceof Byte && (Byte) value == Byte.MIN_VALUE) {
			return true;
		}
		return false;
	}

	/**
	 * <p>
	 * Get a convertor for current component. If component is a Value Holder,
	 * just return the convertor from Holder. Else create a Convertor from
	 * valuebinding or value.
	 * </p>
	 * 
	 * @param context
	 *            FacesContext
	 * @param component
	 *            component to get convertor
	 * @param value
	 *            the value to be converted
	 * @param fromValueBinding
	 *            generate convertor from value binding or value
	 * @return the generated convertor
	 */
	public static Converter getConvertor(FacesContext context,
			UIComponent component, Object value, boolean fromValueBinding) {
		// return the convertor from Holder if has
		if (component instanceof ValueHolder) {
			Converter candidate = ((ValueHolder) component).getConverter();
			if (candidate != null)
				return candidate;
		}
		Class type;
		// get convertor from value binding or value
		if (fromValueBinding) {
			ValueExpression ve = component.getValueExpression("value");
			type = ve.getType(context.getELContext());
		} else {
			type = value.getClass();
		}
		// return the convertor if has
		if (type == null || type == String.class || type == Object.class) {
			return null;
		} else {
			Application application = context.getApplication();
			return application.createConverter(type);
		}
	}

	public static String toString(Object obj) {
		return obj != null ? obj.toString() : null;
	}
}
