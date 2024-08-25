package part1.simulationframework.simtrafficlauncher.impl;

import part1.simulationframework.common.AbstractSimulation;
import part1.simulationframework.common.ResettableCountdownLatch;
import part1.simulationframework.simtrafficbase.*;

public class TrafficSimulationSingleRoadMassiveNumberOfCars extends AbstractSimulation {

    private int numCars;

    public TrafficSimulationSingleRoadMassiveNumberOfCars(int numCars) {
        super();
        this.numCars = numCars;
    }

    public void setup() {
        this.setupTimings(0, 1);

        RoadsEnv env = new RoadsEnv();
        this.setupEnvironment(env);

        Road road = env.createRoad(new P2d(0, 300), new P2d(15000, 300));
        latch = new ResettableCountdownLatch(numCars);

        for (int i = 0; i < numCars; i++) {

            String carId = "car-" + i;
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
            /* no sync with wall-time */
        }

        for (int i = 0; i < numOfWorkers; i++) {
            new CarBasicWorker("w-" + i, bagOfTasks, latch, env, dt).start();
        }

    }
}
