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
package com.sas.sling.resource.parser.provider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

import org.apache.sling.api.resource.Resource;

public class InstantProvider implements ValueProvider {

	@Override
	public Function<Resource, Object> provision(List<Function<Resource, Object>> arguments) {

		return resource -> {
			if (arguments.isEmpty()) {
				return Instant.now();
			}
			String dateString = arguments.get(0).apply(resource).toString();
			String formatString = null;
			if (arguments.size() > 1) {
				formatString = arguments.get(1).apply(resource).toString();
				SimpleDateFormat dateFormat = new SimpleDateFormat(formatString);
				try {
					return Instant.ofEpochMilli(dateFormat.parse(dateString).getTime());
				} catch (ParseException e) {
					return null;
				}
			} else {
				return DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(dateString, OffsetDateTime::from).toInstant();
			}
		};
	}

}
