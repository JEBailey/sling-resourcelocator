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
package com.sas.sling.resource.query.function;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

import org.apache.sling.api.resource.Resource;

import com.sas.sling.resource.parser.node.Node;
import com.sas.sling.resource.parser.node.Visitor;

public class InstantProvider implements ValueProvider {

	@Override
	public Function<Resource, Object> provision(Node node, Visitor<Function<Resource, Object>, Void> visitor) {
		List<Function<Resource, Object>> children = node.visitChildren(visitor, null);
		return resource -> {
			if (children.isEmpty()) {
				return Instant.now();
			}
			String dateString = children.get(0).apply(resource).toString();
			String formatString = null;
			if (children.size() > 1) {
				formatString = children.get(1).apply(resource).toString();
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
