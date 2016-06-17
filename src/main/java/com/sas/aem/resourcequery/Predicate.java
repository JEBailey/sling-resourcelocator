package com.sas.aem.resourcequery;

import org.apache.sling.api.resource.Resource;

/**
 * Defines a predicate to indicate the acceptance of the resource into
 * the final list
 * 
 * @author J.E. Bailey
 *
 */
public interface Predicate {

	/**
	 * Evaluates value to determine suitability.
	 * 
	 * @param value
	 * @return true if the value is acceptable
	 */
	public boolean accepts(Resource value);

}
