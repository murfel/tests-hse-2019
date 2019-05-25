package ru.hse.spb.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ButtonElement {
    private WebElement element;

    public ButtonElement(WebDriver driver, String id) {
        WebDriverWait wait = new WebDriverWait(driver, 3);
        element = wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
    }

    public void click() {
        element.click();
    }
}
