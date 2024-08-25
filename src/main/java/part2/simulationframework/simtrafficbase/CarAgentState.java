package part2.simulationframework.simtrafficbase;

public enum CarAgentState {
    STOPPED, ACCELERATING,
    DECELERATING_BECAUSE_OF_A_CAR,
    DECELERATING_BECAUSE_OF_A_NOT_GREEN_SEM,
    WAIT_A_BIT, MOVING_CONSTANT_SPEED,
    WAITING_FOR_GREEN_SEM
}
