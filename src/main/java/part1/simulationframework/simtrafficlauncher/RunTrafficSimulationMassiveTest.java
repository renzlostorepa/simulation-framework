package part1.simulationframework.simtrafficlauncher;

import part1.simulationframework.common.TimeStatisticsState;
import part1.simulationframework.simtrafficlauncher.impl.TrafficSimulationSingleRoadMassiveNumberOfCars;

public class RunTrafficSimulationMassiveTest {

    public static void main(String[] args) throws InterruptedException {

        int numCars = 5000;
        int nSteps = 1000;

        var simulation = new TrafficSimulationSingleRoadMassiveNumberOfCars(numCars);
        simulation.setup();

        log("Running the simulation: " + numCars + " cars, for " + nSteps + " steps ...");

        simulation.run(nSteps);
        TimeStatisticsState timeStatisticsState = simulation.getTimeStatisticsState();
        long d = timeStatisticsState.getSimulationDuration();
        log("Completed in " + d + " ms - average time per step: " + timeStatisticsState.getAverageTimePerCycle() + " ms");
    }

    private static void log(String msg) {
        System.out.println("[ SIMULATION ] " + msg);
    }
}
