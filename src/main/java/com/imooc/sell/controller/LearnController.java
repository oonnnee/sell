package com.imooc.sell.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/learn")
public class LearnController {

    @GetMapping("/ftl")
    public ModelAndView ftl(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ftl/learn");
        modelAndView.addObject("name", "one");
        return modelAndView;
    }

}
