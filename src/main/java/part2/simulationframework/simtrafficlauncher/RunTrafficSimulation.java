package part2.simulationframework.simtrafficlauncher;

import part2.simulationframework.simtrafficbase.Controller;
import part2.simulationframework.simtrafficlauncher.impl.TrafficSimulationWithCrossRoads;

public class RunTrafficSimulation {

    public static void main(String[] args) {
        RoadSimView view = new RoadSimView(100);

        Controller controller = new Controller(view);
        view.addListener(controller);
        view.display();
    }
}
