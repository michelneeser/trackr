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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

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

    @PutMapping("/{token}")
    public Resource<Stat> updateStat(@PathVariable String token, @RequestBody UpdateStatRequest request) {
        Stat stat = getStatOrThrow(token);
        stat.setName(request.name);
        statService.save(stat);

        return assembler.toResource(stat);
    }

    @DeleteMapping("/{token}")
    public ResponseEntity deleteToken(@PathVariable String token) {
        Stat stat = getStatOrThrow(token);
        statService.delete(stat);

        return ResponseEntity.ok(String.format("stat '%s' deleted", token));
    }

    @GetMapping("/{token}/values")
    public Resources<Resource<StatValue>> getStatValues(@PathVariable String token) {
        Stat stat = getStatOrThrow(token);
        return assembler.statValuesToResources(stat);
    }

    @PostMapping("/{token}/values")
    public ResponseEntity createStatValue(@PathVariable String token, @RequestBody CreateStatValueRequest request) {
        if (!StringUtils.isNotBlank(request.value)) {
            throw new NoValueProvidedException(token);
        }

        Stat stat = getStatOrThrow(token);
        StatValue newStatValue = stat.addStatValue(request.value);
        statService.save(stat);
        URI location = toURI(assembler.toResource(stat, newStatValue));

        return ResponseEntity.created(location).body(assembler.statValuesToResources(stat));
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

    @DeleteMapping("/{token}/values/{statValueId}")
    public ResponseEntity deleteStatValue(@PathVariable String token, @PathVariable long statValueId) {
        Stat stat = getStatOrThrow(token);
        boolean deleteSuccessful = statService.delete(stat, statValueId);

        if (deleteSuccessful) {
            return ResponseEntity.ok(String.format("value '%d' of stat '%s' deleted", statValueId, token));
        }
        else {
            return ResponseEntity.badRequest().body(String.format("could not delete value '%d' of stat '%s'", statValueId, token));
        }
    }

    private Stat getStatOrThrow(String token) {
        return statService.get(token).orElseThrow(() -> new StatNotFoundException(token));
    }

    private URI toURI(Resource resource) {
        try {
            return new URI(resource.getLink("self").getHref());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("resource has not a valid self link");
        }
    }

    private static class CreateStatValueRequest {
        public String value;
    }

    private static class UpdateStatRequest {
        public String name;
    }

}