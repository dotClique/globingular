# Globingular.UI

The UI module handles the graphical part of the desktop application. It is dependent upon Core for all its functionality. This is also the module that launches the desktop application, meaning that the main class is `globingular/ui/src/main/java/globingular/ui/App.java`.


## Diagrams

This page contains class-diagrams for the Core-module. For other diagrams see [/diagrams](/diagrams).


## Main classes and internal connections

This diagram shows how the classes in this module connects with each other. `AppController` is the main class, keeping track of all user interaction. It connects to the other modules where necessary, and connects to storage through the interface `GlobingularDataAccess` and its two implementations `LocalGlobingularDataAccess` and `RestGlobingularDataAccess`.

```plantuml
package globingular.ui {
    class App [[java:globingular.ui.App]] {
        +{static}int WINDOW_HEIGHT
        +{static}int WINDOW_WIDTH
        ~{static}String BASE_URI
        +void start(Stage primaryStage)
        +{static}void main(String[] args)
    }
    class Application [[java:jakarta.ws.rs.core.Application]] {
    }
    Application <|-- App
    class AppController [[java:globingular.ui.AppController]] {
        +{static}PseudoClass INVALID
        +{static}PseudoClass BLANK
        -{static}String MAP_ELEMENT_NAME
        -{static}String MAP_ELEMENT_ID
        -{static}String MAP_VISITED_COUNTRY_ATTRIBUTE
        -{static}String LOCAL_USER
        -ListView<Visit> visitsPopupListView
        -Parent visitsPopupRoot
        -Label visitsPopupCountryNameLabel
        -DatePicker arrivalDatePicker
        -DatePicker departureDatePicker
        -Parent root
        -ListView<Country> countriesList
        -TextField countryInput
        -Button countryAdd
        -Button countryDel
        -TextField userInput
        -WebView webView
        -HBox statistics
        -GridPane statisticsGrid
        -GridPane badgesGrid
        -WebEngine webEngine
        -PersistenceHandler persistence
        -CountryCollector countryCollector
        -World world
        -Country inputCountry
        -Country popupCountry
        -Document document
        -String currentUser
        -CountryStatistics countryStatistics
        -AutoCompletionBinding<String> countryInputAutoCompletionBinding
        -boolean initialized
        -Badges badges
        -{static}int TITLE_FONT_SIZE
        -{static}int TEXT_LABEL_PADDING
        -{static}int PROGRESSINDICATOR_PADDING
        -Popup visitsPopup
        -EventListener mapListenerForOpeningVisitsPopup
        -Listener<Visit> countryCollectorListenerForMapAndStatistics
        -Listener<Visit> countryCollectorListenerForSaving
        -Listener<Visit> popupVisitsSetListener
        +AppController()
        +void initialize(URL location, ResourceBundle resources)
        +CountryCollector getCountryCollector()
        -void onCountryAdd()
        -void registerClickListenersOnExistingCountries()
        -void onCountryInputChange()
        -void onUserInputChange()
        -void onCountryDel()
        -void setVisitedOnMap(Country country)
        -void setNotVisitedOnMap(Country country)
        -void setVisitedOnMapAll(Collection<Country> countries)
        -void setNotVisitedOnMapAll(Collection<Country> countries)
        -void updateStatistics()
        -Label createConfiguredLabel(String text)
        -void configureAutoComplete()
        -void initializeCountriesList()
        -Element getCountryMapElement(Country country)
        -void onChangeUserRequested()
        -void changeUser(String toUser)
        -void clearAppState()
        -void resetMapCountryElements()
        -void changeAppState(CountryCollector toCountryCollector)
        -void preInitConfigureState(CountryCollector toCountryCollector)
        -void postInitConfigureState(CountryCollector toCountryCollector)
        -void postMapLoadConfigureState(CountryCollector toCountryCollector)
        -ObservableList<Country> createSortedVisitedCountriesList(CountryCollector targetCountryCollector)
        -ObservableList<Visit> createObservableVisitsToCountryList(CountryCollector targetCountryCollector, Country filterCountry)
        -void onClickUserField(MouseEvent mouseEvent)
        -void requestRegisterVisit()
        -void removeVisit()
        -void validateVisitDates()
        -void popupVisits(Country country)
        -void popupVisitsForSelectedCountry()
        -void setupVisitsPopup()
        -void updateBadges()
        -ProgressIndicator getProgressIndicator(String num)
    }
    class GlobingularDataAccess [[java:globingular.ui.GlobingularDataAccess]] {
    }
    AppController --> "1" GlobingularDataAccess : dataAccess
    interface Initializable [[java:net.bytebuddy.dynamic.ClassFileLocator$AgentBased$ClassLoadingDelegate$ForDelegatingClassLoader$Dispatcher$Initializable]] {
    }
    Initializable <|.. AppController
    interface GlobingularDataAccess [[java:globingular.ui.GlobingularDataAccess]] {
        CountryCollector getCountryCollector()
        boolean saveCountryCollector(CountryCollector collector)
        boolean renameCountryCollector(String newUsername, CountryCollector collector)
        boolean deleteCountryCollector()
        boolean saveVisit(CountryCollector collector, Visit visit)
        boolean deleteVisit(CountryCollector collector, Visit visit)
    }
    class LocalGlobingularDataAccess [[java:globingular.ui.LocalGlobingularDataAccess]] {
        -PersistenceHandler persistenceHandler
        -String username
        +LocalGlobingularDataAccess(String username, PersistenceHandler persistenceHandler)
        +CountryCollector getCountryCollector()
        +boolean saveCountryCollector(CountryCollector collector)
        +boolean renameCountryCollector(String newUsername, CountryCollector collector)
        +boolean deleteCountryCollector()
        +boolean saveVisit(CountryCollector collector, Visit visit)
        +boolean deleteVisit(CountryCollector collector, Visit visit)
    }
    interface GlobingularDataAccess [[java:globingular.ui.GlobingularDataAccess]] {
    }
    GlobingularDataAccess <|.. LocalGlobingularDataAccess
    class RestGlobingularDataAccess [[java:globingular.ui.RestGlobingularDataAccess]] {
        +{static}String ACCEPT_HEADER_NAME
        +{static}String CONTENT_TYPE_HEADER_NAME
        +{static}String MEDIA_TYPE_JSON
        +{static}int HTTP_STATUS_CODE_NO_CONTENT
        +{static}int HTTP_STATUS_CODE_SUCCESS
        +{static}String HTTP_METHOD_DELETE
        +{static}String HTTP_METHOD_GET
        +{static}String HTTP_METHOD_PUT
        +{static}String HTTP_METHOD_POST
        -{static}String GLOBINGULAR_SERVICE_PATH
        -{static}String COUNTRY_COLLECTOR_RESOURCE_PATH
        -{static}String COUNTRY_COLLECTOR_RESOURCE_ACTION_RENAME
        -{static}String VISIT_RESOURCE_PATH
        -{static}String VISIT_RESOURCE_PATH_ACTION_REGISTER
        -{static}String VISIT_RESOURCE_PATH_ACTION_REMOVE
        -String username
        -String baseUri
        -PersistenceHandler persistenceHandler
        -HttpClient client
        +RestGlobingularDataAccess(String baseUri, String username, PersistenceHandler persistenceHandler)
        +CountryCollector getCountryCollector()
        +boolean saveCountryCollector(CountryCollector collector)
        +boolean renameCountryCollector(String newUsername, CountryCollector collector)
        +boolean deleteCountryCollector()
        +boolean saveVisit(CountryCollector collector, Visit visit)
        +boolean deleteVisit(CountryCollector collector, Visit visit)
        -Boolean executeRequest(String method, String uri, Object parameter)
        -T executeRequest(String method, String uri, Object parameter, Class<T> returnType, T defaultReturn, int[] acceptableStatusCodes)
    }
    interface GlobingularDataAccess [[java:globingular.ui.GlobingularDataAccess]] {
    }
    GlobingularDataAccess <|.. RestGlobingularDataAccess
}
```


