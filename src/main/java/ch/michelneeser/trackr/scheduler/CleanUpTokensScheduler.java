package ch.michelneeser.trackr.scheduler;

import ch.michelneeser.trackr.dao.StatRepository;
import ch.michelneeser.trackr.model.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
public class CleanUpTokensScheduler {

    private static final Logger logger = LoggerFactory.getLogger(CleanUpTokensScheduler.class);
    private static final int CLEANUP_THRESHOLD_HOURS = 6;

    @Autowired
    private StatRepository repo;

    @Scheduled(fixedRate = 5 * 60000) // 5 mins
    public void cleanUpUnusedTokens() {
        LocalDateTime threeHoursAgo = LocalDateTime.now().minusHours(CLEANUP_THRESHOLD_HOURS);
        List<Stat> statsFromAtLeastThreeHoursAgo = repo.findByCreateDateLessThan(
                Date.from(threeHoursAgo.atZone(ZoneId.systemDefault()).toInstant()));
        statsFromAtLeastThreeHoursAgo.stream()
            .filter(stat -> stat.getStatValues().size() == 0)
            .forEach(stat -> {
                repo.delete(stat);
                logger.debug("{} has been cleaned up, because it was not in use for {} hours", stat, CLEANUP_THRESHOLD_HOURS);
            });
    }

}