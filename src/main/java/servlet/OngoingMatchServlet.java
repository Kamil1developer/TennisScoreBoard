package servlet;

import bootstrap.AppContainer;
import dto.view.CurrentMatchDto;
import dto.view.MatchOverDto;
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
    private OngoingMatchService ongoingMatchService;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String uuid = req.getParameter("uuid");

        CurrentMatchDto matchViewDto = ongoingMatchService.getMatchView(uuid);

        renderView(req,resp, matchViewDto);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String uuid = req.getParameter("uuid");

        String playerId = req.getParameter("playerId");

        ongoingMatchService.addScore(uuid, playerId);

        if (!ongoingMatchService.hasMatchWinner(uuid)) {
            CurrentMatchDto matchViewDto = ongoingMatchService.getMatchView(uuid);

            renderView(req, resp, matchViewDto);
        }
        else {
            Optional<MatchOverDto> optional = ongoingMatchService.getMatchOverView(uuid);
            if (optional.isPresent()) {
                MatchOverDto matchOverViewDto = optional.get();
                req.setAttribute("matchInProgress", false);
                req.setAttribute("matchOver", true);
                req.setAttribute("matchOverView", matchOverViewDto);
                req.getRequestDispatcher("/match-score.jsp").forward(req, resp);
            }
        }

    }
    private void renderView(HttpServletRequest req,
                            HttpServletResponse resp,
                            CurrentMatchDto matchViewDto) throws ServletException, IOException {
        req.setAttribute("matchInProgress", true);
        req.setAttribute("currentMatchView", matchViewDto);
        req.getRequestDispatcher("/match-score.jsp").forward(req,resp);
    }


    public void init(){
        AppContainer appContainer = (AppContainer) getServletContext().getAttribute("appContainer");
        ongoingMatchService = appContainer.services().getOngoingMatchService();
    }
}
