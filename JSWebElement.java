package com.hsbc.grt.lem;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by pdcwzl on 12/7/2016.
 */
public class JSWebElement {

    private RemoteWebElement remoteWebElement = null;
    private JavascriptExecutor javascriptExecutor = null;

    public JSWebElement() {
    }

    public JSWebElement(RemoteWebElement remoteWebElement) {
        this.remoteWebElement = remoteWebElement;
    }

    public void setRemoteWebElement(RemoteWebElement remoteWebElement) {
        this.remoteWebElement = remoteWebElement;
    }

    public JSWebElement findElementById(String using) {
        try {
            return new JSWebElement((RemoteWebElement) this.remoteWebElement.findElementById(using));
        } catch (NoSuchElementException e) {
            return new JSWebElement();
        }
    }

    public JSWebElement findElementByCssSelector(String using) {
        try {
            return new JSWebElement((RemoteWebElement) this.remoteWebElement.findElementByCssSelector(using));
        } catch (NoSuchElementException e) {
            return new JSWebElement();
        }
    }

    public JSWebElement findElementByXPath(String using) {
        try {
            return new JSWebElement((RemoteWebElement) this.remoteWebElement.findElementByXPath(using));
        } catch (NoSuchElementException e) {
            return new JSWebElement();
        }
    }

