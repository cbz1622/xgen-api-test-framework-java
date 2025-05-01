# xgen-api-test-framework-java
Automation Framework using the XGEN Smart Locator Api's from [TestDynamix.io](#https://www.testdynamix.io)

## Table of Contents
- [About](#about)
- [API references](#apireference)
- [Setup](#setup)

## About
- This automation test framework uses Playwright to interact with the [application under test](#https://www.saucedemo.com/v1/index.html)
- The framework adopts 'Page Object model' for the application under test but does not store any static element locators as like the traditional approach
  ```
  E.g
    Locator username = getLocator("username", "input");
    Locator password = getLocator("password", "input");
    Locator login = getLocator("login","input");
  ```
- The framework hits the end points of XGEN Smart locator System to get the locators by passing the keyword of the element and its tag name
  ```
  E.g
  A login button whose display name is "Login" will have the locator fetched through the getLocator("login", "button)
  The getLocator function is a wrapper around the API calls made to the Xgen Smart locator system
  ```

## Api references

```Host: https://www.testdynamix.io/api/<endpoint>```

    Endpoint: PUT /getLocator
    Description : Gets the locator for a specific search criteria
    Request Body (JSON): The current url and the pagecontents are sent along with the search criteria and tagname as parameters
        {
            "url" : "https://<weburl>"
            "pageSource" : <html/xml document>
        }
    
    Parameters: 
        searchCriteria : <String>
        tagname : <String>

    Request Example:
        'https://www.testdynamix.io/api/getLocator?tagName=input&searchCriteria=username'

    Response:
        {
            "<locatoridentifier>" : "<locator>"
        }

    Response Status(es):
        200 OK / 404 NOT FOUND
    
    Response Body Example:
        {
        "xpath" : "//div[2]/div[1]/div/div/form/input[contains(@class,'form_input') and contains(@type, 'text') and contains(@data-test, 'username') and contains(@id, 'user-name') and contains(@name, 'user-name') and contains(@placeholder, 'Username') and contains(@autocorrect, 'off') and contains(@autocapitalize, 'none') and contains(@value, '')]"
        }

    Endpoint: PUT /getLocators
    Description : Gets a list of locator for a specific search criteria
    Request Body (JSON): The current url and the pagecontents are sent along with the search criteria and tagname as parameters
        {
            "url" : "https://<weburl>"
            "pageSource" : <html/xml document>
        }
    
    Parameters: 
        searchCriteria : <String>
        tagname : <String>

    Request Example:
        'https://www.testdynamix.io/api/getLocators?searchCriteria=add+to+cart&tagName=BUTTON'

    Response:
    [
        {
            "<locatoridentifier>" : "<locator>"
        },
        {
            "<locatoridentifier>" : "<locator>"
        }
    ]
        

    Response Status:
        200 OK / 404 NOT FOUND
    
    Response Body Example:
    [ 
       {
        "xpath" : "//div[2]/div[1]/div/div/form/input[contains(@class,'form_input') and contains(@type, 'text') and contains(@data-test, 'username') and contains(@id, 'user-name') and contains(@name, 'user-name') and contains(@placeholder, 'Username') and contains(@autocorrect, 'off') and contains(@autocapitalize, 'none') and contains(@value, '')]"
       },
       {
        "xpath" : "//div/div[2]/div[2]/div/div[2]/div/div[2]/div[3]/button[contains(@class,'btn_primary btn_inventory') and contains(normalize-space(), 'ADD TO CART')]"
       }
    ]

    Endpoint: PUT /getLocatorWithContext
    Description : Gets the locator for a specific search criteria using realtive context
    Request Body (JSON): The current url and the pagecontents are sent along with the search criteria, tagname and optional realtive context as parameters
        {
            "url" : "https://<weburl>"
            "pageContent" : <html/xml document>
        }
    
    Parameters: 
        searchCriteria : <String>
        tagname : <String>
        relativeContext : <String>

    Request Example:
        Without relative context - 'https://www.testdynamix.io/api/getLocatorWithContext?searchCriteria=product_sort_container&tagName=SELECT'
        With relative context - 'https://www.testdynamix.io/api/getLocatorWithContext?searchCriteria=add to cart&tagName=BUTTON&relativeContext=%2449.99'

    Response:
        {
            "<locatoridentifier>" : "<locator>"
        }

    Response Status(es):
        200 OK / 404 NOT FOUND
    
    Response Body Example:
        {
         "xpath" : "//div/div[2]/div[2]/div/div[2]/div/div[6]/div[3]/button[contains(@class,'btn_primary btn_inventory') and contains(normalize-space(), 'ADD TO CART')]"
        }

    


