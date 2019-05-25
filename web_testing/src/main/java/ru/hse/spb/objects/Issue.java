package ru.hse.spb.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Issue {
    private String summary;
    private String description;

    public Issue(String summary, String description) {
        this.summary = summary;
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public static Issue extractIssue(WebDriver driver, WebElement issueContainer) {
        String link = issueContainer.findElements(By.className("issueId")).get(0).getAttribute("href");
        driver.navigate().to(link);
        String summary = driver.findElement(By.id("id_l.I.ic.icr.it.issSum")).getText();
        String description = driver.findElement(By.id("id_l.I.ic.icr.d.description")).getText();
        return new Issue(summary, description);
    }
}
