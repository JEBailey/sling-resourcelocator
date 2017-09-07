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

public class ConversionHandler {

	@SuppressWarnings("unchecked")
	public static <T> T adapt(final Object initialValue, final Class<T> type) {
		if (initialValue == null) {
			return null;
		}
		if (type.isInstance(initialValue)) {
			return (T) initialValue;
		}
		if (Null.class == type){
			return (T) new Null();
		}

		Converter converter = null;
		if (initialValue instanceof Number) {
			converter = new NumberConverter((Number) initialValue);
		} else {
			converter =  new StringConverter(initialValue.toString());
		}
		
		if (type.isInstance(String.class)){
			return (T) converter.getString();
		}
		
		return (T) converter.getNumber();
	}


}
