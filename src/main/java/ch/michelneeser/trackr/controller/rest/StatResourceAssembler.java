package ch.michelneeser.trackr.controller.rest;

import ch.michelneeser.trackr.model.Stat;
import ch.michelneeser.trackr.model.StatValue;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class StatResourceAssembler implements ResourceAssembler<Stat, Resource<Stat>> {

    @Override
    public Resource<Stat> toResource(Stat stat) {
        return new Resource<>(stat,
                linkTo(methodOn(StatRestController.class).getStat(stat.getToken())).withSelfRel(),
                linkTo(methodOn(StatRestController.class).getStatValues(stat.getToken())).withRel("values"));
    }

    public Resource<StatValue> toResource(Stat stat, StatValue statValue) {
        return new Resource(statValue,
                linkTo(methodOn(StatRestController.class).getStatValue(stat.getToken(), statValue.getId())).withSelfRel(),
                linkTo(methodOn(StatRestController.class).getStatValues(stat.getToken())).withRel("values"),
                linkTo(methodOn(StatRestController.class).getStat(stat.getToken())).withRel("stat"));
    }

    public Resources<Resource<StatValue>> statValuesToResources(Stat stat) {
        List<Resource<StatValue>> resources = stat.getStatValues().stream()
                .map(value -> toResource(stat, value))
                .collect(Collectors.toList());

        return new Resources<>(resources,
                linkTo(methodOn(StatRestController.class).getStatValues(stat.getToken())).withSelfRel(),
                linkTo(methodOn(StatRestController.class).getStat(stat.getToken())).withRel("stat"));
    }

}