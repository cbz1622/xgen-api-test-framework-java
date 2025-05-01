package API.pageobjects;

import API.LocatorService;
import com.microsoft.playwright.Page;

public class AboutPage extends BasePage{
    AboutPage(Page page, LocatorService locatorService) {
        super(page, locatorService);
    }
}
