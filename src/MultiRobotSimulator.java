import javax.swing.*;
import java.util.ArrayList;

public class MultiRobotSimulator {

    static final double GOAL_THRESHOLD = 3.0;

    public static void main(String[] args) throws Exception {

        // Create Objects Robot
        Robot robot1 = new Robot(
                10,
                15,
                90,
                85,
                2
        );

        Robot robot2 = new Robot(
                90,
                15,
                10,
                85,
                2
        );

        Robot[] robots = {robot1, robot2};

        // Create Obstacles objects
        Obstacle[] obstacles = {
                new Obstacle(50, 50, 8),
                new Obstacle(25, 25, 5),
                new Obstacle(70, 70, 5),
                new Obstacle(65, 35, 5),
                new Obstacle(30, 75, 5),
        };

        // WINDOW
        JFrame frame = new JFrame("Multi Robot sdSCA");

        SimulationPanel panel = new SimulationPanel(
                robots,
                obstacles
        );

        frame.add(panel);

        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);

        int step = 0;

        // Main Loop
        while (true) {

            step++;

            
            // Store Next Positions
            
            double[] nextX = new double[robots.length];
            double[] nextY = new double[robots.length];

            
            // Optimize each Robot
            for (int i = 0; i < robots.length; i++) {

                double[] bestMove =
                        SdSCAOptimizer.optimizeMovement(
                                robots[i],
                                obstacles
                        );

                double velocity = bestMove[0];
                double theta = bestMove[1];

                nextX[i] =
                        robots[i].x
                                + velocity * Math.cos(theta);

                nextY[i] =
                        robots[i].y
                                + velocity * Math.sin(theta);
            }

            // Collision checks
            boolean[] blocked = new boolean[robots.length];

            for (int i = 0; i < robots.length; i++) {

                for (int j = i + 1; j < robots.length; j++) {

                    double d = Utils.distance(
                            nextX[i],
                            nextY[i],
                            nextX[j],
                            nextY[j]
                    );

                    if (d < robots[i].radius
                            + robots[j].radius
                            + 6) {

                        blocked[j] = true;
                    }
                }
            }

            // Update Positions
            for (int i = 0; i < robots.length; i++) {

                if (!blocked[i]) {

                    robots[i].x = nextX[i];
                    robots[i].y = nextY[i];
                }
            }

            
            // Repaint
            panel.repaint();

            // Print Robots
            System.out.println("STEP " + step);

            for (int i = 0; i < robots.length; i++) {

                System.out.println(
                        "Robot " + (i + 1)
                                + ": ("
                                + String.format("%.2f", robots[i].x)
                                + ", "
                                + String.format("%.2f", robots[i].y)
                                + ")"
                );
            }

            System.out.println("---------------------");

            // Check Goals
            boolean allReached = true;

            for (Robot robot : robots) {

                double goalDistance = Utils.distance(
                        robot.x,
                        robot.y,
                        robot.goalX,
                        robot.goalY
                );

                if (goalDistance >= GOAL_THRESHOLD) {
                    allReached = false;
                }
            }

            // End Condition
            
            if (allReached) {

                System.out.println(
                        "ALL ROBOTS REACHED GOALS!"
                );

                break;
            }

            Thread.sleep(150);

            if (step > 400) {

                System.out.println(
                        "SIMULATION STOPPED"
                );

                break;
            }
        }
    }
}