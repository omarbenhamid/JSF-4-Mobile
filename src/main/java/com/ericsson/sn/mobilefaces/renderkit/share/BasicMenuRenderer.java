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

import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.lang.reflect.Array;

import javax.el.ValueExpression;
import javax.faces.convert.Converter;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.convert.ConverterException;

import com.ericsson.sn.mobilefaces.util.RenderUtils;

/**
 * <p>
 *      BasicMenuRenderer implements methods for decode and select value convert.
 * </p>
 *
 *
 *
 * <p>Company:   Ericsson AB</p>
 *
 *
 * @author Daning Yang
 * @version 1.0
 */
public abstract class BasicMenuRenderer extends BasicInputRenderer {

    /**
     * <p>Create the instance.</p>
     * @param markup markup language type for constants
     */
    public BasicMenuRenderer(String markup) {
        super(markup);
    }

    /**
     * <p>The option submit value is array or long string pattern</p>
     * @return true if it is a array
     */
    public abstract boolean isArrayParams();

    /**
     * <p>Decode the menu component. Set the submit value
     *    for select many or select one component</p>
     * @param context FacesContext
     * @param component UIComponent
     */
    @Override
    public void decode(FacesContext context, UIComponent component) {
        super.decode( context, component);

        String clientId = component.getClientId(context);
        Map requestParam = null;
        // decode select many
        if (component instanceof UISelectMany) {
            requestParam = context.getExternalContext().getRequestParameterValuesMap();
            if (requestParam.containsKey(clientId)) {
                // set submit value
                String[] value;
                if (isArrayParams())
                    value = (String[]) requestParam.get(clientId);
                else {
                    requestParam = context.getExternalContext().getRequestParameterMap();
                    value = RenderUtils.parseStrings((String)requestParam.get(clientId), ";");
                }
                submitValue(component, value);
            } else{
                // set empty value for null submit value
                submitValue(component, new String[0]);
            }
        }
        // decode select one
        else if (component instanceof UISelectOne) {
            requestParam = context.getExternalContext().getRequestParameterMap();
            if (requestParam.containsKey(clientId)) {
                // set submit value
                String value = (String) requestParam.get(clientId);
                submitValue(component, value);
            } else {
                // set empty value for null submit value
                submitValue(component, new String());
            }
        }
    }

