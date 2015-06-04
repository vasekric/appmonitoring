Autor Richard Vašek VAS0122

Projekt používá Apache Maven pro řešení závislostí na knihovnách, dále je použit Spring Boot a jeho JDBC.
Dále je použit oracle ojdbc6, který není dostupný v Maven central repository a proto je přidán repositář, jenž ho obsahuje.
Výpis všech dependencí je k nalezení v souboru pom.xml
Pro spouštění se používá embedded Tomcat, který je součástí Spring Boot, aplikace se kompiluje do .jar souboru,
který je spustitelný z konzole "java -jar appmonitoring-0.1.jar", pro spuštění je potřeba JDK8
Kompilace probíhá pomocí Mavenu. Dá se použít například Eclipse s Maven pluginem nebo Spring Tool Suite, což je Eclipse obohacený o vše potřebné.
Projekt se dá kompilovat z konzole, stačí do OS nainstalovat Apache Maven.
Maven je stažitelný zde: https://maven.apache.org/download.cgi Je potřeba jej pak přidat na PATH, návod na instalaci je na stejné stránce.
Pro kompilaci projektu stačí najet do složky appmonitoring a spustit v konzoli příkaz "mvn package".
Maven si stáhne potřebné knihovny a projekt zkompiluje do složky target. Občas se může stát, že nastane chyba, většinou kvůli problému s připojením na nějaký server s maven reposirářem, stačí příkaz spustit znovu.
Po spuštění se do konzole vypíšou INFO logy ze spouštění aplikace, poté System.out.println z metod s @PostConstruct anotací a nakonec další INFO logy dokončení spouštění aplikace.


SQL skripty pro vytvoření tabulek, procedur a naplnění daty se nachází v src/main/resources/sql
Údaje pro připojení k databázi jsou v src/main/resources/application.properties
V balíčku edu.vsb.dais.appmonitoring je třída pro spouštění aplikace Application.java
v tomtéž balíčku je třída SnapshotGenerator pro generování dat do tabulky Snapshot zaběhu aplikace
dále třída DatabaseLayerTest, která obsahuje volání funkcí databázové vrstvy, pro spuštění je potřeba odkomentovat anotaci @PostConstruct


Webové rozhraní lze po spuštění nalézt na adrese
http://localhost:8080/index.xhtml
Funkční položky v horním menu jsou:
- Dashboard
- View->My Configurations
- View->Locations
- Create->Location