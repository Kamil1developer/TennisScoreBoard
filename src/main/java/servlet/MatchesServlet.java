package servlet;

import bootstrap.AppContainer;
import dto.page.MatchesOnPageViewDto;
import dto.view.MatchRowViewDto;
import dto.view.PaginatedMatchesViewDto;
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
    private MatchesService matchesService;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageParameter = req.getParameter("page");
        String filterParameter = req.getParameter("playerName");
        Optional<List<PaginatedMatchesViewDto>> optionalList = matchesService.showMatches();
        if (optionalList.isPresent()) {
            List<PaginatedMatchesViewDto> paginatedMatchPages = optionalList.get();
            int totalPages = paginatedMatchPages.size();
            req.setAttribute("totalPages", totalPages);

            if (pageParameter == null) {
                PaginatedMatchesViewDto paginatedMatchPage = paginatedMatchPages.getFirst();
                List <MatchRowViewDto> matchesList = paginatedMatchPage.getMatchesList();

                req.setAttribute("matchesList", matchesList);
                req.setAttribute("matchesPage", paginatedMatchPage);
            }
            else{
                int pageNumber = Integer.parseInt(pageParameter);
                PaginatedMatchesViewDto paginatedMatchPage = paginatedMatchPages.get(pageNumber - 1);
                List <MatchRowViewDto> matchesList = paginatedMatchPage.getMatchesList();

                req.setAttribute("matchesList", matchesList);
                req.setAttribute("matchesPage", paginatedMatchPage);
            }
        }
        req.setAttribute("playerName", filterParameter);
        req.getRequestDispatcher("/matches.jsp").forward(req,resp);
    }

    @Override
    public void init() throws ServletException {
        AppContainer appContainer = (AppContainer) getServletContext().getAttribute("appContainer");
        this.matchesService = appContainer.services().getMatchesService();
    }
}
