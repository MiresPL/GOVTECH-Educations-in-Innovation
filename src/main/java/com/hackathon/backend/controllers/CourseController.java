package com.hackathon.backend.controllers;

import com.hackathon.backend.helpers.ApiHelper;
import com.hackathon.backend.helpers.CourseHelper;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "", method = RequestMethod.POST, produces = "application/json")
public class CourseController {



    @CrossOrigin
    @RequestMapping(path = "/test", method = RequestMethod.POST, produces = "application/json")
    public String test() {
       //CourseHelper.getCoursesCat("mazowieckie", "2", "2", "informatyka", true, 0);
        return "test";
    }

    @CrossOrigin
    @RequestMapping(path = "/course", method = RequestMethod.POST, produces = "application/json")
    public String getCourse(@RequestBody Map<String, Object> postData) {
        final String voievodeship = (String) postData.get("wojewodztwo");
        final String form = (String) postData.get("forma");
        final String level = (String) postData.get("poziom");
        final boolean isAbroad = (boolean) postData.get("isAbroad");
        final double maxPrice = Double.parseDouble((String) postData.get("maxPrice"));

        if (postData.containsKey("kategoria")) {
            return CourseHelper.getCoursesCat(voievodeship, form, level, (String) postData.get("kategoria"), isAbroad, maxPrice);
        } else {
            return CourseHelper.getCourses(voievodeship, form, level, isAbroad, maxPrice);
        }
    }
}
