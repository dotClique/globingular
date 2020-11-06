# Globingular.Core
The Core module contains the main logic of the application, and is the central part of both the dekstop and server setups.

## CountryCollector
The main class is the `CountryCollector`-class. It keeps track of all country visits a user has logged.

## World, Country and Visit
`World`, `Country` and `Visit` are container-classes. A `World`-object contains a collection of `Country`-objects, each representing a visitable country in the world. A `Visit`-object describes a users visit to a `Country`, with their arrival and departure times and dates.

## CustomBindings
A custom class to customize handling of JavaFX SetProperties.
