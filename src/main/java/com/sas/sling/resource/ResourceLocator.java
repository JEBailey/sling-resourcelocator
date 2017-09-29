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
package com.sas.sling.resource;

import java.io.ByteArrayInputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.sling.api.resource.Resource;

import com.sas.sling.resource.parser.ParseException;
import com.sas.sling.resource.parser.Parser;
import com.sas.sling.resource.parser.node.Node;
import com.sas.sling.resource.query.LogicVisitor;

/**
 * Base class from which a fluent api can be created or which can be defined
 * using the integrated query language.
 * 
 * Additionally provides the ability to stream child resources.
 *
 */
public class ResourceLocator {

	// starting resource
	private Resource resource;

	private Optional<Consumer<Resource>> callback = Optional.empty();

	private Optional<Predicate<Resource>> traversalControl = Optional.empty();

	private long limit = Long.MAX_VALUE;

	private long startOfRange;
	
	private LogicVisitor logicVisitor = null;

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

	/*
	 * Constructor to establish the starting resource
	 * 
	 * @param resource
	 */
	private ResourceLocator(Resource resource) {
		Objects.nonNull(resource);
		this.resource = resource;
	}

	/**
	 * When a matching resource is located, pass that resource to the callback
	 * handler. This is used when the handling of the resources needs to be done as
	 * those resources are identified. This replaces the default Consumer that
	 * appends the resource to the internal list returned will be returned.
	 * 
	 * This is equivalent of a .forEach in the stream api
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
	 * When iterating over the child resources, this is used as a validation that a
	 * specific child resource should be traversed
	 * 
	 * This can be used to limit the possible branching options beneath a resource
	 * tree
	 * 
	 * As the Stream API provides an inherent depth first Resource stream this
	 * provides the ability to limit the children which are acceptable.
	 * 
	 * 
	 * @param condition
	 *            Add child resource if 'true'
	 * @return this locator
	 */
	public ResourceLocator traversalControl(Predicate<Resource> condition) {
		this.traversalControl = Optional.ofNullable(condition);
		return this;
	}

	/**
	 * When iterating over the child resources, this is used as a validation that a
	 * specific child resource should be traversed
	 * 
	 * This can be used to limit the possible branching options beneath a resource
	 * tree
	 * 
	 * As the Stream API provides an inherent depth first Resource stream this
	 * provides the ability to limit the children which are acceptable.
	 * 
	 * 
	 * @param condition
	 *            Add child resource to the traversal path if condition is 'true'
	 * @return this locator
	 * @throws ParseException
	 */
	public ResourceLocator traversalControl(String condition) throws ParseException {
		this.traversalControl = Optional.of(parse(condition));
		return this;
	}

	/**
	 * Rests the starting path for the query to be the This can be used to limit the
	 * possible branching options beneath a resource tree
	 * 
	 * As the Stream API provides an inherent depth first Resource stream this
	 * provides the ability to limit the children which are acceptable.
	 * 
	 * 
	 * @param path
	 *            resets resource to path
	 * @return this locator
	 */
	public ResourceLocator startingPath(String path) {
		this.resource = resource.getResourceResolver().getResource(path);
		return this;
	}

	/**
	 * Sets the maximum number of items to be returned or processed. Starting from
	 * the first matching resource. This method is mutually exclusive to the range
	 * method
	 * 
	 * This performs the same form of limitation as a limit on a Stream
	 * 
	 * @param number
	 *            maximum number of items to be returned
	 * @return this locator
	 */
	public ResourceLocator limit(long number) {
		if (number < 0) {
			throw new IllegalArgumentException("value may not be negative");
		}
		this.startOfRange = 0;
		this.limit = number;
		return this;
	}

	/**
	 * Sets the maximum number of items to be returned or processed. Starting from
	 * the nth identified resource as set by the startOfRange. This method is
	 * mutually exclusive to the limit method
	 * 
	 * This can be achieved on a Stream by performing a a filter operation
	 * 
	 * @param startOfRange
	 * @param limit
	 * @return
	 */
	public ResourceLocator range(long startOfRange, long limit) {
		if (startOfRange < 0 || limit < 0) {
			throw new IllegalArgumentException("value may not be negative");
		}
		this.startOfRange = startOfRange;
		this.limit = limit;
		return this;
	}

