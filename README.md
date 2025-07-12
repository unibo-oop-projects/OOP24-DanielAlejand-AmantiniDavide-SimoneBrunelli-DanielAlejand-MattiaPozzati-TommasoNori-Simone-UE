# UniversityEscape 🎓

Un gioco di avventura Java con minigame integrati ambientato in un'università. Naviga tra le stanze, completa puzzle e accumula punti per fuggire dall'università!

## 📋 Descrizione

UniversityEscape è un gioco 2D sviluppato in Java utilizzando Swing. Il giocatore esplora diverse stanze dell'università, ognuna con il proprio NPC e minigame unico. L'obiettivo è completare tutti i puzzle per accumulare punti e "fuggire" dall'università.

## 🎮 Caratteristiche

- **Movimento fluido**: Controlla il personaggio con i tasti WASD
- **Sistema di stanze**: 6 stanze diverse da esplorare (Hub, Garden, 2.12, Lab, Gym, Bar)
- **Minigame vari**: 
  - Quiz Kahoot (stanza 2.12)
  - Sort & Serve - puzzle di ordinamento bottiglie (Bar)
- **Sistema di punteggio**: Punti basati sul tempo di completamento con bonus e penalità
- **HUD informativo**: Traccia il progresso e i punteggi in tempo reale
- **Interfaccia responsive**: Supporta ridimensionamento finestra e modalità fullscreen

## 🕹️ Controlli

- **WASD**: Movimento del personaggio
- **E**: Interazione con NPC e oggetti
- **F11**: Toggle fullscreen
- **ESC**: Torna al menu principale (dal gioco)
- **Alt+F4**: Esci dall'applicazione

## 🏗️ Architettura

Il progetto segue il pattern MVC (Model-View-Controller):

### Model
- **Entità**: Player, NPC, Room, Door
- **Minigame**: Sistema modulare per diversi tipi di puzzle
- **Scoring**: Sistema di punteggio con decoratori (Strategy Pattern)
- **Game State**: Gestione dello stato globale del gioco

### View
- **Renderer**: Sistema di rendering separato per ogni tipo di entità
- **Panels**: Menu principale e pannello di gioco
- **HUD**: Interfaccia utente per punteggi e progresso

### Controller
- **MainController**: Gestione del game loop e logica principale
- **Input Handling**: Gestione input da tastiera
- **Minigame Manager**: Coordinamento e lancio dei minigame

## 🧩 Minigame

### Quiz Kahoot (Stanza 2.12)
- Rispondi a domande a scelta multipla
- Ogni risposta sbagliata aggiunge 1 secondo di penalità
- Completa tutte le domande per vincere

### Sort & Serve (Bar)
- Organizza i liquidi colorati nelle bottiglie
- Ogni bottiglia deve contenere un solo colore
- Usa la logica per versare i liquidi nell'ordine corretto
- Premi 'R' per ricominciare con la stessa configurazione

## 🎯 Sistema di Punteggio

Il sistema di punteggio utilizza il **Strategy Pattern** con decoratori:

- **Base**: Punti basati sul tempo (Fast: 100pt, Medium: 70pt, Slow: 40pt)
- **Time Bonus**: +20 punti se completato sotto i 30 secondi
- **Cap**: Massimo 120 punti per stanza

## 🛠️ Tecnologie Utilizzate

- **Java 17+**
- **Swing**: Per l'interfaccia grafica
- **Java 2D**: Per il rendering personalizzato
- **Gradle**: Gestione dipendenze e build
- **FindBugs**: Analisi statica del codice

## 📁 Struttura del Progetto

```
src/main/java/it/unibo/exam/
├── controller/          # Logica di controllo
│   ├── input/          # Gestione input
│   ├── minigame/       # Controller dei minigame
│   └── position/       # Gestione posizioni
├── model/              # Modello dati
│   ├── entity/         # Entità del gioco
│   ├── game/           # Stato del gioco
│   └── scoring/        # Sistema punteggi
├── view/               # Interfaccia utente
│   ├── panel/          # Pannelli Swing
│   ├── renderer/       # Sistema rendering
│   └── hud/            # Head-Up Display
└── utility/            # Classi di utilità
    ├── factory/        # Factory patterns
    ├── generator/      # Generatori entità
    └── geometry/       # Geometria 2D
```

