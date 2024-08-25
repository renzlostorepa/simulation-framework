package part1.simulationframework.simtrafficlauncher.impl;

import part1.simulationframework.common.AbstractSimulation;
import part1.simulationframework.common.ResettableCountdownLatch;
import part1.simulationframework.simtrafficbase.*;

public class TrafficSimulationWithCrossRoads extends AbstractSimulation {
    public TrafficSimulationWithCrossRoads() {
        super();
    }

    public void setup() {

        this.setupTimings(0, 1);

        RoadsEnv env = new RoadsEnv();
        this.setupEnvironment(env);

        TrafficLight tl1 = env.createTrafficLight(new P2d(740, 300), TrafficLight.TrafficLightState.GREEN, 75, 25, 100);

        Road r1 = env.createRoad(new P2d(0, 300), new P2d(1500, 300));
        r1.addTrafficLight(tl1, 740);

        CarInfo car1 = new CarInfo("car-1", r1, 0, 0.1, 0.3, 6);
        this.addAgent(car1);
        env.registerNewCar(car1.getCarName(), car1);
        CarInfo car2 = new CarInfo("car-2", r1, 100, 0.1, 0.3, 5);
        this.addAgent(car2);
        env.registerNewCar(car2.getCarName(), car2);

        TrafficLight tl2 = env.createTrafficLight(new P2d(750, 290), TrafficLight.TrafficLightState.RED, 75, 25, 100);

        Road r2 = env.createRoad(new P2d(750, 0), new P2d(750, 600));
        r2.addTrafficLight(tl2, 290);

        CarInfo car3 = new CarInfo("car-3", r2, 0, 0.1, 0.2, 5);
        this.addAgent(car3);
        env.registerNewCar(car3.getCarName(), car3);
        CarInfo car4 = new CarInfo("car-4", r2, 100, 0.1, 0.1, 4);
        this.addAgent(car4);
        env.registerNewCar(car4.getCarName(), car4);

        latch = new ResettableCountdownLatch(agents.size());
        for (int i = 0; i < this.numOfWorkers; i++) {
            new CarExtendedWorker("w" + i, bagOfTasks, latch, env, dt).start();
        }
        this.syncWithTime(25);
    }

}
