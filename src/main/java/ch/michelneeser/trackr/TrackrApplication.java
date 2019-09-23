package ch.michelneeser.trackr;

import ch.michelneeser.trackr.dao.StatRepository;
import ch.michelneeser.trackr.model.Stat;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
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