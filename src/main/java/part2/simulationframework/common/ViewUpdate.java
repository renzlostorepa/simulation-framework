package part2.simulationframework.common;

import part2.simulationframework.simtrafficbase.CarInfo;
import part2.simulationframework.simtrafficbase.Flag;
import part2.simulationframework.simtrafficbase.Road;
import part2.simulationframework.simtrafficlauncher.RoadSimView;

import java.util.List;

public class ViewUpdate extends Thread {

    private RoadSimView view;
    private Flag done;
    private int t;
    private List<CarInfo> agents;
    private AbstractEnvironment env;

    protected ViewUpdate(AbstractEnvironment env, RoadSimView view, Flag done) {
        super("viewer");
        this.view = view;
        this.done = done;

        this.env = env;
    }

    public void run() {
        while (done.isSet()) {
            try {
                view.notifyStepDone( env);
                Thread.sleep(10);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        view.notifyStepDone(env);
    }
}
