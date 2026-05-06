package servlet;

import bootstrap.AppContainer;
import dto.view.CurrentMatchViewDto;
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

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String uuid = req.getParameter("uuid");
        CurrentMatchViewDto matchViewDto = matchScoreService.getCurrentMatchView(uuid);
        renderView(req,resp, matchViewDto, uuid);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String uuid = req.getParameter("uuid");
        String playerId = req.getParameter("playerId");

        matchScoreService.addScore(uuid, playerId);
        CurrentMatchViewDto matchViewDto = matchScoreService.getCurrentMatchView(uuid);

        renderView(req,resp, matchViewDto,uuid);

    }
    private void renderView(HttpServletRequest req,HttpServletResponse resp, CurrentMatchViewDto matchViewDto, String uuid) throws ServletException, IOException {
        req.setAttribute("currentMatchView", matchViewDto);
        req.setAttribute("uuid", uuid);

        req.getRequestDispatcher("/match-score.jsp").forward(req,resp);
    }


    public void init(){
        AppContainer appContainer = (AppContainer) getServletContext().getAttribute("appContainer");
        matchScoreService = appContainer.services().getMatchScoreService();
    }
}