    /**
     * <p>Get converted value for menu component.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @param submittedValue value object for convert
     * @throws javax.faces.convert.ConverterException except if cannot do convert
     * @return converted value
     */
    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue)
    throws ConverterException {
        if (component instanceof UISelectMany) {
            return convertSelectManyValue(context, ((UISelectMany) component), (String[]) submittedValue);
        } else {
            return super.getConvertedValue(context, (UISelectOne) component, (String) submittedValue);
        }
    }

    /**
     * <p>Get disabled item count. Disabled items will be rendered as text
     *    for mobile device. So we need the count number for render.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @return disabled item count
     */
    protected int getDisableCount(FacesContext context, UIComponent component) {

        Iterator items = getSelectItems(context, component);
        SelectItem curItem = null;
        int disableCount = 0;
        // check the disabled items count for the group
        while (items.hasNext()) {
            curItem = (SelectItem) items.next();
            if (curItem instanceof SelectItemGroup) {
                SelectItem[] itemsArray =
                        ((SelectItemGroup) curItem).getSelectItems();
                for (int i=0; i<itemsArray.length; ++i) {
                    if (itemsArray[i].isDisabled()) {
                        disableCount++;
                    }
                }
            } else {
                if (curItem.isDisabled()) disableCount++;
            }
        }
        return disableCount;
    }

    /**
     * <p>Set the menu is sizeble or not.</p>
     * @param sizeable sizeble or not
     */
    protected void setSizeable(boolean sizeable) {
        this.sizeable = sizeable;
    }

    /**
     * <p>Get the sizeable value.</p>
     * @return is sizeable or not
     */
    protected boolean isSizeable() {
        return sizeable;
    }

    /**
     * <p>Get converted selectmany value.</p>
     *
     * @param context FacesContext
     * @param component SelectMany component
     * @param newValue menu new value
     * @return converted value
     * @throws javax.faces.convert.ConverterException except if cannot convert
     */
    protected Object convertSelectManyValue(FacesContext context, UISelectMany component, String[] newValue)
    throws ConverterException {
        if (newValue == null) return null;

        ValueExpression ve = component.getValueExpression("value");
        Class veType = null;
        Class arrayType = null;
        if (ve != null) {
            veType = ve.getType(context.getELContext());
            if (veType != null && veType.isArray()) {
                arrayType = veType.getComponentType();
            }
        }

        Converter converter = component.getConverter();
        if (converter == null) {
            if (veType == null) {
                return newValue;
            }

            if (List.class.isAssignableFrom(veType)) {
                List<String> lst = new ArrayList<String>(newValue.length);
                for (int i=0; i<newValue.length; i++) {
                    lst.add(newValue[i]);
                }
                return lst;
            }

            if (arrayType == null) {
                throw new IllegalArgumentException("ValueBinding for UISelectMany must be of type List or Array");
            }

            if (String.class.equals(arrayType) || Object.class.equals(arrayType)) return newValue;

            try {
                converter = context.getApplication().createConverter(arrayType);
            } catch (Exception e) {
                return newValue;
            }
        }

        if (veType == null) {
            int len = newValue.length;
            Object [] convertedValues = (Object []) Array.newInstance(arrayType, len);
            for (int i = 0; i < len; i++) {
                convertedValues[i]
                        = converter.getAsObject(context, component, newValue[i]);
            }
            return convertedValues;
        }

        if (List.class.isAssignableFrom(veType)) {
            int len = newValue.length;
            List<Object> lst = new ArrayList<Object>(len);
            for (int i=0; i<len; i++) {
                lst.add(converter.getAsObject(context, component, newValue[i]));
            }
            return lst;
        }

        if (arrayType == null) {
            throw new IllegalArgumentException("ValueBinding for UISelectMany must be of type List or Array");
        }

        if (arrayType.isPrimitive()) {
            int len = newValue.length;
            Object convertedValues = Array.newInstance(arrayType, len);
            for (int i=0; i<len; i++) {
                Array.set(convertedValues, i,
                        converter.getAsObject(context, component, newValue[i]));
            }
            return convertedValues;
        } else {
            int len = newValue.length;
            Object[] convertedValues = new Object[len];
            for (int i=0; i<len; i++) {
                convertedValues[i] = converter.getAsObject(context, component, newValue[i]);
            }
            return convertedValues;
        }
    }

    /**
     * <p>Return an Iterator over instances representing the
     * available options for this component, assembled from the set of
     * and/or components that are
     * direct children of this component.  If there are no such children, a
     * zero-length array is returned.</p>
     *
     *
     * @param component UIComponent
     * @param context The {@link FacesContext} for the current request.
     *                If null, the UISelectItems behavior will not work.
     * @return select item iterator
     */
    protected Iterator getSelectItems(FacesContext context, UIComponent component) {
        ArrayList<SelectItem> list = new ArrayList<SelectItem>();
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            if (kid instanceof UISelectItem) {
                Object value = ((UISelectItem) kid).getValue();
                if (value == null) {
                    // construct iterator list by items
                    UISelectItem item = (UISelectItem) kid;
                    list.add(new SelectItem(item.getItemValue(),
                            item.getItemLabel(),
                            item.getItemDescription(),
                            item.isItemDisabled()));
                } else if (value instanceof SelectItem) {
                    list.add((SelectItem)value);
                } else {
                    return null;
                }
            } else if (kid instanceof UISelectItems && context != null) {
                Object value = ((UISelectItems) kid).getValue();
                if (value instanceof SelectItem) {
                    list.add((SelectItem)value);
                } else if (value instanceof SelectItem[]) {
                    SelectItem items[] = (SelectItem[]) value;
                    for (int i=0; i<items.length; i++) {
                        list.add(items[i]);
                    }
                } else if (value instanceof Collection) {
                    Iterator elements = ((Collection) value).iterator();
                    while (elements.hasNext()) {
                        list.add((SelectItem)elements.next());
                    }
                } else if (value instanceof Map) {
                    Iterator keys = ((Map) value).keySet().iterator();
                    while (keys.hasNext()) {
                        Object key = keys.next();
                        if (key == null) {
                            continue;
                        }
                        Object val = ((Map) value).get(key);
                        if (val == null) {
                            continue;
                        }
                        list.add(new SelectItem(val.toString(), key.toString(), null));
                    }
                } else {
                    return null;
                }
            }
        }
        return (list.iterator());
    }

    /**
     * <p>Get option item number.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @return option number
     */
    protected int getOptionNumber(FacesContext context, UIComponent component) {
        Iterator items = getSelectItems(context, component);
        int itemCount = 0;
        // count the children items
        while (items.hasNext()) {
            itemCount++;
            SelectItem item = (SelectItem) items.next();
            if (item instanceof SelectItemGroup) {
                int optionsLength = ((SelectItemGroup) item).getSelectItems().length;
                itemCount = itemCount + optionsLength;
            }
        }
        return itemCount;
    }

    /**
     * <p>Get selected item of menu for submitting.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @return get selected item values
     */
    protected Object[] getSubmittedSelectedValues(FacesContext context,
            UIComponent component) {
        if (component instanceof UISelectMany) {
            // get submit value of select many component
            UISelectMany select = (UISelectMany) component;
            return (Object[]) select.getSubmittedValue();
        }

        UISelectOne select = (UISelectOne) component;
        Object returnObject;
        if (null != (returnObject = select.getSubmittedValue())) {
            // get submit value of select one component
            return new Object[]{returnObject};
        }
        return null;
    }

    /**
     * <p>Check the menu has been selected or not.</p>
     * @param itemValue selected value
     * @param valueArray submit value arrary
     * @return selected or not
     */
    protected boolean isSelected(Object itemValue, Object valueArray) {
        if (valueArray != null) {
            int len = Array.getLength(valueArray);
            // check all items in the array
            for (int i=0; i<len; i++) {
                Object value = Array.get(valueArray, i);
                if (value == null) {
                    if (itemValue == null) {
                        return true;
                    }
                } else if (value.equals(itemValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>Check the menu has been selected or not.</p>
     * @param itemValue selected value
     * @param values submit value arrary
     * @return selected or not
     */
    protected boolean isSelected(Object itemValue, Object[] values) {
        if (values != null) {
            int len = values.length;
            // check all items in the array
            for (int i=0; i<len; i++) {
                if (values[i].equals(itemValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>Get current selected value for the menu.</p>
     * @param context FacesContext
     * @param component UIComponent
     * @return selected value
     */
    protected Object getCurrentSelectedValues(FacesContext context,  UIComponent component) {
        if (component instanceof UISelectMany) {
            // get the selected value for select many
            UISelectMany select = (UISelectMany) component;
            Object value = select.getValue();
            if (value instanceof List)
                return ((List) value).toArray();

            return value;
        }
        // get the selected value for select one
        UISelectOne select = (UISelectOne) component;
        Object returnObject = select.getValue();
        if (returnObject != null) {
            return new Object[]{returnObject};
        }
        return null;
    }

    private boolean sizeable = false;
}
