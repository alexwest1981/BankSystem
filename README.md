# BankSystem – JavaFX Banking Application 🏦

BankSystem är en enkel bankapplikation byggd med **Java** och **JavaFX** för grafiskt användargränssnitt, tillsammans med **Gson** för JSON-hantering. Projektet syftar till att erbjuda en robust och användarvänlig plattform för att hantera kunder, konton och banktransaktioner, med möjlighet att spara och läsa data från disk i JSON-format.

## ✨ Genomförda Förbättringar och Refaktoriseringar

Vi har arbetat aktivt med att förbättra kodbasens struktur och funktionalitet:

* Infört separata serviceklasser (`CustomerService` och `AccountService`) för att hantera affärslogik och minska komplexiteten i UI-koden.
* Flyttat generering av kontonummer och clearingnummer till `AccountService`.
* Skapad central valideringsmetod `validateCustomerData` i UI.
* Omstrukturerat kundregistreringslogik i GUI:t för att använda serviceklasser.
* Påbörjat uppdelning av stora metoder till mindre delar för bättre översiktlighet.
* **Implementerad integration med MariaDB** via JDBC för datalagring av kunder och konton.
* Implementerad metod i `AccountService` för att dynamiskt läsa in och sortera lista av städer från den statiska `CityClearingNumbers`.
* Rensat och separerat GUI-logik och databaslogik för bättre kodstruktur och underhållbarhet.
* Infört hantering av bankens totala saldo direkt från databasen.
* Planerat migrering till **MVC-arkitektur** med användning av FXML och Controller-klasser.

## 🛠️ Teknologier och Verktyg

| Teknologi | Version / Beskrivning |
| :--- | :--- |
| **Java** | 24 – Senaste versionen av plattformen. |
| **JavaFX** | 24 – För grafiskt användargränssnitt. |
| **Gson** | 2.10.1 – JSON-serialisering och deserialisering. |
| **MariaDB** | Databashanterare via JDBC-connector. |
| **Maven** | Projekt- och beroendehantering. |
| **IntelliJ IDEA** | 2025.2 – Rekommenderat utvecklingsverktyg. |

## 🌟 Funktioner

* Registrering av kunder med unika ID:n och fullständig information.
* Visning och sökning av kunder och deras konton.
* Skapande av bankkonton med individanpassad räntesats.
* Automatiskt genererade kontonummer och clearingnummer, hämtade dynamiskt från stadlista.
* Hantering av insättningar, uttag och överföringar.
* Visning av transaktionshistorik per konto.
* Räntesatsjustering via GUI.
* Automatisk sparning och inläsning av data i JSON-fil (även om databasen nu är primär).
* Realtidsuppdatering i grafiskt gränssnitt.
* Flikbaserad navigation för enkel användning.

## 🚀 Installation och Användning

För att komma igång, följ dessa steg:

### 1. Databasinställning (MariaDB)

Installera **MariaDB** och skapa en databas i enlighet med projektets namn. Skapa sedan nödvändiga tabeller med följande SQL-kommandon:

```sql
CREATE DATABASE banksystem;
GRANT ALL PRIVILEGES ON banksystem.* TO 'pma'@'localhost';
FLUSH PRIVILEGES;

CREATE TABLE customers (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100),
    personal_number VARCHAR(12),
    address VARCHAR(150),
    email VARCHAR(100),
    phone VARCHAR(50)
);

CREATE TABLE accounts (
    number VARCHAR(20) PRIMARY KEY,
    customer_id VARCHAR(50),
    interest_rate DOUBLE,
    balance DOUBLE,
    clearing_number VARCHAR(10),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE transactions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(20),
    date TIMESTAMP,
    type VARCHAR(20),
    amount DOUBLE,
    description TEXT,
    FOREIGN KEY (account_number) REFERENCES accounts(number)
);

CREATE TABLE bank_balance (
    id INT PRIMARY KEY DEFAULT 1,
    balance DOUBLE NOT NULL
);
```

### 2. Projektinställning

Följ dessa steg för att konfigurera och förbereda projektet lokalt:

1.  **Klona Repository:**
    Använd Git för att klona projektet till din lokala maskin:
    ```bash
    git clone https://github.com/alexwest1981/BankSystem.git
    ```

2.  **Importera i IDE:**
    Importera projektet som ett **Maven-projekt** i din rekommenderade IDE, **IntelliJ IDEA**.

