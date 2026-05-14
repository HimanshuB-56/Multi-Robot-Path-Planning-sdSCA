import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomInputSimulator {

    static final double GOAL_THRESHOLD = 2.0;

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println(" MULTI ROBOT CUSTOM SIMULATOR ");
        System.out.println("=================================");

        System.out.println("\nCoordinate Limits:");
        System.out.println("X : 0 to 100");
        System.out.println("Y : 0 to 100");

        System.out.println("\nRecommended:");
        System.out.println("- Robot radius: 2 to 4");
        System.out.println("- Obstacle radius: 4 to 15");

        System.out.println(
                "\nNOTE: Too many robots may cause overlaps."
        );

        System.out.println("=================================\n");
        // =====================================
        // INPUT ROBOTS
        // =====================================
        System.out.print("Enter number of robots: ");

        int robotCount = sc.nextInt();

        ArrayList<Robot> robotList =
                new ArrayList<>();

        for (int i = 0; i < robotCount; i++) {

            System.out.println(
                    "\n--- Robot " + (i + 1) + " ---"
            );

            System.out.print("Start X: ");
            double startX = sc.nextDouble();

            System.out.print("Start Y: ");
            double startY = sc.nextDouble();

            System.out.print("Goal X: ");
            double goalX = sc.nextDouble();

            System.out.print("Goal Y: ");
            double goalY = sc.nextDouble();

            System.out.print("Radius: ");
            double radius = sc.nextDouble();

            robotList.add(
                    new Robot(
                            startX,
                            startY,
                            goalX,
                            goalY,
                            radius
                    )
            );
        }

        // =====================================
        // INPUT OBSTACLES
        // =====================================
        System.out.print(
                "\nEnter number of obstacles: "
        );

        int obstacleCount = sc.nextInt();

        ArrayList<Obstacle> obstacleList =
                new ArrayList<>();

        for (int i = 0; i < obstacleCount; i++) {

            System.out.println(
                    "\n--- Obstacle " + (i + 1) + " ---"
            );

            System.out.print("X: ");
            double x = sc.nextDouble();

            System.out.print("Y: ");
            double y = sc.nextDouble();

            System.out.print("Radius: ");
            double radius = sc.nextDouble();

            obstacleList.add(
                    new Obstacle(
                            x,
                            y,
                            radius
                    )
            );
        }

        // =====================================
        // CONVERT TO ARRAYS
        // =====================================
        Robot[] robots =
                robotList.toArray(new Robot[0]);

        Obstacle[] obstacles =
                obstacleList.toArray(new Obstacle[0]);

        // =====================================
        // WINDOW
        // =====================================
        JFrame frame = new JFrame(
                "Custom Multi Robot Simulator"
        );

        SimulationPanel panel =
                new SimulationPanel(
                        robots,
                        obstacles
                );

        frame.add(panel);

        frame.pack();

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

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
            double[] nextX =
                    new double[robots.length];

            double[] nextY =
                    new double[robots.length];

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
                                + velocity
                                * Math.cos(theta);

                nextY[i] =
                        robots[i].y
                                + velocity
                                * Math.sin(theta);
            }

            // =====================================
            // COLLISION CHECKS
            // =====================================
            boolean[] blocked =
                    new boolean[robots.length];

            for (int i = 0; i < robots.length; i++) {

                for (int j = i + 1;
                     j < robots.length;
                     j++) {

                    double d = Utils.distance(
                            nextX[i],
                            nextY[i],
                            nextX[j],
                            nextY[j]
                    );

                    if (d <
                            robots[i].radius
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
            // PRINT POSITIONS
            // =====================================
            System.out.println(
                    "\nSTEP " + step
            );

            for (int i = 0; i < robots.length; i++) {

                System.out.println(
                        "Robot "
                                + (i + 1)
                                + ": ("
                                + String.format(
                                "%.2f",
                                robots[i].x
                        )
                                + ", "
                                + String.format(
                                "%.2f",
                                robots[i].y
                        )
                                + ")"
                );
            }

            // =====================================
            // CHECK GOALS
            // =====================================
            boolean allReached = true;

            for (Robot robot : robots) {

                double goalDistance =
                        Utils.distance(
                                robot.x,
                                robot.y,
                                robot.goalX,
                                robot.goalY
                        );

                if (goalDistance
                        >= GOAL_THRESHOLD) {

                    allReached = false;
                }
            }

            // =====================================
            // END CONDITION
            // =====================================
            if (allReached) {

                System.out.println(
                        "\nALL ROBOTS REACHED GOALS!"
                );

                break;
            }

            Thread.sleep(150);

            if (step > 500) {

                System.out.println(
                        "\nSIMULATION STOPPED"
                );

                break;
            }
        }

        sc.close();
    }
}