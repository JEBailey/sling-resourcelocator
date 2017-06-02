package com.sas.sling.resourcelocator;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ResourceLocatorTest {
	
	  @Rule
	  public final SlingContext context = new SlingContext();
	  
	  @Before
	  public void setUp(){
		  
	  }
	  
	  @Test
	  public void testSomething() {
	    Resource resource = context.resourceResolver().getResource("/content/sample/en");
	    // further testing
	  }

}
