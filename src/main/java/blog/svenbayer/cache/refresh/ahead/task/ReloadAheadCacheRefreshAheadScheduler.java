package blog.svenbayer.cache.refresh.ahead.task;

import blog.svenbayer.cache.refresh.ahead.config.ReloadAheadProperties;
import blog.svenbayer.cache.refresh.ahead.service.ReloadAheadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ReloadAheadCacheRefreshAheadScheduler {

    private ReloadAheadProperties reloadAheadProperties;
    private ReloadAheadService reloadAheadService;

    @Autowired
    public ReloadAheadCacheRefreshAheadScheduler(ReloadAheadProperties reloadAheadProperties, ReloadAheadService reloadAheadService) {
        this.reloadAheadProperties = reloadAheadProperties;
        this.reloadAheadService = reloadAheadService;
    }

    @EventListener
    public void refreshCaches(ContextRefreshedEvent event) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(reloadAheadService::reloadAheadValuesOfCaches,0L, reloadAheadProperties.getRefreshAheadInterval().getSeconds(), TimeUnit.SECONDS);
    }
}
