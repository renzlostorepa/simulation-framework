package part2.simulationframework.contracts;

public interface InputListener {

    void start(int numSteps) throws InterruptedException;

    void stop();
}
