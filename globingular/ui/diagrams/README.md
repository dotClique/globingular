# Class diagram for the UI module

This page only shows this modules classes. For other diagrams, like modules, dependencies and user stories: [/diagrams](/diagrams)

```plantuml

package globingular.ui {
	class App [[java:globingular.ui.App]] {
		+{static}int WINDOW_HEIGHT
		+{static}int WINDOW_WIDTH
		+void start(Stage primaryStage)
		+{static}void main(String[] args)
	}
	class Application [[java:com.apple.eawt.Application]] {
	}
	Application <|-- App
	
	
	class AppController [[java:globingular.ui.AppController]] {
		+{static}PseudoClass INVALID
		+{static}PseudoClass BLANK
		-{static}String MAP_ELEMENT_NAME
		-{static}String MAP_ELEMENT_ID
		-{static}String MAP_VISITED_COUNTRY_ATTRIBUTE
		-Parent root
		-ListView<Country> countriesList
		-TextField countryInput
		-Button countryAdd
		-Button countryDel
		-WebView webView
		-WebEngine webEngine
		-PersistenceHandler persistence
		-CountryCollector countryCollector
		-World world
		-Country inputCountry
		-Document document
		+AppController()
		+void initialize(URL location, ResourceBundle resources)
		+CountryCollector getCountryCollector()
		~void onCountryAdd()
		-void registerClickListenersOnExistingCountries()
		-void onInputChange()
		~void onCountryDel()
		-void setVisitedOnMap(Country country)
		-void setNotVisitedOnMap(Country country)
		-void setVisitedOnMapAll(Collection<Country> countries)
		-void setNotVisitedOnMapAll(Collection<Country> countries)
		-void initializeCountriesList()
		-Element getCountryMapElement(Country country)
	}
	interface Initializable [[java:org.glassfish.jersey.client.Initializable]] {
	}
	Initializable <|.. AppController
}

class "persistence.PersistenceHandler" as PersistenceHandler {
}

class "core.Country" as Country {
}

class "core.CountryCollector" as CountryCollector {
}

class "core.World" as World {
}

AppController --> "persistence: 1" PersistenceHandler
AppController --> "countryCollector: 1" CountryCollector
AppController --> "world: 1" World
AppController --> "inputCountry: 1 \n countriesList: *" Country

```