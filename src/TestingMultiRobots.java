import javax.swing.*;

public class TestingMultiRobots {

    static final double GOAL_THRESHOLD = 3.0;

    public static void main(String[] args) throws Exception {

        // =====================================
        // CREATE ROBOTS
        // =====================================
        Robot robot1 = new Robot(
                10,
                10,
                85,
                85,
                2
        );

        Robot robot2 = new Robot(
                85,
                10,
                10,
                85,
                2
        );

        Robot robot3 = new Robot(
                50,
                90,
                50,
                10,
                2
        );


        Robot[] robots = {robot1, robot2,robot3};

        // =====================================
        // CREATE OBSTACLES
        // =====================================
            Obstacle[] obstacles = {

                new Obstacle(50, 50, 10),
                new Obstacle(30, 30, 5),
                new Obstacle(70, 25, 5),
                new Obstacle(50, 80, 5)
            };

        // =====================================
        // WINDOW
        // =====================================
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

        // =====================================
        // MAIN LOOP
        // =====================================
        while (true) {

            step++;

            // =====================================
            // STORE NEXT POSITIONS
            // =====================================
            double[] nextX = new double[robots.length];
            double[] nextY = new double[robots.length];

            // =====================================
            // OPTIMIZE EACH ROBOT
            // =====================================
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

            // =====================================
            // COLLISION CHECKS
            // =====================================
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

            // =====================================
            // UPDATE POSITIONS
            // =====================================
            for (int i = 0; i < robots.length; i++) {

                if (!blocked[i]) {

                    robots[i].x = nextX[i];
                    robots[i].y = nextY[i];
                }
            }

            // =====================================
            // REPAINT
            // =====================================
            panel.repaint();

            // =====================================
            // PRINT ROBOTS
            // =====================================
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

            // =====================================
            // CHECK GOALS
            // =====================================
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

            // =====================================
            // END CONDITION
            // =====================================
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