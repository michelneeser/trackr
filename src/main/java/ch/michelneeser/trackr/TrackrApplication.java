package ch.michelneeser.trackr;

import ch.michelneeser.trackr.dao.StatRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TrackrApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackrApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(StatRepository repo) {
        return (args) -> {

        };
    }

}