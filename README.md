# Orbit Sim

An interactive orbital mechanics simulation built with Java.

Launch a spacecraft around a planet, experiment with different trajectories, and observe how gravity affects its motion.

## Overview

Orbit Sim is a Java-based simulation designed to demonstrate basic orbital mechanics and gravitational physics through an interactive visual interface.

The spacecraft is influenced by the planet's gravity and leaves a visible trajectory as it moves. Players can click and drag the spacecraft to change its launch velocity and trajectory.

## Features

- Interactive spacecraft controls
- Gravitational physics simulation
- Orbital trajectory visualization
- Adjustable launch direction and velocity
- Elliptical orbital paths
- Crash detection
- Escape detection
- Real-time velocity display
- Orbiting, crashed, and escaped status indicators
- Reset Simulation button
- Keyboard reset using the R key
- Java Swing graphical interface

## How It Works

The simulation calculates the distance between the spacecraft and the planet each frame.

Gravity increases as the spacecraft gets closer to the planet and decreases as it moves farther away.

The gravitational acceleration is applied to the spacecraft's velocity, which determines its position and creates the resulting orbital path.

The trajectory is stored as a series of previous spacecraft positions and displayed as a visual trail.

## Interactive Controls

### Launching

Click and drag the spacecraft to set its launch direction.

Release the mouse to launch the spacecraft.

The spacecraft launches in the opposite direction of the drag.

### Resetting

Use the **Reset Simulation** button to restart the simulation.

You can also press **R** on the keyboard.

## Simulation States

The simulation tracks three primary outcomes:

- **Orbiting** — The spacecraft is actively moving under gravitational influence.
- **Crashed** — The spacecraft entered the planet's crash radius.
- **Escaped** — The spacecraft traveled beyond the simulation's escape boundary.

## Technologies

- Java
- Java Swing
- Object-oriented programming
- Event-driven programming
- Basic physics simulation

## Requirements

- Java
- Eclipse IDE or another Java-compatible development environment

No external libraries are required.

## How to Run

Clone the repository:

```bash
git clone https://github.com/Tjordanart/orbit-sim.git
