package API;

import API.pageobjects.InventoryPage;
import API.pageobjects.LoginPage;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class E2EPlayWrightAPITest {
    static Browser browser;
    static Page page;
    static BrowserContext browserContext;
    static Playwright playwright;

    static String rootUrl = "https://www.saucedemo.com/v1/";
    static String homePageUrl = rootUrl + "index.html";
    static String inventoryPageUrl = rootUrl + "inventory.html";
    static String cartPageUrl = rootUrl + "cart.html";
    static String checkOutStepOnePageUrl = rootUrl + "checkout-step-one.html";
    static String checkOutStepTwoPageUrl = rootUrl + "checkout-step-two.html";
    static String checkOutCompleteUrl = rootUrl + "checkout-complete.html";
    static String aboutPageUrl = "https://saucelabs.com/";
    static LocatorService locatorService;

    @BeforeAll
    public static void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        browserContext = browser.newContext();
        page = browserContext.newPage();
        locatorService = new LocatorService();
    }

    @AfterAll
    public static void tearDown() {
        // Close browser and Playwright instance
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @Test
    public void test_login_AddItem_Checkout_Complete() throws IOException, InterruptedException {
        page.navigate(rootUrl);
        LoginPage loginPage = new LoginPage(page,locatorService);

        /* validate for empty username */
        boolean usernameError = loginPage.emptyUserName("dhdh");
        Assertions.assertTrue(usernameError);

        /* validate for empty password */
        boolean passwordError = loginPage.emptyPassword("dhdhd");
        Assertions.assertTrue(passwordError);

        /* validate for incorrect credentials */
        boolean incorrectCredentialsError = loginPage.incorrectUsernamePassword("incorrectUName", "incorrectPwd");
        Assertions.assertTrue(incorrectCredentialsError);

        /* validate successful login & inventory page display */
        loginPage.login("standard_user","secret_sauce");
        Assertions.assertEquals(page.url(), inventoryPageUrl);

        /* validate page sorting */
        InventoryPage inventoryPage = new InventoryPage(page,locatorService);
        inventoryPage.sortPageBy("Price (low to high)");
        String sortedContent = inventoryPage.returnSortedOptionForVerification();
        Assertions.assertEquals("$7.99",sortedContent);

        /* add item matching specific price value */
        inventoryPage.addItemToBasketMatchingSpecificValue("$49.99");
        inventoryPage.addItemToBasketMatchingSpecificValue("15.99", "Test.all");

        Assertions.assertEquals(4, inventoryPage.countItems("add to cart", "BUTTON"));
        Assertions.assertEquals(2, inventoryPage.countItems("remove","BUTTON"));
    }
}