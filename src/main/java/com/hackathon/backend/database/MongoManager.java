package com.hackathon.backend.database;

import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongoManager {
    private final MongoConnectionPoolManager pool;

    public MongoManager() {
        this.pool = new MongoConnectionPoolManager();
    }

    public void addPoolVote(final String pathUuid, final Map<Integer, Integer> voteMap) {
        if (this.pool.getPools().find(new Document("_id", pathUuid)).first() == null) {
            final List<Map<Integer, Integer>> voteList = new ArrayList<>();
            voteList.add(voteMap);
            final List<Map<String, String>> list = new ArrayList<>();
            final Map<String, String> map = new HashMap<>();
            for (Map.Entry<Integer, Integer> entry : voteMap.entrySet()) {
                map.put(entry.getKey().toString(), entry.getValue().toString());
            }
            list.add(map);
            this.pool.getPools().insertOne(new Document("_id", pathUuid).append("voteList", list));
        } else {
            final Document document = this.pool.getPools().find(new Document("_id", pathUuid)).first();
            final List<Map<String, String>> voteList = new ArrayList<>((List<Map<String, String>>) document.get("voteList"));
            final Map<String, String> map = new HashMap<>();
            for (Map.Entry<Integer, Integer> entry : voteMap.entrySet()) {
                map.put(entry.getKey().toString(), entry.getValue().toString());
            }
            voteList.add(map);
            this.pool.getPools().findOneAndReplace(new Document("_id", pathUuid), new Document("_id", pathUuid).append("voteList", voteList));
        }
    }

    public List<Map<Integer, Integer>> getPoolVotes(final String pathUuid) {
        final Document document = this.pool.getPools().find(new Document("pathUuid", pathUuid)).first();
        if (document == null) {
            return new ArrayList<>();
        }
        final List<Map<String, String>> voteList = new ArrayList<>((List<Map<String, String>>) document.get("voteList"));
        final List<Map<Integer, Integer>> voteList2 = new ArrayList<>();
        for (final Map<String, String> map : voteList) {
            final Map<Integer, Integer> map2 = new HashMap<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                map2.put(Integer.parseInt(entry.getKey()), Integer.parseInt(entry.getValue()));
            }
            voteList2.add(map2);
        }
        return voteList2;
    }

    public Map<String, List<Map<Integer, Integer>>> loadAllPools() {
        final Map<String, List<Map<Integer, Integer>>> pools = new HashMap<>();
        for (final Document document : this.pool.getPools().find()) {
            final List<Map<String, String>> voteList = new ArrayList<>((List<Map<String, String>>) document.get("voteList"));
            final List<Map<Integer, Integer>> voteList2 = new ArrayList<>();
            for (final Map<String, String> map : voteList) {
                final Map<Integer, Integer> map2 = new HashMap<>();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    map2.put(Integer.parseInt(entry.getKey()), Integer.parseInt(entry.getValue()));
                }
                voteList2.add(map2);
            }
            pools.put(document.getString("_id"), voteList2);
        }
        return pools;
    }
}