    public JSWebElement findElementByLinkText(String using) {
        try {
            return new JSWebElement((RemoteWebElement) this.remoteWebElement.findElementByLinkText(using));
        } catch (NoSuchElementException e) {
            return new JSWebElement();
        }
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

    public Boolean isExist() {
        return this.remoteWebElement != null;
    }

    public String getHtml() {
        return this.remoteWebElement.getAttribute("outerHTML");
    }

    public String getText() {
        return this.remoteWebElement.getText();
    }

    public String getValue() {
        return this.remoteWebElement.getAttribute("value");
    }

    public String getAttribute(String name) {
        return this.remoteWebElement.getAttribute(name);
    }

    public JSWebElement sendKeys(String keys) {
        String oldBg = this.setBackgroundAndReturnOld("yellow");
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        remoteWebElement.sendKeys(keys);
        this.setBackground(oldBg);
        return this;
    }

    public boolean isEnabled() {
        return remoteWebElement.isEnabled();
    }

    public RemoteWebElement getNativeWebElement() {
        return this.remoteWebElement;
    }

    public boolean isVisible() {
        return remoteWebElement.isDisplayed();
    }

    public JSWebElement clear() {
        remoteWebElement.clear();
        return this;
    }

    public JSWebElement click() {
        remoteWebElement.click();
        return this;
    }

    public String getCssValue(String name) {
        return remoteWebElement.getCssValue(name);
    }

    public boolean isSelected() {
        return remoteWebElement.isSelected();
    }

    public JSWebElement select(String by, String value) throws Exception {
        if (remoteWebElement.getTagName().equals("select")) {
            Select select = new Select(remoteWebElement);
            if (by.equals("index")) {
                select.selectByIndex(Integer.parseInt(value));
            } else if (by.equals("value")) {
                select.selectByValue(value);
            } else if (by.equals("text")) {
                select.selectByVisibleText(value);
            }
        } else {
            Exception e = new Exception("The element is not SELECT Object");
            throw e;
        }
        return this;
    }

    public JSWebElement deSelect(String by, String value) throws Exception {
        if (remoteWebElement.getTagName().equals("select")) {
            Select select = new Select(remoteWebElement);
            if (by.equals("index")) {
                select.deselectByIndex(Integer.parseInt(value));
            } else if (by.equals("value")) {
                select.deselectByValue(value);
            } else if (by.equals("text")) {
                select.deselectByVisibleText(value);
            } else if (by.equals("*")) {
                select.deselectAll();
            }
        } else {
            Exception e = new Exception("The element is not SELECT Object");
            throw e;
        }
        return this;
    }

    public boolean isSelectMultiple() throws Exception {
        if (remoteWebElement.getTagName().equals("select")) {
            Select select = new Select(remoteWebElement);
            if (select.isMultiple()) {
                return true;
            } else {
                return false;
            }
        } else {
            Exception e = new Exception("The element is not SELECT Object");
            throw e;
        }
    }

    public String getSelectedText() throws Exception {
        if (remoteWebElement.getTagName().equals("select")) {
            String text = "";
            Select select = new Select(remoteWebElement);
            List<WebElement> options = select.getAllSelectedOptions();
            for (WebElement e : options) {
                text += e.getText() + "\r\n";
            }
            return text;
        } else {
            Exception e = new Exception("The element is not SELECT Object");
            throw e;
        }
    }

    public boolean isSelectInclude(String name) throws Exception {
        if (remoteWebElement.getTagName().equals("select")) {
            Select select = new Select(remoteWebElement);
            List<WebElement> options = select.getOptions();
            for (WebElement w : options) {
                if (w.getText().equals(name)) {
                    return true;
                }
            }
            return false;
        } else {
            Exception e = new Exception("The element is not SELECT Object");
            throw e;
        }
    }

    public String getTagName() {
        return remoteWebElement.getTagName();
    }

    public String getId() {
        return remoteWebElement.getId();
    }

    public Point getLocation() {
        return remoteWebElement.getLocation();
    }

    public Point getLocationOnScreenOnceScrolledIntoView() {
        return remoteWebElement.getLocation();
    }

    public Coordinates getCoordinates() {
        return remoteWebElement.getCoordinates();
    }

    public Dimension getSize() {
        return remoteWebElement.getSize();
    }

    public JSWebElement submit() {
        remoteWebElement.submit();
        return this;
    }

    public JSWebElement check(String by, String value) throws Exception {
        if (remoteWebElement.getTagName().equals("input")) {
            if (remoteWebElement.getAttribute("type").equals("radio")) {
                WebDriver wd = remoteWebElement.getWrappedDriver();
                List<WebElement> wl = wd.findElements(By.name(remoteWebElement.getAttribute("name")));
                if (by.equals("index")) {
                    wl.get(Integer.parseInt(value)).click();
                } else if (by.equals("value")) {
                    for (WebElement w : wl) {
                        if (w.getAttribute("value").equals(value)) {
                            w.click();
                            break;
                        }
                    }
                }
            } else if (remoteWebElement.getAttribute("type").equals("checkbox")) {
                if (!remoteWebElement.isSelected()) {
                    remoteWebElement.click();
                }
            } else {
                Exception e = new Exception("The element is not Radio or CheckBox Object");
                throw e;
            }
        } else {
            Exception e = new Exception("The element is not INPUT Object");
            throw e;
        }
        return this;
    }

    public JSWebElement unCheck() throws Exception {
        if (remoteWebElement.getTagName().equals("input") && remoteWebElement.getAttribute("type").equals("checkbox")) {
            if (remoteWebElement.isSelected()) {
                remoteWebElement.click();
            }
        } else {
            Exception e = new Exception("The element is not CheckBox Object");
            throw e;
        }
        return this;
    }

    public boolean isChecked(String by, String value) throws Exception {
        if (remoteWebElement.getTagName().equals("input")) {
            if (remoteWebElement.getAttribute("type").equals("radio")) {
                WebDriver wd = remoteWebElement.getWrappedDriver();
                List<WebElement> wl = wd.findElements(By.name(remoteWebElement.getAttribute("name")));
                if (by.equals("index")) {
                    return wl.get(Integer.parseInt(value)).isSelected();
                } else if (by.equals("value")) {
                    for (WebElement w : wl) {
                        if (w.getAttribute("value").equals(value)) {
                            return w.isSelected();
                        }
                    }
                }
                return false;
            } else if (remoteWebElement.getAttribute("type").equals("checkbox")) {
                return remoteWebElement.isSelected();
            } else {
                Exception e = new Exception("The element is not Radio or CheckBox Object");
                throw e;
            }
        } else {
            Exception e = new Exception("The element is not INPUT Object");
            throw e;
        }
    }

    public JSWebElement scroll() {
        this.focus();
        return this;
    }

    public JSWebElement highLight() throws InterruptedException {
        this.focus();
        JavascriptExecutor js = getJavascriptExecutor();
        String oldStyle = remoteWebElement.getAttribute("style");
        for (int i = 0; i < 3; i++) {
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", this.remoteWebElement, "background-color: red; border: 2px solid red;" + oldStyle);
            Thread.sleep(500);
            js.executeScript("arguments[0].setAttribute('style', arguments[1]);", this.remoteWebElement, oldStyle);
            Thread.sleep(500);
        }
        return this;
    }

    public JSWebElement fireEvent(String event) {
        JavascriptExecutor js = getJavascriptExecutor();
        js.executeScript(String.format("arguments[0].%s()", event), this.remoteWebElement);
        return this;
    }

    public JSWebElement focus() {
        JavascriptExecutor js = getJavascriptExecutor();
        js.executeScript("arguments[0].focus();", this.remoteWebElement);
        return this;
    }

    public JSWebElement executeJS(String commands) {
        JavascriptExecutor js = getJavascriptExecutor();
        String[] comandArr = commands.split(";");
        commands = "";
        for (String comand : comandArr) {
            if (!comand.trim().equals("")) {
                commands += String.format("arguments[0].%s;", comand);
            }
        }
        if (!commands.equals("")) {
            js.executeScript(commands, this.remoteWebElement);
        }
        return this;
    }


    private JavascriptExecutor getJavascriptExecutor() {
        if (this.isExist()) {
            if (this.javascriptExecutor == null) {
                WebDriver webDriver = this.remoteWebElement.getWrappedDriver();
                this.javascriptExecutor = (JavascriptExecutor) webDriver;
            }
        }
        return this.javascriptExecutor;
    }

    private String getBackground() {
        return this.remoteWebElement.getCssValue("background-color");
    }

    private String setBackgroundAndReturnOld(String color) {
        String oldBg = this.getBackground();
        setBackground(color);
        return oldBg;
    }

    private void setBackground(String color) {
        this.getJavascriptExecutor().executeScript("arguments[0].style.background = arguments[1];", this.remoteWebElement, color);
    }
}
