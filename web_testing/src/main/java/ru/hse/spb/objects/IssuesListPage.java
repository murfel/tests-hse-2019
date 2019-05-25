package ru.hse.spb.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class IssuesListPage {
    private final WebDriver driver;
    private final List<WebElement> issueContainers;

    public IssuesListPage(WebDriver driver) {
        this.driver = driver;
        driver.navigate().to("http://localhost:8080/issues");
        issueContainers = driver.findElements(By.className("issueContainer"));  // a table for each issue
    }

    public Issue extractLastIssue() throws IssueDoesNotExistException {
        if (issueContainers.size() == 0) {
            throw new IssueDoesNotExistException();
        }
        return Issue.extractIssue(driver, issueContainers.get(0));
    }
}
