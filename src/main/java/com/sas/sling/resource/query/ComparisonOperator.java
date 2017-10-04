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
package com.sas.sling.resource.query;

import java.util.Optional;

/**
 * Provides an Enumeration of all of the types of comparisons that exist.
 * Additionally provides a default mapping of String values that match these
 * types
 */
public enum ComparisonOperator {
	EQUAL, NOT_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, LIKE, CONTAINS, CONTAINS_NOT, CONTAINS_ANY, IN, NOT_IN;

	
	/**
	 * Finds enum which represents the provided String
	 * 
	 * @param operation as a string
	 * @return Optional Enum which represents the comparison.
	 */
	public static Optional<ComparisonOperator> getSimpleOperator(String operation) {
		switch (operation) {
		case "==":
		case " is":
			return Optional.of(EQUAL);
		case "!=":
		case " is not":
			return Optional.of(NOT_EQUAL);
		case ">":
		case " greater than":
			return Optional.of(GREATER_THAN);
		case ">=":
			return Optional.of(GREATER_THAN_OR_EQUAL);
		case "<":
		case " less than":
			return Optional.of(LESS_THAN);
		case "<=":
			return Optional.of(LESS_THAN_OR_EQUAL);
		case " like":
			return Optional.of(LIKE);
		case " contains":
			return Optional.of(CONTAINS);
		case " contains not":
			return Optional.of(CONTAINS_NOT);
		case " contains any":
			return Optional.of(CONTAINS_ANY);
		case " in":
			return Optional.of(IN);
		case " not in":
			return Optional.of(NOT_IN);
		}
		return Optional.empty();
	}
}