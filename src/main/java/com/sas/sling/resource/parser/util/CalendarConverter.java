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

import org.apache.jackrabbit.util.ISO8601;

/**
 * A converter for Calendar
 */
public class CalendarConverter extends NumberConverter implements Converter {

    private final Calendar value;

    public CalendarConverter(final Calendar val) {
        super(val.getTimeInMillis());
        this.value = val;
    }

    @Override
    public String toString() {
        return ISO8601.format(this.value);
    }
    
	@SuppressWarnings("unchecked")
	@Override
	public <T> T adaptTo(Class<T> klass) {
		switch (ConverstionTypes.valueOf(klass.getSimpleName())){
		case Calendar:
	        return (T) this.value;
		case Date:
			return (T) this.value.getTime();
		default:
			return super.adaptTo(klass);
		}
	}
	
}