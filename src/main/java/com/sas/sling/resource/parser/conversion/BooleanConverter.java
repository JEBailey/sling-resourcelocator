/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sas.sling.resource.parser.conversion;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Conversion implementer to convert a Boolean to different values.
 */
public class BooleanConverter implements Converter {

    private final Boolean value;

    public BooleanConverter(final Boolean val) {
        this.value = val;
    }

    private Integer getNumber() {
        return ( value.booleanValue() ? 1 : 0);
    }

	@SuppressWarnings("unchecked")
	@Override
	public <T> T adaptTo(Class<T> klass) {
		switch (ConversionTypes.valueOf(klass.getSimpleName())){
		case BigDecimal:
			return (T) new BigDecimal(this.getNumber().toString());
		case Boolean:
			return (T) this.value;
		case Byte:
			return (T) Byte.valueOf(this.getNumber().byteValue());
		case GregorianCalendar:
	        Calendar cal = Calendar.getInstance();
	        cal.setTimeInMillis(this.getNumber().longValue());
	        return (T) cal;
		case Date:
			return (T) new Date(this.getNumber().longValue());
		case Double:
			return (T) Double.valueOf(this.getNumber().doubleValue());
		case Float:
			return (T) Float.valueOf(this.getNumber().floatValue());
		case Integer:
			return (T) this.getNumber();
		case Long:
			return (T) Long.valueOf(this.getNumber().longValue());
		case Short:
			return (T) Short.valueOf(this.getNumber().shortValue());
		case String:
			return (T) this.value.toString();
		default:
			break;
		}
		return null;
	}
}