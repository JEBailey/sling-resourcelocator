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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A converter for any object based on toString()
 */
public class ConverterForString implements Converter {

	private final String value;

	public ConverterForString(final String val) {
		this.value = val;
	}

	@Override
	public Number getNumber() {
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException nfe) {
			try {
				return new BigDecimal(value);
			} catch (NumberFormatException nfe2) {
				try {
					return LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME).toInstant(ZoneOffset.UTC)
							.toEpochMilli();
				} catch (DateTimeParseException dtpe) {
					// swallow
					return null;
				}
			}
		}
	}

	@Override
	public String getString() {
		return value;
	}
}