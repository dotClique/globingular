# Class diagram for the Core module

This page only shows this modules classes. For other diagrams, like modules, dependencies and user stories: [/diagrams](/diagrams)


## Diagram 1

This diagram shows how most of the classes in this module connects with each other. Further down are the remaining classes in a separate diagram.

`CountryCollector` is the main logic-class in the application, keeping track of which countries a user has visited. `CountryStatistics` relies on data from a `CountryCollector` to compute and produce statistics to show to a user. `GlobingularModule` connects `CountryCollector`s to a username, keeping track of multiple users at once. `Badges` relies on the `CountryCollector` to provide the user with badges they can complete by visiting countries, and computes a users progress towards these badges.

`World`, `Country` and `Visit` are mostly just dataclasses. They have some logic for validation, but mostly work as containers for data.

```plantuml
package globingular.core {
    class CountryCollector [[java:globingular.core.CountryCollector]] {
        -Set<Visit> visits
        -Set<Country> visitedCountries
        -Collection<Listener<Visit>> listeners
        +CountryCollector(World world)
        +World getWorld()
        +boolean registerVisit(Country country)
        +boolean registerVisit(Country country, LocalDate arrival, LocalDate departure)
        +boolean registerVisit(Visit visit)
        +boolean removeAllVisitsToCountry(Country country)
        +boolean removeVisit(Visit visit)
        +boolean isVisited(Country country)
        +Collection<Visit> getVisitsToCountry(Country country)
        +Set<Visit> getVisits()
        +Set<Country> getVisitedCountries()
        +int numberOfVisits()
        +int numberOfCountriesVisited()
        -void throwExceptionIfInvalidCountry(Country country)
        +String toString()
        +Collection<Listener<Visit>> getListeners()
        +void addListener(Listener<Visit> listener)
        +void removeListener(Listener<Visit> listener)
        -void notifyListeners(ChangeEvent<Visit> event)
    }
    class World [[java:globingular.core.World]] {
    }
    CountryCollector --> "1" World : world
    interface "Observable<Visit>" as Observable_Visit_ {
    }
    Observable_Visit_ <|.. CountryCollector
    class World [[java:globingular.core.World]] {
        -Set<Country> countries
        -String worldName
        -HashMap<String,Country> countriesByCode
        -HashMap<String,Country> countriesByName
        +Country getCountryFromCode(String countryCode)
        +Country getCountryFromName(String countryName)
        +Set<Country> getCountries()
        +String getWorldName()
        +World(Country[] countries)
        +World(String worldName, Country[] countries)
        +boolean countryExists(Country country)
    }
    class Country [[java:globingular.core.Country]] {
        -String countryCode
        -String shortName
        -String longname
        -String sovereignty
        -String region
        -long population
        +Country(String countryCode, String shortName, String longname, String sovereignty, String region, long population)
        +Country(String countryCode, String shortName)
        +String getCountryCode()
        +String getShortName()
        +String getLongName()
        +String getSovereignty()
        +String getRegion()
        +long getPopulation()
        +String toString()
        +int hashCode()
        +boolean equals(Object obj)
    }
    class Visit [[java:globingular.core.Visit]] {
        -LocalDate arrival
        -LocalDate departure
        +Visit(Country country, LocalDate arrival, LocalDate departure)
        +Country getCountry()
        +LocalDate getArrival()
        +LocalDate getDeparture()
        +String toString()
        +int hashCode()
        +boolean equals(Object obj)
        +{static}Visit newVisitFromWorld(Visit visit, World world)
        +{static}boolean isValidDateInterval(LocalDate arrival, LocalDate departure)
    }
    class Country [[java:globingular.core.Country]] {
    }
    Visit --> "1" Country : country
    class Badges [[java:globingular.core.Badges]] {
        +Badges(CountryCollector collector)
        +Double getNumberOfCountriesVisitedAsPercentageOfBadge()
        +Double getContinentBadge(String region)
        +Double getWorldPopulationBadge()
        +Map<String,String> getBadgeData()
    }
    class CountryCollector [[java:globingular.core.CountryCollector]] {
    }
    Badges --> "1" CountryCollector : collector
    class CountryStatistics [[java:globingular.core.CountryStatistics]] {
        -NumberFormat statisticFormat
        +CountryStatistics(CountryCollector countryCollector)
        +String getNumberOfVisitedCountries()
        +String getMostPopulatedVisitedCountry()
        +Long getNumberOfCountriesVisitedInRegion(String region)
        +Long getNumberOfCountriesVisitedThatStartWithLetter(char letter)
        +Map<String,String> getAllStatistics()
    }
    class CountryCollector [[java:globingular.core.CountryCollector]] {
    }
    CountryStatistics --> "1" CountryCollector : countryCollector
    class GlobingularModule [[java:globingular.core.GlobingularModule]] {
        -Map<String,CountryCollector> countryCollectorsByUsername
        +GlobingularModule()
        +CountryCollector getCountryCollector(String username)
        +boolean putCountryCollector(String username, CountryCollector countryCollector)
        +boolean removeCountryCollector(String username)
        +boolean isUsernameAvailable(String username)
        +{static}boolean isUsernameValid(String username)
    }
}


World --> "countries: * \n countriesByCode: * \n countriesByName: *" Country
Visit --> "country: 1" Country
GlobingularModule --> "countryCollectorsByUsername: *" CountryCollector
CountryStatistics --> "countryCollector: 1" CountryCollector
Badges --> "collector: 1" CountryCollector

CountryCollector --> "world: 1" World
CountryCollector --> "visits: * \n visitsReadOnly: *" Visit
CountryCollector --> "visitedCountries: * \n visitedCountriesReadOnly: * \n visitedCountriesSorted: *" Country
```


## Diagram 2

These are the remaining "helper" classes and interfaces implemented. `Observable` and `Listener` allows for implementation of a flexible observer-observable, using `ChangeEvent` for passing context from observable to observer. `DuplicateIdentifierException` is used as a custom exception if a `World` is created with multiple `Country`-instances with the same identifiers.

```plantuml
package globingular.core {
    class DuplicateIdentifierException [[java:globingular.core.DuplicateIdentifierException]] {
        -{static}long serialVersionUID
        +DuplicateIdentifierException()
        +DuplicateIdentifierException(String s)
        +DuplicateIdentifierException(String message, Throwable cause)
        +DuplicateIdentifierException(Throwable cause)
    }
    class IllegalArgumentException {
    }
    IllegalArgumentException <|-- DuplicateIdentifierException
    class "ChangeEvent<T>" as ChangeEvent_T_ [[java:globingular.core.ChangeEvent]] {
        -Status status
        -T element
        +ChangeEvent(Status status, T element)
        +boolean wasAdded()
        +boolean wasUpdated()
        +boolean wasRemoved()
        +T getElement()
        +boolean equals(Object o)
        +int hashCode()
    }
    interface "Listener<T>" as Listener_T_ [[java:globingular.core.Listener]] {
        void notifyListener(ChangeEvent<T> event)
    }
    interface "Observable<T>" as Observable_T_ [[java:globingular.core.Observable]] {
        Collection<Listener<T>> getListeners()
        void addListener(Listener<T> listener)
        void removeListener(Listener<T> listener)
    }
}
```
