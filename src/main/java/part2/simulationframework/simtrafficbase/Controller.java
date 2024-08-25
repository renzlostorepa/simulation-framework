package part2.simulationframework.simtrafficbase;

import part2.simulationframework.simtrafficlauncher.RoadSimStatistics;
import part2.simulationframework.contracts.InputListener;
import part2.simulationframework.simtrafficlauncher.RoadSimView;
import part2.simulationframework.simtrafficlauncher.impl.TrafficSimulationSingleRoadTwoCars;
import part2.simulationframework.simtrafficlauncher.impl.TrafficSimulationSingleRoadWithTrafficLightTwoCars;
import part2.simulationframework.simtrafficlauncher.impl.TrafficSimulationWithCrossRoads;
import part2.simulationframework.simtrafficlauncher.impl.TrafficSimulationSingleRoadSeveralCars;


public class Controller implements InputListener {

    private Flag stopFlag;
    private RoadSimView view;

    public Controller(RoadSimView view) {
        this.stopFlag = new Flag();
        this.view = view;
    }

    public synchronized void start(int numSteps) throws InterruptedException {
        stopFlag.set();
        //var simulation = new TrafficSimulationSingleRoadTwoCars(view);
        //var simulation = new TrafficSimulationSingleRoadSeveralCars(view);
        //var simulation = new TrafficSimulationSingleRoadWithTrafficLightTwoCars(view);
        var simulation = new TrafficSimulationWithCrossRoads(view);
        simulation.setup();
        RoadSimStatistics stat = new RoadSimStatistics();
        simulation.addSimulationListener(stat);
        simulation.setNumStep(numSteps);
        simulation.setFlag(stopFlag);
        simulation.start();
    }

    public synchronized void stop() {
        stopFlag.reset();
    }

}