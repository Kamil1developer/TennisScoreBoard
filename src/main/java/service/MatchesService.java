package service;

import dao.MatchDao;
import dto.view.MatchRowViewDto;
import dto.view.PaginatedMatchesViewDto;
import entity.Match;
import entity.Player;
import validator.TextValidator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MatchesService {

    // TODO: Нет интерфейса для этого класса. (см. файл "service.md" в этом же пакете)

    // TODO: Класс нарушает Принцип единой ответственности (SRP).
        // Он выполняет как минимум три разные задачи:
            // - Запрашивает у `MatchDao` матчи (причём абсолютно все) из базы данных.
            // - Самостоятельно реализует сложную и неэффективную логику для фильтрации матчей по имени игрока и для разбиения общего списка на страницы.
            // - Преобразует сущности `Match` в `MatchRowViewDto`, а затем упаковывает их в `PaginatedMatchesViewDto`
        // Из-за этого у класса появляется несколько причин для изменения:
            // - Изменится логика пагинации (например, размер страницы).
            // - Изменится структура DTO.
            // - Изменится способ получения данных из DAO.
        // Как исправить:
            // - Ответственность за пагинацию и фильтрацию следует делегировать слою DAO.
                // DAO должен предоставлять методы, которые сразу возвращают нужную страницу данных из БД.
            // - Логику преобразования `Entity` в `DTO` можно вынести в отдельный класс-маппер.
        // Тогда MatchesService будет выполнять только одну задачу: координировать запрос — вызывать нужный метод DAO
        // и передавать результат мапперу. Это сделает класс проще, компактнее и легче для понимания и тестирования.

    // TODO: Класс всегда загружает абсолютно все матчи из базы данных в память (`matchDao.findAll()`), и только потом выполняет фильтрацию и пагинацию.
        // Загрузка тысяч или десятков тысяч записей из БД — очень медленная операция.
        // Пользователь будет долго ждать ответа, а база данных будет испытывать неоправданно высокую нагрузку.
        // По мере роста количества матчей в базе, приложение неизбежно столкнется с нехваткой памяти и аварийно завершит работу.

    // TODO: Методы `showMatches` и `findMatchesByPrefix` содержат сложную вложенную логику для ручного формирования страниц.
        // Вспомогательные методы `addMatchToPage` и `buildPaginatedMatchesView` оперируют общим состоянием и имеют побочные эффекты
        // (модифицируют коллекции, переданные как параметры), что усложняет отладку и понимание кода.

    // Код методов `showMatches` и `findMatchesByPrefix` почти полностью дублируется.
        // Они оба содержат одинаковый код для инициализации коллекций, построения пагинации и маппинга.
        // Это нарушение принципа DRY (Don't Repeat Yourself).
        // В таких случаях стоит придумать, как избавиться от дублирования.

    // Публичные методы возвращают `Optional<List<PaginatedMatchesViewDto>>`. Это неидиоматичное использование Optional.
        // Для коллекций стандартной практикой является возврат пустого списка, если результаты не найдены.
        // Это избавляет вызывающий код от необходимости дополнительной проверки на `isPresent()` и вызова `get()`.

    // Размер страницы по умолчанию жёстко закодирован в методах. Более уместно хранить его в сервлете, так как в идеале
        // он должен приходить с фронтенда. А сервис должен принимать это значение в качестве аргумента в методы.

    private final MatchDao matchDao;
    public MatchesService(MatchDao matchDao){
        this.matchDao = matchDao;
    }

    // Использование private record — это хороший приём, который делает запутанный алгоритм немного более организованным.
        // Но здесь необходимость в таком сложном контексте для передачи состояния между несколькими приватными методами,
        // указывает на то, что сам алгоритм (ручная пагинация в памяти) слишком сложен.
        // После рефакторинга логика упростится и этот объект станет не нужен.
    private record MatchPaginationContext(
            List<Match> matches,
            List<MatchRowViewDto> matchesOnPage,
            List<List<MatchRowViewDto>> matchPages
    ) {}

    public Optional<List<PaginatedMatchesViewDto>> showMatches(){
        Optional<List<Match>> optionalMatches = matchDao.findAll();
        if (optionalMatches.isPresent()){

            // Нет необходимости использовать LinkedList — можно просто ArrayList
            List<PaginatedMatchesViewDto> paginatedMatchPages = new LinkedList<>();
            List<List<MatchRowViewDto>> pages = new LinkedList<>();
            List<MatchRowViewDto> matchesOnPage = new LinkedList<>();
            List<Match> matches = optionalMatches.get();

            // Эта сортировка должна выполняться на уровне базы данных (`ORDER BY id DESC`), чтобы быть эффективной
            matches = matches.reversed();

            for (int i = 0; i < matches.size(); i++){
                MatchPaginationContext context = new MatchPaginationContext(matches, matchesOnPage, pages);
                addMatchToPage(context, i);
            }

            if (!matchesOnPage.isEmpty()) {
                pages.add(new ArrayList<>(matchesOnPage));

                matchesOnPage.clear();
            }

            if (!pages.isEmpty()){
                int totalPages;
                int matchesCount = matches.size();
                int pagesCount = pages.size();

                totalPages = pagesCount;

                MatchPaginationContext context = new MatchPaginationContext(matches, matchesOnPage, pages);
                buildPaginatedMatchesView(context, paginatedMatchPages, totalPages);
                matchesOnPage.clear();
            }
            return Optional.of(paginatedMatchPages);
        }
        return  Optional.empty();
    }

    private void buildPaginatedMatchesView(MatchPaginationContext context, List<PaginatedMatchesViewDto> paginatedMatchPages, int totalPages){

        for (int i = 0; i < totalPages; i++){

            int pageNumber = i + 1;

            // Тело всего цикла можно записать так:
            /*
            PaginatedMatchesViewDto paginatedMatchPage = new PaginatedMatchesViewDto(
                    context.matchPages.get(i),
                    pageNumber,
                    pageNumber > 1,
                    pageNumber < totalPages
            );
             */

            if (pageNumber == 1 && pageNumber < totalPages){
                PaginatedMatchesViewDto paginatedMatchPage = new PaginatedMatchesViewDto(
                        context.matchPages.get(i),
                        pageNumber,
                        false,
                        true
                );

                paginatedMatchPages.add(paginatedMatchPage);

            }
            else if (pageNumber > 1 && pageNumber < totalPages){
                PaginatedMatchesViewDto paginatedMatchPage = new PaginatedMatchesViewDto(
                        context.matchPages.get(i),
                        pageNumber,
                        true,
                        true
                );

                paginatedMatchPages.add(paginatedMatchPage);
            }
            else if (pageNumber > 1 && pageNumber == totalPages){
                PaginatedMatchesViewDto paginatedMatchPage = new PaginatedMatchesViewDto(
                        context.matchPages.get(i),
                        pageNumber,
                        true,
                        false
                );

                paginatedMatchPages.add(paginatedMatchPage);
            }
            else if (pageNumber == 1){
                PaginatedMatchesViewDto paginatedMatchPage = new PaginatedMatchesViewDto(
                        context.matchPages.get(i),
                        pageNumber,
                        false,
                        false
                );

                paginatedMatchPages.add(paginatedMatchPage);
            }
        }
    }

    private void addMatchToPage(MatchPaginationContext context, int i) {
        Match match = context.matches.get(i);
        Player firstPlayer = match.getFirstPlayerId();
        Player secondPlayer = match.getSecondPlayerId();
        Player winner = match.getWinnerId();

        MatchRowViewDto matchRowViewDto = new MatchRowViewDto(
                firstPlayer.getName(),
                secondPlayer.getName(),
                winner.getName()
        );
        context.matchesOnPage.add(matchRowViewDto);

        if ((i + 1) % 5 == 0) {
            context.matchPages.add(new ArrayList<>(context.matchesOnPage));

            context.matchesOnPage.clear();
        }
    }

    public Optional<List<PaginatedMatchesViewDto>> findMatchesByPrefix(String playerName){
        TextValidator.validateLatinTextCharacters(playerName);

        Optional<List<Match>> optionalMatches = matchDao.findAll();
        if (optionalMatches.isPresent()){

            // Нет необходимости использовать LinkedList — можно просто ArrayList
            List<PaginatedMatchesViewDto> paginatedMatchPages = new LinkedList<>();
            List<List<MatchRowViewDto>> pages = new LinkedList<>();
            List<MatchRowViewDto> matchesOnPage = new LinkedList<>();
            List<Match> matches = optionalMatches.get();

            // Эта сортировка должна выполняться на уровне базы данных (`ORDER BY id DESC`), чтобы быть эффективной
            matches = matches.reversed();

            for (int i = 0; i < matches.size(); i++){
                MatchPaginationContext context = new MatchPaginationContext(matches, matchesOnPage, pages);
                addMatchToPageIfMatchesPrefix(context, i, playerName);
            }

            if (!matchesOnPage.isEmpty()) {
                pages.add(new ArrayList<>(matchesOnPage));

                matchesOnPage.clear();
            }

            if (!pages.isEmpty()){
                int totalPages = pages.size();

                MatchPaginationContext context = new MatchPaginationContext(matches, matchesOnPage, pages);
                buildPaginatedMatchesView(context, paginatedMatchPages, totalPages);
                matchesOnPage.clear();
            }
            return Optional.of(paginatedMatchPages);
        }
        return  Optional.empty();
    }

    private void addMatchToPageIfMatchesPrefix(MatchPaginationContext context, int i, String playerName){
        Match match = context.matches.get(i);
        Player firstPlayer = match.getFirstPlayerId();
        Player secondPlayer = match.getSecondPlayerId();
        Player winner = match.getWinnerId();

        if (playerName.equals(firstPlayer.getName()) ||
                playerName.equals(secondPlayer.getName())) {

            MatchRowViewDto matchRowViewDto = new MatchRowViewDto(
                    firstPlayer.getName(),
                    secondPlayer.getName(),
                    winner.getName()
            );
            context.matchesOnPage.add(matchRowViewDto);
        }

        if (context.matchesOnPage.size() % 5 == 0 && !context.matchesOnPage.isEmpty()) {
            context.matchPages.add(new ArrayList<>(context.matchesOnPage));

            context.matchesOnPage.clear();
        }
    }

}
