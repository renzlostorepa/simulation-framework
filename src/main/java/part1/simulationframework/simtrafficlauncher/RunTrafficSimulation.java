package part1.simulationframework.simtrafficlauncher;

import part1.simulationframework.simtrafficlauncher.impl.TrafficSimulationSingleRoadSeveralCars;
import part1.simulationframework.simtrafficlauncher.impl.TrafficSimulationSingleRoadTwoCars;
import part1.simulationframework.simtrafficlauncher.impl.TrafficSimulationWithCrossRoads;

public class RunTrafficSimulation {

    public static void main(String[] args) throws InterruptedException {

        //var simulation = new TrafficSimulationSingleRoadTwoCars();
        var simulation = new TrafficSimulationSingleRoadSeveralCars();
        //var simulation = new TrafficSimulationSingleRoadWithTrafficLightTwoCars();
        //var simulation = new TrafficSimulationWithCrossRoads();
        simulation.setup();

        RoadSimStatistics stat = new RoadSimStatistics();
        RoadSimView view = new RoadSimView();
        view.display();

        simulation.addSimulationListener(stat);
        simulation.addSimulationListener(view);
        simulation.run(10000);
    }
}
