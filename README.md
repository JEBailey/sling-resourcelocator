# Resource Locator Utility
Resource Locator Utility for Apache Sling 

Fluent interface for filtering of a resource tree.

Example of a traversal using a callback to print out located 

```java
ResourceLocator
		.startFrom(resource)
		.usingCallback(e -> out.println(e.getPath()))
		.traversalControl(
				where(property("jcr:primaryType").is("cq:Page")))
		.locateResources(
				where(aChildResource("jcr:content")
						.has(property("sling:resourceType")
								.isNot("sas/components/page/folder"))));
```

The ResourceLocator package encompasses two distinct pieces of functionality. 

1. The ResourceLocator provides a fluent interface to perform a recursive search of resource tree with the ability to set specific constraints and predicates.
2. A series of predicates have been defined around the Resource object(s) to assist in filtering out unwanted resources. These predicates are independent of the ResourceLocator

Example of no callback, which produces a list that can then be Streamed across

```java
List<Resource> resources = ResourceLocator
		.startFrom(resource)
		.traversalControl(
				where(property("jcr:primaryType").is("cq:Page")))
		.locateResources(
				where(aChildResource("jcr:content")
						.has(property("sling:resourceType")
								.isNot("sas/components/page/folder"))));
``` 

