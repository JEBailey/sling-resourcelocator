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

import java.util.Calendar;
import java.util.Date;

public class ConversionHandler {

	@SuppressWarnings("unchecked")
	public static <T> T adapt(final Object initialValue, final Class<T> type) {
		if (type.isInstance(initialValue)) {
			return (T) initialValue;
		}

		Converter converter = null;
		if (initialValue instanceof Number) {
			converter = new NumberConverter((Number) initialValue);
		} else if (initialValue instanceof Boolean) {
			converter =  new BooleanConverter((Boolean) initialValue);
		} else if (initialValue instanceof Date) {
			converter = new DateConverter((Date) initialValue);
		} else if (initialValue instanceof Calendar) {
			converter = new CalendarConverter((Calendar) initialValue);
		}
		// default string based
		converter =  new StringConverter(initialValue.toString());
		
		if (type.isInstance(String.class)){
			return (T) converter.getString();
		}
		
		return (T) converter.getNumber();
	}


}
