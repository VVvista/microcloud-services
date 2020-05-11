package com.kt.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wen TingTing by 2020/5/10
 */
@RestController
public class HiController {
    @Autowired
    private CallServiceHi hiServiceCaller;

    @RequestMapping("hi")
    public String hi(@RequestParam(required = false) String name) {
        return hiServiceCaller.sayHiFromClientOne(name);
    }
}