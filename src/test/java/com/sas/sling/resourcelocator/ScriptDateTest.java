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

import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.junit.SlingContext;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.sas.sling.resource.ResourceLocator;
import com.sas.sling.resource.parser.ParseException;

public class ScriptDateTest {

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
	public void testPropLessThanDateFunction() throws ParseException {
		String query = "[jcr:content/created] < date('2013-08-08T16:32:59.000+02:00')";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(3, found.size());
	}
	
	@Test
	public void testPropLessThanDateString() throws ParseException {
		String query = "[jcr:content/created] < '2013-08-08T16:32:59.000'";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(3, found.size());
	}
	
	@Test
	public void testPropLessThanShortDateString() throws ParseException {
		String query = "[jcr:content/created] < '2013-08-08T16:32'";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(3, found.size());
	}
	
	@Test
	public void testPropLessThatCustomDateFunction() throws ParseException {
		String query = "[jcr:content/created] < date('2013-08-08','yyyy-MM-dd')";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(3, found.size());
	}
	
	@Test
	public void testPropLessThanShortDateStringAlpha() throws ParseException {
		String query = "[jcr:content/created] less than '2013-08-07T14:32:59'";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(2, found.size());
	}
	
	
	@Test  //Thu Aug 07 2013 16:32:59
	public void testPropLessThanEqualShortDateString() throws ParseException {
		String query = "[jcr:content/created] <= '2013-08-07T14:32:59'";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(3, found.size());
	}
	
	@Test  //Thu Aug 07 2013 16:32:59 - sans seconds
	public void testPropLessThanEqualShortDateString2() throws ParseException {
		String query = "[jcr:content/created] <= '2013-08-07T14:32'";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(2, found.size());
	}
	
	@Test  //Thu Aug 07 2013 16:32:59 //milliseconds
	public void testPropLessThanEqualShortDateString3() throws ParseException {
		String query = "[jcr:content/created] < '2013-08-07T14:32:59.010'";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(3, found.size());
	}
	
	@Test
	public void testPropGreaterThanShortDateString() throws ParseException {
		String query = "[jcr:content/created] > '2013-08-07T14:32'";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(3, found.size());
	}
	
	@Test
	public void testPropGreaterThanShortDateStringAlpha() throws ParseException {
		String query = "[jcr:content/created] greater than '2013-08-07T14:32:59'";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(2, found.size());
	}
	
	@Test
	public void testPropGreaterThanEqualDateString() throws ParseException {
		String query = "[jcr:content/created] >= '2013-08-07T14:32:59'";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(3, found.size());
	}
	
	@Test
	public void testPropisLikeDateRegex() throws ParseException {
		String query = "[jcr:content/created] like '2013-08-07.*'";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(1, found.size());
	}
	
	@Test
	public void testPropisLikeDateRegex2() throws ParseException {
		String query = "[jcr:content/created] like '201[2-5].*'";
		List<Resource> found = handle(START_PATH, query);
		assertEquals(4, found.size());
	}
	
	
	private List<Resource> handle(String path, String filter) throws ParseException {
		Resource resource = context.resourceResolver().getResource(path);
		return ResourceLocator.startFrom(resource).locateResources(filter);
	}
}
