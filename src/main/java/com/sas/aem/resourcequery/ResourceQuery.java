package com.sas.aem.resourcequery;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.apache.sling.api.resource.Resource;

public class ResourceQuery {

	private Resource resource;
	private Callback callback;
	
	private Predicate pathRestriction = e -> { return true; };
	
	Deque<Resource> resourceToCheck = new ArrayDeque<>();

	/**
	 * Resource which is used as the starting point to locate requested
	 * resources. Assumption is that the resources being located are child
	 * resources of the start resources.
	 * 
	 * @param resource
	 * @return new instance of ResourceLocator;
	 */
	public static ResourceQuery startFrom(Resource resource) {
		return new ResourceQuery(resource);
	}

	private ResourceQuery(Resource resource) {
		this.resource = resource;
	}

	/**
	 * When a matching resource is located, pass that resource to the callback
	 * handler. This is used when the handling of the resources needs to be done
	 * as those resources are identified
	 * 
	 * @param callback
	 * @return
	 */
	public ResourceQuery usingCallback(Callback callback) {
		this.callback = callback;
		return this;
	}

	/**
	 * When iterating over the child resources, this is used as a validation
	 * that the child resource potentially its sub-resources should be
	 * considered.
	 * 
	 * This can be used to limit the possible options beneath a specific
	 * resource
	 * 
	 * @param condition
	 * @return this locator
	 */
	public ResourceQuery traversalControl(Predicate condition) {
		this.pathRestriction = condition;
		return this;
	}

	/**
	 * Takes one or more Predicates that are dealt with as a series of 'and'
	 * conditions. Meaning that all predicates at this level must return true
	 * for the resource before it can be considered a valid resource for
	 * consumption.
	 * 
	 * @param conditions
	 * @return
	 */
	public List<Resource> findAllResources(Predicate... conditions) {
		List<Resource> resourcesToReturn = new LinkedList<>();

		addChildrenToList(resource);

		while (!resourceToCheck.isEmpty()) {
			Resource current = resourceToCheck.pop();
			conditionCheck: {
				for (Predicate condition : conditions) {
					if (!condition.accepts(current)) {
						break conditionCheck;
					}
				}
				resourcesToReturn.add(current);
				if (callback != null) {
					callback.handle(current);
				}
			}
			addChildrenToList(current);
		}

		return resourcesToReturn;
	}

	/*
	 * Used to add child resources to the list of items to process
	 */
	private void addChildrenToList(Resource resource) {
		for (Resource child: resource.getChildren()) {
			if (pathRestriction == null || pathRestriction.accepts(child)) {
				resourceToCheck.push(child);
			}
		}
	}

}
