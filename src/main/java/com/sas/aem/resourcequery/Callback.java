package com.sas.aem.resourcequery;

import org.apache.sling.api.resource.Resource;

/**
 * Functional callback to provide a handler for the matched resource
 * 
 * @author J.E. Bailey
 *
 */
public interface Callback {

	/**
	 * Method is called when a matching resource for the query is located.
	 * Currently this occurs in realtime, prior to any sorting or ranges being
	 * applied this may not have provide the desired results in those use cases
	 * 
	 * @param resouce
	 */
	void handle(Resource resouce);

}
