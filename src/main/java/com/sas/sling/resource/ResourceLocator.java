package com.sas.sling.resource;

/*
 * Copyright 2016 Jason E Bailey
 *
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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.apache.sling.api.resource.Resource;

/**
 * Base class from which the fluent api is constructed to 
 * locate resources which we are interested in.
 * 
 * @author JE Bailey
 *
 */
public class ResourceLocator {

	// starting resource
	private Resource resource;

	private Optional<Consumer<Resource>> callback = Optional.empty();

	private Optional<Predicate<Resource>> traversalControl = Optional.empty();

	/**
	 * Starting point to locate resources. resources of the start resource.
	 * 
	 * @param resource
	 *            starting point for the traversal
	 * @return new instance of ResourceQuery;
	 */
	public static ResourceLocator startFrom(Resource resource) {
		return new ResourceLocator(resource);
	}

	/**
	 * 
	 * @param resource
	 */
	private ResourceLocator(Resource resource) {
		this.resource = resource;
	}

	/**
	 * When a matching resource is located, pass that resource to the callback
	 * handler. This is used when the handling of the resources needs to be done
	 * as those resources are identified. This replaces the default function of
	 * returning the located items as a List. Do not use if ordering or a range
	 * is set
	 * 
	 * @param callback
	 *            Consumer that processes the located resource
	 * @return Resource locator instance
	 */
	public ResourceLocator usingCallback(Consumer<Resource> callback) {
		this.callback = Optional.ofNullable(callback);
		return this;
	}

	/**
	 * When iterating over the child resources, this is used as a validation
	 * that a specific child resource should be traversed
	 * 
	 * This can be used to limit the possible options beneath a specific
	 * resource
	 * 
	 * @param condition used to approve child resource for traversal
	 * @return this locator
	 */
	public ResourceLocator traversalControl(Predicate<Resource> condition) {
		this.traversalControl = Optional.ofNullable(condition);
		return this;
	}

	/**
	 * Recursively descends through the available resources and locates
	 * resources that match the provided predicate. Additional restrictions can
	 * be set to limit the paths that the traversal takes, and how the located
	 * resources are handled.
	 * 
	 * @param condition
	 *            predicate to be used against all matching child resources
	 * @return List of matching resource or empty list if callback is enabled
	 */
	public List<Resource> locateResources(Predicate<Resource> condition) {
		List<Resource> resourcesToReturn = new LinkedList<>();
		Deque<Resource> resourcesToCheck = new ArrayDeque<>();
		
		resourcesToCheck.add(resource);
		

		while (!resourcesToCheck.isEmpty()) {
			Resource current = resourcesToCheck.pop();
			if (condition.test(current)) {
				callback.orElse(e -> resourcesToReturn.add(e)).accept(current);
			}
			resource.listChildren().forEachRemaining(child -> {
				if (traversalControl.orElse(e -> true).test(child)) {
					resourcesToCheck.push(child);
				}
			});
		}
		return resourcesToReturn;
	}

}
