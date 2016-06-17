package com.sas.aem.resourcequery;

import org.apache.sling.api.resource.Resource;

/**
 * Collection of terms to assist in building a readable search function. These
 * terms may not have functionality other than providing context
 * 
 * @author JE Bailey
 *
 */
public class Condition {

	/**
	 * Syntactic sugar to provide context for the builder
	 * 
	 * @param predicate
	 * @return the predicate that was passed in
	 */
	public static Predicate where(Predicate predicate) {
		return predicate;
	}

	/**
	 * Syntactic sugar to provide context for the builder
	 * 
	 * @param predicate
	 * @return the predicate that was passed in
	 */
	public static Predicate and(Predicate predicate) {
		return predicate;
	}

	/**
	 * Syntactic sugar to provide context for the builder
	 * 
	 * @param predicate
	 * @return the predicate that was passed in
	 */
	public static Predicate when(Predicate predicate) {
		return predicate;
	}

	/**
	 * 'or' equivalent, only evaluates the first predicate if the second one
	 * fails
	 * 
	 * @param predicate
	 * @return the predicate that was passed in
	 */
	public static Predicate either(final Predicate firstPredicate,
			final Predicate secondPredicate) {
		return new Predicate() {

			@Override
			public boolean accepts(Resource value) {
				if (!firstPredicate.accepts(value)) {
					return secondPredicate.accepts(value);
				}
				return true;
			}
		};
	}

}
