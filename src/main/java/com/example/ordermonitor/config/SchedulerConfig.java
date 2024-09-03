package com.example.ordermonitor.config;

import com.example.ordermonitor.service.MonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig implements SchedulingConfigurer {

    private final MonitorService monitorService;

    @Bean
    public Executor taskExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
//        taskRegistrar.addFixedDelayTask(monitorService::scheduleCheckOrders,
//                Duration.ofMillis(monitorService.getOrderCheckSchedulerDelay()));
        taskRegistrar.addTriggerTask(
                monitorService::checkExchangesOrders,
                context -> {
                    Optional<Instant> lastCompletion = Optional.ofNullable(context.lastCompletion());
                    Integer delay = monitorService.getOrderCheckSchedulerDelay().get();
                    return lastCompletion.orElseGet(Instant::now).plusMillis(delay);
                }
        );


    }

}
