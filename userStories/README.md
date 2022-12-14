# User Stories

For the development of this app we have used the following 9 user stories. All of them are described with a short text. Some are then described with a short *"how to"*, while some have a sequence-diagram to show the apps behavior.


## User Story 1

A user has visited Spain and wish to log their visit. They enter Spains country code «ES» in the app and gets their visit logged.

```plantuml

actor User
User -> "~#countryInput: TextField" as countryInput: "ES"
User -> "~#countryAdd: Button" as countryAdd: *click*
countryAdd -> AppController: onCountryAdd()
AppController -> countryInput: getText()
return "ES"
AppController -> World: getCountryFromCode("ES")
return ~#Spain: Country
AppController -> World: getCountryFromName("ES")
return null
AppController -> CountryCollector: registerVisit(#Spain: Country)
CountryCollector -> CountryCollector: registerVisit(#Spain: Country, null, null)
CountryCollector -> Visit: new Visit(#Spain: Country, null, null)
Visit --> CountryCollector: ~#Spain00: Visit
CountryCollector -> CountryCollector: registerVisit(#Spain00: Visit)
CountryCollector -> CountryCollector: throwExceptionIfInvalidCountry(#Spain: Country)
CountryCollector -> AppController: *notifyListener: setColor(#Spain: Country, Colors.COUNTRY_VISITED)*
AppController --> User: *Map updated with colored countries*

```


## User Story 2

User wants to see which date they first visited every country. They add a visit to France with a start date for the first visit. This is then visible to the user in the form of a list over visited countries.

- Click France on the map, add start and end date, press add (+), this will then show up in the listView on the right hand side in the UI.


## User Story 3

A user has been travelling through Andorra on their way from France to Spain, and wants to log the short visit to Andorra. They add the visit with a date-range (start / end). This requires that several visits can be registered on each country.

- Same as User Story 2.


## User Story 4

User has been in Italy on holiday. They open the app and click on Italy on the map to tag it as visited.

- This has since been expanded. The user now gets a dialog to set their arrival and departure time.

```plantuml

actor User
User -> "~#webView: WebView" as webView: *click on Italy*
webView -> AppController: *notifyListener("IT")*
AppController -> World: getCountryFromCode("IT")
World --> AppController: ~#Italy: Country
AppController -> AppController: popupVisits(#Italy: Country)
AppController --> User: *Display popup*
User -> Appcontroller: "~#addVisitButton: Button" as addVisitButton: *click on '+'*
addVisitButton -> AppController: requestRegisterVisit()
AppController -> CountryCollector: registerVisit(#Italy: Country, null, null)
CountryCollector -> Visit: new Visit(#Italy: Country, null, null)
Visit --> CountryCollector: ~#Italy00: Visit
CountryCollector -> CountryCollector: registerVisit(#Italy00: Visit)
CountryCollector -> CountryCollector: throwExceptionIfInvalidCountry(#Italy: Country)
CountryCollector --> webView: *notifyListener*
webView --> User: *Map updated with colored countries*

```


## User Story 5

A user went to Azerbaijan, but doesn't know how to spell it. They enter the first few letters and choose Azerbaijan from a drop-down list of suggestions.

```plantuml

actor User
User -> "~#countryInput: TextField" as countryInput: "Azer"
User -> "~#suggestions: TextFields" as suggestions: *click*
suggestions -> countryInput: "Azerbaijan"

```


## User Story 6

A user gets statistics on how many countries they've visited on each continent.

```plantuml

actor User
User -> "~#tab: Statistics" as tab: *click on Statistics*

```


## User Story 7

Upon visiting every country in Europe, the user gets a badge-popup.

- User adds last country in Europe either by clicking on it or writing it in the inputField.
- Badge is then completed, so popup is displayed in lower right corner of UI.


## User Story 8

The user can open a view showing all their achieved badges (and progress on new badges). 

- This has part of the Badge-system has not been implemented



## User Story 9

A user opens the app on two separate devices, and retains their data by using a username (on a server-backend).

- User clicks on Change button. They write their username in the textField, before pressing Change button again to confirm the user change.
- If the user is a new user, an empty map and listView will appear.
- If the user is an existing one, the state for this user will be retrieved from the server and displayed to the user in the UI.
