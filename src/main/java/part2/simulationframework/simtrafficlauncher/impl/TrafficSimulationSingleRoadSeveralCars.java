package part2.simulationframework.simtrafficlauncher.impl;

import part2.simulationframework.common.AbstractSimulation;
import part2.simulationframework.common.ResettableCountdownLatch;
import part2.simulationframework.simtrafficbase.*;
import part2.simulationframework.simtrafficlauncher.RoadSimView;

public class TrafficSimulationSingleRoadSeveralCars extends AbstractSimulation {

    public TrafficSimulationSingleRoadSeveralCars(RoadSimView view) {
        super(view);
    }

    public void setup() {

        this.setupTimings(0, 1);

        RoadsEnv env = new RoadsEnv();
        this.setupEnvironment(env);

        Road road = env.createRoad(new P2d(0, 300), new P2d(1500, 300));

        int nCars = 30;
        latch = new ResettableCountdownLatch(nCars);

        for (int i = 0; i < nCars; i++) {

            String carId = "car-" + i;
            // double initialPos = i*30;
            double initialPos = i * 10;

            double carAcceleration = 1; //  + gen.nextDouble()/2;
            double carDeceleration = 0.3; //  + gen.nextDouble()/2;
            double carMaxSpeed = 7; // 4 + gen.nextDouble();

            CarInfo car = new CarInfo(carId,
                    road,
                    initialPos,
                    carAcceleration,
                    carDeceleration,
                    carMaxSpeed);
            this.addAgent(car);

            env.registerNewCar(carId, car);
        }

        for (int i = 0; i < numOfWorkers; i++) {
            new CarBasicWorker("w-" + i, bagOfTasks, latch, env, dt).start();
        }

        this.syncWithTime(25);
    }
}
