# Class diagram for the Core module

This page only shows this modules classes. For other diagrams, like modules, dependencies and user stories: [/diagrams](/diagrams)

```plantuml

package globingular.core {
	class Country [[java:globingular.core.Country]] {
		-String countryCode
		-String shortName
		-String longname
		-String sovereignty
		-String region
		-long population
		-Province[] provinces
		+Country(String countryCode, String shortName, String longname, String sovereignty, String region, long population, Province[] provinces)
		+Country(String countryCode, String shortName)
		+String getCountryCode()
		+String getShortName()
		+String getLongName()
		+String getSovereignty()
		+String getRegion()
		+long getPopulation()
		+String toString()
	}
	
	class CountryCollector [[java:globingular.core.CountryCollector]] {
		-SetProperty<Visit> visits
		-ReadOnlySetProperty<Visit> visitsReadOnly
		-SetProperty<Country> visitedCountries
		-ReadOnlySetProperty<Country> visitedCountriesReadOnly
		-SortedList<Country> visitedCountriesSorted
		-World world
		+CountryCollector(World world)
		+World getWorld()
		+void registerVisit(Country country)
		+void registerVisit(Country country, LocalDateTime arrival, LocalDateTime departure)
		+void registerVisit(Visit visit)
		+void removeAllVisitsToCountry(Country country)
		+void removeVisit(Visit visit)
		+Collection<Visit> getCountryVisits(Country country)
		+boolean isVisited(Country country)
		+ReadOnlySetProperty<Visit> visitsProperty()
		+Collection<Visit> getVisitsToCountry(Country country)
		+ObservableSet<Visit> getVisits()
		+ReadOnlySetProperty<Country> visitedCountriesProperty()
		+ObservableSet<Country> getVisitedCountries()
		+SortedList<Country> getVisitedCountriesSorted()
		+int numberVisited()
		+String toString()
	}
	
	class CustomBindings [[java:globingular.core.CustomBindings]] {
		-CustomBindings()
		+{static}SortedList<T> createSortedListView(ObservableSet<T> targetSet, Comparator<T> comparator)
	}
	
	class DuplicateIdentifierException [[java:globingular.core.DuplicateIdentifierException]] {
		+DuplicateIdentifierException()
		+DuplicateIdentifierException(String s)
		+DuplicateIdentifierException(String message, Throwable cause)
		+DuplicateIdentifierException(Throwable cause)
	}
	class IllegalArgumentException [[java:java.lang.IllegalArgumentException]] {
	}
	IllegalArgumentException <|-- DuplicateIdentifierException
	
	class Province [[java:globingular.core.Province]] {
		-String provinceCode
		-String capital
		-String name
		-long population
		+Province(String provinceCode, String capital, String name, long population)
		+String getProvinceCode()
		+String getName()
		+String getCapital()
		+long getPopulation()
	}
	
	class Visit [[java:globingular.core.Visit]] {
		-Country country
		-LocalDateTime arrival
		-LocalDateTime departure
		+Visit(Country country, LocalDateTime arrival, LocalDateTime departure)
		+Country getCountry()
		+LocalDateTime getArrival()
		+LocalDateTime getDeparture()
		+String toString()
	}
	
	class World [[java:globingular.core.World]] {
		-Set<Country> countries
		-HashMap<String,Country> countriesByCode
		-HashMap<String,Country> countriesByName
		+Country getCountryFromCode(String countryCode)
		+Country getCountryFromName(String countryName)
		+Set<Country> getCountries()
		+World(Country[] countries)
		+boolean countryExists(Country country)
	}
}

Country --> "provinces: *" Province
CountryCollector --> "world: 1" World
CountryCollector --> "visits: * \n visitsReadOnly: *" Visit
CountryCollector --> "visitedCountries: * \n visitedCountriesReadOnly: * \n visitedCountriesSorted: *" Country
World --> "countriesByCode: * \n countriesByName: *" Country
Visit --> "country: 1" Country

```