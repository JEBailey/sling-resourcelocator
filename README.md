# Resource Locator Utility

* Resource Locator Utility for Apache Sling 
* Fluent interface for filtering of a resource tree.
* Predefined predicates for Resources and Properties
* Filter language to easily define predicates

Example of a traversal.

```java
ResourceLocator
	.startFrom(resource)
	.traversalControl(
		where(property("jcr:primaryType").is("cq:Page")))
	.locateResources(
		where(aChildResource("jcr:content")
			.has(property("sling:resourceType")
			.isNot("sas/components/page/folder"))));
```

same results using the filter language

```java
ResourceLocator
    .startFrom(resource)
    .traversalControl("[jcr:primaryType] == 'cq:Page'")
    .locateResources("[jcr:content/sling:resourceType] != 'sas/components/page/folder'");
```


The ResourceLocator provides 

1. The ResourceLocator which provides a fluent interface to perform a recursive traversal of a given resource tree with the ability to set specific constraints and predicates.
2. A series of predicates have been defined around the Resource object(s) to assist in filtering out unwanted resources. These predicates are independent of the ResourceLocator
3. A domain specific language to create a predicate which filters the streamed resources.

## Filter Language
Derivative of JCR-SQL2.

### Operators

| Name         | Description                                |
| ---------    | --------------------------------           |
| and          | Logical AND                                |
| or           | Logical OR                                 |
| ==           | Equal operator for Strings                 |
| <            | Less than operator for Numbers             |
| <=           | Less than or equal operator for Numbers    |
| >            | Greater than operator for Numbers          |
| >=           | Greater than or equal operator for Numbers |
| !=           | Is not equal to for Strings                |
| less than    | less than operator for Numbers             |
| greater than | greater than operator for Numbers          |
| is           | Equal operator for Strings                 |
| is not       | Is not equal operator for Strings          |
| like         | Regex match against String                 |

### Types
All types are pared down to either a String or a Number. For direct equivalence the comparison is done as a String. For relational comparisons the object will be adapted to a number.



