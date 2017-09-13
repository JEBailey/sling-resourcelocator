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
| contains     | Is matching for one or more items in array |
| contains not | is right hand value not in left hand values|


### Values

Values for comparison are obtained through multiple methods

| Method       | Description                               |
| ----------   | ----------------------------------------  |
| Literal      | Single(') or double (") quoted text in the query will be interpreted as a String. Boolean values of *true* and *false* will be translated to a String. |
| Property     | A String between square brackets '[',']'s will be interpreted as a property value and will be retrieved from the Resource using the get method |
| Function     | A string followed by parens containing an optional comma seperated list of values. |

### Types
All types are converted to either a String or a Number. For direct equivalence the comparison is done as a String. For relational comparisons the object will be adapted to a number.

### Dates
Dates are special, there are multiple ways to enter a date.

Inline, as part of the query, a date can be identified as a string that conforms to a standard ISO-8601 date time.

   '2013-08-08T16:32:59.000'
   '2013-08-08T16:32:59'
   '2013-08-08T16:32'

Are all valid date representations that are defaulting to the UTC timezone.

For a ISO8601 date with timezone offset use the date function.

   date('2013-08-08T16:32:59.000+02:00')

If you need a different date format then the date function can accomadate that

   date('2013-08-08','yyyy-MM-dd')

Or you could just add your own custom Function 





