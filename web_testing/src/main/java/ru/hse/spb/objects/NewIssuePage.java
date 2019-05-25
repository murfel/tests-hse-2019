package ru.hse.spb.objects;

import ru.hse.spb.elements.ButtonElement;
import ru.hse.spb.elements.TextElement;
import org.openqa.selenium.WebDriver;

import static java.lang.System.exit;

public class NewIssuePage {
    private final TextElement summary;
    private final TextElement description;
    private final ButtonElement createIssueButton;

    public NewIssuePage(WebDriver driver) {
        driver.navigate().to("http://localhost:8080/dashboard#newissue=yes");
        summary = new TextElement(driver, "l.D.ni.ei.eit.summary");
        description = new TextElement(driver, "l.D.ni.ei.eit.description");
        createIssueButton = new ButtonElement(driver, "id_l.D.ni.ei.submitButton_74_0");
    }

    public void setSummaryAndDescription(String summary, String description) {
        this.summary.setText(summary);
        this.description.setText(description);
//        System.out.println(summary);
//        System.out.println(description);
//        System.out.println(this.summary.getText());
//        System.out.println(this.description.getText());

    }

    public Issue getCachedIssue() {
        return new Issue(summary.getText(), description.getText());
    }

    public static void createNewIssue(WebDriver driver, String summary, String description) {
        (new NewIssuePage(driver)).createNewIssue(summary, description);
    }

    public void createNewIssue(String summary, String description) {
        setSummaryAndDescription(summary, description);
        createIssueButton.click();
    }
}
