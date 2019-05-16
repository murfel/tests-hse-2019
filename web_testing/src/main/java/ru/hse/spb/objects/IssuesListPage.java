package ru.hse.spb.objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class IssuesListPage {
    private final WebDriver driver;
    private final List<WebElement> issueTables;

    public IssuesListPage(WebDriver driver) {
        this.driver = driver;
        issueTables = driver.findElements(By.className("issue-wrp"));  // a table for each issue
    }

    public Issue extractIssue(int index) throws IssueDoesNotExistException {
        if (issueTables.size() < index) {
            throw new IssueDoesNotExistException();
        }
        return Issue.extractIssue(driver, issueTables.get(index));
    }
}
