# User Stories

Below are descriptions and sequence diagrams for the user stories implemented in this app.


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
AppController -> CountryCollector: setVisited(#Spain: Country)
CountryCollector -> AppController: *notifyListener: setColor(#Spain: Country, Colors.COUNTRY_VISITED)*
AppController --> User: *Map updated with colored countries*

```


## User Story 2

User wants to see which date they first visited every country. They add a visit to France with a start date for the first visit. This is then visible to the user in the form of a list over visited countries.


## User Story 3

A user has been travelling through Andorra on their way from France to Spain, and wants to log the short visit to Andorra. They add the visit with a datetime-range (start / end). This requires that several visits can be registered on each country.


## User Story 4

User has been in Italy on holiday. They open the app and click on Italy on the map to tag it as visited.

```plantuml

actor User
User -> "~#webView: WebView" as webView: *click on Italy*
webView -> AppController: *notifyListener(#Italy: Country)*
AppController -> CountryCollector: isVisited(#Italy: Country)
CountryCollector --> AppController: false
AppController -> CountryCollector: registerVisit(#Italy: Country)
CountryCollector --> webView: *notifyListener*
webView --> User: *Map updated with colored countries*

```


## User Story 5

A user went to Azerbaijan, but doesn't know how to spell it. They enter the first few letters and choose Azerbaijan from a drop-down list of suggestions.
```plantuml

actor User
User -> "~#countryInput: TextField" as countryInput: "Azer"
User -> "~#suggestions: TextFields" as suggestions: *click*
User -> "~#countryInput: TextField" as countryInput: "Azerbaijan"
User -> "~#countryAdd: Button" as countryAdd: *click*
countryAdd -> AppController: onCountryAdd()
AppController -> countryInput: getText()
return "Azerbaijan"
AppController -> World: getCountryFromCode("AZ")
return ~#Azerbaijan: Country
AppController -> World: getCountryFromName("Azerbaijan")
return null
AppController -> CountryCollector: setVisited(#Azerbaijan: Country)
CountryCollector -> AppController: *notifyListener: setColor(#Azerbaijan: Country, Colors.COUNTRY_VISITED)*
AppController --> User: *Map updated with colored countries*
```


## User Story 6

A user gets statistics on how many countries they've visited on each continent.
```plantuml

actor User
User -> "~#tab: Statistics" as tab: *click on Statistics*

```


## User Story 7

Upon visiting every country in Europe, the user gets a badge-popup.


## User Story 8

The user can open a view showing all their achieved badges (and progress on new badges). 


## User Story 9

A user opens the app on two separate devices, and retains their data by using a username (on a server-backend).