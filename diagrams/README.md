# PlantUML diagrams

## Classes

The projects class diagrams are split into modules, and can be found within each modules' README:
- [/globingular/core](/globingular/core)
- [/globingular/persistence](/globingular/persistence)
- [/globingular/ui](/globingular/ui)
- [/globingular/restapi](/globingular/restapi)
- [/globingular/restserver](/globingular/restserver)


## User stories
The projects user stories, and their sequence diagrams can be found in [/userStories](/userStories).


## Modules and dependencies
The following diagram shows the different modules and packages in this project, as well as their dependencies between each other and to external dependencies:

```plantuml


component globingular {
    component core {
    }
    component ui {
    }
    component persistence {
    }
    component restapi {
    }
    component restserver {
    }
}

component jackson {
    component "core" as jackson.core {
    }
    component "databind" as jackson.databind {
    }
}

component javafx {
	component "fxml" as javafx.fxml {
    }
    component "web" as javafx.web {
    }
}

component controlsfx {
    component "controls" as controlsfx.controls {
    }
}

component java {
    component "desktop" as java.desktop {
    }
    component "net.http" as java.net.http {
    }
}

component jakarta {
    component "ws.rs" as jakarta.ws.rs {
    }
    component "inject" as jakarta.inject {
    }
}

component grizzly {
    component "http.server" as grizzly.http.server {
    }
    component "framework" as grizzly.framework {
    }
}

component jersey {
    component "server" as jersey.server {
    }
    component "container.grizzly2.http" as jersey.container.grizzly2.http {
    }
    component "media.json.jackson" as jersey.media.json.jackson {
    }
    component "common" as jersey.common {
    }
}

ui ..> core
ui ..> persistence
ui ..> javafx
'ui ..> javafx.fxml
'ui ..> javafx.web
ui ..> controlsfx.controls
ui ..> java
'ui ..> java.desktop
'ui ..> java.net.http

restapi ..> jakarta
'restapi ..> jakarta.ws.rs
'restapi ..> jakarta.inject
restapi ..> persistence

restserver ..> grizzly
'restserver ..> grizzly.http.server
'restserver ..> grizzly.framework
restserver ..> jackson.databind
restserver ..> jersey
'restserver ..> jersey.server
'restserver ..> jersey.container.grizzly2.http
'restserver ..> jersey.media.json.jackson
'restserver ..> jersey.common
restserver ..> persistence
restserver ..> restapi
restserver ..> jakarta.ws.rs

persistence ..> core
persistence ..> jackson
'persistence ..> jackson.core
'persistence ..> jackson.databind

```
