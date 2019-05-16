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

    public static Issue extractIssue(WebDriver driver, WebElement issueTable) {
        WebElement issueSummaryLink = issueTable.findElement(By.className("issue-summary"));
        String summary = issueSummaryLink.getText();
        String relativeLink = issueSummaryLink.getAttribute("href");
        driver.navigate().to("http://localhost:8080/" + relativeLink);
        driver.navigate().back();
        String description = driver.findElements(By.className("wiki")).get(0).getText();
        return new Issue(summary, description);
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }
}
