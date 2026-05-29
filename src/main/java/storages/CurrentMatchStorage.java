package storages;

import matches.CurrentMatch;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CurrentMatchStorage {

    // TODO: Нет интерфейса для этого класса.

    // TODO: Веб-приложения по своей природе являются многопоточными.
        // Поэтому стоит использовать потокобезопасную реализацию `Map`, специально предназначенную для многопоточной среды.
        // Лучшим выбором здесь является `java.util.concurrent.ConcurrentHashMap`.
    private final Map<UUID, CurrentMatch> currentMatchesMap = new HashMap<>();

    // Этот метод не должен принимать `UUID uuid`.
        // Хранилище должно само создавать ID для матча (по аналогии с БД) и возвращать его из этого метода.
    public void put(UUID uuid, CurrentMatch currentMatch){
        currentMatchesMap.put(uuid, currentMatch);
    }

    public void remove(UUID uuid){
        currentMatchesMap.remove(uuid);
    }

    // Этот класс не должен возвращать копию коллекции текущих матчей.
        // Сейчас она используется, чтобы получить в клиентском коде конкретный матч по ID.
        // Создавать копию всего хранилища только чтобы получить один матч — избыточно.
        // Правильным решением будет создать в этом классе метод `public CurrentMatch get(UUID uuid)`.
    public Map<UUID, CurrentMatch> getMap() {
        return Map.copyOf(currentMatchesMap);
    }
}
