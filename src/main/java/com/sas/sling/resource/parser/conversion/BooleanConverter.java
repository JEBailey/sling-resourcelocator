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

/**
 * Conversion implementer to convert a Boolean to different values.
 */
public class BooleanConverter implements Converter {

    private final Boolean value;

    public BooleanConverter(final Boolean val) {
        this.value = val;
    }

    public Number getNumber() {
        return ( value.booleanValue() ? 1 : 0);
    }

	@Override
	public String getString() {
		return value.toString();
	}
    
    

}