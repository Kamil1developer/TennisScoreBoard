package servlet;

import bootstrap.AppContainer;
import dto.view.MatchRowViewDto;
import dto.view.PaginatedMatchesViewDto;
import exceptions.TextValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.MatchesService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/matches")
public class MatchesServlet extends HttpServlet {

    // Все повторяющиеся или важные строковые литералы лучше выносить в `private static final` константы с понятными именами.
        // Именованная константа делает код более семантически понятным.

    // TODO: Сервлет берёт на себя лишнюю ответственность — занимается бизнес-логикой
        // (получает от сервиса список ВСЕХ страниц, разбирает этот список и решает, какую именно страницу из списка выбрать),
        // хотя его задача — только принимать HTTP-запросы и делегировать их обработку. Это нарушает принцип единственной ответственности (SRP)
        // и делает код сервлета более сложным и трудным для тестирования.
        // Сервлет должен быть "тонким контроллером", делегирующим всю бизнес-логику сервисному слою.
        // (см. файл "fat-controller.md" в этом же пакете)

    // Методы `prepareAllMatchesPageAttributes` и `prepareFilteredMatchesAttributes` очень похожи:
        // они оба получают `Optional<List<PaginatedMatchesViewDto>>`, проверяют его на `isPresent`, а затем вызывают `prepareMatchesPageAttributes`.
        // В таких случаях стоит придумать, как избавиться от дублирования.

    private MatchesService matchesService;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageParameter = req.getParameter("page");
        String filterParameter = req.getParameter("filter_by_player_name");

        try {
            if (filterParameter != null && filterParameter.isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/matches");
            } else if (filterParameter != null) {
                prepareFilteredMatchesAttributes(req, filterParameter, pageParameter);
                req.getRequestDispatcher("/matches.jsp").forward(req, resp);
            } else if (filterParameter == null) { // В этом if условие filterParameter == null всегда true
                prepareAllMatchesPageAttributes(req, pageParameter);
                req.getRequestDispatcher("/matches.jsp").forward(req, resp);
            }
        }
        catch (TextValidationException e){
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/matches.jsp").forward(req,resp);
        }
    }

    // TODO: Сервлет не должен заниматься бизнес-логикой. Это обязанность сервисного слоя.
    private void prepareAllMatchesPageAttributes(HttpServletRequest req, String pageParameter){

        // Здесь нет необходимости разделять объявление переменной от её инициализации
        Optional<List<PaginatedMatchesViewDto>> optionalList;
        optionalList = matchesService.showMatches();
        if (optionalList.isPresent()) {

            List<PaginatedMatchesViewDto> paginatedMatchPages = optionalList.get();

            // Можно записать лаконичнее: req.setAttribute("matchFound", !paginatedMatchPages.isEmpty());
            if (paginatedMatchPages.size() == 0){ // Можно так: paginatedMatchPages.isEmpty()
                req.setAttribute("matchFound", false);
            }
            else {
                req.setAttribute("matchFound", true);
            }
            prepareMatchesPageAttributes(paginatedMatchPages, req, pageParameter);
        }
    }

    // TODO: Сервлет не должен заниматься бизнес-логикой. Это обязанность сервисного слоя.
    private void prepareFilteredMatchesAttributes(HttpServletRequest req, String filterParameter, String pageParameter){
        Optional<List<PaginatedMatchesViewDto>> optionalList = matchesService.findMatchesByPrefix(filterParameter);
        if (optionalList.isPresent()) {
            List<PaginatedMatchesViewDto> paginatedMatchPages = optionalList.get();
            if (paginatedMatchPages.isEmpty()){
                req.setAttribute("matchFound", false);
            }
            else {
                prepareMatchesPageAttributes(paginatedMatchPages, req, pageParameter);
            }
        }

    }

    // TODO: Сервлет не должен заниматься бизнес-логикой. Это обязанность сервисного слоя.
    private void prepareMatchesPageAttributes(List<PaginatedMatchesViewDto> paginatedMatchPages, HttpServletRequest req, String pageParameter){
        int totalPages = paginatedMatchPages.size();
        req.setAttribute("totalPages", totalPages);
        if (pageParameter == null){
            PaginatedMatchesViewDto paginatedMatchPage = paginatedMatchPages.getFirst();
            List<MatchRowViewDto> matchesList = paginatedMatchPage.getMatchesList();

            // DTO нужен, чтобы передавать данные одним объектом — не нужно "разбирать" его и передавать по частям
            req.setAttribute("matchesList", matchesList);
            req.setAttribute("matchesPage", paginatedMatchPage);
        }
        else {

            // Нет обработки исключений при Integer.parseInt()
            int pageNumber = Integer.parseInt(pageParameter);
            PaginatedMatchesViewDto paginatedMatchPage = paginatedMatchPages.get(pageNumber - 1);
            List<MatchRowViewDto> matchesList = paginatedMatchPage.getMatchesList();

            // DTO нужен, чтобы передавать данные одним объектом — не нужно "разбирать" его и передавать по частям
            req.setAttribute("matchesList", matchesList);
            req.setAttribute("matchesPage", paginatedMatchPage);
        }
    }

    // Метод `init` можно расположить выше `doGet` и `doPost` — по аналогии с родительским классом HttpServlet.
    @Override
    public void init() throws ServletException {

        // Для получения объектов из контекста можно использовать "естественные константы" — ClassName.class.getSimpleName() или ClassName.class.getName()
        AppContainer appContainer = (AppContainer) getServletContext().getAttribute("appContainer");

        // Получение зависимостей из контейнера контейнеров — излишнее усложнение.
            // Достаточно иметь один контейнер на приложение, который бы содержал в себе все бины.
        this.matchesService = appContainer.services().getMatchesService();
    }
}
