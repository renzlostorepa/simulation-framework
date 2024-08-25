package part2.simulationframework.contracts;

import part2.simulationframework.common.AbstractEnvironment;
import part2.simulationframework.simtrafficbase.CarInfo;

import java.util.List;

public interface SimulationListener {

    /**
     * Called at the beginning of the simulation
     *
     * @param t
     * @param agents
     * @param env
     */
    void notifyInit(int t, List<CarInfo> agents, AbstractEnvironment env);

    /**
     * Called at each step, updater all updates
     *
     * @param t
     * @param agents
     * @param env
     */
    void notifyStepDone(int t, List<CarInfo> agents, AbstractEnvironment env);
}