	/**
	 * Recursively descends through the available resources and locates resources
	 * that match the provided predicate. Additional restrictions can be set to
	 * limit the paths that the traversal takes, and how the located resources are
	 * handled.
	 * 
	 * @param condition
	 *            predicate to be used against all matching child resources
	 * @return List of matching resource or empty list if callback is enabled
	 */
	public List<Resource> locateResources(Predicate<Resource> condition) {
		List<Resource> resourcesToReturn = new LinkedList<>();
		Deque<Resource> resourcesToCheck = new ArrayDeque<>();

		resourcesToCheck.add(resource);

		long count = 0;
		long max = startOfRange + limit;

		if (max < 0) {
			max = Long.MAX_VALUE;
		}

		while (!resourcesToCheck.isEmpty()) {
			Resource current = resourcesToCheck.pop();
			if (condition.test(current)) {
				++count;
				if (count > startOfRange) {
					callback.orElse(e -> resourcesToReturn.add(e)).accept(current);
				}
			}
			Iterator<Resource> childs = current.listChildren();
			childs.forEachRemaining(child -> {
				if (traversalControl.orElse(e -> true).test(child)) {
					resourcesToCheck.push(child);
				}
			});
			if (count > max) {
				break;
			}
		}
		return resourcesToReturn;
	}

	/**
	 * Recursively descends through the available resources and locates resources
	 * that match the provided filter lan. Additional restrictions can be set to
	 * limit the paths that the traversal takes, and how the located resources are
	 * handled.
	 * 
	 * @param condition
	 *            predicate to be used against all matching child resources
	 * @return List of matching resource or empty list if callback is enabled
	 * @throws ParseException
	 */
	public List<Resource> locateResources(String condition) throws ParseException {
		Predicate<Resource> predicate = parse(condition);
		return locateResources(predicate);
	}

	/**
	 * Provides a stream of resources starting from the initiator resource and
	 * traversing through it's descendants The only fluent api check it performs is
	 * of the traversal predicate.
	 * 
	 * @return self closing {@code Stream<Resource>} of unknown size.
	 */
	public Stream<Resource> stream() {
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<Resource>() {

			LinkedList<Resource> resourcesToCheck = new LinkedList<>();
			LinkedList<Resource> approvedChildren = new LinkedList<>();

			{
				resourcesToCheck.addFirst(resource);
			}

			@Override
			public boolean hasNext() {
				return !resourcesToCheck.isEmpty();
			}

			@Override
			public Resource next() {
				Resource current = resourcesToCheck.removeFirst();
				
				approvedChildren.clear();
				current.listChildren().forEachRemaining(child -> {
					if (traversalControl.orElse(e -> true).test(child)) {
						approvedChildren.push(child);
					}
				});
				resourcesToCheck.addAll(0, approvedChildren);
				return current;
			}
		}, Spliterator.ORDERED | Spliterator.IMMUTABLE), false);
	}

	/**
	 * Provides a stream of resources starting from the initiator resource and
	 * traversing through it's descendants The only fluent api check it performs is
	 * of the traversal predicate.
	 * 
	 * @return self closing {@code Stream<Resource>} of unknown size.
	 */
	public Stream<Resource> stream(Predicate<Resource> traversalConstraint) {
		this.traversalControl = Optional.of(traversalConstraint);
		return stream();
	}
	
	public Predicate<Resource> parse(String filter) throws ParseException{
		Node rootNode = new Parser(new ByteArrayInputStream(filter.getBytes())).Input();
		return rootNode.accept(getVisitor(),null);
	}
	
	private LogicVisitor getVisitor() {
		if (logicVisitor == null) {
			logicVisitor = new LogicVisitor();
		}
		return logicVisitor;
	}

}
