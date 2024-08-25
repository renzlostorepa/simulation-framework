package part2.simulationframework.simtrafficbase;

import part2.simulationframework.common.BoundedBuffer;
import part2.simulationframework.common.ResettableCountdownLatch;
import part2.simulationframework.common.WorkerTopic;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import static part2.simulationframework.simtrafficbase.CarAgentState.*;


public class CarExtendedWorker extends CarBasicWorker {

    private static final int MAX_WAITING_TIME = 2;
    private static final int SEM_NEAR_DIST = 100;

    public CarExtendedWorker(String name, BoundedBuffer<WorkerTopic,CarInfo> bagOfTasks, ResettableCountdownLatch latch, RoadsEnv env, int dt) {
        super(name, bagOfTasks, latch, env, dt);
    }

    @Override
    protected void decide(int dt, CarInfo carInfo) {
        switch (carInfo.getState()) {
            case STOPPED:
                if (!detectedNearCar()) {
                    carInfo.setState(ACCELERATING);
                }
                break;
            case ACCELERATING:
                if (detectedNearCar()) {
                    carInfo.setState(DECELERATING_BECAUSE_OF_A_CAR);
                } else if (detectedRedOrOrgangeSemNear()) {
                    carInfo.setState(DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM);
                } else {
                    carInfo.currentSpeed += carInfo.acceleration * dt;
                    if (carInfo.currentSpeed >= carInfo.maxSpeed) {
                        carInfo.setState(CarAgentState.MOVING_CONSTANT_SPEED);
                    }
                }
                break;
            case MOVING_CONSTANT_SPEED:
                if (detectedNearCar()) {
                    carInfo.setState(DECELERATING_BECAUSE_OF_A_CAR);
                } else if (detectedRedOrOrgangeSemNear()) {
                    carInfo.setState(DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM);
                }
                break;
            case DECELERATING_BECAUSE_OF_A_CAR:
                carInfo.currentSpeed -= carInfo.deceleration * dt;
                if (carInfo.currentSpeed <= 0) {
                    carInfo.setState(STOPPED);
                } else if (this.carFarEnough()) {
                    carInfo.setState(WAIT_A_BIT);
                    carInfo.waitingTime = 0;
                }
                break;
            case DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM:
                carInfo.currentSpeed -= carInfo.deceleration * dt;
                if (carInfo.currentSpeed <= 0) {
                    carInfo.setState(WAITING_FOR_GREEN_SEM);
                } else if (!detectedRedOrOrgangeSemNear()) {
                    carInfo.setState(ACCELERATING);
                }
                break;
            case WAIT_A_BIT:
                carInfo.waitingTime += dt;
                if (carInfo.waitingTime > MAX_WAITING_TIME) {
                    carInfo.setState(ACCELERATING);
                }
                break;
            case WAITING_FOR_GREEN_SEM:
                if (detectedGreenSem()) {
                    carInfo.setState(ACCELERATING);
                }
                break;
        }

        if (carInfo.currentSpeed > 0) {
            carInfo.selectedAction = Optional.of(new MoveForward(carInfo.currentSpeed * dt));
        } else carInfo.setSelectedAction(Optional.empty());
    }


    private boolean detectedRedOrOrgangeSemNear() {
        Optional<TrafficLightInfo> sem = currentPercept.nearestSem();
        if (sem.isEmpty() || sem.get().sem().isGreen()) {
            return false;
        } else {
            double dist = sem.get().roadPos() - currentPercept.roadPos();
            return dist > 0 && dist < SEM_NEAR_DIST;
        }
    }

    private boolean detectedGreenSem() {
        Optional<TrafficLightInfo> sem = currentPercept.nearestSem();
        return (!sem.isEmpty() && sem.get().sem().isGreen());
    }
}

