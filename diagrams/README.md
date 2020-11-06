# PlantUML diagrams

## Classes
The projects class diagrams can be found in each modules own Diagrams folder.
For example the Class-diagram for the Core module can be found in [globingular/core/diagrams/classes.md](globingular/core/diagrams/classes.mc).

## Modules and dependencies
The following diagram shows the different modules and packages in this project, as well as their dependencies between each other and to external dependencies:

```plantuml

component core {
	package globingular.core
}

component persistence {
	package globingular.persistence
}

component ui {
	package globingular.ui
}

component jackson {
    component "core" as jackson.core {
    }
    component "databind" as jackson.databind {
    }
}

persistence ..> jackson.core
persistence ..> jackson.databind



globingular.ui ..> globingular.core
globingular.ui ..> globingular.persistence

component javafx {
	component "fxml" as javafx.fxml {
    }
    component "web" as javafx.web {
    }
    component "base" as javafx.base {
    }
}

'ui ..> javafx
ui ..> javafx.fxml
ui ..> javafx.web
core ..> javafx.base

```

## User stories
The projects user stories, and their sequence diagrams can be found in [userStories](../userStories/README.md)
