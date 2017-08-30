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
package com.sas.sling.resourcelocator;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.sas.sling.resource.ResourceLocator;
import com.sas.sling.resource.parser.ParseException;
import com.sas.sling.resource.query.ScriptHandler;

public class ResourceLocatorScriptTest {

	@Rule
	public final SlingContext context = new SlingContext();
	
	private static String START_PATH = "/content/sample/en";
	private Date midPoint;
	
	private static String DATE_STRING = "Thu Aug 07 2013 16:32:59 GMT+0200";
	private static String NEW_DATE = "2013-08-08T16:32:59.000+02:00";
	private static String DATE_FORMAT = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z";

	@Before
	public void setUp() throws ParseException, java.text.ParseException {
		context.load().json("/data.json", "/content/sample/en");
		midPoint = new SimpleDateFormat(DATE_FORMAT).parse(DATE_STRING);
	}

	@Test 
	public void testNameFunctionIs() throws ParseException {
		String query = "name() == 'testpage1'";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(1, found.size());
	}
	
	@Test
	public void testPropertyIs() throws ParseException {
		String query = "[jcr:content/jcr:title] == 'English'";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(4, found.size());
	}
	
	@Test
	public void testDateBeforeValue() throws ParseException {
		String query = "[jcr:content/created] < '2013-08-08T16:32:59.000+02:00'";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(3, found.size());
		found = handle2(START_PATH, query);
		assertEquals(3, found.size());
	}
	
	@Test
	public void testDateAndProperty() throws ParseException {
		String query = "[jcr:content/created] < '2013-08-08T16:32:59.000+02:00' and [jcr:content/jcr:title] == 'English'";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(3, found.size());
	}
	
	@Test
	public void testDateOrProperty() throws ParseException {
		String query = "[jcr:content/created] < '2013-08-08T16:32:59.000+02:00' or [jcr:content/jcr:title] == 'Mongolian'";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(4, found.size());
	}
	
	@Test
	public void testNullProperty() throws ParseException {
		String query = "[jcr:content/foo] == null ";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(20, found.size());
	}
	
	@Test
	public void testNotNullProperty() throws ParseException {
		String query = "[layout] != null ";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(5, found.size());
	}
	
	private List<Resource> handle(String path, String filter) throws ParseException {
		Resource resource = context.resourceResolver().getResource(path);
		Predicate<Resource> predicate =  ScriptHandler.parseQuery(filter);
		return ResourceLocator.startFrom(resource).stream().filter(predicate).collect(Collectors.toList());
	}
	
	private List<Resource> handle2(String path, String filter) throws ParseException {
		Resource resource = context.resourceResolver().getResource(path);
		return ResourceLocator.startFrom(resource).locateResources(filter);
	}
}
