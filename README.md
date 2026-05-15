# Multi-Robot Path Planning using sdSCA-Inspired Optimization

This project presents a simplified implementation of a multi-robot path planning simulator inspired by the Self-Adaptive Differential Sine Cosine Algorithm (sdSCA).

The simulator demonstrates autonomous robot navigation in a 2D environment where robots move toward goal positions while avoiding static obstacles. The system uses optimization-based movement generation and real-time visualization using Java Swing.

---

# Features

- Multi-robot path planning simulation
- Static obstacle avoidance
- sdSCA-inspired optimization strategies
- Real-time Java Swing visualization
- Dynamic robot path generation
- Robot path trail visualization
- Coordinate grid system
- Adaptive movement optimization
- Configurable robot and obstacle input

---

# Optimization Strategies Used

The optimizer uses multiple search and update strategies including:

- Sine-based movement
- Differential update strategy
- Best-guided exploration
- Randomized hybrid exploration

The optimizer generates multiple candidate velocity-angle movements and evaluates them using a fitness function based on:

- Distance to goal
- Obstacle avoidance
- Collision penalties
- Soft obstacle repulsion

---

# Technologies Used

- Java
- Java Swing
- Metaheuristic Optimization
- sdSCA-inspired Optimization

---

# Project Structure

```text
src/
│
├── Robot.java
├── Obstacle.java
├── Fitness.java
├── SdSCAOptimizer.java
├── SimulationPanel.java
├── Utils.java
├── Main.java
