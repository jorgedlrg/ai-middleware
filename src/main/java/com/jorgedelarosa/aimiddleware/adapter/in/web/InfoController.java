package com.jorgedelarosa.aimiddleware.adapter.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jorge
 */
@RestController
public class InfoController {

    @GetMapping("/info")
    public String info(@RequestParam(value = "echo", defaultValue = "hello") String echo) {
        return echo;
    }

}
