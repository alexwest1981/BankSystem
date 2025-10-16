# BankSystem – JavaFX Banking Application

## Beskrivning
BankSystem är en enkel bankapplikation byggd med Java och JavaFX för grafiskt användargränssnitt, tillsammans med Gson för JSON-hantering. Projektet syftar till att erbjuda en robust och användarvänlig plattform för att hantera kunder, konton och banktransaktioner, med möjlighet att spara och läsa data från disk i JSON-format.

## Teknologier och Verktyg
- **Java 24** – senaste versionen av plattformen för moderna språkfunktioner.
- **JavaFX 24** – för att skapa ett rikt och enkelt grafiskt användargränssnitt.
- **Gson (2.10.1)** – bibliotek för JSON-serialisering och deserialisering, används för datalagring.
- **Maven** – hanterar beroenden och byggprocessen för att förenkla projektkonfigurationen.
- **IntelliJ IDEA 2025.2** – rekommenderat IDE för utveckling och körning av projektet.

## Funktioner
- Registrera nya kunder med unika kund-ID och fullständiga personuppgifter.
- Visa befintliga kunder i en användarvänlig lista med sökfunktion.
- Skapa bankkonton kopplade till kunder med individuell räntesats.
- Automatiskt genererat kontonummer och clearingnummer baserat på kontorsort.
- Visa samtliga konton för en vald kund.
- Genomföra insättningar, uttag och överföringar mellan konton.
- Visa historik/logg över transaktioner per konto.
- Justera räntesats för varje konto via grafiskt gränssnitt.
- Spara och ladda kund- och kontodata automatiskt i en JSON-fil.
- Realtidsuppdatering av kund- och kontoinformation i GUI:t.
- Grafiskt gränssnitt med flikar för enkel navigation.

## Kommande funktioner (Roadmap)
- Användarautentisering för säkerhet.
- Förbättrat gränssnitt med mer responsiva och estetiska element.
- Integration med databaser som SQLite eller MySQL för långsiktig lagring.
- Mobilt stöd eller webbgränssnitt med modern teknik som React eller Vaadin.

## Kända buggar och problem
- Felhantering kan förbättras, t.ex. vid inmatningsfel i GUI:t.
- Nuvarande implementation tillåter inte flersessionstillgång till JSON-filen (risk för datakorruption).
- Ingen avancerad säkerhet kring kunddata.
- Ej testat för mycket stora datamängder (skalbarhetsaspekter saknas).

## Installation och användning
1. Klona detta repository:
<code>git clone https://github.com/alexwest1981/BankSystem.git</code>
2. Importera maven-projektet i IntelliJ IDEA.
3. Säkerställ att du har JDK 24 och JavaFX SDK 24 installerade.
4. Kör kommandot för att starta:
<code>mvn clean javafx:run</code>
5. Applikationen öppnar GUI där du kan interagera med bankfunktionen.

## Hur man bygger vidare
Utforska klasserna i `org.example`-paketet: `Bank`, `Customer`, `Account` och `BankApp`. Använd Maven för beroendehantering och paketering. Förbättra GUI:t genom att lägga till fler funktioner och implementera databaskopplingar eller avancerad säkerhet.

## Licens
MIT License © 2025 Alex Weström / Fenrir Studios

MIT-licensen är en enkel och generös öppen källkodslicens utvecklad av Massachusetts Institute of Technology (MIT). Den tillåter vem som helst att använda, kopiera, modifiera, sammanfoga, publicera, distribuera, underlicensiera och sälja programvaran, utan några stora restriktioner.

**Viktiga punkter:**
- Du får använda programvaran för alla ändamål, även kommersiella.
- Du får ändra och bygga vidare på koden.
- Licensmeddelandet och upphovsrättsnotisen måste följa med i alla kopior av programvaran, inklusive modifierade versioner.
- Programvaran tillhandahålls "i befintligt skick" utan någon garanti. Utvecklarna ansvarar inte för problem eller skador som orsakas av att använda programvaran.

Du kan tryggt använda, ändra och dela koden.  
Du måste behålla licensinformationen i källkoden.  
Du behöver inte öppna din egen kod om du bygger vidare på detta projekt.  
Licensen är flexibel och populär för både privata och kommersiella projekt.

## Kontakt & Bidrag
- Buggrapporter och funktionsförslag öppnas som Issues på GitHub.
- Pull requests välkomnas för förbättringar och buggfixar.
- Kontakta för frågor eller samarbete via GitHub eller e-post.
