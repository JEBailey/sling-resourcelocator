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

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * A converter for Date
 */
public class DateConverter extends NumberConverter implements Converter {


    public DateConverter(final Date date) {
        super(date.getTime());
    }

    protected DateConverter(long timeInMillis) {
    	super(timeInMillis);
	}

	@Override
    public String getString() {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(Instant.ofEpochMilli(value.longValue()));
    }
}
