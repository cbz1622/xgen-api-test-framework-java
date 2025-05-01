package API.pageobjects;

import API.LocatorService;
import com.microsoft.playwright.Page;

import java.io.IOException;

public class InventoryPage extends BasePage {
    public InventoryPage(Page page, LocatorService locatorService) {
        super(page, locatorService);
    }

    public void sortPageBy(String sortingOption) throws IOException, InterruptedException {
        getLocatorWithContext("product_sort_container","SELECT")
                .selectOption(sortingOption);
    }

    public String returnSortedOptionForVerification() throws IOException, InterruptedException {
        return getLocator("inventory_item_price", "DIV")
                .textContent();
    }

    public void addItemToBasketMatchingSpecificValue(String... value) throws IOException, InterruptedException {
        getLocatorWithContext("add to cart", "BUTTON", value)
                .click();
    }

    public int countItems(String searchCriteria, String tagName) throws IOException, InterruptedException {
        return getLocators(searchCriteria,tagName).size();
    }
}
