package part1.simulationframework.common;

public class TimeStatisticsState {
    /* in the case of sync with wall-time */
    private boolean toBeInSyncWithWallTime;
    private int nStepsPerSec;

    /* for time statistics*/
    private long currentWallTime;
    private long startWallTime;
    private long endWallTime;
    private long averageTimePerStep;

    private long timePerStep;

    public TimeStatisticsState() {
        this.toBeInSyncWithWallTime = false;
        this.timePerStep = 0;
    }

    public void startWallTime() {
        this.startWallTime = System.currentTimeMillis();
    }

    public void setCurrentWallTime() {
        this.currentWallTime = System.currentTimeMillis();
    }

    public void updateTimePerStep() {
        this.timePerStep += System.currentTimeMillis() - this.currentWallTime;
    }

    public void setEndWallTime(int numSteps) {
        this.endWallTime = System.currentTimeMillis();
        this.averageTimePerStep = timePerStep / numSteps;
    }

    /* method to sync with wall time at a specified step rate */
    public void syncWithWallTime() {
        try {
            long newWallTime = System.currentTimeMillis();
            long delay = 1000 / this.nStepsPerSec;
            long wallTimeDT = newWallTime - currentWallTime;
            if (wallTimeDT < delay) {
                Thread.sleep(delay - wallTimeDT);
            }
        } catch (Exception ex) {
        }
    }

    public boolean isToBeInSyncWithWallTime() {
        return toBeInSyncWithWallTime;
    }


    public long getSimulationDuration() {
        return endWallTime - startWallTime;
    }

    public long getAverageTimePerCycle() {
        return averageTimePerStep;
    }

    protected void syncWithTime(int nCyclesPerSec) {
        this.toBeInSyncWithWallTime = true;
        this.nStepsPerSec = nCyclesPerSec;
    }
}
