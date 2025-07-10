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
        2. [Altri membri](#altri-membri)
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

### 2.2 Design dettagliato

#### 2.2.1 Pozzati Mattia

**Gestione della creazione delle entità:**  
Utilizzo del **Factory Pattern** per la creazione centralizzata delle entità.
Le entità sono composte tramite componenti (es. movimento, collisione, input). Le collisioni sono gestite tramite hitbox e metodi dedicati.

#### 2.2.2 Davide Amantini

**Gestione del menu:**  
Il menu iniziale consente di avviare la partita, accedere alle impostazioni e uscire. Gli input sono gestiti tramite listener generici e notificano il controller per cambiare stato.

**Gestione delle impostazioni:**  
Permette di modificare audio e musica di sottofondo, con una logica simile a quella del menu.

#### 2.2.3 Daniel Alejandro Horna

**Gestione sistema di punteggi**

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

- [Fesliyan Studios - Musiche di gioco](https://www.fesliyanstudios.com/)
- [Sprite e risorse grafiche](https://www.spriters-resource.com/)
- [Ispirazione e tutorial](https://www.youtube.com/@RyiSnow)

---

*(Personalizza i dettagli tecnici, i nomi dei membri e le sezioni secondo il tuo progetto!)* 