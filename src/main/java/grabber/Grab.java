package grabber;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

/**
 * Allows to start grab process.
 */
public interface Grab {
    /**
     * The input scheduler periodically
     * launches the parser for saving
     * parsed data in the store.
     *
     * @param parse     Parser to search data.
     * @param store     Storage for saving data.
     * @param scheduler Main scheduler for
     *                  periodical launch.
     * @throws SchedulerException If smth will broke in
     *                            scheduler.
     */
    void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException;
}
