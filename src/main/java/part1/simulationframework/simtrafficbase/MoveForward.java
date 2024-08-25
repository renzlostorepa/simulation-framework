package part1.simulationframework.simtrafficbase;

import part1.simulationframework.contracts.Action;

public record MoveForward(double distance) implements Action {
}
