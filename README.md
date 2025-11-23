
# Battle of Realms - Ultimate

## Description/Overview
**Battle of Realms - Ultimate** is a text-based, turn-based battle game implemented in Java. Players select a hero from a roster of unique character classes, each featuring distinct abilities and strategic advantages. The game simulates a battle between the chosen hero and a variety of enemies, leveraging object-oriented programming (OOP) concepts to manage character data, actions, debuffs, and game flow. **Main features** include hero/enemy selection, skill usage with cooldown and energy management, debuff application, healing, and an automatic replay/summary system.

**Problem it solves:** This project demonstrates how to structure a moderately complex game using OOP, encouraging strategic thinking and providing a fun, replayable console experience.

## OOP Concepts Applied

- **Encapsulation:** Each character (players and enemies) is represented as its own class, encapsulating health, attack stats, debuffs, skills, and actions. Variables are kept private or protected.
- **Inheritance:** `Player` and `Enemy` classes inherit from the abstract base class `GameCharacter`, sharing common behavior and attributes while allowing specialization.
- **Polymorphism:** Actions such as `skill1`, `skill2`, `skill3`, and `ultimate` are defined as abstract methods in `GameCharacter` and implemented differently for each child class, allowing dynamic invocation based on character type.
- **Abstraction:** The use of interfaces and abstract classes to define generic actions (e.g. attacks, debuffs) while hiding implementation details.
- **Modularity:** Classes are split by role (`Player`, `Enemy`, specific character types), supporting extensions and maintainability.

## Program Structure

- **MainApp:** Handles overall game flow, user input, replay logic, and battle loop.
- **GameCharacter (abstract):** Base class for all characters, containing stats (health, energy), debuff systems, attack calculations, turn management.
- **Player (abstract):** Inherits from `GameCharacter`, represents playable heroes. Each hero (Assassin, Avenger, Barbarian, Devourer, Gambler, Inquisitor, Necromancer, Shapeshifter) extends `Player` and customizes skills/ultimates.
- **Enemy (abstract):** Inherits from `GameCharacter`, with enemy AI for skills/actions. Each enemy (ArcaneWraith, EarthGolem, etc.) extends `Enemy` and customizes attack routines.
- **Relationships:**  
  - `MainApp` uses `Player` and `Enemy` factories to initialize combatants.
  - Skills and ultimates interact polymorphically.

**Text-Based Class Diagram:**
```
MainApp
 │
GameCharacter (abstract)
 ├── Player (abstract)
 │    ├── Assassin
 │    ├── Avenger
 │    ├── Barbarian
 │    ├── Devourer
 │    ├── Gambler
 │    ├── Inquisitor
 │    ├── Necromancer
 │    └── Shapeshifter
 └── Enemy (abstract)
      ├── ArcaneWraith
      ├── EarthGolem
      ├── FrostWraith
      ├── InfernoTitan
      ├── SavageBeast
      ├── ShadowOverlord
      ├── StormSerpent
      └── VenomousSerpent
```

## How to Run the Program

1. **Compile the Java source files.**
   - Make sure your working directory contains the `main`, `main.characters`, and `main.enemies` packages (as seen in the source).
   - Run:
     ```
     javac main/*.java main/characters/*.java main/enemies/*.java
     ```
2. **Run the application:**
   - Execute the main class:
     ```
     java main.MainApp
     ```
   - Follow on-screen prompts to select your hero and play.

## Sample Output

```
BATTLE OF REALMS - ULTIMATE
Choose your hero:
1. Assassin
2. Avenger
...
Enter choice: 2
An enemy appeared! Arcane Wraith

--------------------------------------------
ROUND 1
PLAYER Avenger HP: 150/150 Energy: 0/100 Heals: 2
ENEMY Arcane Wraith HP: 130/130 Energy: 0/100 Heals: 2
--------------------------------------------
Your action:
1. Basic Attack
2. Crush (Ready)
3. Siphon (Cooldown 3 turns)
4. Corruption (Cooldown 3 turns)
5. Power Unleashed (Not Ready)
6. Heal
Enter your action: 1
--- Your Turn ---
You dealt 26 damage to Arcane Wraith!

--- Enemy's Turn ---
Arcane Wraith used ARCANE BOLT!
You received 24 damage!

--- TURN SUMMARY ---
Player HP: 126/150
Enemy HP: 104/130
...
BATTLE RESULT
YOU WIN! Defeated Arcane Wraith!
--- PLAYER ACTION HISTORY ---
Round 1: 26 damage to enemy
...
Play again? (yes/no)
```

## Authors and Acknowledgements


**Authors:**  
- *James Benedict A. Praxides*
(*24-03662@g.batstate-u.edu.ph*)
- *John Lorenz G. De Alva*
(*24-06251@g.batstate-u.edu.ph*)
- *Lee Ambross M. Manalo*
(*24-07344@g.batstate-u.edu.ph*)
- *Mark Lawrence M. Cabali*
(*24-01285@g.batstate-u.edu.ph*)

**Acknowledgements:**  
- Thanks to our Instructor for guidance and feedback.
- Appreciation to fellow members.
- Java documentation and various online programming tutorials.
- Online sources such as Youtube videos and the help of AI's.

## Future Enhancements

- New hero and enemy types with more complex mechanics.
- Save/load system for ongoing campaigns.
- Enhanced AI for enemies.
- Visual/GUI interface (e.g., using JavaFX or Swing).

## References

- Java Documentation : [Oracle](https://docs.oracle.com/en/java/)
- OOP Concepts Overview : [Programiz](https://www.programiz.com/java-programming/object-oriented-programming)
- Sample tutorials : [w3schools.com](https://www.w3schools.com/), [Programiz](https://www.programiz.com/java-programming/object-oriented-programming)
- Youtube Tutorial/s : [YouTube Video](https://youtu.be/OFnFHjzbUVI?si=azjRLnBJtzfWxveg)
- AI's : [ChatGPT](https://chatgpt.com/)

---