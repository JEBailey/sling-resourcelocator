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

import org.apache.jackrabbit.util.ISO8601;

/**
 * A converter for any object based on toString()
 */
public class StringConverter implements Converter {

	private final String value;

	public StringConverter(final String val) {
		this.value = val;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T adaptTo(Class<T> klass) {
		switch (ConverstionTypes.valueOf(klass.getSimpleName())) {
		case BigDecimal:
			return (T) new BigDecimal(value);
		case Boolean:
			return (T) Boolean.valueOf(value);
		case Byte:
			return (T) Byte.valueOf(Byte.parseByte(value));
		case GregorianCalendar:
			final Calendar c = ISO8601.parse(value);
			if (c == null) {
				throw new IllegalArgumentException("Not a date string: " + value);
			}
			return (T) c;
		case Date:
			final Calendar cal = ISO8601.parse(value);
			return (T) cal.getTime();
		case Double:
			return (T) Double.valueOf(Double.parseDouble(value));
		case Float:
			return (T) Float.valueOf(Float.parseFloat(value));
		case Integer:
			return (T) Integer.valueOf(Integer.parseInt(value));
		case Long:
			return (T) Long.valueOf(Long.parseLong(value));
		case Short:
			return (T) Short.valueOf(Short.parseShort(value));
		case String:
			return (T) value;
		default:
			break;
		}
		return null;
	}
}