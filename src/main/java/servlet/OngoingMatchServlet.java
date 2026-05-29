package servlet;

import bootstrap.AppContainer;
import dto.view.CurrentMatchViewDto;
import dto.view.MatchOverViewDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.OngoingMatchService;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/match-score")
public class OngoingMatchServlet extends HttpServlet {

    // Все повторяющиеся или важные строковые литералы лучше выносить в `private static final` константы с понятными именами.
        // Именованная константа делает код более семантически понятным.

    // TODO: Сервлет передаёт в слой представления доменные модели (`Score` в `CurrentMatchViewDto`).
        // Передача доменных моделей в JSP не является хорошей практикой. Это нарушает принцип разделения ответственности между слоями
        // и связывает слой представления с моделью данных (что чревато ошибками, например, в случае переименования полей).
        // Лучше использовать DTO (Data Transfer Object) для передачи данных в представление.
        // DTO позволяют контролировать, какие именно данные передаются.

    // TODO: Сейчас при обработке каждого выигранного очка выполняется 4 запроса к БД.
        // То есть минимум 192 лишних запроса на обработку очков (4 запроса при обработке каждого из 48 очков — минимум для победы одного из игроков).
        // Это создаёт чрезмерно избыточную нагрузку на БД и снижает производительность.
        // Стоит пересмотреть логику этой части приложения и избавиться от такого количества лишних запросов.
        // Идеальной картиной будет делать только один запрос в БД при сохранении уже завершённого матча, в котором одновременно сохранятся игроки, если их ещё нет в БД, а также сам матч.
        // Достаточно хорошим для этого проекта решением будет при завершении матча сохранять отдельно игроков и отдельно матч (3-5 запросов).

    private OngoingMatchService ongoingMatchService;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String uuid = req.getParameter("uuid");

        CurrentMatchViewDto matchViewDto = ongoingMatchService.getMatchView(uuid);

        renderView(req,resp, matchViewDto);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String uuid = req.getParameter("uuid");

        String playerId = req.getParameter("playerId");

        ongoingMatchService.addScore(uuid, playerId);

        // Условие в if лучше читается без отрицания, поэтому можно в if реализовать ветку
            // для 'ongoingMatchService.hasMatchWinner(uuid)', а противоположную логику — в else
        if (!ongoingMatchService.hasMatchWinner(uuid)) {
            CurrentMatchViewDto matchViewDto = ongoingMatchService.getMatchView(uuid);

            renderView(req, resp, matchViewDto);
        }
        else {
            Optional<MatchOverViewDto> optional = ongoingMatchService.getMatchOverView(uuid);
            if (optional.isPresent()) {
                MatchOverViewDto matchOverViewDto = optional.get();

                // Атрибуты "matchInProgress" и "matchOver" дублируют одну и ту же информацию о состоянии матча.
                req.setAttribute("matchInProgress", false);
                req.setAttribute("matchOver", true);
                req.setAttribute("matchOverView", matchOverViewDto);
                req.getRequestDispatcher("/match-score.jsp").forward(req, resp);
            }

            // Если Optional<MatchOverViewDto> optional по какой-то причине окажется пустым,
                // то пользователь останется на странице и для него визуально ничего не произойдёт.
        }

    }
    private void renderView(HttpServletRequest req,
                            HttpServletResponse resp,
                            CurrentMatchViewDto matchViewDto) throws ServletException, IOException {
        req.setAttribute("matchInProgress", true);

        // TODO: Сервлет не должен передавать доменные модели во View
        req.setAttribute("currentMatchView", matchViewDto);
        req.getRequestDispatcher("/match-score.jsp").forward(req,resp);
    }

    // Метод `init` можно расположить выше `doGet` и `doPost` — по аналогии с родительским классом HttpServlet.
    public void init(){

        // Для получения объектов из контекста можно использовать "естественные константы" — ClassName.class.getSimpleName() или ClassName.class.getName()
        AppContainer appContainer = (AppContainer) getServletContext().getAttribute("appContainer");

        // Получение зависимостей из контейнера контейнеров — излишнее усложнение.
            // Достаточно иметь один контейнер на приложение, который бы содержал в себе все бины.
        ongoingMatchService = appContainer.services().getOngoingMatchService();
    }
}
