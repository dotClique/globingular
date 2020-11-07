# Globingular.Persistence
The Persistence module handles all things related to saving and loading of the application-state to and from file and/or database. It's separate from Core in order to interchangeably use different back-ends for storing state. 
The app uses an app model of persistence, meaning that any changes are implicitly saved in the background and loaded on restart.
This makes more sense for such a simple record-keeping app, as users are unlikely to make many changes at once.
It would be useful to also be able to take a snapshot for backup purposes, and this is expected for future development, however it is currently not a priority.

## PersistenceHanlder
This is the class handling writing to and from filestore for handling persistence of the application.

## CountryCollectorModule
Creates a custom JacksonDatabind Module for custom serialization and deserialization to and from JSON. Registers all of the custom (de)serializers in the module.

## ...Serializer
An implementation of Jacksons JsonSerializer for serializing ... instances to JSON (... referes to the prefix in the filename).

## ...Deserializer
An implementation of JAcksons JsonDeserializer for deserializing ... instances from JSON (... referes to the prefix in the filename).
