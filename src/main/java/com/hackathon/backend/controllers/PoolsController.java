package com.hackathon.backend.controllers;

import com.hackathon.backend.Main;
import com.hackathon.backend.utils.DoubleUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/pools", method = RequestMethod.GET, produces = "application/json")
public class PoolsController {

    @CrossOrigin
    @RequestMapping(path = "/test", method = RequestMethod.GET, produces = "application/json")
    public String test() {
        final String uuid = "be527aed-1ac7-4764-bf68-0275ef24ad0f";
        for (int j = 0; j < 10; j++) {
            final Map<Integer, Integer> votes = new HashMap<>();
            for (int k = 0; k < 10; k++) {
                votes.put(k, new Random().nextInt(10) + 1);
            }
            Main.getPoolsManager().addPoolVote(uuid, votes);
        }
        return "test";
    }

    @CrossOrigin
    @RequestMapping(path = "/getVotes", method = RequestMethod.GET, produces = "application/json")
    public String getVote(@RequestBody Map<String, Object> body) {
        return DoubleUtils.round(Main.getPoolsManager().getPoolVotes(String.valueOf(body.get("pathUuid")), Integer.parseInt(String.valueOf(body.get("question")))), 1) + "";
    }

    @CrossOrigin
    @RequestMapping(path = "/submitVote", method = RequestMethod.GET, produces = "application/json")
    public String submitVote(@RequestBody Map<String, Object> body) {
        final Map<Integer, Integer> votes = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            votes.put(i, Integer.parseInt(String.valueOf(body.get("question" + i))));
        }
        Main.getPoolsManager().addPoolVote(String.valueOf(body.get("pathUuid")), votes);
        return "{\"result\": \"success\"}";

    }

}
