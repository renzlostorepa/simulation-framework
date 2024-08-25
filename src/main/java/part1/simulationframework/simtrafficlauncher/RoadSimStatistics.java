package part1.simulationframework.simtrafficlauncher;

import part1.simulationframework.contracts.SimulationListener;
import part1.simulationframework.simtrafficbase.CarInfo;
import part1.simulationframework.common.AbstractEnvironment;

import java.util.List;

public class RoadSimStatistics implements SimulationListener {


    private double averageSpeed;
    private double minSpeed;
    private double maxSpeed;

    public RoadSimStatistics() {
    }

    @Override
    public void notifyInit(int t, List<CarInfo> agents, AbstractEnvironment env) {
        // TODO Auto-generated method stub
        // log("reset: " + t);
        averageSpeed = 0;
    }

    @Override
    public void notifyStepDone(int t, List<CarInfo> agents, AbstractEnvironment env) {
        double avSpeed = 0;

        maxSpeed = -1;
        minSpeed = Double.MAX_VALUE;
        for (var agent : agents) {
            CarInfo car = agent;
            double currSpeed = car.getCurrentSpeed();
            avSpeed += currSpeed;
            if (currSpeed > maxSpeed) {
                maxSpeed = currSpeed;
            } else if (currSpeed < minSpeed) {
                minSpeed = currSpeed;
            }
        }

        if (agents.size() > 0) {
            avSpeed /= agents.size();
        }
        log("average speed: " + avSpeed);
    }

    public double getAverageSpeed() {
        return this.averageSpeed;
    }

    public double getMinSpeed() {
        return this.minSpeed;
    }

    public double getMaxSpeed() {
        return this.maxSpeed;
    }


    private void log(String msg) {
        System.out.println("[STAT] " + msg);
    }

}
