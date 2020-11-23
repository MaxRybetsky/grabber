package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {
    public static void main(String[] args) throws ClassNotFoundException {
        Properties properties = getProperties();
        System.out.println(properties);
        Class.forName(properties.getProperty("jdbc.driver"));
        try (Connection cn = DriverManager.getConnection(
                properties.getProperty("jdbc.url"),
                properties.getProperty("jdbc.login"),
                properties.getProperty("jdbc.password"))) {
            int interval = Integer.parseInt(properties.getProperty("rabbit.interval"));
            int timeout = Integer.parseInt(properties.getProperty("rabbit.timeout"));
            scheduleGo(cn, interval, timeout);
            printAllData(cn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void scheduleGo(Connection cn, int seconds, int limit)
            throws SchedulerException, InterruptedException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        JobDataMap data = new JobDataMap();
        data.put("connection", cn);
        JobDetail job = newJob(Rabbit.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(seconds)
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
        Thread.sleep(limit * 1000);
        scheduler.shutdown();
    }

    public static void printAllData(Connection cn) throws SQLException {
        String query = "select * from rabbit;";
        try (PreparedStatement statement = cn.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.println(resultSet.getLong(1));
                }
            }
        }
    }

    public static void writeDataToDB(Connection cn, long value) {
        String query = "insert into rabbit(created_date) values(?)";
        try (PreparedStatement statement = cn.prepareStatement(query)) {
            statement.setLong(1, value);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Properties getProperties() {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream("./rabbit.properties")) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext jobExecutionContext) {
            System.out.println("Rabbit runs here ...");
            Connection cn = (Connection) jobExecutionContext.getJobDetail()
                    .getJobDataMap()
                    .get("connection");
            writeDataToDB(cn, System.currentTimeMillis());
        }
    }
}
