package servlet;

import bootstrap.AppContainer;
import dto.NewMatchRequestDto;
import exceptions.NewMatchValidationException;
import exceptions.TextValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.NewMatchService;
import validator.NewMatchValidator;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {

    // Все повторяющиеся или важные строковые литералы лучше выносить в `private static final` константы с понятными именами.
        // Именованная константа делает код более семантически понятным.

    private NewMatchService newMatchService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/new-match.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstPlayerName =  req.getParameter("firstPlayerName");
        String secondPlayerName = req.getParameter("secondPlayerName");

        NewMatchRequestDto requestDto = new NewMatchRequestDto(firstPlayerName, secondPlayerName);

        try {
            UUID matchID = newMatchService.createPlayers(requestDto);

            resp.sendRedirect(req.getContextPath() + "/match-score?uuid=" + matchID);

        }
        catch (NewMatchValidationException | TextValidationException e){
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/new-match.jsp").forward(req,resp);
        }




    }

    // Метод `init` можно расположить выше `doGet` и `doPost` — по аналогии с родительским классом HttpServlet.
    @Override
    public void init(){

        // Для получения объектов из контекста можно использовать "естественные константы" — ClassName.class.getSimpleName() или ClassName.class.getName()
        AppContainer appContainer = (AppContainer) getServletContext().getAttribute("appContainer");

        // Получение зависимостей из контейнера контейнеров — излишнее усложнение.
            // Достаточно иметь один контейнер на приложение, который бы содержал в себе все бины.
        newMatchService = appContainer.services().getNewMatchService();
    }
}
