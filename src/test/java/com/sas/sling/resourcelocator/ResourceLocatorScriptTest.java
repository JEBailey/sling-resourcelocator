package com.sas.sling.resourcelocator;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.sas.sling.resource.query.RqlQueryHandler;

public class ResourceLocatorScriptTest {

	@Rule
	public final SlingContext context = new SlingContext();
	
	private Date midPoint;
	
	private static String DATE_STRING = "Thu Aug 07 2013 16:32:59 GMT+0200";
	
	private static String DATE_FORMAT = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z";

	@Before
	public void setUp() throws ParseException {
		context.load().json("/data.json", "/content/sample/en");
		midPoint = new SimpleDateFormat(DATE_FORMAT).parse(DATE_STRING);
	}

	@Test
	public void testObtainResourceFromContext() {
		Resource resource = context.resourceResolver().getResource("/content/sample/en");
		assertEquals("en", resource.getName());
	}


	@Test
	public void testMatchingNameScript() {
		Resource resource = context.resourceResolver().getResource("/content/sample/en");
		List<Resource> found = RqlQueryHandler.parseRqlQuery(resource, "name() == 'testpage1'");
		assertEquals(1, found.size());
	}
	
	@Test
	public void testBeforeMidDateScript() {
		Resource resource = context.resourceResolver().getResource("/content/sample/en");
		String query = String.format(" jcr:content/created < '%s' ", DATE_STRING);
		List<Resource> found = RqlQueryHandler.parseRqlQuery(resource, query);
		assertEquals(5, found.size());
	}
	
}