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
package com.sas.sling.resource.parser.util;

import java.util.Calendar;
import java.util.Date;

public class ConverterImpl {

	@SuppressWarnings("unchecked")
	public static <T> T adapt(final Object initialValue, final Class<T> type) {
		if (type.isInstance(initialValue)) {
			return (T) initialValue;
		}

		return (T) getConverter(initialValue).adaptTo(type);
	}

	/**
	 * Create a converter for an object.
	 * 
	 * @param value
	 *            The object to convert
	 * @return A converter for {@code value}
	 */
	private static Converter getConverter(final Object value) {
		if (value instanceof Number) {
			return new NumberConverter((Number) value);
		} else if (value instanceof Boolean) {
			return new BooleanConverter((Boolean) value);
		} else if (value instanceof Date) {
			return new DateConverter((Date) value);
		} else if (value instanceof Calendar) {
			return new CalendarConverter((Calendar) value);
		}
		// default string based
		return new StringConverter(value);
	}
}
