package com.sas.aem.resourcequery;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;

public class ChildResourcePredicate {

	private final String name;

	private ChildResourcePredicate(String name) {
		this.name = name;
	}

	/**
	 * Static reference that creates access to the ResourcePredicate object to
	 * provide the ability to condition a predicate based on that child's values
	 * 
	 * @param name
	 *            of the expected child resource
	 * @return Object providing helper predicates for a child resource
	 */
	static public ChildResourcePredicate aChildResource(String name) {
		return new ChildResourcePredicate(name);
	}

	/**
	 * Applies a series of predicates against the identified child resource.
	 * The returned predicate will always return 'false' for a null child
	 * 
	 * @param predicates
	 * @return
	 */
	public Predicate has(final Predicate... predicates) {
		return new Predicate() {
			@Override
			public boolean accepts(Resource resource) {
				Resource child = resource.getChild(name);
				if (child != null) {
					for (Predicate predicate : predicates) {
						// return false if any of the predicates fail
						if (!predicate.accepts(child)) {
							return false;
						}
					}
					return true;
				}
				return false;
			}
		};
	}

	public static Predicate isResourceType(final String resourceType) {
		return new Predicate() {
			@Override
			public boolean accepts(Resource resource) {
				return ResourceUtil.isA(resource, resourceType);
			}
		};
	}

}
