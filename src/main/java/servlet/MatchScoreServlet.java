package servlet;

import bootstrap.AppContainer;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import matches.CurrentMatch;
import service.MatchScoreService;
import storages.CurrentMatchStorage;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {
    private MatchScoreService matchScoreService;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameter("uuid");
        UUID matchId = UUID.fromString(uuid);

        CurrentMatchStorage matchStorage = matchScoreService.getCurrentMatchStorage();
        CurrentMatch currentMatch = matchStorage.getMap().get(matchId);


        req.setAttribute("firstPlayerId", currentMatch.firstPlayerId());
        req.setAttribute("secondPlayerId", currentMatch.secondPlayerId());
        req.setAttribute("uuid", uuid);
        req.getRequestDispatcher("/match-score.jsp").forward(req,resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

        String playerId = req.getParameter("playerId");
        String uuid = req.getParameter("uuid");
        matchScoreService.addScore(uuid, playerId);


        req.getRequestDispatcher("/match-score.jsp").forward(req,resp);

    }


    public void init(){
        AppContainer appContainer = (AppContainer) getServletContext().getAttribute("appContainer");
        matchScoreService = appContainer.services().getMatchScoreService();
    }
}
