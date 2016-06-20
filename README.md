# Resource Locator Utility
Apache Sling Resource Locator Utility

Fluent interface for filtering of a resource tree.

Example of a traversal

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

