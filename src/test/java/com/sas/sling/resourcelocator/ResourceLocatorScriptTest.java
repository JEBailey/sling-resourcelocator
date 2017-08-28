package com.sas.sling.resourcelocator;
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
import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Ignore;
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

	@Test @Ignore
	public void testObtainResourceFromContext() {
		Resource resource = context.resourceResolver().getResource("/content/sample/en");
		assertEquals("en", resource.getName());
	}


	@Test
	public void testMatchingNameScript() {
		Resource resource = context.resourceResolver().getResource("/content/sample/en");
		List<Resource> found = RqlQueryHandler.parseRqlQuery(resource, "name() == \"testpage1\"");
		assertEquals(1, found.size());
	}
	
	@Test @Ignore
	public void testBeforeMidDateScript() {
		Resource resource = context.resourceResolver().getResource("/content/sample/en");
		String query = String.format(" jcr:content/created < '%s' ", DATE_STRING);
		List<Resource> found = RqlQueryHandler.parseRqlQuery(resource, query);
		assertEquals(5, found.size());
	}
	
}
