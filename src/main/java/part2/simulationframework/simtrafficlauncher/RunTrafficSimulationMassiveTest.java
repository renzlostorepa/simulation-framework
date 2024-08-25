package part2.simulationframework.simtrafficlauncher;

import part2.simulationframework.common.TimeStatisticsState;
import part2.simulationframework.simtrafficlauncher.impl.TrafficSimulationSingleRoadMassiveNumberOfCars;

public class RunTrafficSimulationMassiveTest {

    public static void main(String[] args) throws InterruptedException {

        int numCars = 5000;
        int nSteps = 100;
        RoadSimView view = new RoadSimView(nSteps);
        var simulation = new TrafficSimulationSingleRoadMassiveNumberOfCars(numCars, view);
        simulation.setup();

        log("Running the simulation: " + numCars + " cars, for " + nSteps + " steps ...");

        //simulation.run(nSteps, stopFlag);
        TimeStatisticsState timeStatisticsState = simulation.getTimeStatisticsState();
        long d = timeStatisticsState.getSimulationDuration();
        log("Completed in " + d + " ms - average time per step: " + timeStatisticsState.getAverageTimePerCycle() + " ms");
    }

    private static void log(String msg) {
        System.out.println("[ SIMULATION ] " + msg);
    }
}
