[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-darkred?logo=gitpod)](https://gitpod.idi.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2020/gr2002/gr2002)

# Globingular

The project is contained within the `globingular`-directory, and is separated into three separate modules: Core, Persistence and UI.

## Diagrams
Class-, Module- and Sequence-diagrams can be found in the [Diagrams-folder](diagrams).

## Module descriptions

### Core module
The Core module contains the main logic of the application, and is the central part of both the current dekstop and future server setup.

### Persistence module
The Persistence module handles all things related to saving and loading of the application-state to and from file and/or database. It's separate from Core in order to interchangeably use different back-ends for storing state. 
The app uses an app model of persistence, meaning that any changes are implicitly saved in the background and loaded on restart.
This makes more sense for such a simple record-keeping app, as users are unlikely to make many changes at once.
It would be useful to also be able to take a snapshot for backup purposes, and this is expected for future development, however it is currently not a priority.

### UI module
The UI module handles the graphical part of the desktop application. It is dependent upon Core for all its functionality. This is also the module that launches the desktop application, meaning that the main class is `globingular/ui/src/main/java/globingular/ui/App.java`.

## Testing and coverage reports
The project uses Checkstyle to enforce styling guidelines, Spotbugs to check for overlooked bugs and Jacoco to check test coverage. Auto-generated reports can be viewed by running `mvn site` in the project directory, and viewing `target/staging/index.html`. From there you can browse into the individual modules' subfolder and reports. It can be run as a local web server by executing `mvn site:run` afterwards.
For the master branch, the latest reports site is automatically deployed [here](http://it1901.pages.stud.idi.ntnu.no/groups-2020/gr2002/gr2002/).

### Aggregated reports
Certain reports are automatically aggregated and put into `target/staging/project-reports.html`.

**Jacoco** is aggregated and a report is saved in a separate 'aggregator'-module. It can be viewed there: `target/staging/aggregator/project-reports.html`.

**Surefire tests** are aggregated *only* if the reports already exist when `site` is run.
To get the aggregated test results without running the tests twice, use
`mvn verify -fn; mvn site -DskipTests`. The aggregate report can then be viewed in `target/staging/project-reports.html`.

**Spotbugs** cannot be aggregated, so any errors are only visible on the page for each individual module, for example `target/staging/core/project-reports.html`, or when running `mvn verify`.

## Build and launch
In order to build and install the project run `mvn install` in the project folder. This must be done before launching the application, by using `mvn javafx:run -pl ui`. You will need to recompile and install for every code change, as the code is being run from the local Maven repository. To speed up this process you can use our handy shortcut: `mvn install -Dfast`, which effectively just runs `mvn install -DskipTests '-Djacoco.skip' '-Dcheckstyle.skip' '-spotbugs.skip'`. If omitted, `-Dfast` will be set to `false`, overwriting any of these values passed through the command line.

To launch the project in 'one line', run: `mvn install; mvn javafx:run -pl ui`.

## Data sources
Country data & SVG World Map: [raphaellepuschitz/SVG-World-Map.](https://github.com/raphaellepuschitz/SVG-World-Map)