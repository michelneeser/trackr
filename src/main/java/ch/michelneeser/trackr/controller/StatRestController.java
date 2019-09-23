package ch.michelneeser.trackr.controller;

import ch.michelneeser.trackr.model.Stat;
import ch.michelneeser.trackr.model.StatValue;
import ch.michelneeser.trackr.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stats/api")
public class StatRestController {

    @Autowired
    private StatService statService;

    @PostMapping("/{token}")
    public ResponseEntity postStatValue(@PathVariable String token, @RequestBody StatPostRequest request) {
        Stat stat = statService.get(token).orElse(statService.create());
        stat.addStatValue(new StatValue(request.value));
        statService.save(stat);

        StatPostResponse response = new StatPostResponse();
        response.statToken = stat.getToken();
        response.text = "value successfully added";

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{token}")
    public ResponseEntity getStat(@PathVariable String token) {
        Stat stat = statService.get(token).orElse(statService.create());
        return ResponseEntity.ok(stat);
    }

    private static class StatPostRequest {
        public String value;
    }

    private static class StatPostResponse {
        public String statToken;
        public String text;
    }

}