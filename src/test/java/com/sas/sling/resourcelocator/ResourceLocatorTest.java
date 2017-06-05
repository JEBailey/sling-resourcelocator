package com.sas.sling.resourcelocator;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.sas.sling.resource.ResourceLocator;

public class ResourceLocatorTest {
	
	  @Rule
	  public final SlingContext context = new SlingContext();
	  
	  @Before
	  public void setUp(){
		  context.load().json("/data.json", "/content/sample/en");
	  }
	  
	  @Test
	  public void testSomething() {
	    Resource resource = context.resourceResolver().getResource("/content/sample/en");
	    assertEquals("en",resource.getName());
	  }

	  @Test
	  public void testSomethingelse() {
	    Resource resource = context.resourceResolver().getResource("/content/sample/en");
	    List<Resource> found = ResourceLocator.startFrom(resource).locateResources(item -> {
	    	return item.getName().equals("testpage1");
	    });
	    assertEquals(1, found.size());
	  }
}
