package ru.hse.spb.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TextElement {
    private WebElement element;

    public TextElement(WebDriver driver, String name) {
        WebDriverWait wait = new WebDriverWait(driver, 3);
        element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(name)));
    }

    public void setText(String text) {
        element.sendKeys(text);
    }
}
