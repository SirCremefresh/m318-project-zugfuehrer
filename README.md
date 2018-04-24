# m318-project-zugfuehrer
## 1 Inhaltsverzeichnis
* [2 Projekt Informationen](#information) 
* [3 Konzeptionierung](#concept)  
  * [3.1 Coderichtlinie](#code) 
  * [3.2 Mockup](#mockup)  
  * [3.3 Validierung](#validation)  
  * [3.4 Testfälle](#test) 
  * [3.5 Use Case](#usecase)  
  * [3.6 Aktivitätendiagramm](#activity) 
* [4 Umsetzung](#implementation)  
  * [4.1 Funktionen](#features)  
  * [4.2 Bugs und Fehler](#bugs)  
  * [4.3 Screenshot](#screenshot) 
* [5 Installation](#install)  
* [6 Deinstallation](#deinstall)  

<a name="information"/>

# 2 Projekt Informationen
## 2.1 Autor und Dokument
* Autor: **Donato Wolfisberg**
* Erstelldatum: **24.04.2018**
* letzte Aktualisierung: **24.04.2018**

## 2.2 Projekt
* Projekt-Start: **18.04.2018**   
* Projekt-Ende: **24.04.2018** 
* Quellcode + Dokumentation:  **[Github Repo](https://github.com/SirCremefresh/m318-project-zugfuehrer)**

## 2.3 Einleitung
Dies ist ein Projekt für das modul m318. In diesem geht es um die erstellung einer desktop applikation. 
Mit dieser Applikation kann man z.B. Verbindungen von ort zu ort suchen oder sich Stationen in der nähe anzeigen lassen.
Die daten werden über eine Api geladen die von der **[Open Knowledge Foundation](https://opendata.ch/)** zur verfügung gestellt wird.
Das gui wurde mit JavaFx erstellt. Der Gesamte Quellcode und die Dokumentation sind auf **[Github](https://github.com/SirCremefresh/m318-project-zugfuehrer)** zu finden.

## 2.4 Dokumentation
Die Dokumentation ist dafür dah dass wenn jemand lust hat an dieser Projekt weiterzuarbeiten,
dass es dieser person einfacher geht zu starten. Es beinhaltet auch eine installations anleitung.

## 2.5 Anforderungen
### 2.5.1 Priorität 1
- [x] 01 Start- und Endstationen mittels Eingabefeld suchen
- [x] 02 Die nächste vier bis fünf Verbindungen zwischen angegebener Start- und Endstationen sehen
- [x] 03 Verbindungen ab einer bestimmten Station anzeigen

### 2.5.2 Priorität 2
- [x] 04 Während der Eingabe einer Station bereits Suchresultate erhalten
- [x] 05 Verbindungen an beliebigem Datum anuzeigen

### 2.5.3 Priorität 3
- [x] 06 Ort einer Station anzeigen (Maps)
- [x] 07 Stationen in meiner Nähe finden
- [x] 08 Gefundene Resultate per Mail verschicken

<a name="concept"/>

# 3 Konzeptionierung
Das Gui Wird mit JavaFx entwickelt alle web calls werden in service ausgelagert.

<a name="code"/>

## 3.1 Coderichtlinien
### 3.1.1 Namensgebung
Alle Variablen namen und Methoden namen werden camelCase geschrieben.  
Klassen werden mit PascalCase Geschrieben.

```java
public class SomeClassInPascalCase {
    public void someMethodNameInCamelCase() {
	    int someVariableInCamelCase;
    }
}
```


### 3.1.2 Methoden, Schleifen, Verzweigungen, Try Catch
Die geschweiften klammern werden immer auf der gleichen linie geschrieben.

```java
if (a) {
	b = false;
} else {
	c = true;
}

for (int i = 0; i < 10; i++) {
	b = true
} 

```

### 3.1.3 Kommentare
Es wird grundsätzlich nichts kommentiert außer wenn es eine fix gibt der nicht ersichtlich ist.

<a name="mockup"/>

## 3.2 Mockup
Die Applikation sieht jetzt nicht mehr so aus wie es auf den Mockups ersichtlich ist.   
Dah weitere funktionen und seiten hinzugekommen sind.

### 3.2.1 Verbindung's plan

Auf dieser seite kann man einen abfahrt's ort und eine ankunft's ort eingeben.
Es werden vorschläge von orten unterhalb der text-feldern angezeigt.
In der Liste werden dan die nächsten verbindungen angezeigt.

![Timetable-mockup](/img/Timetable-mockup.png)

### 3.2.2 Abfahrt's tafel

Man kann die gewünschte Station eingeben. 
Es werden vorschläge von orten unterhalb der text-feldern angezeigt.
In der Liste werden dan die nächsten verbindungen von dieser station aus angezeigt.

![DepartureBoard-mockup](/img/DepartureBoard-mockup.png)


<a name="validation"/>

## 3.3 Validierung
* **Stationen** Bei der eingabe bekommt der benutzer direkt vorschlage von denen er auswählen kann.
Die eingabe darf nicht lehr sein wenn.

* **Datum** das datum darf irgend ein gültiges datum sein.

* **Zeit** Die Zeit wird soo validiert dass nur zahlen eingegeben werden dürfen und dass stunden und
minuten jeh nur 2 zahlen lang sind.

<a name="test"/>

## 3.4 Testfälle
### 3.4.1 Station suchen
**Beschreibung:** Nach verbindungen zwischen zwei station suchen

**Anforderung:** A001 & A002 & A004 & A005 & A006 & A008

**Test-Szenario:**

| Schritt | Aktivität                                                     | Erwartetes Resultat                                      |
| ------- | ------------------------------------------------------------- | -------------------------------------------------------- | 
| 1       | Ich gebe in dass feld "From" "Lu" ein | Das oberste resultat im dropdown is "Luzern" und das zweit oberste ist "Luzern, Bahnhof"  |
| 2       | Ich gebe in das feld "To" "Zürich" ein | Wehrend der eingabe aktualisiert sich das dropdown immer mit stationen die die aktuelle eingabe im wort beinhalten. |
| 3       | Ich klicke auf den button "show location" | es öffnet sich ein web browser bei dem ein marker beim Zürich HB ist |
| 4       | Ich gebe im feld "Hour" "15" ein | es verändert sich sonst nichts |
| 5       | Ich klicke auf den button "search" | Es werden nun 4 verbindungen in der liste angezeigt. Alle verbindungen haben eine ankunft's zeit nach 15 uhr |
| 6       | Ich klicke auf den button "share Connections" | Es wird nun ein mail programm geöffnet wo man die gleichen verbindungen wie vorher in der liste jetzt in der nachricht stehen |


<a name="usecase"/>

## 3.5 Use Case

<a name="activity"/>

## 3.6 Aktivitätendiagramm

<a name="implementation"/>

# 4 Umsetzung

<a name="features"/>

## 4.1 Funktionen
| Anforderung | Priorität | Status | Beschreibung                                                                                                                                                                                                                                                       |
| ----------- | --------- | ------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | 
| A01         | 1         | ✔      | Als ÖV-Benutzer möchte ich Start- und Endstation mittels Textsuche suchen können, damit ich nicht alle Stationsnamen auswendig lernen muss.                                                                                                                        | 
| A02         | 1         | ✔      | Als ÖV-Benutzer möchte ich die aktuellen, d.h. mindestens die nächsten vier bis fünf Verbindungen zwischen den beiden gefundenen und ausgewählten Stationen sehen, damit ich weiss wann ich zur Station muss, um den für mich idealen Anschluss zu erwischen.      | 
| A03         | 1         | ✔      | Als ÖV-Benutzer möchte ich sehen, welche Verbindungen ab einer bestimmten Station vorhanden sind, damit ich bei mir zuhause eine Art Abfahrtstafel haben kann.                                                                                                     | 
| A04         | 2         | ✔      | Als ÖV-Benutzer möchte ich, dass schon während meiner Eingabe erste SuchResultate erscheinen, damit ich effizienter nach Stationen suchen kann.                                                                                                                    | 
| A05         | 2         | ✔      | ALs ÖV-Benutzer möchte ich nicht nur aktuelle Verbindungen suchen können, sondern auch solche zu einem beliebigen anderen Zeitpunkt, damit ich zukünftige Reisen planen kann.                                                                                      |
| A06         | 3         | ✔      | Als ÖV-Benutzer möchte ich sehen, wo sich eine Station befindet, damit ich mir besser vorstellen kann, wie die Situation vor Ort aussieht.                                                                                                                         |  
| A07         | 3         | ✔      | Als ÖV-Benutzer möchte Stationen finden, die sich ganz in der Nähe meiner aktuellen Position befinden, damit ich schnell einen Anschluss erreichen kann.                                                                                                           |  
| A08         | 3         | ✔      | Ich möchte meine gefundenen Resultate via Mail weiterleiten können, damit auch andere von meinen Recherchen profitieren können.                                                                                                                                    |  


### 4.1.1 zusätzliche Funktionen
| Anforderung | Priorität | Status | Beschreibung                                                                                                                                                                                                                                                       |
| ----------- | --------- | ------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | 
| Z01         | -         | ✔      | Mann hat einen knopf auf der Verbindung's plan Seite wo man die zeit auf die aktuelle zeit zurück stellen kann.                                                                                                                        | 


<a name="bugs"/>

## 4.2 🐛 Bugs und Fehler

| Bug Nr |                                             Beschreibung                                                       |
| ------ | -------------------------------------------------------------------------------------------------------------- |
|  001   | Es kann vorkommen dass wen man mit der maus zu schnell auf ein autocomplete klickt dass es selektiert wird.    |
|  002   | Die resultate vom Autocomplete werden synchron geladen daher ist dass tippen sehr langsam.                     |
|  003   | Der Standort wird über die öffentliche Ip-Adresse ermittelt, daher ist er sehr ungenau.                       |




<a name="install"/>

# 6 Installation

Man kann einfach auf das gegebene .jar file doppelklicken. die Anwendung wird dann sogleich gestartet.

<a name="deinstall"/>

# 6 Deinstallation

Einfach das .jar file in den Papierkorb ziehen. 
Es werden keine daten im hintergrund abgelegt.

