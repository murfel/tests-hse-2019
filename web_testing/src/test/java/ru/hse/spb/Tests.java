package ru.hse.spb;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.hse.spb.objects.*;
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
    private final int WAIT_TIME_OUT_IN_SECONDS = 3;

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

    private void createIssue(String summary, String description) {
        NewIssuePage.createNewIssue(driver, summary, description);
        Wait<WebDriver> wait = new WebDriverWait(driver, WAIT_TIME_OUT_IN_SECONDS);
        wait.until(ExpectedConditions.not(ExpectedConditions.urlToBe("http://localhost:8080/dashboard#newissue=yes")));
    }

    private String trimNewLines(String s) {
        // trim left
        String[] parts = s.split("^\\n+", 2);
        s = parts.length > 1 ? parts[1] : parts[0];
        // trim right
        return s.split("\\n+$", 2)[0];
    }

    private void assertLastIssueIs(String summary, String description) throws IssueDoesNotExistException {
        Issue issue = (new IssuesListPage(driver)).extractLastIssue();
        assertEquals(summary.trim().replaceAll("\n", "").replaceAll(" +", " "), issue.getSummary());
        assertEquals(trimNewLines(description), issue.getDescription());
    }

    private void createIssueAndAssertItIsLastIssue(String summary, String description) throws IssueDoesNotExistException {
        String finalDescription = description.isEmpty() ? "No description" : description;
        createIssue(summary, description);
        assertLastIssueIs(summary, finalDescription);
    }

    private void assertNotCreatedBecauseOfEmptySummary() {
        Wait<WebDriver> wait = new WebDriverWait(driver, WAIT_TIME_OUT_IN_SECONDS);
        WebElement popup = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("__popup__1")));
        assertEquals("!\n" +
                "Summary is required\n" +
                "close", popup.getText());
        assertEquals("http://localhost:8080/dashboard#newissue=yes", driver.getCurrentUrl());
    }

    // Positive tests

    @Test
    public void addIssue() throws IssueDoesNotExistException {
        createIssueAndAssertItIsLastIssue("summary", "description");
    }

    @Test
    public void emptyDescription() throws IssueDoesNotExistException {
        createIssueAndAssertItIsLastIssue("summary", "");
    }

    @Test
    public void sameSummaryAndDescription() throws IssueDoesNotExistException {
        createIssueAndAssertItIsLastIssue("issue", "issue");
    }

    @Test
    public void duplicateSummary() throws IssueDoesNotExistException {
        createIssueAndAssertItIsLastIssue("non-unique summary", "description1");
        createIssueAndAssertItIsLastIssue("non-unique summary", "description2");
    }

    @Test
    public void duplicateDescription() throws IssueDoesNotExistException {
        createIssueAndAssertItIsLastIssue("summary1", "non-unique description");
        createIssueAndAssertItIsLastIssue("summary2", "non-unique description");
    }

    @Test
    public void duplicateSummaryAndDescription() throws IssueDoesNotExistException {
        createIssueAndAssertItIsLastIssue("my summary", "my description");
        createIssueAndAssertItIsLastIssue("my summary", "my description");
    }

    @Test
    public void cyrillicIssue() throws IssueDoesNotExistException {
        createIssueAndAssertItIsLastIssue("ишу", "опишу");
    }

    @Test
    public void upperCaseIssue() throws IssueDoesNotExistException {
        createIssueAndAssertItIsLastIssue("SUMMARY", "DESCRIPTION");
    }

    @Test
    public void capitalizedIssue() throws IssueDoesNotExistException {
        createIssueAndAssertItIsLastIssue("Summary", "Description");
    }

    @Test
    public void noMultilineSummaryAndYesMultilineDescription() throws IssueDoesNotExistException {
        createIssueAndAssertItIsLastIssue("Multiline\nSummary", "Multiline\nDescription");
    }

    @Test
    public void replaceMultipleSpaceWithOneInSummaryAndDoNothingInDescription() throws IssueDoesNotExistException {
        createIssueAndAssertItIsLastIssue("sum   ma  ry", "de   scrip  tion");
    }

    @Test
    public void trimsSpacesInSummaryAndTrimsNewLinesInDescription() throws IssueDoesNotExistException {
        createIssueAndAssertItIsLastIssue(" sum\n   mary \n ", " \n de\n   scription   \n\n\nlines  \n\n");
    }

    @Test
    public void oneSpaceSummary() throws  IssueDoesNotExistException {
        createIssueAndAssertItIsLastIssue(" ", "");
    }

    @Test
    public void summaryAndDescriptionAcceptBackslash() throws IssueDoesNotExistException {
        createIssueAndAssertItIsLastIssue("\\", "\\");
    }

    @Test
    public void summaryAndDescriptionDeleteBackslashBeforeAsciiSymbol() throws IssueDoesNotExistException {
        createIssueAndAssertItIsLastIssue("\\#", "\\#");
    }

    @Test
    public void summaryAndDescriptionDoNotDeleteBackslashBeforeAsciiLetter() throws IssueDoesNotExistException {
        createIssueAndAssertItIsLastIssue("\\n", "\\n");
    }

    @Test
    public void summaryAndDescriptionAcceptAllPrintableAsciiCharacters() throws IssueDoesNotExistException {
        String allPrintableAsciiCharacters = " !\"#$%&'()*+,-./0123456789:;<=>?@\\ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        createIssueAndAssertItIsLastIssue(allPrintableAsciiCharacters, allPrintableAsciiCharacters);
    }

    @Test
    public void summaryAndDescriptionAreCachedAtLeastIntoBrowser() {
        String summary = "Summary cached for later at least into browser";
        String description = "Description saved for later into browser\nWait for it";

        (new NewIssuePage(driver)).setSummaryAndDescription(summary, description);

        Issue cachedIssue = (new NewIssuePage(driver)).getCachedIssue();
        assertEquals(summary, cachedIssue.getSummary());
        assertEquals(description, cachedIssue.getDescription());
    }

    @Test
    public void summaryAndDescriptionAreCachedIntoServer() {
        String summary = "Summary cached for later";
        String description = "Description saved for later\nWait for it";

        (new NewIssuePage(driver)).setSummaryAndDescription(summary, description);

        teardown();
        setup();

        Issue cachedToServerIssue = (new NewIssuePage(driver)).getCachedIssue();
        assertEquals(summary, cachedToServerIssue.getSummary());
        assertEquals(description, cachedToServerIssue.getDescription());
    }

    @Test
    public void longSummaryAndDescription() throws IssueDoesNotExistException {
        String longText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas et malesuada dolor. Mauris et molestie nisi. Proin et consequat sapien, a dignissim tellus. Pellentesque consectetur aliquam dui, sed fermentum mi imperdiet in. Nulla sed mauris posuere, bibendum purus sed, gravida risus. Aenean eget pellentesque libero. Etiam in hendrerit massa, rhoncus congue diam.\n" +
                "\n" +
                "Phasellus molestie nisi eget vehicula accumsan. Maecenas leo neque, sodales vel accumsan id, congue sit amet enim. Vestibulum vestibulum quis nunc nec euismod. Integer pretium ligula quis ex commodo, vitae dignissim urna euismod. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Maecenas diam metus, pharetra sit amet vulputate quis, sagittis non ipsum. Nullam faucibus tellus cursus ultrices posuere. In finibus egestas diam vitae ullamcorper. Integer vehicula nisl enim, ac posuere sem cursus eget. Proin sed velit rhoncus, rhoncus velit id, pharetra mauris. Praesent vulputate aliquam nibh. Ut iaculis dignissim nulla, in viverra sem suscipit eu. In a accumsan est.\n" +
                "\n" +
                "Sed quis fringilla enim, ac consequat orci. Sed varius in leo non pharetra. Integer non pretium nibh. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Curabitur pretium nulla justo, vel suscipit orci tincidunt in. Aliquam sit amet dictum urna. Donec mollis felis consequat, scelerisque ex vitae, vestibulum arcu. Vivamus finibus, enim ut vulputate luctus, nunc odio dapibus purus, ac gravida tellus dui vitae ipsum.\n" +
                "\n";
        String anotherLongText = "Description: " + longText;
        createIssueAndAssertItIsLastIssue(longText, anotherLongText);
    }

    // TODO: Add a test for a JavaScript injection

    // Negative tests

    @Test
    public void emptySummaryNotAllowed() throws InterruptedException {
        NewIssuePage.createNewIssue(driver, "", "kukarek description");
        assertNotCreatedBecauseOfEmptySummary();
    }

    @Test
    public void emptySummaryAndDescriptionNotAllowed() throws InterruptedException {
        NewIssuePage.createNewIssue(driver, "", "");
        assertNotCreatedBecauseOfEmptySummary();
    }
}