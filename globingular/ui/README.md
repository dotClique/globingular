# Globingular.UI
The UI module handles the graphical part of the desktop application. It is dependent upon Core for all its functionality. This is also the module that launches the desktop application, meaning that the main class is `globingular/ui/src/main/java/globingular/ui/App.java`.

## App
This main class' only task is to start the JavaFX application, with the specified windows size, title and icon.

## AppController
This class handles all interaction between the user, through the UI, and the "business logic" in the back-end.
