package ch.michelneeser.trackr.controller;

import ch.michelneeser.trackr.model.Stat;
import ch.michelneeser.trackr.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/stats/api")
public class StatRestController {

    @Autowired
    private StatService statService;

    @PostMapping("/{token}")
    public ResponseEntity postStatValue(@PathVariable String token, @RequestBody StatOperationRequest request) {
        Stat stat = statService.get(token).orElse(statService.create());
        long statValueId = stat.addStatValue(request.value);
        statService.save(stat);

        StatOperationResponse response = new StatOperationResponse();
        response.success = true;
        response.statToken = stat.getToken();
        response.statValueId = statValueId;
        response.text = "value successfully added";

        return getResponseEntity(response);
    }

    @GetMapping("/{token}")
    public ResponseEntity getStat(@PathVariable String token) {
        Stat stat = statService.get(token).orElse(statService.create());
        return ResponseEntity.ok(stat);
    }

    @DeleteMapping("/{token}/{statValueId}")
    public ResponseEntity deleteStatValue(@PathVariable String token, @PathVariable long statValueId) {
        StatOperationResponse response = new StatOperationResponse();
        Optional<Stat> statOptional = statService.get(token);

        if (statOptional.isPresent()) {
            Stat stat = statOptional.get();
            response.statToken = stat.getToken();
            boolean deleteSuccessful = statService.delete(stat, statValueId);

            if (deleteSuccessful) {
                response.success = true;
                response.statValueId = statValueId;
                response.text = "value successfully deleted";
            }
        }

        return getResponseEntity(response);
    }

    private ResponseEntity getResponseEntity(StatOperationResponse response) {
        return ResponseEntity.status(response.success ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }

    private static class StatOperationRequest {
        public String value;
    }

    private static class StatOperationResponse {
        public boolean success = false;
        public String statToken;
        public long statValueId;
        public String text;
    }

}