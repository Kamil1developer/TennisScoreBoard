# Роадмап рефакторинга по файлам

Это упорядоченный список файлов, которые следует исправлять в соответствии с замечаниями в комментариях. Рекомендую двигаться последовательно.

Файлы, не указанные в списке, можно исправлять в любом порядке.

### Шаг 1: Слой доступа к данным и конфигурация

- `/entity/Player.java`
- `/entity/Match.java`
- `/dao/PlayerDao.java`
- `/dao/impl/HibernatePlayerDao.java`
- `/dao/MatchDao.java`
- `/dao/impl/HibernateMatchDao.java`

### Шаг 2: Создание "богатой" доменной модели

- `/scores/Score.java`
- `/matches/CurrentMatch.java`
- `/service/OngoingMatchService.java`

### Шаг 3: Сервисный слой

- `/storages/CurrentMatchStorage.java`
- `/service/MatchesService.java`
- `/service/NewMatchService.java`
- `/service/CompletedMatchService.java`

### Шаг 4: Слой DTO (Data Transfer Object)

- Все классы в пакете `/dto/view/`

### Шаг 5: Слой представления (Bootstrap и сервлеты)

- Все классы в пакете `/bootstrap/`
- Все классы в пакете `/infrastructure/`
- Все классы в пакете `/servlet/`

### Шаг 6: Интерфейс (JSP) и Тесты

- `/src/main/webapp/matches.jsp`
- `/src/test/java/OngoingMatchServiceTest.java`
