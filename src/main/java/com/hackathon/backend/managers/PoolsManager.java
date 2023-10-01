package com.hackathon.backend.managers;

import com.hackathon.backend.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PoolsManager {
    private final Map<String, List<Map<Integer, Integer>>> pools;

    public PoolsManager() {
        this.pools = Main.getMongoManager().loadAllPools();
    }

    public void addPoolVote(final String pathUuid, final Map<Integer, Integer> voteMap) {
        Main.getMongoManager().addPoolVote(pathUuid, voteMap);

        if (this.pools.containsKey(pathUuid)) {
            this.pools.get(pathUuid).add(voteMap);
            return;
        }

        this.pools.put(pathUuid, new ArrayList<>());
        this.pools.get(pathUuid).add(voteMap);
    }

    public double getPoolVotes(final String pathUuid, final int question) {
        if (!this.pools.containsKey(pathUuid)) {
            return 0;
        }
        return this.pools.get(pathUuid).stream().mapToInt(map -> map.get(question)).sum() / this.pools.get(pathUuid).size();
    }



}
