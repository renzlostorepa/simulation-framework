package part2.simulationframework.simtrafficlauncher.impl;

import part2.simulationframework.common.AbstractSimulation;
import part2.simulationframework.common.ResettableCountdownLatch;
import part2.simulationframework.simtrafficbase.*;
import part2.simulationframework.simtrafficlauncher.RoadSimView;

public class TrafficSimulationSingleRoadWithTrafficLightTwoCars extends AbstractSimulation {

    public TrafficSimulationSingleRoadWithTrafficLightTwoCars(RoadSimView view) {
        super(view);
    }

    public void setup() {

        this.setupTimings(0, 1);

        RoadsEnv env = new RoadsEnv();
        this.setupEnvironment(env);

        Road r = env.createRoad(new P2d(0, 300), new P2d(1500, 300));

        TrafficLight tl = env.createTrafficLight(new P2d(740, 300), TrafficLight.TrafficLightState.GREEN, 75, 25, 100);
        r.addTrafficLight(tl, 740);

        CarInfo car1 = new CarInfo("car-1", r, 0, 0.1, 0.3, 6);
        env.registerNewCar(car1.getCarName(), car1);
        this.addAgent(car1);
        CarInfo car2 = new CarInfo("car-2", r, 100, 0.1, 0.3, 5);
        env.registerNewCar(car2.getCarName(), car2);
        this.addAgent(car2);

        latch = new ResettableCountdownLatch(agents.size());
        for (int i = 0; i < this.numOfWorkers; i++) {
            new CarExtendedWorker("w" + i, bagOfTasks, latch, env, dt).start();
        }
        this.syncWithTime(25);
    }
}
