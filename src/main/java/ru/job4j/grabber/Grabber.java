package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Starts grabbing process of site.
 */
public class Grabber implements Grab {
    /**
     * Grabber configs.
     */
    private final Properties cfg = new Properties();

    /**
     * Url of site to grab.
     */
    private String url;

    /**
     * Initializes storage.
     *
     * @return new DB storage object
     * to save results.
     */
    public Store store() {
        return new PsqlStore(cfg);
    }

    /**
     * Initializes simple scheduler for
     * periodical referring to site with
     * the current {@link Grabber#url}.
     *
     * @return Scheduler of process.
     * @throws SchedulerException If there were any scheduler
     *                            exceptions.
     */
    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    /**
     * Initializes configs from property file.
     *
     * @throws IOException if there were any IO errors.
     */
    public void cfg() throws IOException {
        try (InputStream in = PsqlStore.class.getClassLoader()
                .getResourceAsStream("app.properties")) {
            cfg.load(in);
        }
    }

    /**
     * Initializes url value from configs.
     */
    private void setUrl() {
        url = cfg.getProperty("url");
    }

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
    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        setUrl();
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        data.put("link", url);
        JobDetail job = newJob(GrabJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(cfg.getProperty("time")))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    public static class GrabJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            String url = (String) map.get("link");
            for (Post post : parse.list(url)) {
                store.save(post);
            }
        }
    }

    public void web(Store store) {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(Integer.parseInt(cfg.getProperty("port")))) {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    try (OutputStream out = socket.getOutputStream()) {
                        out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                        out.write(("<html><head><meta http-equiv=\"Content-Type\" "
                                + "content=\"text/html; charset=UTF-8\"></head><body>\n")
                                .getBytes());
                        for (Post post : store.getAll()) {
                            out.write(post.toString().getBytes());
                            out.write("<br><br>".getBytes());
                        }
                        out.write(("</body></html>\n").getBytes());
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    public static void main(String[] args) throws Exception {
        Grabber grab = new Grabber();
        grab.cfg();
        Scheduler scheduler = grab.scheduler();
        Store store = grab.store();
        grab.init(new SqlRuParse(), store, scheduler);
        grab.web(store);
    }
}
