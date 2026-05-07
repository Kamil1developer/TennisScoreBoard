package storages;

import matches.CurrentMatch;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CurrentMatchStorage {
    private final Map<UUID, CurrentMatch> currentMatchesMap = new HashMap<>();

    public void put(UUID uuid, CurrentMatch currentMatch){
        currentMatchesMap.put(uuid, currentMatch);
    }

    public void remove(UUID uuid){
        currentMatchesMap.remove(uuid);
    }

    public Map<UUID, CurrentMatch> getMap() {
        return Map.copyOf(currentMatchesMap);
    }
}
