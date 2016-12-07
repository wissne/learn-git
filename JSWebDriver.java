package com.hsbc.grt.lem;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

public class JSWebDriver {
    private RemoteWebDriver remoteWebDriver = null;
    private JavascriptExecutor javascriptExecutor = null;

    public JSWebDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        remoteWebDriver = new RemoteWebDriver(remoteAddress, desiredCapabilities);
    }

    public JSWebDriver goTo(String url) {
        remoteWebDriver.get(url);
        return this;
    }

    public JSWebDriver quit() {
        remoteWebDriver.quit();
        return this;
    }

    public JSWebDriver back() {
        remoteWebDriver.navigate().back();
        return this;
    }

    public JSWebDriver forward() {
        remoteWebDriver.navigate().forward();
        return this;
    }

    public JSWebDriver refresh() {
        remoteWebDriver.navigate().refresh();
        return this;
    }

    private JSWebDriver switchToWindow(String by, String value, String... regex) throws Exception {
        String currenthandle = remoteWebDriver.getWindowHandle();
        Set<String> handles = remoteWebDriver.getWindowHandles();
        int currentIndex = -1;
        String searchString = "";
        for (String handle : handles) {
            currentIndex += 1;
            if (handle.equals(currenthandle)) {
                continue;
            } else {
                remoteWebDriver.switchTo().window(handle);
                if (regex.length == 1 && regex[0].equals("regex")) {
                    if (by.equals("title")) {
                        searchString = remoteWebDriver.getTitle();
                    } else if (by.equals("url")) {
                        searchString = remoteWebDriver.getCurrentUrl();
                    }
                    Pattern pattern = Pattern.compile(value);
                    Matcher matcher = pattern.matcher(searchString);
                    if (matcher.find()) {
                        return this;
                    }
                } else {
                    if (by.equals("title")) {
                        searchString = remoteWebDriver.getTitle();
                    } else if (by.equals("url")) {
                        searchString = remoteWebDriver.getCurrentUrl();
                    } else if (by.equals("index")) {
                        searchString = Integer.toString(currentIndex);
                    }
                    if (searchString.equals(value)) {
                        return this;
                    }
                }
            }
        }
        Exception e = new Exception("Swtich Window Failed, Please Make Sure The Locator Was Right.");
        throw e;
    }

    public JSWebDriver switchToWinowByUrl(String value, String... regex) throws Exception {
        return this.switchToWindow("url", value, regex);
    }

    public JSWebDriver switchToWinowByTitle(String value, String... regex) throws Exception {
        return this.switchToWindow("title", value, regex);
    }

    public JSWebDriver switchToWinowByIndex(String value) throws Exception {
        return this.switchToWindow("index", value);
    }

    public JSWebDriver clickAlertSure() {
        Alert alert = remoteWebDriver.switchTo().alert();
        alert.accept();
        return this;
    }

    public JSWebDriver clickAlertDismiss() {
        Alert alert = remoteWebDriver.switchTo().alert();
        alert.dismiss();
        return this;
    }

    public JSWebDriver setPromptMessage(String parameter) {
        Alert alert = remoteWebDriver.switchTo().alert();
        alert.sendKeys(parameter);
        return this;
    }

    public String getPromptMessage() {
        Alert alert = remoteWebDriver.switchTo().alert();
        return alert.getText();
    }

    public JSWebDriver switchToFrame(JSWebElement jseElement) {
        remoteWebDriver.switchTo().frame(jseElement.getNativeWebElement());
        return this;
    }

    public JSWebDriver executeScript(String parameter) {
        JavascriptExecutor js = getJavascriptExecutor();
        js.executeScript(parameter);
        return this;
    }

    public String getCookie(String name) {
        Cookie cookie = remoteWebDriver.manage().getCookieNamed(name);
        if (cookie == null) {
            return "null";
        }
        return cookie.getValue();
    }

    public Map<String, String> getCookies() {
        Map<String, String> newCookies = new HashMap<String, String>();
        Set<Cookie> cookies = remoteWebDriver.manage().getCookies();
        for (Cookie cookie : cookies) {
            newCookies.put(cookie.getName(), cookie.getValue());
        }
        return newCookies;
    }

    public JSWebDriver getScreen(String filepath) {
        WebDriver augmentedDriver = new Augmenter().augment(this.remoteWebDriver);
        TakesScreenshot ts = (TakesScreenshot) augmentedDriver;
        File screenShotFile = ts.getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenShotFile, new File(filepath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String getTitle() {
        return remoteWebDriver.getTitle();
    }

    public String getUrl() {
        return remoteWebDriver.getCurrentUrl();
    }

    public String getSource() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return remoteWebDriver.getPageSource();
    }

    public JSWebDriver scroll(String x, String y) {
        if (x.equals("left")) {
            x = "0";
        } else if (x.equals("right")) {
            x = "document.body.scrollWidth";
        } else if (x.equals("middle")) {
            x = "document.body.scrollWidth/2";
        }
        if (y.equals("top")) {
            y = "0";
        } else if (y.equals("buttom")) {
            y = "document.body.scrollHeight";
        } else if (y.equals("middle")) {
            y = "document.body.scrollHeight/2";
        }
        this.executeScript(String.format("scroll(%s,%s);", x, y));
        return this;
    }

    public JSWebDriver maximize() {
        remoteWebDriver.manage().window().maximize();
        return this;
    }

    public JSWebElement findElementById(String using) {
        try {
            return new JSWebElement((RemoteWebElement) remoteWebDriver.findElementById(using));
        } catch (NoSuchElementException e) {
            return new JSWebElement();
        }
    }

    public JSWebElement findElementByCssSelector(String using) {
        try {
            return new JSWebElement((RemoteWebElement) remoteWebDriver.findElementByCssSelector(using));
        } catch (NoSuchElementException e) {
            return new JSWebElement();
        }
    }

    public JSWebElement findElementByXPath(String using) {
        try {
            return new JSWebElement((RemoteWebElement) remoteWebDriver.findElementByXPath(using));
        } catch (NoSuchElementException e) {
            return new JSWebElement();
        }
    }

    public JSWebElement findElementByLinkText(String using) {
        try {
            return new JSWebElement((RemoteWebElement) remoteWebDriver.findElementByLinkText(using));
        } catch (NoSuchElementException e) {
            return new JSWebElement();
        }
    }

    public JSWebElement findElementByPartialLinkText(String using) {
        try {
            return new JSWebElement((RemoteWebElement) remoteWebDriver.findElementByPartialLinkText(using));
        } catch (NoSuchElementException e) {
            return new JSWebElement();
        }
    }

    public JSWebElement findElement(By by) {
        try {
            return new JSWebElement((RemoteWebElement) this.remoteWebDriver.findElement(by));
        } catch (java.util.NoSuchElementException e) {
            return new JSWebElement();
        }
    }

    public List<JSWebElement> findElements(By by) {
        List<JSWebElement> result = new ArrayList<JSWebElement>();
        try {
            List<WebElement> elements = this.remoteWebDriver.findElements(by);

            for (WebElement element : elements) {
                result.add(new JSWebElement((RemoteWebElement) element));
            }
        } catch (java.util.NoSuchElementException e) {
        }
        return result;
    }

    public JSWebElement findElementByDom(String using) {
        try {
            JavascriptExecutor js = this.getJavascriptExecutor();
            WebElement we = (WebElement) js.executeScript(String.format("return %s", using));
            return new JSWebElement((RemoteWebElement) we);
        } catch (NoSuchElementException e) {
            return new JSWebElement();
        }
    }

    public RemoteWebDriver getNativeWebDriver() {
        return this.remoteWebDriver;
    }

    private JavascriptExecutor getJavascriptExecutor() {
        if (this.javascriptExecutor == null) {
            this.javascriptExecutor = (JavascriptExecutor) this.remoteWebDriver;
        }
        return javascriptExecutor;
    }
}
