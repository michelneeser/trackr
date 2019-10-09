package ch.michelneeser.trackr.controller;

import ch.michelneeser.trackr.model.Stat;
import ch.michelneeser.trackr.model.StatValue;
import ch.michelneeser.trackr.service.StatService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/stats")
public class StatController {

    @Autowired
    private StatService statService;

    @GetMapping({"", "/"})
    public String newStat() {
        return getRedirectToNewStat();
    }

    @GetMapping("/{token}")
    public String stat(@PathVariable String token, Model model) {
        Optional<Stat> statOptional = statService.get(token);

        if (statOptional.isPresent()) {
            Stat stat = statOptional.get();
            List<StatValue> statValues = stat.getStatValues();

            statValues = statValues.stream()
                    .sorted(Comparator.comparing(StatValue::getCreateDate).reversed())
                    .collect(Collectors.toList());

            model.addAttribute("stat", stat);
            model.addAttribute("hasName", StringUtils.isNotBlank(stat.getName()));
            model.addAttribute("statValues", statValues);
            model.addAttribute("hasStatValues", statValues.size() > 0);
            return "stat";
        }
        else {
            return getRedirectToNewStat();
        }
    }

    private String getRedirectToNewStat() {
        Stat stat = statService.create();
        return "redirect:/stats/" + stat.getToken();
    }

}