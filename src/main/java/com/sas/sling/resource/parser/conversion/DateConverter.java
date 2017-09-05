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

import org.apache.jackrabbit.util.ISO8601;

/**
 * A converter for Date
 */
public class DateConverter extends NumberConverter implements Converter {

    private final Date value;

    public DateConverter(final Date val) {
        super(val.getTime());
        this.value = val;
    }

    @Override
    public String toString() {
        return ISO8601.format(adaptTo(Calendar.class));
    }
    
	@SuppressWarnings("unchecked")
	@Override
	public <T> T adaptTo(Class<T> klass) {
		switch (ConversionTypes.valueOf(klass.getSimpleName())){
		case Date:
			return (T) this.value;
		default:
			return super.adaptTo(klass);
		}
	}
}