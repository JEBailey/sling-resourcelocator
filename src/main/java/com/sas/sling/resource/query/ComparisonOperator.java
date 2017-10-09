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
	EQUAL, NOT_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, LIKE, LIKE_NOT, CONTAINS, CONTAINS_NOT, CONTAINS_ANY, CONTAINS_NOT_ANY, IN, NOT_IN;

	
	/**
	 * Finds enum which represents the provided String
	 * 
	 * @param operation as a string
	 * @return Optional Enum which represents the comparison.
	 */
	public static Optional<ComparisonOperator> getSimpleOperator(String operation) {
		ComparisonOperator op = null;
		switch (operation) {
		case "==":
		case " is":
			op = EQUAL;
		case "!=":
		case " is not":
			op = NOT_EQUAL;
		case ">":
		case " greater than":
			op = GREATER_THAN;
		case ">=":
			op = GREATER_THAN_OR_EQUAL;
		case "<":
		case " less than":
			op = LESS_THAN;
		case "<=":
			op = LESS_THAN_OR_EQUAL;
		case " like":
			op = LIKE;
		case " like not":
		case " not like":
			op = LIKE_NOT;
		case " contains":
			op = CONTAINS;
		case " contains not":
			op = CONTAINS_NOT;
		case " contains any":
			op = CONTAINS_ANY;
		case " contains not any":
			op = CONTAINS_NOT_ANY;
		case " in":
			op = IN;
		case " not in":
			op = NOT_IN;
		}
		return Optional.ofNullable(op);
	}
}