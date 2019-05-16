package ru.hse.spb.objects;

import ru.hse.spb.elements.ButtonElement;
import ru.hse.spb.elements.TextElement;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private final TextElement login;
    private final TextElement password;
    private final ButtonElement loginButton;

    public LoginPage(WebDriver driver) {
        driver.navigate().to("http://localhost:8080/login");
        login = new TextElement(driver, "l.L.login");
        password = new TextElement(driver, "l.L.password");
        loginButton = new ButtonElement(driver, "id_l.L.loginButton");
    }

    public void login(String login, String password) {
        this.login.setText(login);
        this.password.setText(password);
        loginButton.click();
    }
}