3.  **Beroenden:**
    Kontrollera att följande utvecklingsmiljö-komponenter är tillgängliga och korrekt konfigurerade i din IDE:
    * **JDK 24**
    * **JavaFX SDK 24**
    * **MariaDB JDBC-driver** (Hämtas automatiskt av Maven)

### 3. Konfigurera JavaFX i IntelliJ (Om Nödvändigt)

Eftersom JavaFX inte längre ingår i standard-JDK:n kan det krävas manuell konfiguration för att köra projektet via din IDE (även om Maven-kommandot hanterar det automatiskt).

1.  **Hämta SDK:** Se till att du har hämtat **JavaFX SDK 24** (för din plattform).
2.  **Modulinställningar:**
    * Gå till **File** -> **Project Structure** -> **Libraries**.
    * Lägg till sökvägen till din hämtade JavaFX SDK som ett **Globalt Bibliotek** (eller Modulbibliotek).
3.  **Körkonfiguration (Run Configuration):**
    * Gå till **Run** -> **Edit Configurations**.
    * I din **Application Run Configuration**, lägg till VM-argument för att peka på modulerna i JavaFX SDK:
      ```
      --module-path /path/to/javafx-sdk-24/lib --add-modules javafx.controls,javafx.fxml
      ```
      *(Ersätt `/path/to/javafx-sdk-24/lib` med den faktiska sökvägen till mappen `lib` i din JavaFX SDK-nedladdning).*

Om du använder **`mvn clean javafx:run`** (som rekommenderas i steg 3) kommer Maven-pluginet att hantera dessa beroenden och argument åt dig.

### 4. Körning

När projektet är konfigurerat kan du starta applikationen med Maven:

1.  **Kör Projektet:**
    Navigera till projektets rotkatalog i terminalen och kör:
    ```bash
    mvn clean javafx:run
    ```

2.  **Resultat:**
    Applikationen startar med ett **grafiskt JavaFX-gränssnitt** och ansluter omedelbart till din konfigurerade **MariaDB-databas** för att hantera bankdata.



## 🗺️ Kommande Funktioner (Roadmap)

Projektet har en ambitiös framtidsplan för att utöka funktionalitet, struktur och säkerhet:

* **Säkerhet:** Implementering av användarautentisering och robust säkerhet.
* **Användarupplevelse:** Förbättrat och mer responsivt grafiskt gränssnitt.
* **Plattformsstöd:** Stöd för webb- och mobilgränssnitt med moderna ramverk.
* **Arkitektur:** Full implementering av **MVC-arkitektur** med FXML och Controllers.
* **Stabilitet:** Utökad felhantering och starkare transaktionssäkerhet.
* **Prestanda:** Optimering för storskaliga databasinteraktioner.
* **Flera Användare:** Multiuser- och sessionhantering med samtidighetskontroll.

## ⚠️ Kända Buggar och Begränsningar

Var medveten om följande kända begränsningar och områden som kräver förbättring:

* **GUI Felhantering:** Begränsad felhantering i det grafiska användargränssnittet, vilket kan hanteras bättre i framtida versioner.
* **Säkerhet:** Avsaknad av avancerade säkerhetsmekanismer för känslig kunddata.
* **Datakonsistens:** Ingen hantering för samtidig åtkomst till den äldre JSON-filen (då databas nu är primär lagringslösning).
* **Prestanda:** Applikationen är ej optimerad för mycket stora datamängder eller hög belastning.

## 📜 Licens

Projektet är licensierat under **MIT License** © 2025 Alex Weström / Fenrir Studios.

> MIT-licensen är en fri och öppen källkods-licens som tillåter användning, kopiering, modifiering och distribution, under villkor om att licens och upphovsrättsnotis följer med. Programvaran erbjuds "i befintligt skick" utan garanti.

## 🤝 Kontakt & Bidrag

Alla bidrag, buggrapporter och förslag är varmt välkomna! Vi uppskattar alla former av engagemang.

* **Buggrapporter & Förslag:** Öppna gärna ett [issue på GitHub](https://github.com/alexwest1981/BankSystem/issues).
* **Kodbidrag:** **Pull requests** (PRs) för nya funktioner eller buggfixar är välkomna!
* **Kontakt:** Kontakta mig direkt för frågor eller samarbete via GitHub eller e-post.
