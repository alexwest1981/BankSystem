# BankSystem – JavaFX Banking Application

## Beskrivning
BankSystem är en enkel bankapplikation byggd med Java och JavaFX för grafiskt användargränssnitt, tillsammans med Gson för JSON-hantering. Projektet syftar till att erbjuda en robust och användarvänlig plattform för att hantera kunder, konton och banktransaktioner, med möjlighet att spara och läsa data från disk i JSON-format.

### Genomförda förbättringar och refaktoriseringar
- Infört separata serviceklasser (`CustomerService` och `AccountService`) för att hantera affärslogik och minska komplexiteten i UI-koden.
- Flyttat generering av kontonummer och clearingnummer till `AccountService`.
- Skapad central valideringsmetod `validateCustomerData` i UI.
- Omstrukturerat kundregistreringslogik i GUI:t för att använda serviceklasser.
- Påbörjat uppdelning av stora metoder till mindre delar för bättre översiktlighet.
- Planerat migrering till MVC-arkitektur med användning av FXML och Controller-klasser.

## Teknologier och Verktyg
- **Java 24** – senaste versionen av plattformen.
- **JavaFX 24** – grafiskt användargränssnitt.
- **Gson 2.10.1** – JSON-serialisering och deserialisering.
- **Maven** – projekt- och beroendehantering.
- **IntelliJ IDEA 2025.2** – rekommenderat utvecklingsverktyg.

## Funktioner
- Registrering av kunder med unika ID:n och fullständig information.
- Visning och sökning av kunder och deras konton.
- Skapande av bankkonton med individanpassad räntesats.
- Automatiskt genererade kontonummer och clearingnummer.
- Hantering av insättningar, uttag och överföringar.
- Visning av transaktionshistorik per konto.
- Räntesatsjustering via GUI.
- Automatisk sparning och inläsning av data i JSON-fil.
- Realtidsuppdatering i grafiskt gränssnitt.
- Flikbaserad navigation för enkel användning.

## Kommande Funktioner (Roadmap)
- Användarautentisering och säkerhet.
- Förbättrat och mer responsivt gränssnitt.
- Databasintegration (SQLite, MySQL).
- Mobil och webbgränssnitt med moderna ramverk.
- Full implementering av MVC-arkitektur med FXML och Controllers.

## Kända Buggar och Begränsningar
- Begränsad felhantering i GUI.
- Ingen hantering för samtidig åtkomst till JSON-filen.
- Avsaknad av avancerad säkerhet för kunddata.
- Ej optimerad för stora datamängder eller hög belastning.

## Installation och användning
1. Klona repository:
<code>git clone https://github.com/alexwest1981/BankSystem.git</code>
2. Importera Maven-projektet i IntelliJ IDEA.
3. Se till att du har JDK 24 och JavaFX SDK 24 installerade.
4. Kör projektet med:
<code>mvn clean javafx:run</code>
5. Applikationen startar med grafiskt gränssnitt.

## Hur man bygger vidare
- Utforska `org.example`-paketet med klasser för modell, service och UI.
- Fortsätt refaktorera UI-kod och implementera MVC med FXML.
- Skriv enhetstester för servicelager.
- Lägg till fler funktioner och säkerhet.

## Licens
MIT License © 2025 Alex Weström / Fenrir Studios

MIT-licensen är en fri och öppen källkods-licens som tillåter användning, kopiering, modifiering och distribution, under villkor om att licens och upphovsrättsnotis följer med. Programvaran erbjuds "i befintligt skick" utan garanti.

## Kontakt & Bidrag
- Buggrapporter och förslag öppnas som issues på GitHub.
- Pull requests är välkomna.
- Kontakta för frågor eller samarbete via GitHub eller e-post.
