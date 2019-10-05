package ch.michelneeser.trackr.controller.rest;

import ch.michelneeser.trackr.controller.exception.NoValueProvidedException;
import ch.michelneeser.trackr.controller.exception.StatNotFoundException;
import ch.michelneeser.trackr.controller.exception.StatValueNotFoundException;
import ch.michelneeser.trackr.model.Stat;
import ch.michelneeser.trackr.model.StatValue;
import ch.michelneeser.trackr.service.StatService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/stats/api")
public class StatRestController {

    @Autowired
    private StatService statService;
    @Autowired
    private StatResourceAssembler assembler;

    @PostMapping(value={"", "/"})
    public ResponseEntity createStat() {
        Stat stat = statService.create();
        Resource resource = assembler.toResource(stat);
        URI location = toURI(resource);

        return ResponseEntity.created(location).body(resource);
    }

    @GetMapping("/{token}")
    public Resource<Stat> getStat(@PathVariable String token) {
        Stat stat = getStatOrThrow(token);
        return assembler.toResource(stat);
    }

    @GetMapping("/{token}/values")
    public Resources<Resource<StatValue>> getStatValues(@PathVariable String token) {
        Stat stat = getStatOrThrow(token);
        return assembler.statValuesToResources(stat);
    }

    @PostMapping("/{token}/values")
    public ResponseEntity createStatValue(@PathVariable String token, @RequestBody CreateStatValueRequest request) {
        if (StringUtils.isNotBlank(request.value)) {
            Stat stat = getStatOrThrow(token);
            StatValue newStatValue = stat.addStatValue(request.value);
            statService.save(stat);
            URI location = toURI(assembler.toResource(stat, newStatValue));
            return ResponseEntity.created(location).body(assembler.statValuesToResources(stat));
        }
        else {
            throw new NoValueProvidedException(token);
        }
    }

    @GetMapping("/{token}/values/{statValueId}")
    public Resource<StatValue> getStatValue(@PathVariable String token, @PathVariable long statValueId) {
        Stat stat = getStatOrThrow(token);
        StatValue statValue = stat.getStatValues().stream()
                .filter(value -> value.getId() == statValueId)
                .findFirst()
                .orElseThrow(() -> new StatValueNotFoundException(token, statValueId));

        return assembler.toResource(stat, statValue);
    }

    // TODO refactor from here!

    @PutMapping("/{token}")
    public ResponseEntity putStat(@PathVariable String token, @RequestBody CreateStatValueRequest request) {
        Optional<Stat> statOptional = statService.get(token);

        if (statOptional.isPresent()) {
            Stat stat = statOptional.get();
            stat.setName(request.value);
            statService.save(stat);
            return ResponseEntity.ok(stat);
        }

        return ResponseEntity.badRequest().body("not a valid token"); // FIXME
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

    private static class CreateStatValueRequest {
        public String value;
    }

    private static class StatOperationResponse {
        public boolean success = false;
        public String statToken;
        public long statValueId;
        public String text;
    }

    private Stat getStatOrThrow(String token) {
        return statService.get(token).orElseThrow(() -> new StatNotFoundException(token));
    }

    private URI toURI(Resource resource) {
        try {
            return new URI(resource.getLink("self").getHref());
        } catch (URISyntaxException e) {
            e.printStackTrace(); // FIXME
        }
        return null; // FIXME
    }

}