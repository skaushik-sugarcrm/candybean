/**
 * Candybean is a next generation automation and testing framework suite.
 * It is a collection of components that foster test automation, execution
 * configuration, data abstraction, results illustration, tag-based execution,
 * top-down and bottom-up batches, mobile variants, test translation across
 * languages, plain-language testing, and web service testing.
 * Copyright (C) 2013 SugarCRM, Inc. <candybean@sugarcrm.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sugarcrm.voodoo.automation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.sugarcrm.voodoo.automation.VInterface.Type;
import com.sugarcrm.voodoo.automation.control.VControl;
import com.sugarcrm.voodoo.automation.control.VHook.Strategy;
import com.sugarcrm.voodoo.configuration.Configuration;

public class VInterfaceSystemTest {

	protected static Voodoo voodoo;
	protected static VInterface iface;
	protected static final String curWorkDir = System.getProperty("user.dir");
	protected static final String relPropsPath = curWorkDir + File.separator + "src"
			+ File.separator + "test" + File.separator + "resources";
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void first() throws Exception {
		String voodooPropsPath = relPropsPath + File.separator;
		String voodooPropsFilename = System.getProperty("voodoo_prop_filename");
		if (voodooPropsFilename == null)
			voodooPropsFilename = "voodoo-mac.properties";
		voodooPropsPath += voodooPropsFilename;
		Configuration voodooConfig = new Configuration();
		voodooConfig.load(voodooPropsPath);
		voodoo = Voodoo.getInstance(voodooConfig);
		iface = voodoo.getInterface();
		iface.start();
	}
	
//	@Ignore
	@Test
	public void backwardForwardRefreshTest() throws Exception {
		String url1 = "https://www.google.com/";
		String url2 = "http://espn.go.com/";
		String url3 = "http://www.sugarcrm.com/";
		iface.go(url1);
		iface.go(url2);
		iface.go(url3);
		iface.refresh();
		assertEquals(url3, iface.getURL());
		iface.backward();
		iface.refresh();
		assertEquals(url2, iface.getURL());
		iface.backward();
		iface.refresh();
		assertEquals(url1, iface.getURL());
//		iface.interact("backward at 1");
//		iface.backward();
		iface.forward();
		iface.refresh();
		assertEquals(url2, iface.getURL());
		iface.forward();
		iface.refresh();
		assertEquals(url3, iface.getURL());		
//		iface.interact("forward at 3");
//		iface.forward();
	}
	
//	@Ignore
	@Test
	public void screenshotTest() throws Exception {
		File screenshotFile = new File(relPropsPath + File.separator + "screenshot.png");
		String url = "https://www.google.com/";
		iface.go(url);
		iface.screenshot(screenshotFile);
		assertTrue(screenshotFile.exists());
	}

//	@Ignore
	@Test
	public void startStopRestartTest() throws Exception {
		String expUrl = "https://www.google.com/";
		iface.go(expUrl);
		String actUrl = iface.getURL();
		assertEquals(expUrl, actUrl);
		iface.stop();
		iface.start(Type.FIREFOX);
		iface.go(expUrl);
		actUrl = iface.getURL();
		assertEquals(expUrl, actUrl);
		iface.restart();
		iface.go(expUrl);
		actUrl = iface.getURL();
		assertEquals(expUrl, actUrl);
		iface.stop();
		iface.start(Type.CHROME);
		iface.go(expUrl);
		actUrl = iface.getURL();
		assertEquals(expUrl, actUrl);
		iface.stop();
		try {
			thrown.expect(Exception.class);
			thrown.expectMessage("Automation interface not yet started; cannot restart.");
			iface.restart();
		} finally {
			iface.start();
		}
	}

	@Ignore
	@Test
	public void closeWindowTest() throws Exception {
//		this.iface.closeWindow();
	}

	@Ignore
	@Test
	public void goTest() throws Exception {
//		this.iface.go("");
	}

	@Ignore
	@Test
	public void acceptDialogTest() throws Exception {
//		this.iface.acceptDialog();
	}

	@Ignore
	@Test
	public void dismissDialogTest() throws Exception {
//		this.iface.dismissDialog();
	}

//	@Ignore
	@Test
	public void containsTest() throws Exception {
		iface.go("https://code.google.com/");
		boolean actCaseSensPos = iface.contains("Google Developers", true); //true
		boolean actCaseSensNeg = iface.contains("google developers", true); //false
		boolean actNeg = iface.contains("goggle devs", false); //false
		assertEquals("Expecting: " + true + ", actual: " + actCaseSensPos, true, actCaseSensPos);
		assertEquals("Expecting: " + false + ", actual: " + actCaseSensNeg, false, actCaseSensNeg);
		assertEquals("Expecting: " + false + ", actual: " + actNeg, false, actNeg);
	}
	
	@Ignore
	@Test
	public void focusDefaultTest() throws Exception {
//		this.iface.focusDefault();
	}

//	@Ignore
	@Test
	public void focusFrameTest() throws Exception {
		String expDefStr = "Your Guide To Web Design";
		String expFrmStr = "http://www.littlewebhut.com/images/eightball.gif";
		iface.go("http://www.littlewebhut.com/articles/html_iframe_example/");
		String actDefStr = iface.getControl(Strategy.TAG, "p").getText();
		assertEquals("Expecting: " + expDefStr + ", actual: " + actDefStr, expDefStr, actDefStr);
		iface.focusFrame(1);
//		System.out.println("SOURCE:\n" + iface.wd.getPageSource());
		String actFrmStr = iface.getControl(Strategy.TAG, "img").getAttribute("src");
		assertEquals("Expecting: " + expFrmStr + ", actual: " + actFrmStr, expFrmStr, actFrmStr);
		iface.focusDefault();
		actDefStr = iface.getControl(Strategy.TAG, "p").getText();
		assertEquals("Expecting: " + expDefStr + ", actual: " + actDefStr, expDefStr, actDefStr);
		iface.focusFrame("imgbox");
		actFrmStr = iface.getControl(Strategy.TAG, "img").getAttribute("src");
		assertEquals("Expecting: " + expFrmStr + ", actual: " + actFrmStr, expFrmStr, actFrmStr);
		iface.focusDefault();
		actDefStr = iface.getControl(Strategy.TAG, "p").getText();
		assertEquals("Expecting: " + expDefStr + ", actual: " + actDefStr, expDefStr, actDefStr);
		iface.focusFrame(new VControl(voodoo, iface, Strategy.ID, "imgbox"));
		actFrmStr = iface.getControl(Strategy.TAG, "img").getAttribute("src");
		assertEquals("Expecting: " + expFrmStr + ", actual: " + actFrmStr, expFrmStr, actFrmStr);
		iface.focusDefault();
		actDefStr = iface.getControl(Strategy.TAG, "p").getText();
		assertEquals("Expecting: " + expDefStr + ", actual: " + actDefStr, expDefStr, actDefStr);
	}

//	@Ignore
	@Test
	public void focusWindowTest() throws Exception {
		String expWindow0Title = "HTML Examples";
		String expWindow0URL = "http://www.w3schools.com/html/html_examples.asp";
		String expWindow1Title = "Tryit Editor v1.8";
		String expWindow1URL = "http://www.w3schools.com/html/tryit.asp?filename=tryhtml_intro";
		String expWindow2Title = "HTML Popup Windows - HTML Code Tutorial";
		String expWindow2URL = "http://www.htmlcodetutorial.com/linking/linking_famsupp_70.html";
		String expWindow3Title = "Popup Window - HTML Code Tutorial";
		String expWindow3URL = "http://www.htmlcodetutorial.com/linking/popup_test_a.html";
		
		iface.go(expWindow0URL);
		
		// Check assumptions
		String actWindowTitle = iface.getControl(Strategy.TAG, "title").getText();
		assertEquals(expWindow0Title, actWindowTitle);
//		iface.interact(iface.getWindowsString());
		
		// Click pops-up window titled "Tryit Editor v1.8"
		iface.getControl(Strategy.PLINK, "A very simple HTML document").click();
		
		// Verify title without switching
		actWindowTitle = iface.getControl(Strategy.TAG, "title").getText();
		assertEquals(expWindow0Title, actWindowTitle);
		
		// Verify title with switching
		iface.focusWindow(1);
		actWindowTitle = iface.getControl(Strategy.TAG, "title").getText();
		assertEquals(expWindow1Title, actWindowTitle);
//		iface.interact(iface.getWindowsString());
		
		// Close window which should auto-focus to previous window; verify title
		iface.closeWindow();
		actWindowTitle = iface.getControl(Strategy.TAG, "title").getText();
		assertEquals(expWindow0Title, actWindowTitle);
//		iface.interact(iface.getWindowsString());
		
		// Click pop-up window titled "Tryit Editor v1.8"
		iface.getControl(Strategy.PLINK, "A very simple HTML document").click();
		
		// Navigate elsewhere and trigger popup window
//		iface.interact("window focus before go: " + iface.wd.getWindowHandle());
		iface.go(expWindow2URL);
//		iface.interact("window focus after go: " + iface.wd.getWindowHandle());
		iface.getControl(Strategy.PLINK, "this link").click();
//		iface.interact(iface.getWindowsString());
		
		// Verify title with (not) switching to current window by index
		iface.focusWindow(0);
		actWindowTitle = iface.getControl(Strategy.TAG, "title").getText();
		assertEquals(expWindow2Title, actWindowTitle);
//		iface.interact(iface.getWindowsString());
				
		// Verify URL with switching to window by title
		iface.focusWindow(expWindow1Title);
		String actWindowURL = iface.getURL();
		assertEquals(expWindow1URL, actWindowURL);
//		iface.interact(iface.getWindowsString());
		
		// Verify URL with switching to window by URL
		iface.focusWindow(expWindow3URL);
		actWindowTitle = iface.getControl(Strategy.TAG, "title").getText();
		assertEquals(expWindow3Title, actWindowTitle);
//		iface.interact(iface.getWindowsString());
		
		// Close window and revert to previous window (1 index); verify URL
		iface.closeWindow();
		actWindowURL = iface.getURL();
		assertEquals(expWindow1URL, actWindowURL);
//		iface.interact(iface.getWindowsString());
		
		// Close window and revert to previous window (0 index); verify URL
		iface.closeWindow();
		actWindowURL = iface.getURL();
		assertEquals(expWindow2URL, actWindowURL);
//		iface.interact(iface.getWindowsString());
		
		// Verify error by switching to erroneous window titles & indices
		thrown.expect(Exception.class);
		thrown.expectMessage("The given focus window string matched no title or URL: garbage");
		iface.focusWindow("garbage");
		thrown.expectMessage("Given focus window index is out of bounds: -1 current size: 1");
		iface.focusWindow(-1);
		thrown.expectMessage("Given focus window index is out of bounds: 1 current size: 1");
		iface.focusWindow(1);
//		iface.interact(iface.getWindowsString());
	}

	@Ignore
	@Test
	public void maximizeTest() {
//		this.iface.maximize();
	}

	@Ignore
	@Test
	public void getControlTest() throws Exception {
//		this.iface.getControl(null);
	}

	@Ignore
	@Test
	public void getSelectTest() throws Exception {
//		this.iface.getSelect(null);
	}

	@AfterClass
	public static void last() throws Exception {
		iface.stop();
	}
}
