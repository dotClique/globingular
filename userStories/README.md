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
