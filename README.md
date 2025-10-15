<h1>BankApp – JavaFX Banking Application</h1>

<h2>Beskrivning</h2>
<p>BankApp är en enkel bankapplikation byggd med Java och JavaFX för grafiskt användargränssnitt, tillsammans med Gson för JSON-hantering. Projektet syftar till att erbjuda en robust och användarvänlig plattform för att hantera kunder, konton och banktransaktioner, med möjlighet att spara och läsa data från disk i JSON-format.</p>

<h2>Teknologier och Verktyg</h2>
<ul>
  <li><strong>Java 24</strong> – senaste versionen av plattformen för moderna språkfunktioner.</li>
  <li><strong>JavaFX 24</strong> – för att skapa ett rikt och enkelt grafiskt användargränssnitt.</li>
  <li><strong>Gson (2.10.1)</strong> – bibliotek för JSON-serialisering och deserialisering, används för datalagring.</li>
  <li><strong>Maven</strong> – hanterar beroenden och byggprocessen för att förenkla projektkonfigurationen.</li>
  <li><strong>IntelliJ IDEA 2025.2</strong> – rekommenderat IDE för utveckling och körning av projektet.</li>
</ul>

<h2>Funktioner</h2>
<ul>
  <li>Registrera nya kunder med unika kund-ID.</li>
  <li>Visa befintliga kunder i en användarvänlig lista.</li>
  <li>Skapa bankkonton kopplade till kunder med räntesats.</li>
  <li>Spara och ladda kund- och kontodata automatiskt i en JSON-fil.</li>
  <li>Grafiskt gränssnitt med flikar för enkel navigation.</li>
</ul>

<h2>Kommande funktioner (Roadmap)</h2>
<ul>
  <li>Visa samtliga konton för en vald kund.</li>
  <li>Funktion för insättningar, uttag och överföringar mellan konton.</li>
  <li>Historik/logg över transaktioner per konto.</li>
  <li>Användarautentisering för säkerhet.</li>
  <li>Förbättrat gränssnitt med mer responsiva och estetiska element.</li>
  <li>Integration med databaser som SQLite eller MySQL för långsiktig lagring.</li>
  <li>Mobilt stöd eller webbgränssnitt med modern teknik som React eller Vaadin.</li>
</ul>

<h2>Kända buggar och problem</h2>
<ul>
  <li>Felhantering kan förbättras, t.ex. vid inmatningsfel i GUI:t.</li>
  <li>Nuvarande implementation tillåter inte flersessionstillgång till JSON-filen (risk för datakorruption).</li>
  <li>Ingen avancerad säkerhet kring kunddata.</li>
  <li>Ej testat för mycket stora datamängder (skalbarhetsaspekter saknas).</li>
</ul>

<h2>Installation och användning</h2>
<ol>
  <li>Klona detta repository:<br>
    <code>git clone https://github.com/alexwest1981/BankSystem.git</code>
  </li>
  <li>Importera maven-projektet i IntelliJ IDEA.</li>
  <li>Säkerställ att du har JDK 24 och JavaFX SDK 24 installerade.</li>
  <li>Kör kommandot för att starta:<br>
    <code>mvn clean javafx:run</code>
  </li>
  <li>Applikationen öppnar GUI där du kan interagera med bankfunktionen.</li>
</ol>

<h2>Hur man bygger vidare</h2>
<p>Utforska klasserna i <code>org.example</code>-paketet: <code>Bank</code>, <code>Customer</code>, <code>Account</code> och <code>BankApp</code>. Använd Maven för beroendehantering och paketering. Förbättra GUI:t genom att lägga till fler funktioner och implementera databaskopplingar eller avancerad säkerhet.</p>
<p>Alla ändringar är under MIT-licens, vilket betyder att du fritt kan använda, modifiera och distribuera koden, även kommersiellt, så länge du inkluderar samma licens och ursprung.</p>

<h2>Licens</h2>
<p>MIT License © 2025 Alex Weström / Fenrir Studios</p>

<h2>Kontakt &amp; Bidrag</h2>
<ul>
  <li>Buggrapporter och funktionsförslag öppnas som Issues på GitHub.</li>
  <li>Pull requests välkomnas för förbättringar och buggfixar.</li>
  <li>Kontakta för frågor eller samarbete via GitHub eller e-post.</li>
</ul>