## External connections and dependencies

This diagram shows how classes within UI connects to the dependency-classes from `globingular.persistence` and `globingular.core`. 

```plantuml
class "ui.AppController" as AppController {
    -PersistenceHandler persistence
    -Country inputCountry
    -ListView<Visit> visitsPopupListView
    -ListView<Country> countriesList
    -World world
    -Country popupCountry
    -GlobingularDataAccess dataAccess
    -CountryStatistics countryStatistics
    -Badges badges
}

class "persistence.PersistenceHandler" as PersistenceHandler {
}

class "core.Country" as Country {
}

class "core.CountryCollector" as CountryCollector {
}

class "core.World" as World {
}

class "core.Badges" as Badges {
}

class "core.Visit" as Visit {
}

class "core.CountryStatistics" as CountryStatistics {
}

class "ui.RestGlobingularDataAccess" as RestGlobingularDataAccess {
	-PersistenceHandler persistenceHandler
}
class "ui.LocalGlobingularDataAccess" as LocalGlobingularDataAccess {
	-PersistenceHandler persistenceHandler
}

AppController --> "persistence: 1" PersistenceHandler
AppController --> "countryCollector: 1" CountryCollector
AppController --> "world: 1" World
AppController --> "inputCountry: 1 \n countriesList: * \n popupCountry: 1" Country

AppController --> "visitsPopupListView" Visit


AppController --> "countryStatistics: 1" CountryStatistics
AppController --> "badges: 1" Badges

RestGlobingularDataAccess --> "persistenceHandler: 1" PersistenceHandler
LocalGlobingularDataAccess --> "persistenceHandler: 1" PersistenceHandler
```
