public class SdSCAOptimizer {

    static final int POPULATION = 30;
    static final int ITERATIONS = 60;

    static final double MIN_V = 0.2;
    static final double MAX_V = 3.0;

    static final double MIN_THETA = 0.0;
    static final double MAX_THETA = 2 * Math.PI;


    // STRATEGY SELECTION

    public static int selectStrategy(double[] probabilities) {

        double r = Utils.rand.nextDouble();
        double cumulative = 0;

        for (int i = 0; i < probabilities.length; i++) {

            cumulative += probabilities[i];

            if (r <= cumulative) {
                return i;
            }
        }

        return probabilities.length - 1;
    }
    // MAIN OPTIMIZER
    // RETURNS [velocity, theta]
    public static double[] optimizeMovement(Robot robot, Obstacle[] obstacles) {

        double[][] population = new double[POPULATION][2];
        double[] fitnessArray = new double[POPULATION];

        // INITIAL POPULATION
        for (int i = 0; i < POPULATION; i++) {

            population[i][0] = Utils.randomRange(MIN_V, MAX_V);
            population[i][1] = Utils.randomRange(MIN_THETA, MAX_THETA);

            fitnessArray[i] = Fitness.calculate(
                    robot,
                    obstacles,
                    population[i][0],
                    population[i][1]
            );
        }
        // GLOBAL BEST
        double[] best = population[0].clone();
        double bestFitness = fitnessArray[0];

        for (int i = 1; i < POPULATION; i++) {

            if (fitnessArray[i] < bestFitness) {
                bestFitness = fitnessArray[i];
                best = population[i].clone();
            }
        }
        // ADAPTIVE STRATEGIES
        double[] probabilities = {0.25, 0.25, 0.25, 0.25};
        int[] success = {1, 1, 1, 1};

        // MAIN ITERATION LOOP
        for (int t = 0; t < ITERATIONS; t++) {

            for (int i = 0; i < POPULATION; i++) {

                int strategy = selectStrategy(probabilities);

                double[] newSolution = population[i].clone();

                int r1 = Utils.rand.nextInt(POPULATION);
                int r2 = Utils.rand.nextInt(POPULATION);
                int r3 = Utils.rand.nextInt(POPULATION);

                double F = 0.8;

                for (int d = 0; d < 2; d++) {
                    // STRATEGY 1
                    if (strategy == 0) {

                        newSolution[d] += Utils.rand.nextDouble()
                                * Math.sin(Utils.randomRange(0, 2 * Math.PI))
                                * Math.abs(best[d] - population[i][d]);
                    }
                    // STRATEGY 2
                    else if (strategy == 1) {

                        newSolution[d] = population[r1][d]
                                + F * (population[r2][d] - population[r3][d]);
                    }
                    // STRATEGY 3
                    else if (strategy == 2) {

                        newSolution[d] += F * (
                                best[d]
                                        - population[i][d]
                                        + population[r1][d]
                                        - population[r2][d]
                        );
                    }
                    // STRATEGY 4
                    else {

                        newSolution[d] += Utils.rand.nextDouble()
                                * (population[r1][d] - population[i][d])
                                + F * (population[r2][d] - population[r3][d]);
                    }
                }

                // CLAMP VELOCITY

                newSolution[0] = Math.max(MIN_V, Math.min(MAX_V, newSolution[0]));


                // NORMALIZE ANGLE
                while (newSolution[1] < 0) {
                    newSolution[1] += 2 * Math.PI;
                }

                while (newSolution[1] > 2 * Math.PI) {
                    newSolution[1] -= 2 * Math.PI;
                }

                double newFitness = Fitness.calculate(
                        robot,
                        obstacles,
                        newSolution[0],
                        newSolution[1]
                );

                // GREEDY SELECTION
                if (newFitness < fitnessArray[i]) {

                    population[i] = newSolution;
                    fitnessArray[i] = newFitness;

                    success[strategy]++;
                }

                // UPDATE GLOBAL BEST
                if (fitnessArray[i] < bestFitness) {
                    bestFitness = fitnessArray[i];
                    best = population[i].clone();
                }
            }

            // UPDATE STRATEGY PROBABILITIES
            double total = 0;

            for (int s : success) {
                total += s;
            }

            for (int i = 0; i < 4; i++) {
                probabilities[i] = success[i] / total;
            }
        }

        return best;
    }
}