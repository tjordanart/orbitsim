package orbitsim;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class OrbitSim {

    public static void main(String[] args) {

        // Create the main application window
        JFrame window = new JFrame("Orbit Sim");

        // Create the simulation
        SimulationPanel simulation = new SimulationPanel();

        // Create the reset button
        JButton resetButton = new JButton("Reset Simulation");
        resetButton.addActionListener(e -> simulation.resetSimulation());

        // Add the simulation and reset button
        window.add(simulation);
        window.add(resetButton, "South");

        // Configure the window
        window.setSize(1280, 750);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // Give the simulation keyboard focus
        simulation.requestFocusInWindow();
    }
}

class SimulationPanel extends JPanel {

    // Planet position
    private static final double PLANET_CENTER_X = 640;
    private static final double PLANET_CENTER_Y = 360;

    // Spacecraft starting position
    private static final double START_X = 640;
    private static final double START_Y = 100;

    // Spacecraft starting velocity
    private static final double START_VELOCITY_X = 5.0;
    private static final double START_VELOCITY_Y = 0;

    // Physics settings
    private static final double GRAVITY_CONSTANT = 500000;
    private static final double TIME_STEP = 0.016;

    // Simulation limits
    private static final double CRASH_DISTANCE = 110;
    private static final double ESCAPE_DISTANCE = 600;
    private static final int MAX_TRAJECTORY_POINTS = 500;

    // Spacecraft position and velocity
    private double spacecraftX = START_X;
    private double spacecraftY = START_Y;
    private double velocityX = START_VELOCITY_X;
    private double velocityY = START_VELOCITY_Y;

    // Stores the spacecraft's previous positions
    private final ArrayList<double[]> trajectory = new ArrayList<>();

    // Mouse launch information
    private double launchStartX;
    private double launchStartY;
    private double mouseX;
    private double mouseY;

    // Simulation state
    private boolean dragging = false;
    private boolean crashed = false;
    private boolean escaped = false;

    // Animation timer
    private final Timer timer;

    public SimulationPanel() {

        setBackground(Color.BLACK);
        setFocusable(true);

        // Update the simulation approximately 60 times per second
        timer = new Timer(16, e -> updateSimulation());
        timer.start();

        setupMouseControls();
        setupKeyboardControls();
    }

    // Set up mouse controls
    private void setupMouseControls() {

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                if (crashed || escaped) {
                    return;
                }

                double dx = e.getX() - spacecraftX;
                double dy = e.getY() - spacecraftY;

                double distance = Math.sqrt(dx * dx + dy * dy);

                // Only grab the spacecraft when the mouse is nearby
                if (distance < 30) {

                    launchStartX = e.getX();
                    launchStartY = e.getY();

                    mouseX = e.getX();
                    mouseY = e.getY();

                    dragging = true;

                    // Pause the spacecraft while aiming
                    velocityX = 0;
                    velocityY = 0;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                if (!dragging) {
                    return;
                }

                mouseX = e.getX();
                mouseY = e.getY();

                // Launch opposite the direction of the drag
                velocityX = (launchStartX - mouseX) * 0.08;
                velocityY = (launchStartY - mouseY) * 0.08;

                trajectory.clear();
                dragging = false;

                requestFocusInWindow();
            }
        });

        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {

                if (dragging) {

                    mouseX = e.getX();
                    mouseY = e.getY();

                    repaint();
                }
            }
        });
    }

    // Set up keyboard controls
    private void setupKeyboardControls() {

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                // Press R to reset
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    resetSimulation();
                }
            }
        });
    }

    // Update the spacecraft using gravitational physics
    private void updateSimulation() {

        if (crashed || escaped || dragging) {
            repaint();
            return;
        }

        // Calculate distance from spacecraft to planet
        double dx = PLANET_CENTER_X - spacecraftX;
        double dy = PLANET_CENTER_Y - spacecraftY;

        double distance = Math.sqrt(dx * dx + dy * dy);

        // Check for a crash
        if (distance < CRASH_DISTANCE) {

            crashed = true;
            velocityX = 0;
            velocityY = 0;

            repaint();
            return;
        }

        // Check for escape
        if (distance > ESCAPE_DISTANCE) {

            escaped = true;

            repaint();
            return;
        }

        // Calculate gravitational force
        double gravity = GRAVITY_CONSTANT / (distance * distance);

        // Calculate gravitational acceleration
        double accelerationX = gravity * dx / distance;
        double accelerationY = gravity * dy / distance;

        // Update velocity
        velocityX += accelerationX * TIME_STEP;
        velocityY += accelerationY * TIME_STEP;

        // Update position
        spacecraftX += velocityX;
        spacecraftY += velocityY;

        // Store trajectory point
        trajectory.add(new double[] {
            spacecraftX,
            spacecraftY
        });

        // Limit trajectory size
        if (trajectory.size() > MAX_TRAJECTORY_POINTS) {
            trajectory.remove(0);
        }

        repaint();
    }

    // Reset the simulation
    public void resetSimulation() {

        spacecraftX = START_X;
        spacecraftY = START_Y;

        velocityX = START_VELOCITY_X;
        velocityY = START_VELOCITY_Y;

        trajectory.clear();

        crashed = false;
        escaped = false;
        dragging = false;

        repaint();
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // Title
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("ORBIT SIM", 20, 35);

        // Status
        g.setFont(new Font("Arial", Font.PLAIN, 16));

        String status;

        if (crashed) {
            status = "Status: CRASHED";
        } else if (escaped) {
            status = "Status: ESCAPED";
        } else if (dragging) {
            status = "Status: AIMING";
        } else {
            status = "Status: ORBITING";
        }

        g.drawString(status, 20, 60);

        // Draw planet
        g.setColor(Color.BLUE);

        int planetSize = 200;
        int planetX = (int) PLANET_CENTER_X - planetSize / 2;
        int planetY = (int) PLANET_CENTER_Y - planetSize / 2;

        g.fillOval(
            planetX,
            planetY,
            planetSize,
            planetSize
        );

        // Draw trajectory
        g.setColor(Color.GRAY);

        for (double[] point : trajectory) {

            g.fillOval(
                (int) point[0],
                (int) point[1],
                3,
                3
            );
        }

        // Draw spacecraft
        g.setColor(Color.WHITE);

        int spacecraftSize = 20;

        g.fillOval(
            (int) spacecraftX,
            (int) spacecraftY,
            spacecraftSize,
            spacecraftSize
        );

        // Draw launch direction
        if (dragging) {

            g.setColor(Color.YELLOW);

            g.drawLine(
                (int) spacecraftX + 10,
                (int) spacecraftY + 10,
                (int) mouseX,
                (int) mouseY
            );
        }

        // Calculate speed
        double speed = Math.sqrt(
            velocityX * velocityX +
            velocityY * velocityY
        );

        // Display information
        g.setColor(Color.WHITE);

        g.drawString(
            String.format("Velocity: %.2f", speed),
            20,
            90
        );

        g.drawString(
            "Drag the spacecraft to change its trajectory",
            20,
            115
        );

        g.drawString(
            "Press R or use Reset Simulation to restart",
            20,
            140
        );

        // Display final state
        if (crashed || escaped) {

            g.setFont(new Font("Arial", Font.BOLD, 20));

            g.drawString(
                crashed ? "CRASHED" : "ESCAPED",
                20,
                175
            );
        }
    }
}