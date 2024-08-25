package part2.simulationframework.simtrafficbase;

import part2.simulationframework.contracts.Action;

import java.util.Optional;

public class CarInfo {

    private String carName;
    private double currentPos;
    protected double maxSpeed;
    protected double currentSpeed;
    protected double acceleration;
    protected double deceleration;

    protected int waitingTime;
    private Road road;

    protected Optional<Action> selectedAction;

    private CarAgentState state;

    public CarInfo(String name, Road road, double pos, double acc,
                   double dec,
                   double vmax) {
        this.carName = name;
        this.road = road;
        this.currentPos = pos;
        this.acceleration = acc;
        this.deceleration = dec;
        this.maxSpeed = vmax;
        state = CarAgentState.STOPPED;
    }

    public double getPos() {
        return currentPos;
    }


    public void updatePos(double pos) {
        this.currentPos = pos;
    }


    public String getCarName() {
        return carName;
    }

    public Road getRoad() {
        return road;
    }

    public CarAgentState getState() {
        return state;
    }

    public void setState(CarAgentState state) {
        this.state = state;
    }

    public Optional<Action> getSelectedAction() {
        return selectedAction;
    }

    public void setSelectedAction(Optional<Action> selectedAction) {
        this.selectedAction = selectedAction;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

}
