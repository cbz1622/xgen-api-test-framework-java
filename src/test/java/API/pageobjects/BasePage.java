package API.pageobjects;

import API.LocatorService;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BasePage {
    private final LocatorService locatorService;
    private final Page page;

    BasePage(Page page, LocatorService locatorService) {
        this.page = page;
        this.locatorService = locatorService;
        this.page.waitForLoadState(LoadState.DOMCONTENTLOADED, new Page.WaitForLoadStateOptions().setTimeout(120000));
    }

    protected Locator getLocator(String searchCriteria, String tagName) throws IOException, InterruptedException {
        Map<String,String> params = Map.of(
                "searchCriteria", searchCriteria,
                "tagName",tagName
        );
        String queryParams = "?" + params.entrySet()
                .stream().map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        Map<String,String> locatorWithIdentifier = (Map<String, String>) locatorService.getLocatorWithIdentifier(this.page.url(),this.page.content(),queryParams);
        return findBy(locatorWithIdentifier);
    }

    protected List<Locator> getLocators(String searchCriteria, String tagName) throws IOException, InterruptedException {
        Map<String,String> params = Map.of(
                "searchCriteria", searchCriteria,
                "tagName",tagName
        );
        String queryParams = "?" + params.entrySet()
                .stream().map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        Object locatorWithIdentifier = locatorService.getLocatorsWithIdentifier(page.url(), page.content(), queryParams);
        if (locatorWithIdentifier instanceof List<?> rawList) {
            return (List<Locator>) rawList;
        }
        return Collections.emptyList();
    }

    protected Locator getLocatorWithContext(String searchCriteria, String tagName, String... relativeContext) throws IOException, InterruptedException {
        Map<String, String> params;
        if(relativeContext!= null && relativeContext.length > 0) {
            String encodedContext = Arrays.stream(relativeContext)
                    .map(value -> URLEncoder.encode(value, StandardCharsets.UTF_8))
                    .collect(Collectors.joining(","));

                params = Map.of(
                    "searchCriteria", searchCriteria,
                    "tagName", tagName,
                    "relativeContext", encodedContext
            );
        } else {
            params = Map.of(
                    "searchCriteria", searchCriteria,
                    "tagName", tagName
            );
        }
        String queryParams = "?" + params.entrySet()
                .stream().map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        Map<String,String> locatorWithIdentifier = (Map<String, String>) locatorService.getLocatorWithIdentifierUsingRelativeContext(this.page.url(), this.page.content(), queryParams);
        return findBy(locatorWithIdentifier);
    }

    private Locator findBy(Map<String, String> locatorMap) {
        for (Map.Entry<String, String> entry : locatorMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key.equalsIgnoreCase("xpath")) {
                return this.page.locator("xpath=" + value);
            } else if (key.equalsIgnoreCase("id")) {
                return this.page.locator("#" + value);
            } else {
                return this.page.locator(value);
            }
        }
        throw new IllegalArgumentException("No valid locator found");
    }
}
