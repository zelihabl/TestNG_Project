package team8_testngproject.utilities;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class ReusableMethods {

    public static String getScreenshot(String str) {

        // naming the screenshot with the current date to avoid duplication
        String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        // TakesScreenshot is an interface of selenium that takes the screenshot
        TakesScreenshot ts = (TakesScreenshot) Driver.getDriver();
        File source = ts.getScreenshotAs(OutputType.FILE);
        // full path to the screenshot location
        String target = System.getProperty("user.dir") + "/src/test/java/screenshots/" + date + ".png";
        File finalDestination = new File(target);
        // save the screenshot to the path given

        try {
            FileUtils.copyFile(source, finalDestination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return target;
    }

    //========Switching Window=====//
    public static void switchToWindow(String targetTitle) {
        String origin = Driver.getDriver().getWindowHandle();
        for (String handle : Driver.getDriver().getWindowHandles()) {
            Driver.getDriver().switchTo().window(handle);
            if (Driver.getDriver().getTitle().equals(targetTitle)) {
                return;
            }
        }
        Driver.getDriver().switchTo().window(origin);
    }

    //========Hover Over=====//
    public static void hover(WebElement element) {
        Actions actions = new Actions(Driver.getDriver());
        actions.moveToElement(element).perform();
    }

    //==========Return a list of string given a list of Web Element====////
    public static List<String> getElementsText(List<WebElement> list) {
        List<String> elemTexts = new ArrayList<>();
        for (WebElement el : list) {
            if (!el.getText().isEmpty()) {
                elemTexts.add(el.getText());
            }
        }
        return elemTexts;
    }

    //========Returns the Text of the element given an element locator==//
    public static List<String> getElementsText(By locator) {
        List<WebElement> elems = Driver.getDriver().findElements(locator);
        List<String> elemTexts = new ArrayList<>();
        for (WebElement el : elems) {
            if (!el.getText().isEmpty()) {
                elemTexts.add(el.getText());
            }
        }
        return elemTexts;
    }

    //   HARD WAIT WITH THREAD.SLEEP
    //   waitFor(5);  => waits for 5 second => Thread.sleep(5000)
    public static void waitFor(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //===============Explicit Wait==============//

    public static WebElement waitForVisibility(WebElement element, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForVisibility(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickablility(WebElement element, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static WebElement waitForClickablility(By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void clickWithTimeOut(WebElement element, int timeout) {
        for (int i = 0; i < timeout; i++) {
            try {
                element.click();
                return;
            } catch (WebDriverException e) {
                waitFor(1);
            }
        }
    }

    public static void waitForPageToLoad(long timeout) {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        try {
            System.out.println("Waiting for page to load...");
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
            wait.until(expectation);
        } catch (Throwable error) {
            System.out.println(
                    "Timeout waiting for Page Load Request to complete after " + timeout + " seconds");
        }
    }

    //======Fluent Wait====//
    public static WebElement fluentWait(final WebElement webElement, int timeout) {
        //FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Driver.getDriver()).withTimeout(timeinsec, TimeUnit.SECONDS).pollingEvery(timeinsec, TimeUnit.SECONDS);
        FluentWait<WebDriver> wait = new FluentWait<WebDriver>(Driver.getDriver())
                .withTimeout(Duration.ofSeconds(3))//Wait 3 second each time
                .pollingEvery(Duration.ofSeconds(1))////Check for the element every 1 second
                .ignoring(NoSuchMethodException.class);
        WebElement element = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return webElement;
            }
        });
        return element;
    }

    /**
     * Performs double click action on an element
     *
     * @param element
     */
    public static void doubleClick(WebElement element) {
        new Actions(Driver.getDriver()).doubleClick(element).build().perform();
    }

    /**
     * @param element
     * @param check
     */
    public static void selectCheckBox(WebElement element, boolean check) {
        if (check) {
            if (!element.isSelected()) {
                element.click();
            }
        } else {
            if (element.isSelected()) {
                element.click();
            }
        }
    }

    /**
     * Selects a random value from a dropdown list and returns the selected Web Element
     *
     * @param select
     * @return
     */
    public static WebElement selectRandomTextFromDropdown(Select select) {
        Random random = new Random();
        List<WebElement> weblist = select.getOptions();
        int optionIndex = 1 + random.nextInt(weblist.size() - 1);
        select.selectByIndex(optionIndex);
        return select.getFirstSelectedOption();
    }

    /**
     * Verifies whether the element matching the provided locator is displayed on page
     * fails if the element matching the provided locator is not found or not displayed
     *
     * @param by
     */
    public static void verifyElementDisplayed(By by) {
        try {
            assertTrue("Element not visible: " + by, Driver.getDriver().findElement(by).isDisplayed());
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + by);
        }
    }

    /**
     * Verifies whether the element matching the provided locator is NOT displayed on page
     * fails if the element matching the provided locator is not found or not displayed
     *
     * @param by
     */
    public static void verifyElementNotDisplayed(By by) {
        try {
            assertFalse("Element should not be visible: " + by, Driver.getDriver().findElement(by).isDisplayed());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

    }

    /**
     * Verifies whether the element matching the provided WebElement is NOT displayed on page
     * fails if the element matching the WebElement is not found or not displayed
     *
     * @paramWebElement
     */
    public static void verifyElementNotDisplayed(WebElement element) {
        try {
            assertFalse("Element should not be visible: " + element, element.isDisplayed());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifies whether the element is displayed on page
     * fails if the element is not found or not displayed
     *
     * @param element
     */
    public static void verifyElementDisplayed(WebElement element) {
        try {
            assertTrue("Element not visible: " + element, element.isDisplayed());
        } catch (NoSuchElementException e) {
            Assert.fail("Element not found: " + element);
        }
    }

    public static void jsClick(By locator) {
        JavascriptExecutor js = (JavascriptExecutor) Driver.driver;
        WebDriverWait wait = new WebDriverWait(Driver.driver, Duration.ofSeconds(10));
        WebElement elementName = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        js.executeScript("arguments[0].click();", elementName);
    }

    public static void jsClick(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) Driver.driver;
        WebDriverWait wait = new WebDriverWait(Driver.driver, Duration.ofSeconds(10));
        WebElement elementName = wait.until(ExpectedConditions.visibilityOf(element));
        js.executeScript("arguments[0].click();", elementName);
    }

    public static void switchToWindow(int windowNumber) {
        List<String> list = new ArrayList<>(Driver.getDriver().getWindowHandles());
        Driver.getDriver().switchTo().window(list.get(windowNumber));
    }

    public static List<String> getOptionsFromSelect(WebElement selectElement) {
        Select select = new Select(selectElement);
        List<WebElement> options = select.getOptions();
        List<String> optionValues = new ArrayList<>();
        for (WebElement option : options) {
            optionValues.add(option.getText());
        }
        return optionValues;
    }

    //   Js Executer Scroll Locator
    public static void jsScroll(By locator) {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        WebElement elementName = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        js.executeScript("arguments[0].scrollIntoView(true);", elementName);
    }

    //   Js Executer Scroll WebElement
    public static void jsScroll(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(10));
        WebElement elementName = wait.until(ExpectedConditions.visibilityOf(element));
        js.executeScript("arguments[0].scrollIntoView(true);", elementName);
    }

    public static void getScreenshot() {
        String date = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) Driver.getDriver();
        File source = ts.getScreenshotAs(OutputType.FILE);
        String target = System.getProperty("user.dir") + "/src/test/java/screenshots/" + date + ".png";
        File finalDestination = new File(target);
        try {
            FileUtils.copyFile(source, finalDestination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static void uploadFilePath(String filePath) {
        try {
            ReusableMethods.waitFor(3);
            StringSelection stringSelection = new StringSelection(filePath);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            Robot robot = new Robot();
            //pressing ctrl+v
            robot.keyPress(KeyEvent.VK_CONTROL);
            ReusableMethods.waitFor(3);
            robot.keyPress(KeyEvent.VK_V);
            ReusableMethods.waitFor(3);
            //releasing ctrl+v
            robot.keyRelease(KeyEvent.VK_CONTROL);
            ReusableMethods.waitFor(3);
            robot.keyRelease(KeyEvent.VK_V);
            ReusableMethods.waitFor(3);
            System.out.println("PASSED");
            //pressing enter
            ReusableMethods.waitFor(3);
            robot.keyPress(KeyEvent.VK_ENTER);
            ReusableMethods.waitFor(3);
            //releasing enter
            robot.keyRelease(KeyEvent.VK_ENTER);
            ReusableMethods.waitFor(3);
            System.out.println("ENTER");
        } catch (Exception e) {
        }
    }


}

