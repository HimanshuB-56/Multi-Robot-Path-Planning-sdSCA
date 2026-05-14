public class Fitness {
    static final double WORLD_WIDTH = 100;
    static final double WORLD_HEIGHT = 100;

    public static boolean collision(double x, double y, double robotRadius, Obstacle obstacle) {

        double d = Utils.distance(x, y, obstacle.x, obstacle.y);

        return d < (robotRadius + obstacle.radius);
    }

    public static double calculate(
            Robot robot,
            Obstacle[] obstacles,
            double velocity,
            double theta
    ) {

        double newX = robot.x + velocity * Math.cos(theta);
        double newY = robot.y + velocity * Math.sin(theta);

        double fitness = 0;

        // Distance to Goal
        double goalDistance = Utils.distance(
                newX,
                newY,
                robot.goalX,
                robot.goalY
        );
        fitness += goalDistance;

        // Penalty for World Boundary
        if (newX < 0 || newX > WORLD_WIDTH || newY < 0 || newY > WORLD_HEIGHT) {
            fitness += 100000;
        }


        // Penalty for Collision with Obstaclees
        for (Obstacle obstacle : obstacles) {

            if (collision(newX, newY, robot.radius, obstacle)) {
                fitness += 100000;
            }
        }

        return fitness;
    }
}
