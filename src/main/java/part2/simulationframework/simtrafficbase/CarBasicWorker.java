package part2.simulationframework.simtrafficbase;

import part2.simulationframework.common.AbstractWorker;
import part2.simulationframework.common.BoundedBuffer;
import part2.simulationframework.common.ResettableCountdownLatch;
import part2.simulationframework.common.WorkerTopic;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import static part2.simulationframework.simtrafficbase.CarAgentState.*;

public class CarBasicWorker extends AbstractWorker {

    protected CarPercept currentPercept;

    private static final int CAR_NEAR_DIST = 15;
    private static final int CAR_FAR_ENOUGH_DIST = 20;
    private static final int MAX_WAITING_TIME = 2;
    private static final int MIN_DIST_ALLOWED = 5;
    private static final int CAR_DETECTION_RANGE = 30;

    public CarBasicWorker(String name, BoundedBuffer<WorkerTopic, CarInfo> bagOfTasks, ResettableCountdownLatch latch, RoadsEnv env, int dt) {
        super(name, bagOfTasks, latch, env, dt);
    }

    @Override
    protected void senseAndDecide(CarInfo carInfo) {
        /* sense */
        currentPercept = (CarPercept) this.env.getCurrentPercepts(carInfo.getCarName());

        /* decide */
        decide(this.dt, carInfo);
    }

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
                } else {
                    carInfo.currentSpeed += carInfo.acceleration * dt;
                    if (carInfo.currentSpeed >= carInfo.maxSpeed) {
                        carInfo.setState(MOVING_CONSTANT_SPEED);
                    }
                }
                break;
            case MOVING_CONSTANT_SPEED:
                if (detectedNearCar()) {
                    carInfo.setState(DECELERATING_BECAUSE_OF_A_CAR);
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
            case WAIT_A_BIT:
                carInfo.waitingTime += dt;
                if (carInfo.waitingTime > MAX_WAITING_TIME) {
                    carInfo.setState(ACCELERATING);
                }
                break;
        }

        if (carInfo.currentSpeed > 0) {
            carInfo.setSelectedAction(Optional.of(new MoveForward(carInfo.currentSpeed * dt)));
        } else carInfo.setSelectedAction(Optional.empty());
    }

    protected boolean detectedNearCar() {
        Optional<CarInfo> car = currentPercept.nearestCarInFront();
        if (car.isEmpty()) {
            return false;
        } else {
            double dist = car.get().getPos() - currentPercept.roadPos();
            return dist < CAR_NEAR_DIST;
        }
    }


    protected boolean carFarEnough() {
        Optional<CarInfo> car = currentPercept.nearestCarInFront();
        if (car.isEmpty()) {
            return true;
        } else {
            double dist = car.get().getPos() - currentPercept.roadPos();
            return dist > CAR_FAR_ENOUGH_DIST;
        }
    }

    @Override
    protected void act(CarInfo info) {
        if (info.getSelectedAction().isPresent()) {
            switch (info.getSelectedAction().get()) {
                case MoveForward mv: {
                    Road road = info.getRoad();
                    RoadsEnv env = (RoadsEnv) this.env;
                    Optional<CarInfo> nearestCar = env.getNearestCarInFront(road, info.getPos(), CAR_DETECTION_RANGE);

                    if (!nearestCar.isEmpty()) {
                        double dist = nearestCar.get().getPos() - info.getPos();
                        if (dist > mv.distance() + MIN_DIST_ALLOWED) {
                            info.updatePos(info.getPos() + mv.distance());
                        }
                    } else {
                        info.updatePos(info.getPos() + mv.distance());
                    }

                    if (info.getPos() > road.getLen()) {
                        info.updatePos(0);
                    }
                    break;
                }
                default:
                    break;
            }
        } else info.updatePos(info.getPos());
    }

}
