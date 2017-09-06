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

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * A converter for any object based on toString()
 */
public class StringConverter implements Converter {

	private final String value;
	
	private static Pattern DoubleString = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");
	private static Pattern LongString = Pattern.compile("^-?\\d{1,19}$");
	

	public StringConverter(final String val) {
		this.value = val;
	}

	@Override
	public Number getNumber() {
		if (LongString.matcher(value).matches()){
			return Long.parseLong(value);
		}
		if (DoubleString.matcher(value).matches()){
			return Double.parseDouble(value);
		}
		try {
			return LocalDateTime.parse(value,DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant(ZoneOffset.UTC).toEpochMilli();
		} catch (DateTimeParseException dtpe){
			//swallow
			return null;
		}
	}

	@Override
	public String getString() {
		return value;
	}
}