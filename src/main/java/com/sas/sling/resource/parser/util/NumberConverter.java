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

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * A converter for Number
 */
public class NumberConverter implements Converter {

    private final Number value;

    public NumberConverter(final Number val) {
        this.value = val;
    }

	@SuppressWarnings("unchecked")
	@Override
	public <T> T adaptTo(Class<T> klass) {
		switch (ConverstionTypes.valueOf(klass.getSimpleName())){
		case BigDecimal:
			if ( this.value instanceof BigDecimal ) {
	            return (T)this.value;
	        }
	        return (T)new BigDecimal(this.value.toString());
		case Boolean:
			return (T) Boolean.FALSE;
		case Byte:
			 return (T) Byte.valueOf(this.value.byteValue());
		case Calendar:
	        final Calendar cal = Calendar.getInstance();
	        cal.setTimeInMillis(this.value.longValue());
	        return (T) cal;
		case Date:
			return (T) new Date(this.value.longValue());
		case Double:
			return (T) Double.valueOf(this.value.doubleValue());
		case Float:
			return (T) magic(this.value.floatValue());
		case Integer:
			return (T) Integer.valueOf(this.value.intValue());
		case Long:
			return (T) Long.valueOf(this.value.longValue());
		case Short:
			 return (T) Short.valueOf(this.value.shortValue());
		case String:
			return (T)this.value.toString();
		default:
			break;
		}
		return null;
	}
	
	private <T> T magic(T object){
		return object;
	}
	
	
}