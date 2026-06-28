# 🃏 Cincuentazo — Mini Proyecto #3

> **Fundamentos de Programación Orientada a Eventos (750014C)**<br>
> Universidad del Valle — Periodo 2026-1

Implementación del juego de cartas **Cincuentazo**, desarrollado en **Java + JavaFX** siguiendo la arquitectura **Modelo-Vista-Controlador (MVC)**, con concurrencia mediante hilos, manejo robusto de excepciones y pruebas unitarias con JUnit.

---

## 📋 Tabla de contenidos

- [Descripción](#-descripción)
- [Reglas del juego](#-reglas-del-juego)
- [Características](#-características)
- [Requisitos](#-requisitos)
- [Instalación y ejecución](#-instalación-y-ejecución)
- [Cómo jugar](#-cómo-jugar)
- [Arquitectura](#-arquitectura)
- [Estructuras de datos](#-estructuras-de-datos)
- [Concurrencia](#-concurrencia)
- [Manejo de excepciones](#-manejo-de-excepciones)
- [Pruebas unitarias](#-pruebas-unitarias)
- [Documentación](#-documentación)
- [Tecnologías](#-tecnologías)
- [Autor](#-autor)

---

## 📖 Descripción

Cincuentazo es un juego de cartas de tipo Poker donde un jugador humano se enfrenta a 1, 2 o 3 jugadores controlados por la máquina. El objetivo es ser el último jugador en pie, evitando que la suma acumulada en la mesa exceda 50.

---

## 🎮 Reglas del juego

- En la mesa existe una **suma acumulada que no debe exceder 50**.
- Cada jugador mantiene una mano de **4 cartas**.
- En su turno, el jugador juega una carta (modificando la suma) y luego toma una del mazo.
- **Valores de las cartas:**
    - Cartas 2 a 8 y 10: suman su número.
    - Carta 9: no suma ni resta (0).
    - Cartas J, Q, K: restan 10.
    - Carta A: suma 1 o 10, según convenga.
- Si un jugador **no puede jugar** ninguna carta sin exceder 50, queda **eliminado** y sus cartas vuelven al mazo.
- El **último jugador en pie gana**.

---

## ✨ Características

- 🎯 Juego contra 1, 2 o 3 oponentes máquina seleccionables.
- 🤖 Las máquinas juegan automáticamente con tiempos realistas (2-4 segundos).
- 🃏 Cartas del jugador boca arriba; cartas de las máquinas boca abajo.
- 📊 Contador de suma de la mesa siempre visible.
- ⌨️ Interacción por mouse (clic en cartas) y teclado (ESC para volver).
- 🎨 Interfaz temática de mesa de casino con 7 heurísticas de usabilidad.

---

## 💻 Requisitos

- **Java:** JDK 17 o superior
- **Maven:** 3.6 o superior
- **Sistema operativo:** Windows, macOS o Linux

---

## ⚙️ Instalación y ejecución

### Clonar el repositorio
```bash
git clone https://github.com/jorgebelalcazar/miniproyecto-cincuentazo.git
cd miniproyecto-cincuentazo
```

### Ejecutar con Maven
```bash
mvn clean javafx:run
```

### O desde IntelliJ IDEA
Abrir el proyecto, esperar a que Maven descargue las dependencias, y ejecutar la clase `App.java`.

---

## 🎯 Cómo jugar

1. Al iniciar, selecciona contra cuántas máquinas deseas jugar (1, 2 o 3).
2. Pulsa **"Iniciar Juego"**.
3. En tu turno, haz **clic en una carta** de tu mano para jugarla.
4. La suma de la mesa se actualiza. Si tu jugada excediera 50, se rechaza.
5. Tras jugar, tomas una carta y el turno pasa a las máquinas (juegan solas).
6. Si no puedes jugar, eres eliminado. El último jugador en pie gana.
7. Pulsa **ESC** o **"Volver"** para regresar al menú inicial.

---

## 🏗️ Arquitectura

El proyecto sigue el patrón **Modelo-Vista-Controlador (MVC)**:

src/main/java/com/example/cincuentazo/

├── App.java                          # Punto de entrada

├── model/                            # MODELO (lógica del juego)

│   ├── card/                         # Suit, Rank, Card, Deck

│   ├── player/                       # Player

│   ├── game/                         # Table, GameLogic, MachineStrategy

│   └── exception/                    # Excepciones propias

├── view/                             # VISTA

│   ├── ViewManager.java              # Gestor centralizado de escenas

│   └── CardView.java                 # Componente visual de carta

├── controller/                       # CONTROLADOR

│   ├── StartController.java

│   ├── GameController.java

│   └── handler/                      # Interfaces y adaptadores de eventos

└── concurrency/                      # Hilos (turnos de máquina)

└── MachineTurnService.java

**Principios aplicados:** alta cohesión, bajo acoplamiento, modelo independiente de JavaFX (testeable), gestión centralizada de vistas.

---

## 📊 Estructuras de datos

Se emplean estructuras dinámicas diferentes a arreglos:

| Estructura | Ubicación | Propósito |
|---|---|---|
| `Queue<Card>` | Deck (mazo) | Tomar del frente, devolver al final (FIFO) |
| `Deque<Card>` | Table (mesa) | Pila de cartas jugadas (LIFO) |
| `Set<Card>` | Player (mano) | Cartas únicas sin duplicados |
| `Queue<Player>` | GameLogic (turnos) | Rotación circular de turnos |

---

## 🧵 Concurrencia

Las máquinas juegan automáticamente usando hilos en segundo plano (`Task` de JavaFX), sin congelar la interfaz:

- **Hilo de jugar:** la máquina espera 2-4 segundos antes de jugar una carta.
- **Hilo de tomar:** la máquina espera 1-2 segundos antes de tomar del mazo.
- La actualización de la interfaz se realiza de forma segura con `Platform.runLater()`.

---

## ⚠️ Manejo de excepciones

| Excepción | Tipo | Uso |
|---|---|---|
| `InvalidMoveException` | Checked (propia) | Jugada que excedería 50 |
| `EmptyDeckException` | Checked (propia) | Mazo agotado sin reposición |
| `GameStateException` | Unchecked (propia) | Operación en estado inválido del juego |
| `IllegalArgumentException` | Unchecked (Java) | Argumentos inválidos |

---

## 🧪 Pruebas unitarias

El proyecto incluye **6 clases de pruebas** con JUnit 6, validando la lógica del juego:

| Clase | Qué prueba |
|---|---|
| `CardTest` | Valores de las cartas |
| `DeckTest` | Comportamiento del mazo |
| `TableTest` | Suma y reglas de la mesa |
| `PlayerTest` | Gestión de la mano |
| `GameLogicTest` | Flujo del juego |
| `MachineStrategyTest` | Decisiones de la máquina |

Ejecutar las pruebas:
```bash
mvn test
```

---

## 📚 Documentación

La documentación Javadoc en inglés se encuentra en `docs/javadoc/index.html`.

Para regenerarla:
```bash
mvn javadoc:javadoc
```

---

## 🛠️ Tecnologías

- Java SE 17
- JavaFX 21
- Maven
- JUnit 6
- Scene Builder (FXML)
- Git y GitHub

---

## 👤 Autor

| Nombre                    | Código    | Correo |
|---------------------------|-----------|---|
| **Jorge Iván Belalcázar** | `2374654` | `jorge.belalcazar@correounivalle.edu.co` |

**Profesor:** Fabian Stiven Valencia Cordoba<br>
**Curso:** Fundamentos de Programación Orientada a Eventos (750014C)<br>
**Periodo:** 2026-1

---

<p align="center">
  <strong>Universidad del Valle</strong><br>
  Escuela de Ingeniería de Sistemas y Computación<br>
  Cali, Colombia — 2026
</p>