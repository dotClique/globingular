[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-darkred?logo=gitpod)](https://gitpod.idi.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2020/gr2002/gr2002)

# Globingular

The project is contained within the `globingular`-directory, and is separated into three separate modules: Core, Persistence and UI.

## Module descriptions

### Core module
The Core module contains the main logic of the application, and is the central part of both the current dekstop and future server setup.

### Persistence module
The Persistence module handles all things related to saving and loading of the application-state to and from file and/or database. It's separate from Core in order to interchangeably use different back-ends for storing state.

### UI module
The UI module handles the graphical part of the desktop application. It is dependent upon Core for all its functionality. This is also the module that launches the desktop application, meaning that the main class is `globingular/ui/src/main/java/globingular/ui/App.java`.

## Testing and coverage reports
The project uses Checkstyle to enforce styling guidelines, Spotbugs to check for overlooked bugs and Jacoco to check test coverage. Auto-generated reports can be viewed by running `mvn site` in the project directory, and viewing `target/staging/project-reports.html`. A more complete report can be viewed by running `mvn site` and viewing `target/staging`. It can be run as a local web server by executing `mvn site:run` afterwards.
WARNING: Spotbugs cannot be aggregated, so any errors are only visible on the page for each individual module, for example `target/staging/core/project-reports.html`.
WARNING: Jacoco aggregated coverage reports only be viewed in `target/staging/aggregator/project-reports.html`.

## Build and launch
In order to build and install the project run `mvn install` in the project folder. This must be done before launching the application, by using `mvn javafx:run -pl ui`. You will need to recompile and install for every code change, as the code is being run from the local Maven repository. To speed up this process you can use our handy shortcut: `mvn install -Dfast`, which effectively just runs `mvn install -DskipTests '-Djacoco.skip' '-Dcheckstyle.skip' '-spotbugs.skip'`. If omitted, `-Dfast` will be set to `false`, overwriting any of these values passed through the command line.

To launch the project in 'one line', run: `mvn install; mvn javafx:run -pl ui`.