## 🚀 Come Eseguire

### Prerequisiti
- Java 17 o superiore
- Gradle 7.0+

### Compilazione ed Esecuzione
```bash
# Clona il repository
git clone [repository-url]
cd UniversityEscape

# Compila il progetto
./gradlew build

# Esegui il gioco
./gradlew run
```

### Esecuzione JAR
```bash
# Crea il JAR
./gradlew jar

# Esegui
java -jar build/libs/university-escape-1.0.jar

```
A causa delle limitazioni di dimensione di GitHub, il file eseguibile university-escape.jar non è stato incluso nel repository pubblico.
Tuttavia, il progetto è conforme alla regola P8, poiché il file .jar può essere ricreato localmente utilizzando il sistema di build Gradle.


### Altri comandi utili
```bash
# Pulisci il progetto
./gradlew clean

# Esegui i test
./gradlew test

# Genera la documentazione
./gradlew javadoc
```

## 🎨 Risorse

Il gioco include risorse grafiche personalizzate:
- Background del menu principale
- Sfondi per i minigame
- Sprite e texture (estendibili)

## 🔧 Configurazione

Il gioco si avvia automaticamente in modalità fullscreen. Le impostazioni possono essere modificate nel codice sorgente:

- Dimensioni finestra: `Main.java`
- Velocità giocatore: `MovementEntity.java`
- Parametri punteggio: `TieredScoringStrategy.java`

## 🤝 Contribuire

1. Fork il progetto
2. Crea un branch per la feature (`git checkout -b feature/nuova-funzionalita`)
3. Commit delle modifiche (`git commit -am 'Aggiungi nuova funzionalità'`)
4. Push del branch (`git push origin feature/nuova-funzionalita`)
5. Apri una Pull Request

## 📝 TODO

- [ ] Aggiungere più minigame per le stanze vuote
- [ ] Sistema di salvataggio/caricamento
- [ ] Effetti sonori e musica
- [ ] Animazioni sprite avanzate
- [ ] Multiplayer locale
- [ ] Sistema di achievement

## 🐛 Bug Noti

- Il ridimensionamento della finestra durante i minigame potrebbe causare problemi di layout
- Su alcuni sistemi, il fullscreen potrebbe non funzionare correttamente

## 📄 Licenza

Copyright © 2025 UniversityEscape Team. Tutti i diritti riservati.

Questo progetto è sviluppato per scopi educativi nell'ambito del corso di Programmazione ad Oggetti.

**Limitazioni d'uso:**
- ✅ Uso personale e educativo
- ✅ Studio e analisi del codice
- ✅ Modifiche per apprendimento
- ❌ Uso commerciale
- ❌ Ridistribuzione senza autorizzazione
- ❌ Rimozione del copyright

Il software è fornito "così com'è", senza garanzie di alcun tipo. Gli sviluppatori non sono responsabili per eventuali danni derivanti dall'uso del software.

---

## 👥 Team di Sviluppo

Progetto sviluppato da studenti dell'Università di Bologna per l'esame di Programmazione ad Oggetti.

### 🧑‍💻 **Sviluppatori:**

- **Simone Brunelli** - [@Purp7ePi3](https://github.com/Purp7ePi3)
- **Daniel Alejandro Horna** - [@Alejandro-the-Unyielding](https://github.com/Alejandro-the-Unyielding)  
- **Mattia Pozzati** - [@Mattia-Pozzati](https://github.com/Mattia-Pozzati)
- **Davide Amantini** - [@AmantiniDavide](https://github.com/AmantiniDavide)
- **Tommaso Nori** - [@TommasoNori](https://github.com/TommasoNori)

### 🏆 **Contributi:**
- **Architecture & Core Systems**: Design MVC e sistema entità
- **Minigame Development**: Implementazione Quiz Kahoot e Sort & Serve
- **UI/UX Design**: Interfaccia grafica e sistema di rendering
- **Scoring System**: Pattern Strategy e sistema punteggi
- **Game Logic**: Controller principale e gestione stati
