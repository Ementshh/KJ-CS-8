# Touhou OOP Practicum - Module 8: Command & Observer Patterns

## 🎯 Purpose & Design Framework (Tujuan & Modul Design)

Module 8 introduces students to input decoupling and event-driven UI updates through two core Object-Oriented Design Patterns:

1. **Command Pattern**: Encapsulates player actions (`MoveCommand`, `ShootCommand`, `FocusCommand`, `BombCommand`) into standalone Command objects. Decouples raw hardware input handling (`InputHandler`) from `Player` execution.
2. **Observer Pattern**: Implements event-driven UI updates where `Player` acts as a Subject notifying registered `GameObserver` listeners (`GameHUD`) of stat changes (`Score`, `HP`, `Spell Cards / Bombs`, `Power`, `Graze`).

---

## 👨‍💻 Student Implementation Expectations (Tugas Praktikan)

Practikans (students) are expected to implement the following core components in this module:

### 1. Command Pattern (`com.netlab.frontend.commands`)
- **`Command` Interface**:
  ```java
  public interface Command {
      void execute(Player player, BulletManager bulletManager);
  }
  ```
- **Concrete Commands**:
  - `MoveCommand(dx, dy)`: Executes player movement.
  - `ShootCommand`: Triggers `player.shootBullet(bulletManager)`.
  - `FocusCommand(focused)`: Toggles Focus Mode (reduces movement speed for precise dodging).
  - `BombCommand`: Triggers Spell Card (consumes 1 bomb, clears active enemy bullets).
- **`InputHandler`**:
  Maps raw keyboard keys (`Shift`, `Z`, `X`, `WASD` / Arrows) to their corresponding `Command` execution.

### 2. Observer Pattern (`com.netlab.frontend.observers` & `Player.java`)
- **`GameObserver` Interface**:
  ```java
  public interface GameObserver {
      void onScoreChanged(long newScore);
      void onHpChanged(int currentHp);
      void onSpellCardsChanged(int currentSpellCards);
      void onPowerChanged(int currentPower);
      void onGrazeChanged(int currentGraze);
  }
  ```
- **Subject in `Player.java`**:
  Implement observer registration (`registerObserver`) and notification methods (`notifyScoreChanged`, `notifyHpChanged`, `notifySpellCardsChanged`, `notifyPowerChanged`, `notifyGrazeChanged`) triggered whenever player stats mutate.

---

## 🛠️ Pre-Made Framework Components (Framework Bawaan untuk Asisten Laboratory)

To keep the practicum focused on Command & Observer design patterns without spending time on UI rendering math:

* **`GameHUD.java` (Pre-made Observer UI)**: Provided as a pre-made UI class implementing `GameObserver`. Automatically renders the sidebar HUD ($X: 432 \dots 768$) showing Score, HP, Bombs, Power, and Graze counters along with the playfield border ($384 \times 544$).

---

## 🧪 Verification & Testing Commands

To verify compilation and run the full practicum test suite (Modules 1 through 8):

```bash
./gradlew core:runTest
```
