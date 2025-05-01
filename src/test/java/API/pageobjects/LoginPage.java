package API.pageobjects;

import API.LocatorService;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.io.IOException;

public class LoginPage extends BasePage{
    Locator username = getLocator("username", "input");
    Locator password = getLocator("password", "input");
    Locator login = getLocator("login","input");

    public LoginPage(Page page, LocatorService locatorService) throws IOException, InterruptedException {
        super(page, locatorService);
    }

    public boolean emptyUserName(String passwordValue) throws IOException, InterruptedException {
        password.fill(passwordValue);
        login.click();
        return getLocator("Epic sadface: Username is required", "others").isVisible();
    }

    public boolean emptyPassword(String usernameValue) throws IOException, InterruptedException {
        username.fill(usernameValue);
        password.clear();
        login.click();
        return getLocator("Epic sadface: Password is required","others").isVisible();
    }

    public boolean incorrectUsernamePassword(String incorrectUserName, String incorrectPassword) throws IOException, InterruptedException {
        username.clear();
        password.clear();
        login.click();
        username.fill(incorrectUserName);
        password.fill(incorrectPassword);
        login.click();
        return getLocator("Epic sadface: Username and password do not match any user in this service","others").isVisible();
    }

    public void login(String usernameValue, String passwordValue) {
        username.fill(usernameValue);
        password.fill(passwordValue);
        login.click();
    }
}
