package part2.simulationframework.simtrafficbase;

import part2.simulationframework.contracts.Percept;

import java.util.Optional;

public record CarPercept(double roadPos, Optional<CarInfo> nearestCarInFront, Optional<TrafficLightInfo> nearestSem) implements Percept { }
