```plantuml
title <b>User story 1</b>\nEn bruker har vært på reise i Spania og ønsker loggføre besøket.\nHen skriver inn i appen «ES» for Spania og får besøket loggført.

actor User
User -> "~#countryInput: TextField" as countryInput: "ES"
User -> "~#countryAdd: Button" as countryAdd: *click*
countryAdd -> AppController: onCountryAdd()
AppController -> countryInput: getText()
return "ES"
AppController -> World: getCountryFromCode("ES")
return ~#Spain: Country
AppController -> World: getCountryFromName("ES")
return null
AppController -> CountryCollector: setVisited(#Spain: Country)
```


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
    component "controls" as javafx.controls {
    }
    component "swing" as javafx.swing {
    }
    component "base" as javafx.base {
    }
}

'ui ..> javafx
ui ..> javafx.fxml
ui ..> javafx.controls
ui ..> javafx.swing
core ..> javafx.base

component batik {
    component "transcoder" as batik.transcoder {
    }
    component "anim" as batik.anim {
    }
    component "util" as batik.util {
    }
    component "dom" as batik.dom {
    }
}

'ui ..> batik
ui ..> batik.transcoder
ui ..> batik.anim
ui ..> batik.util
ui ..> batik.dom
```