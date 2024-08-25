package part2.simulationframework.simtrafficbase;

import part2.simulationframework.contracts.Action;

public record MoveForward(double distance) implements Action {
}
