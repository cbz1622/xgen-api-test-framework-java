package API.pageobjects;

import API.LocatorService;
import com.microsoft.playwright.Page;

public class CartPage extends BasePage{
    CartPage(Page page, LocatorService locatorService) {
        super(page, locatorService);
    }
}
