# BankSystem – JavaFX Banking Application

## Beskrivning
BankSystem är en enkel bankapplikation byggd med Java och JavaFX för ett grafiskt användargränssnitt, tillsammans med Gson för JSON-hantering. Projektet syftar till att erbjuda en robust och användarvänlig plattform för att hantera kunder, konton och banktransaktioner med sparning och inläsning av data i JSON-format.

### Vad som är gjort hittills
- Separat affärslogik flyttad till `CustomerService` och `AccountService` för bättre ansvarsfördelning.
- Kontonummer- och clearingnummer-generering hanteras nu i `AccountService`.
- Centraliserad och återanvändbar valideringslogik i `validateCustomerData`.
- Omstrukturerad kundregistreringslogik att använda serviceklasser.
- Påbörjad refaktorisering av UI-kod med mindre, återanvändbara metoder.
- Planer finns för att införa MVC-arkitektur med FXML och Controller-klasser för tydligare separation mellan UI och logik.

## Teknologier och Verktyg
- Java 24, JavaFX 24, Gson 2.10.1, Maven, IntelliJ IDEA 2025.2.

## Funktioner
- Kund- och kontoregistrering med unika ID.
- Sökning och visning av kunder och konton.
- Insättningar, uttag, överföringar och räntahantering.
- Transaktionshistorik per konto.
- Data sparas i och laddas från JSON-fil.
- Realtidsuppdatering i GUI.

## Kommande funktioner
- Användarautentisering.
- Integration med relationsdatabaser.
- Mobil- och webbgränssnitt.

## Installation och användning
1. `git clone https://github.com/alexwest1981/BankSystem.git`
2. Importera projektet i IntelliJ IDEA.
3. Se till att JDK 24 och JavaFX SDK 24 finns installerade.
4. Kör `mvn clean javafx:run` för att starta.

## Licens
MIT License © 2025 Alex Weström / Fenrir Studios.  
Fritt att använda och vidareutveckla med krav på att licensmeddelande följer med.

## Kontakt och bidrag
- Öppna Issues för buggrapporter och förslag.
- Pull Requests välkomnas.
- Kontakta via GitHub eller e-post.

