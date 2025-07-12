# University Escape

**Pozzati Mattia**, 
**Simone Brunelli**, 
**Altri**
**Data**: 12 giugno 2025

---

## Indice

1. [Analisi](#analisi)
    1. [Requisiti](#requisiti)
    2. [Analisi e modello del dominio](#analisi-e-modello-del-dominio)
2. [Design](#design)
    1. [Architettura](#architettura)
    2. [Design dettagliato](#design-dettagliato)
        1. [Pozzati Mattia](#pozzati-mattia)
        2. [Amantini Davide](#amantini-davide)
        3. [Brunelli Simone](#brunelli-simone)
        4. [Horna Daniel Alejandro](#horna-daniel-alejandro)
3. [Sviluppo](#sviluppo)
    1. [Testing automatizzato](#testing-automatizzato)
    2. [Metodologia di lavoro](#metodologia-di-lavoro)
    3. [Note di sviluppo](#note-di-sviluppo)
4. [Commenti finali](#commenti-finali)
    1. [Autovalutazione e lavori futuri](#autovalutazione-e-lavori-futuri)
A. [Guida Utente](#guida-utente)

---

## 1. Analisi

### 1.1 Requisiti

Il progetto, commissionato dall’Università di Bologna, ha come obiettivo la realizzazione di un videogioco in stile escape room ambientato in un’università. Il gioco appartiene al genere puzzle/adventure e prevede la risoluzione di minigiochi per progredire tra le stanze.

**Requisiti funzionali:**
- Il gioco gestisce una partita composta da più stanze, ognuna con un minigioco diverso (Bar, Gym, Kahoot, ...).
- Presenza di un menu iniziale per avviare la partita, visualizzare la legenda dei tasti e uscire.
- Il giocatore può muoversi tra le stanze, interagire con oggetti e risolvere enigmi.
- Implementazione di collisioni tra entità e oggetti ambientali.
- Gestione delle condizioni di vittoria e sconfitta.

**Requisiti non funzionali:**
- Grafica coerente e pulita, ispirata allo stile pixel art.
- Interfaccia semplice e intuitiva.
- Ottimizzazione delle prestazioni e fluidità di gioco.

### 1.2 Analisi e modello del dominio

Il gioco è composto da una serie di stanze (room), ognuna con un minigioco specifico. Il giocatore deve risolvere i minigiochi per ottenere chiavi o oggetti necessari a progredire. Il dominio comprende entità come Player, Npc, Door, Room, Minigame.

**Obiettivo:**  
Raggiungere l’uscita finale dell’università risolvendo tutti i minigiochi e superando gli ostacoli.

---

## 2. Design

### 2.1 Architettura

La codebase adotta il pattern architetturale **MVC (Model-View-Controller)** per separare la logica di gioco, la gestione degli input e la visualizzazione grafica.

- **Model:** Gestisce la logica e i dati del gioco (stanze, entità, minigiochi, punteggi). Si trova in `src/main/java/it/unibo/exam/model/`.
- **View:** Si occupa della grafica e dell’interfaccia utente. Si trova in `src/main/java/it/unibo/exam/view/`.
- **Controller:** Media tra model e view, gestendo input e aggiornamenti. Si trova in `src/main/java/it/unibo/exam/controller/`.

Per la logica delle entità, è stato utilizzato il pattern **ECS (Entity-Component-System)**, che permette di comporre comportamenti tramite componenti riutilizzabili.

# 2.2 Design dettagliato

## 2.2.1 Pozzati Mattia

**Gestione della creazione delle entità:**
![Testo alternativo](reportImg/EntityUML.png)
Ho implementato il **Factory Pattern** per centralizzare la creazione delle entità di gioco (giocatore, NPC, oggetti interattivi). Questo approccio permette di gestire facilmente la complessità e la varietà delle entità, garantendo coerenza e riusabilità del codice. Le entità sono composte seguendo il paradigma ECS (Entity-Component-System). Le collisioni sono gestite tramite hitbox e metodi dedicati, assicurando un’interazione precisa tra le entità e l’ambiente. Queste sono le entità globali. In ogni minigioco sono presenti Entità caratteristiche sviluppate estendendo quelle globali.
**Minigioco stanza Gym "Bubble Shooter":**
![Testo alternativo](reportImg/GymUML.png)
Mi sono occupato della progettazione e dello sviluppo del minigioco della stanza Gym, ispirato al classico "Bubble Shooter". Ho realizzato sia la logica di gioco (model) che la gestione degli input e la visualizzazione grafica (controller e view).
- Model: Ho definito le classi per rappresentare i dischi (bubbles), il cannone e i proiettili, implementando la logica di movimento, collisione e rimozione dei dischi colpiti.
- Controller: Ho gestito gli input da tastiera e mouse per controllare il cannone e sparare i proiettili, integrando la logica di aggiornamento dello stato del gioco.
- View: Ho curato la visualizzazione grafica del minigioco, disegnando dinamicamente i dischi, il cannone e gli effetti di gioco, mantenendo uno stile coerente con il resto del progetto.

**Altre responsabilità:**
Ho contribuito all’implementazione delle classi relative all'accesso alle risorse del progetto.
Ho partecipato alla progettazione delle interfacce principali e alla definizione delle entità del dominio.

---

#### 2.2.2 Davide Amantini

**Gestione del Menu Principale e delle Opzioni**

![UML Menu](reportImg/MenuUML.png)

Ho progettato e sviluppato la **GUI del menu principale** utilizzando Swing, seguendo le best practice dell’architettura **MVC** e tenendo conto della futura integrazione di minigiochi e opzioni di gioco.

- **Componenti principali**:
  - **MainMenuPanel**: pannello principale con i pulsanti “Gioca”, “Opzioni” e “Esci”, sfondo personalizzato e label esplicativa dei comandi principali (WASD, E, ESC).
  - **Integrazione responsive**: il layout è realizzato tramite GridBagLayout per adattarsi a qualsiasi dimensione finestra, e la grafica è caricata tramite l’utility `AssetLoader` per una gestione centralizzata delle risorse.

- **Gestione Eventi**:
  - Ogni bottone è associato a un’azione ben distinta tramite listener lambda, che comunica con il controller per avviare il gioco, mostrare il dialog delle opzioni, o uscire dal programma.
  - Il tasto **ESC** è intercettato tramite l’ActionMap/InputMap del pannello di gioco, permettendo di aprire un **menu di pausa** da cui si può tornare al menu principale o regolare le opzioni audio senza perdere lo stato corrente.

- **Gestione delle Opzioni (Audio/Musica)**:
  - La finestra **Opzioni** permette di modificare in tempo reale il volume della musica di sottofondo tramite uno **slider** (JSlider) e di attivare/disattivare l’audio generale tramite un pulsante mute.
  - Il **salvataggio delle preferenze** avviene in modo persistente usando le Preferences di Java (se necessario si può estendere con serialization o salvataggio file JSON/XML).

- **Comandi Principali**: sotto ai pulsanti del menu viene visualizzata una label HTML stilizzata che evidenzia i tasti di movimento e interazione, per una user experience chiara anche a nuovi giocatori.

**Minigioco CatchBall - Architettura e Sviluppo**

![UML CatchBall](reportImg/CatchBallUML.PNG)

Ho progettato e realizzato il **minigioco CatchBall** (MVC pattern), ambientato nella stanza Garden, che simula la raccolta di gocce d’acqua con una borraccia.

- **Architettura MVC**:
  - **Model** (`CatchBallModel`): mantiene lo stato del gioco (posizione e vite, gestione palline, logica di vittoria/sconfitta, generazione random delle gocce).
  - **View** (`CatchBallPanel`): si occupa del rendering della scena, usando immagini custom per la bottiglia e le gocce. Il rendering è double-buffered e ottimizzato per evitare flicker.
  - **Controller** (`CatchBallMinigame`): gestisce input tastiera (A/D per movimento), timer per aggiornare la partita e sincronizza model e view; aggiorna la logica di punteggio con una strategia a decoratori (es. bonus tempo, cap massimo punti).

- **Gestione delle risorse**:
  - Tutte le immagini (sfondo, bottiglia, goccia) sono caricate centralmente con `AssetLoader` e ridimensionate dove necessario, garantendo efficienza e riuso.
  - Uso di fallback: se un’immagine non viene trovata, si passa automaticamente a un disegno placeholder.

- **Salvataggio delle Preferenze**:
  - Le preferenze audio e volume vengono lette/salvate ogni volta che si apre il dialog opzioni o il menu di pausa, usando `AudioManager` e, dove necessario, le Preferences di Java.

- **Comunicazione tra componenti**:
  - Il minigioco notifica il controller principale tramite callback (`MinigameCallback`), passando esito e punteggio. Questo permette di aggiornare la schermata HUD e i punteggi globali.

- **User Experience**:
  - Ogni aspetto grafico e di input è pensato per garantire immediatezza, chiarezza e coerenza con lo stile del resto del gioco (font, colori, trasparenze, icone, feedback visivi).
  - L’utente può sempre tornare al menu principale tramite ESC, senza rischiare di perdere i progressi o le impostazioni scelte.

**Altre Responsabilità e Contributi**

- Ho contribuito alla gestione centralizzata dell’audio di gioco (`AudioManager`).
- Ho collaborato nella definizione degli standard di stile (colori, font, layout) per assicurare coerenza visiva tra menu, minigiochi e HUD.
- Mi sono occupato della progettazione delle interfacce principali e della definizione delle principali entità di dominio.

---

#### 2.2.3 Simone Brunelli

## Panoramica

Questo documento dettaglia l'implementazione di quattro componenti sviluppati per il progetto UniversityEscape. Ogni classe dimostra diversi pattern architetturali e soluzioni tecniche, progettate con particolare attenzione alla robustezza, estensibilità e integrazione con il sistema complessivo.

---

## 🎯 1. KahootMinigame - Quiz Interattivo con Pattern MVC

### Approccio Progettuale

La classe `KahootMinigame` implementa il pattern MVC per separare chiaramente la logica, la presentazione e il controllo. L'implementazione si integra con il sistema di scoring esistente del team.

### Caratteristiche Principali

#### Integrazione con Sistema Scoring Esistente

```java
// Implementazione flessibile per integrazione con sistema scoring
public KahootMinigame(final ScoringStrategy scoringStrategy) {
    this.scoringStrategy = Objects.requireNonNull(scoringStrategy,
        "scoringStrategy must not be null");
}
```

#### Gestione Stati e Threading

Implementazione di un workflow per gestire il ciclo vita del quiz:

```java
// Gestione del threading per feedback non bloccante
new Thread(() -> {
    try {
        Thread.sleep(FEEDBACK_DELAY);
        javax.swing.SwingUtilities.invokeLater(() -> {
            if (model.isGameCompleted()) {
                showDetailedFinalResults();
            } else {
                showCurrentQuestion();
            }
        });
    } catch (final InterruptedException e) {
        Thread.currentThread().interrupt();
        javax.swing.SwingUtilities.invokeLater(this::showDetailedFinalResults);
    }
}).start();
```

#### Sistema Penalità Personalizzato

Aggiunge 10 secondi per ogni risposta sbagliata:

```java
public int getFinalTimeWithPenalty(final int penaltyPerWrongAnswer) {
    return getElapsedTimeSeconds() + (wrongAnswers * penaltyPerWrongAnswer);
}
```

#### Interfaccia Utente Dinamica

* Feedback colorato per risposte corrette/errate
* Aggiornamento real-time di progresso e statistiche
* Gestione graceful degli stati di transizione

---

## ⌨️ 2. KeyHandler - Sistema di Input

### Filosofia di Design

La classe `KeyHandler` fornisce un sistema di input che supporta sia azioni continue (movimento) che discrete (interazioni). L'implementazione risolve il problema classico del "key repeat" per le azioni singole.

### Innovazioni Tecniche

#### Pattern Auto-Reset per Azioni Singole

```java
// Soluzione al problema del key repeat
public boolean isInteractJustPressed() {
    if (interactJustPressed) {
        interactJustPressed = false; // Auto-reset immediato
        return true;
    }
    return false;
}

public boolean isSpaceBarPressed() {
    if (spaceBarPressed) {
        spaceBarPressed = false; // Reset dopo lettura
        return true;
    }
    return false;
}
```

#### Sistema Dual-Key Mapping

Supporto per tasti primari e alternativi per migliorare l'accessibilità:

```java
// Supporto sia WASD che frecce direzionali
if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
    upPressed = true;
}
if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
    leftPressed = true;
}
```

### Vantaggi Implementativi

* **Efficienza di Polling**: I controller leggono lo stato quando necessario
* **Coerenza dello Stato**: Prevenzione stati inconsistenti con auto-reset
* **Esperienza Utente**: Supporto sia WASD che frecce per inclusività
* **Precisione delle Azioni**: Distinzione netta tra azioni continue e discrete

---

## 🎨 3. AssetLoader - Gestione Risorse in Collaborazione

### Approccio Collaborativo

La classe `AssetLoader` è stata sviluppata in stretta collaborazione con un membro del team, combinando le competenze per creare una classe utility robusta per la gestione delle risorse. Insieme è stato progettato un sistema che enfatizza la robustezza e graceful degradation.

### Soluzioni Tecniche

#### Sistema Fallback Intelligente

```java
// Implementazione collaborativa del fallback automatico
public static Image loadImageWithFallback(final String resourcePath, final String fallbackPath) {
    Image image = loadImage(resourcePath);
    if (image == null && fallbackPath != null) {
        LOGGER.info("Attempting to load fallback image: " + fallbackPath);
        image = loadImage(fallbackPath);
    }
    return image;
}
```

#### Gestione Completa degli Errori

```java
// Approccio comprensivo all'error handling
try {
    final var resource = AssetLoader.class.getClassLoader().getResource(resourcePath);
    if (resource == null) {
        LOGGER.warning("Resource not found: " + resourcePath);
        return null;
    }
    
    final Image image = ImageIO.read(resource);
    if (image == null) {
        LOGGER.warning("Failed to read image from resource: " + resourcePath);
        return null;
    }
    
    LOGGER.info("Image loaded successfully: " + resourcePath);
    return image;
    
} catch (final IOException e) {
    LOGGER.log(Level.WARNING, "Failed to load image: " + resourcePath + " - " + e.getMessage(), e);
    return null;
} catch (final IllegalArgumentException e) {
    LOGGER.log(Level.WARNING, "Invalid image format: " + resourcePath + " - " + e.getMessage(), e);
    return null;
}
```

### Caratteristiche Collaborative

* **Validazione Risorse**: Controllo esistenza sviluppato congiuntamente
* **Logging Dettagliato**: Sistema di logging progettato insieme per debugging completo
* **Efficienza di Memoria**: Strategia di caricamento lazy definita in team

---

## 🏭 4. MinigameFactory - Implementazione del Factory Pattern

### Pattern di Design

La classe `MinigameFactory` implementa il Factory Pattern per centralizzare la creazione dei minigame e fornire un'interfaccia uniforme per il sistema.

### Architettura

#### Mapping Room-to-Minigame

```java
// Implementazione del mapping centralizzato
public static Minigame createMinigame(final int roomId) {
    switch (roomId) {
        case ROOM_GARDEN:
            return new CatchBallMinigame();
        case ROOM_LAB:
            return new KahootMinigame();
        case ROOM_MAZE:
            return new MazeMinigame();
        case ROOM_GYM:
            return new GymMinigame();
        case ROOM_BAR:
            return new BarMinigame();
        default:
            throw new IllegalArgumentException(
                "Invalid room ID for minigame: " + roomId
                + ". Valid room IDs are " + FIRST_ROOM + "–" + LAST_ROOM + "."
            );
    }
}
```

#### Metodi di Utilità

Metodi per evitare istanziazioni inutili:

```java
// Informazioni sui minigame senza istanziazione
public static String getMinigameName(final int roomId) {
    switch (roomId) {
        case ROOM_GARDEN: return "Catch the Ball";
        case ROOM_LAB: return "Kahoot";
        case ROOM_MAZE: return "Maze Runner";
        case ROOM_GYM: return "Bubble shooter";
        case ROOM_BAR: return "Sort & Serve";
        default: throw new IllegalArgumentException("Invalid room ID: " + roomId);
    }
}

public static boolean hasMinigame(final int roomId) {
    return roomId >= FIRST_ROOM && roomId <= LAST_ROOM;
}
```

### Vantaggi Architetturali

* **Creazione Centralizzata**: Unico punto per creazione minigame
* **Type Safety**: Validazione input con eccezioni descrittive
* **Estensibilità**: Facile aggiunta nuovi minigame
* **Accesso alle Informazioni**: Metadati senza istanziazione

---

## 📊 Diagramma UML delle Classi

```mermaid
classDiagram
    %% Classi principali implementate
    class KahootMinigame {
        -scoringStrategy: ScoringStrategy
        -gameFrame: JFrame
        -model: KahootModel
        -view: KahootPanel
        -callback: MinigameCallback
        +start(JFrame, MinigameCallback)
        +stop()
        +onAnswerSubmitted(boolean, String)
        +showDetailedFinalResults()
        +getName() String
        +getDescription() String
    }

    class KeyHandler {
        -upPressed: boolean
        -downPressed: boolean
        -leftPressed: boolean
        -rightPressed: boolean
        -interactPressed: boolean
        -interactJustPressed: boolean
        -spaceBarPressed: boolean
        +isUpPressed() boolean
        +isInteractJustPressed() boolean
        +isSpaceBarPressed() boolean
        +keyPressed(KeyEvent)
        +keyReleased(KeyEvent)
    }

    class AssetLoader {
        <<utility>>
        +loadImage(String) Image
        +loadImageWithFallback(String, String) Image
        +imageExists(String) boolean
    }

    class MinigameFactory {
        <<factory>>
        +ROOM_GARDEN: int
        +ROOM_LAB: int
        +ROOM_MAZE: int
        +ROOM_GYM: int
        +ROOM_BAR: int
        +createMinigame(int) Minigame
        +getMinigameName(int) String
        +getMinigameDescription(int) String
        +hasMinigame(int) boolean
    }

    %% Interfacce utilizzate
    class Minigame {
        <<interface>>
        +start(JFrame, MinigameCallback)
        +stop()
        +getName() String
        +getDescription() String
    }

    class KeyListener {
        <<interface>>
        +keyPressed(KeyEvent)
        +keyReleased(KeyEvent)
        +keyTyped(KeyEvent)
    }

    class KahootListener {
        <<interface>>
        +onQuizStarted()
        +onAnswerSubmitted(boolean, String)
        +onNextQuestion(QuizQuestion)
        +onQuizCompleted(boolean, int, int, int)
    }

    %% Classi del team integrate
    class KahootModel {
        -questions: List~QuizQuestion~
        -currentQuestionIndex: int
        -correctAnswers: int
        -wrongAnswers: int
        +startQuiz()
        +submitAnswer(int) boolean
        +getCurrentQuestion() QuizQuestion
        +isGameCompleted() boolean
    }

    class KahootPanel {
        -model: KahootModel
        -answerButtons: JButton[]
        +showQuestion(QuizQuestion)
        +showFeedback(boolean, String)
        +disableAnswerButtons()
    }

    class ScoringStrategy {
        <<interface>>
        +calculate(int) int
    }

    %% Relazioni
    KahootMinigame ..|> Minigame
    KahootMinigame ..|> KahootListener
    KahootMinigame --> KahootModel
    KahootMinigame --> KahootPanel
    KahootMinigame --> ScoringStrategy
    
    KeyHandler ..|> KeyListener
    
    MinigameFactory ..> Minigame : creates
    MinigameFactory ..> KahootMinigame : creates
    
    KahootModel --> KahootListener : notifies
    KahootPanel --> KahootModel : observes
```

---

## 🔧 Tecnologie e Pattern Utilizzati

* **Java Swing** per l'interfaccia utente
* **Pattern MVC** per separazione delle responsabilità
* **Factory Pattern** per creazione centralizzata
* **Observer Pattern** per notifiche eventi
* **Strategy Pattern** per sistemi di scoring flessibili
* **Thread Management** per operazioni non bloccanti

---

## 🚀 Caratteristiche Chiave

* **Robustezza**: Gestione completa degli errori con fallback intelligenti
* **Estensibilità**: Architettura modulare per facilità di espansione
* **Usabilità**: Interfacce intuitive con supporto accessibilità
* **Performance**: Gestione efficiente delle risorse e threading ottimizzato
* **Collaborazione**: Sviluppo in team con integrazione seamless

---

## 📝 Note sull'Implementazione

* Implementazione **MVC** con gestione threading avanzata per `KahootMinigame`
* Sistema **input** con auto-reset per azioni singole in `KeyHandler`
* Utility **robusta** sviluppata in collaborazione con fallback per `AssetLoader`
* **Factory** per creazione centralizzata minigame in `MinigameFactory`


---

## 2.2.3 Daniel Alejandro Horna

## Sistema di Punteggio e della Logica di Scoring

Il sistema di punteggio nel nostro gioco è stato progettato utilizzando **design patterns** per garantire un'architettura robusta, estensibile e facilmente manutenibile. I **design patterns** utilizzati sono fondamentali per la flessibilità del sistema, che permette di modificare facilmente il comportamento del calcolo del punteggio, aggiungere nuovi bonus o penalità, e aggiornare automaticamente l'interfaccia utente.

Inoltre, il sistema è stato progettato per **rispettare il pattern MVC (Model-View-Controller)**, separando chiaramente la logica del gioco (Model), la logica di visualizzazione (View) e il flusso di controllo (Controller).

### 🧠 I Pattern Utilizzati

Il sistema di punteggio e scoring si basa su tre design patterns principali: **Strategy**, **Decorator** e **Observer**. Ogni pattern gioca un ruolo specifico nell'architettura, garantendo la separazione delle preoccupazioni e la facilità di estensione.


### 1) Strategy Pattern

* **Cos'è**:
  Il pattern **Strategy** è stato utilizzato per definire una **famiglia di algoritmi** di calcolo del punteggio, che possono essere intercambiati senza modificare il codice cliente.
  La base del sistema di calcolo dei punteggi è l'interfaccia `ScoringStrategy` che definisce il metodo `calculate(int data)`. Ogni implementazione concreta della strategia definisce come il punteggio deve essere calcolato.

* **Come è stato utilizzato**:

  * **`TieredScoringStrategy`**: assegna un punteggio in base al tempo impiegato dal giocatore per completare il minigioco, suddividendo il punteggio in fasce di tempo (veloce, medio, lento).
  * **Vantaggio**: ogni minigioco può utilizzare una diversa strategia di calcolo dei punteggi senza modificare il codice del controller, garantendo che il sistema sia facilmente estendibile.

* **Beneficio**:

  * Consente di cambiare facilmente il comportamento del calcolo dei punti, aggiungere nuove regole di scoring o modificare il metodo di calcolo per ogni minigioco.
  * Ad esempio, se si volesse introdurre una modalità difficile con un diverso calcolo del punteggio, basterebbe scrivere una nuova strategia senza toccare il codice che gestisce i minigiochi o la visualizzazione del punteggio.

---

### 2) Decorator Pattern

* **Cos'è**:
  Il pattern **Decorator** è stato utilizzato per aggiungere comportamento aggiuntivo alla strategia di punteggio, come **bonus per velocità** o **limiti massimi di punti**. Il decoratore permette di "avvolgere" una strategia esistente, aggiungendo nuovi comportamenti senza modificare la strategia originale.

* **Come è stato utilizzato**:

  * **`TimeBonusDecorator`**: aggiunge un bonus di punti se il minigioco è completato in un tempo inferiore a una certa soglia (ad esempio, sotto i 15 secondi).
  * **`CapDecorator`**: impone un limite massimo ai punti che un giocatore può guadagnare in una stanza, evitando che il punteggio cresca oltre un valore predefinito (es. 120 punti).

* **Beneficio**:

  * Consente di combinare facilmente diversi comportamenti di punteggio.
  * In questo modo, un punteggio può essere modificato dinamicamente, aggiungendo bonus o capi senza cambiare il comportamento di calcolo base.
  * La catena di decoratori può essere modificata o estesa a runtime, il che rende il sistema di punteggio altamente configurabile.

---

### 3) Observer Pattern

* **Cos'è**:
  Il pattern **Observer** è stato utilizzato per implementare l'aggiornamento automatico dell'interfaccia utente quando il punteggio cambia. La classe `ScoreHud` (l'Observer) ascolta i cambiamenti nel punteggio del `Player` (il Subject) e si aggiorna automaticamente senza la necessità di invocazioni manuali.

* **Come è stato utilizzato**:

  * La classe `Player` implementa l'interfaccia `ScoreListener` e notifica gli osservatori ogni volta che il punteggio cambia tramite il metodo `addRoomScore(...)`.
  * **`ScoreHud`** è registrato come listener, e ogni volta che il punteggio cambia, l'interfaccia viene aggiornata per riflettere il nuovo punteggio.

* **Beneficio**:

  * Decouple la logica di aggiornamento dell'interfaccia utente dalla logica di calcolo del punteggio.
  * Il codice del controller non ha bisogno di preoccuparsi di aggiornare la UI ogni volta che cambia il punteggio; è sufficiente che il modello (il `Player`) notifichi gli ascoltatori (come `ScoreHud`), che si occupano di eseguire l'aggiornamento.

---

### 📜 Creazione di `RoomScoreData` (o `RoomPlayerData`)

Per migliorare la tracciabilità e la gestione del punteggio per ogni stanza completata, abbiamo introdotto una nuova classe chiamata **`RoomScoreData`** (o **`RoomPlayerData`**):

* **Perché è stata creata**:
  Ogni volta che un giocatore completa una stanza, bisogna registrare il punteggio per quella stanza, il tempo impiegato e lo stato di completamento. **`RoomScoreData`** tiene traccia di questi dati specifici per ogni stanza.

* **Cosa fa**:
  La classe `RoomScoreData` memorizza:

  * **`timeTaken`**: il tempo impiegato per completarla
  * **`pointsGained`**: i punti guadagnati
  * **`completed`**: se la stanza è stata completata con successo

* **Dove viene utilizzata**:
  Ogni volta che un minigioco viene completato, la classe `Player` registra un nuovo oggetto `RoomScoreData` per la stanza. L'oggetto `RoomScoreData` viene poi usato per calcolare il punteggio totale e notificare gli ascoltatori (ad esempio l'HUD).

**Esempio di utilizzo:**

```java
// Quando il giocatore completa un minigioco:
gameState.getPlayer().addRoomScore(currentMinigameRoomId, timeTaken, pointsGained);
```

In questo caso, **`addRoomScore(...)`** aggiunge i dati alla mappa `roomScores` del `Player` con l'ID della stanza come chiave. L'oggetto `RoomScoreData` contiene le informazioni sui punti guadagnati e il tempo impiegato per completare la stanza.

---

### 🧪 Utilizzo nei Minigiochi

Ogni **minigioco** è legato al sistema di punteggio tramite la seguente interazione:

1. **Chiamata al metodo `endMinigame(boolean success)`**

   ```java
   callback.endMinigame(true);  // Se il giocatore ha vinto
   callback.endMinigame(false); // Se ha fallito
   ```

2. **Calcolo dei Punti**

   ```java
   final int pointsGained = scoring.calculate(data);
   ```

3. **Aggiornamento del Punteggio**

   ```java
   gameState.getPlayer().addRoomScore(currentMinigameRoomId, timeTaken, pointsGained);
   ```

4. **Aggiornamento dell’HUD**
   La classe `ScoreHud` osserva il punteggio del giocatore e si aggiorna automaticamente ogni volta che cambia, grazie al **Pattern Observer**.

![UML Point_System](reportImg/PSUML.PNG)
---

## Bar Minigame

La realizzazione del **Bar Minigame** (“Sort & Serve”) ha seguito un approccio didattico e rigoroso all’**Object-Oriented Programming** (OOP), basandosi su pattern solidi per ottenere modularità, estensibilità e una chiara separazione dei ruoli tra componenti.

### 🧠 Pattern Principali Utilizzati

Il minigioco sfrutta in modo massiccio i pattern **MVC (Model-View-Controller)**, **Observer**, **Builder**, **Strategy** e **Decorator**. Ogni parte del progetto è stata progettata per isolare responsabilità e facilitare la collaborazione di più sviluppatori.

---

### 1 Model-View-Controller (MVC)

* **Model:**

  * **`BarModel`**: Tiene lo stato della partita (glasses, mosse, listeners, regole di gioco) e gestisce la logica di tutte le azioni possibili.
  * **`Glass`**: Rappresenta un singolo bicchiere (con capacità e pila di colori), incluse le regole per i versamenti e i controlli di validità.
* **View:**

  * **`BarPanel`**: Pannello custom di Swing che disegna i bicchieri, le selezioni, i livelli di liquido e lo sfondo. Gestisce click utente e segnala le azioni. Qui si registra anche il **listener** di input dell’utente, **`GlassClickListener`**.
* **Controller:**

  * **`BarMinigame`**: Il “cervello” del minigioco: gestisce la partenza, lo stato di gioco, la logica di restart, la ricezione di eventi dal modello, il punteggio e il callback di fine minigioco.

**Vantaggio:**
Questa separazione permette di modificare l’aspetto grafico, la logica o le interazioni utente senza dover toccare tutte le classi.

---

### 2) Observer Pattern

* **Cosa fa:**
  Il modello (`BarModel`) notifica tutti i listener ogni volta che cambia (es. avviene un versamento o si completa il puzzle).

* **File/Interfacce Coinvolte:**

  * `PuzzleListener` (interfaccia): definisce i metodi `onPoured` e `onCompleted`.
  * Il `BarPanel` si aggiorna con repaint ogni volta che riceve una notifica; il `BarMinigame` può chiudere la finestra o segnalare la fine del minigioco tramite callback.
  * **`GlassClickListener` (view → controller):** listener specifico che collega il click di un bicchiere all’azione (versamento) da gestire.

* **Esempio:**

  ```java
  model.addListener(new PuzzleListener() {
      public void onPoured(int from, int to) { SwingUtilities.invokeLater(panel::repaint); }
      public void onCompleted() { callback.onComplete(true, elapsedSeconds); stop(); }
  });
  ```

**Vantaggio:**
Disaccoppiamento tra input utente, logica di gioco e logica di rendering. Qualunque nuova interazione (ad es. drag\&drop, tastiera, ecc.) si può gestire aggiungendo o cambiando un listener, senza modificare la struttura base.

---

### 3) Builder Pattern

* **Cosa fa:**
  Semplifica la costruzione di oggetti complessi con molti parametri, tramite una DSL fluida.

* **File Coinvolto:**

  * `BarModel.Builder`: consente di creare modelli configurabili (numero bicchieri, capacità, colori, seed, strategia di shuffle…).

* **Esempio:**

  ```java
  new BarModel.Builder()
      .numGlasses(6)
      .capacity(4)
      .colors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
      .shuffleSeed(seed)
      .shuffleStrategy(new RandomShuffleStrategy())
      .build();
  ```

**Vantaggio:**
Codice leggibile, parametri facoltativi, nessuna “costruttore telescopico”.

---

### 4) Strategy & Decorator Pattern (Sistema di Punteggio)

* **Strategy:**

  * Permette di definire diversi algoritmi di calcolo punteggio e selezionarli a runtime.
  * **File Coinvolti:**

    * `ScoringStrategy` (interfaccia): Metodo `calculate(int timeTaken, int roomId)`.
    * `TieredScoringStrategy`: Strategia base che assegna punti in base al tempo.

* **Decorator:**

  * Aggiunge comportamento (bonus/limiti) a una strategia senza modificarne il codice.
  * **File Coinvolti:**

    * `TimeBonusDecorator`: Bonus punti se finisci entro una certa soglia di secondi.
    * `CapDecorator`: Impone un tetto massimo ai punti.

* **Catena d’Uso:**

  ```java
  ScoringStrategy strategy =
    new CapDecorator(
      new TimeBonusDecorator(
        new TieredScoringStrategy(),
        30, // tempo per il bonus
        10  // punti bonus
      ),
      100 // massimo punti
    );
  ```

* **Dove viene usato:**

  * Nel controller (`BarMinigame`), la strategia viene passata al costruttore e usata al completamento:

    ```java
    int score = scoringStrategy.calculate(elapsedSeconds);
    ```

**Vantaggio:**
Sistema di punteggio totalmente configurabile: puoi cambiare strategia, stacking decoratori, o crearne di nuovi senza cambiare nulla in controller, model o view.

---

### 5) Strategy Pattern (ShuffleStrategy & RandomShuffleStrategy)

* **Cosa fa:**
  Permette di astrarre la logica di miscelamento iniziale delle bottiglie, consentendo diversi algoritmi di shuffle.

* **File Coinvolti:**

  * `ShuffleStrategy` (interfaccia): Definisce il metodo `shuffle(List<Color> pool, long seed)`.
  * `RandomShuffleStrategy`: implementa la logica di shuffle casuale.

* **Dove viene usato:**

  ```java
  .shuffleStrategy(new RandomShuffleStrategy())
  ```

**Vantaggio:**
Chiunque può aggiungere nuove modalità di inizializzazione o testare algoritmi diversi di shuffle senza modificare la logica principale del modello o controller.

---

### 6) Flow e Collegamento dei Componenti

* **Integrazione con il sistema di gioco:**

  * Il minigioco viene creato tramite la factory dei minigiochi (`MinigameFactory`) e utilizza un callback (`MinigameCallback`) per notificare la fine partita e passare punteggio/tempo al sistema principale.
* **Restart:**

  * Premendo `R` si ricrea la stessa partita usando lo stesso seed (quindi layout identico, utile per tentativi multipli e fairness).
* **Logica di scoring:**

  * Il controller tiene traccia delle mosse (`moveCount`) e del tempo, passando tutto al calcolatore di punteggio al termine.
  * Il punteggio è separato dalla UI e dalla logica di gioco, pronto per essere letto dal sistema di progressione, HUD, etc.

  ![UML Bar-Minigame](reportImg/BarUML.PNG)

**Altre responsabilità:**

* Ho creato classi relative agli NPC di spostamento con le loro implementazioni.

* Sono stato il Project Manager e Code Checker per la durata del progetto

---

### 2.2.4 Simone Brunelli

**Gestione movimento player**

### 2.2.5 Tommaso Nori

**Gestione schermata finale e fine gioco**

*(Aggiungi qui le responsabilità e le soluzioni adottate dagli altri membri del gruppo, seguendo lo stile sopra)*

---

## 3. Sviluppo

### 3.1 Testing automatizzato

Abbiamo utilizzato **JUnit 5** per i test automatici.  
Esempi di test implementati:
- Test del timer di gioco.
- Test degli input da tastiera e mouse.
- Test delle collisioni tra entità.
- Test della logica dei minigiochi (Bar, Gym, Kahoot).

### 3.2 Metodologia di lavoro

Abbiamo lavorato utilizzando **Git** come DVCS, con repository condiviso.  
La suddivisione del lavoro è stata equa, con interfacce principali progettate insieme e sviluppo parallelo delle varie componenti.  
Le modifiche sono state integrate tramite e code review.

### 3.3 Note di sviluppo

- Uso estensivo di **Optional** e **Stream API** per una gestione funzionale dei dati.
- Utilizzo di pattern come **Factory**, **Component**, **Template Method** per una maggiore modularità.
- Attenzione particolare alla separazione delle responsabilità e alla manutenibilità del codice.
- Risoluzione sistematica di warning statici (SpotBugs, Checkstyle, PMD).
- Gestione input e focus per garantire la responsività dei minigiochi.
- Implementazione di strategie di scoring modulari e riutilizzabili.

---

## 4. Commenti finali

### 4.1 Autovalutazione e lavori futuri

**Pozzati Mattia:**  
All’inizio il progetto è stato impegnativo, ma lavorando in gruppo e suddividendo le responsabilità sono riuscito a migliorare sia tecnicamente che come membro di un team. In futuro vorrei approfondire la gestione di animazioni e l’ottimizzazione delle performance.

**[Altri membri]:**  
*(Aggiungi qui le autovalutazioni degli altri membri, seguendo lo stile sopra)*

---

## A. Guida Utente

**Obiettivo:**  
Raggiungere l’uscita dell’università risolvendo tutti i minigiochi.

**Comandi di gioco:**
- **ESC:** Pausa
- **W / ↑:** Muovi verso l’alto
- **A / ←:** Muovi a sinistra
- **S / ↓:** Muovi verso il basso
- **D / →:** Muovi a destra
- **E:** Interagisci

---

## Bibliografia

- [Fesliyan Studios - Musiche di gioco]()
- [Sprite e risorse grafiche](https://www.spriters-resource.com/)
- [Ispirazione e tutorial](https://www.youtube.com/@RyiSnow)

---
