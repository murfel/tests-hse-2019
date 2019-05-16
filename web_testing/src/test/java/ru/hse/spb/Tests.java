package ru.hse.spb;

import ru.hse.spb.objects.LoginPage;
import ru.hse.spb.objects.NewIssuePage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;

public class Tests {
    private WebDriver driver;

    @Before
    public void setup() {
        String pathToChromeDriver = "chromedriver";
        System.setProperty("webdriver.chrome.driver", pathToChromeDriver);
        driver = new ChromeDriver();
        (new LoginPage(driver)).login("root", "123456");
    }

    @After
    public void teardown() {
        driver.close();
    }

    private void assertCreated() {
        Wait<WebDriver> wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe("http://localhost:8080/dashboard#newissue=yes")));

    }

    private void assertNotCreated() throws InterruptedException {
        Thread.sleep(300);
        assertEquals("http://localhost:8080/dashboard#newissue=yes", driver.getCurrentUrl());
    }

    // Positive tests

    @Test
    public void addIssue() throws InterruptedException {
        NewIssuePage.createNewIssue(driver, "summary", "description");
        assertCreated();
    }

    @Test
    public void emptyDescription() {
        NewIssuePage.createNewIssue(driver, "summary", "");
        assertCreated();
    }

    @Test
    public void sameSummaryAndDescription() {
        NewIssuePage.createNewIssue(driver, "issue", "issue");
        assertCreated();
    }

    @Test
    public void duplicateSummary() {
        NewIssuePage.createNewIssue(driver, "non-unique summary", "description1");
        assertCreated();
        NewIssuePage.createNewIssue(driver, "non-unique summary", "description2");
        assertCreated();
    }

    @Test
    public void duplicateDescription() {
        NewIssuePage.createNewIssue(driver, "summary1", "non-unique description");
        assertCreated();
        NewIssuePage.createNewIssue(driver, "summary2", "non-unique description");
        assertCreated();
    }

    @Test
    public void duplicateSummaryAndDescription() {
        NewIssuePage.createNewIssue(driver, "my summary", "my description");
        assertCreated();
        NewIssuePage.createNewIssue(driver, "my summary", "my description");
        assertCreated();
    }

    @Test
    public void cyrillicIssue() {
        NewIssuePage.createNewIssue(driver, "ишу", "опишу");
        assertCreated();
    }

    @Test
    public void upperCaseIssue() {
        NewIssuePage.createNewIssue(driver, "SUMMARY", "DESCRIPTION");
        assertCreated();
    }

    @Test
    public void capitalizedIssue() {
        NewIssuePage.createNewIssue(driver, "Summary", "Description");
        assertCreated();
    }

    @Test
    public void longSummaryAndDescription() {
        String longText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas et malesuada dolor. Mauris et molestie nisi. Proin et consequat sapien, a dignissim tellus. Pellentesque consectetur aliquam dui, sed fermentum mi imperdiet in. Nulla sed mauris posuere, bibendum purus sed, gravida risus. Aenean eget pellentesque libero. Etiam in hendrerit massa, rhoncus congue diam.\n" +
                "\n" +
                "Phasellus molestie nisi eget vehicula accumsan. Maecenas leo neque, sodales vel accumsan id, congue sit amet enim. Vestibulum vestibulum quis nunc nec euismod. Integer pretium ligula quis ex commodo, vitae dignissim urna euismod. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Maecenas diam metus, pharetra sit amet vulputate quis, sagittis non ipsum. Nullam faucibus tellus cursus ultrices posuere. In finibus egestas diam vitae ullamcorper. Integer vehicula nisl enim, ac posuere sem cursus eget. Proin sed velit rhoncus, rhoncus velit id, pharetra mauris. Praesent vulputate aliquam nibh. Ut iaculis dignissim nulla, in viverra sem suscipit eu. In a accumsan est.\n" +
                "\n" +
                "Sed quis fringilla enim, ac consequat orci. Sed varius in leo non pharetra. Integer non pretium nibh. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Curabitur pretium nulla justo, vel suscipit orci tincidunt in. Aliquam sit amet dictum urna. Donec mollis felis consequat, scelerisque ex vitae, vestibulum arcu. Vivamus finibus, enim ut vulputate luctus, nunc odio dapibus purus, ac gravida tellus dui vitae ipsum.\n" +
                "\n";
        NewIssuePage.createNewIssue(driver, longText, longText);
        assertCreated();
    }

    // Negative tests

    @Test
    public void emptySummaryNotAllowed() throws InterruptedException {
        NewIssuePage.createNewIssue(driver, "", "description");
        assertNotCreated();
    }

    @Test
    public void emptySummaryAndDescriptionNotAllowed() throws InterruptedException {
        NewIssuePage.createNewIssue(driver, "", "");
        assertNotCreated();
    }

}

