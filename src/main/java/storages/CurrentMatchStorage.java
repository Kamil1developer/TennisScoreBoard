package storages;

import matches.CurrentMatch;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CurrentMatchStorage {
    Map<UUID, CurrentMatch> currentMatchesMap = new HashMap<>();

}
