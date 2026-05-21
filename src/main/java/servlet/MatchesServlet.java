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
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Optional;

@WebServlet("/matches")
public class MatchesServlet extends HttpServlet {
    private MatchesService matchesService;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageParameter = req.getParameter("page");
        String filterParameter = req.getParameter("filter_by_player_name");
        Optional<List<PaginatedMatchesViewDto>> optionalList;

        if (filterParameter != null) {
            optionalList = matchesService.findMatchesByPrefix(filterParameter);
            if (optionalList.isPresent()) {
                List<PaginatedMatchesViewDto> paginatedMatchPages = optionalList.get();
                if (paginatedMatchPages.size() == 0){
                    req.setAttribute("matchFound", false);
                }
                else {
                    prepareMatchesPageAttributes(paginatedMatchPages, req, pageParameter);
                }
            }

            req.getRequestDispatcher("/matches.jsp").forward(req, resp);
        }

        else {
            optionalList = matchesService.showMatches();
            if (optionalList.isPresent()) {

                List<PaginatedMatchesViewDto> paginatedMatchPages = optionalList.get();
                if (paginatedMatchPages.size() == 0){
                    req.setAttribute("matchFound", false);
                }
                else {
                    req.setAttribute("matchFound", true);
                }
                prepareMatchesPageAttributes(paginatedMatchPages, req, pageParameter);
            }
            req.getRequestDispatcher("/matches.jsp").forward(req, resp);
        }
    }
    private void prepareMatchesPageAttributes(List<PaginatedMatchesViewDto> paginatedMatchPages, HttpServletRequest req, String pageParameter){
        int totalPages = paginatedMatchPages.size();
        req.setAttribute("totalPages", totalPages);
        if (pageParameter == null){
            PaginatedMatchesViewDto paginatedMatchPage = paginatedMatchPages.getFirst();
            List<MatchRowViewDto> matchesList = paginatedMatchPage.getMatchesList();

            req.setAttribute("matchesList", matchesList);
            req.setAttribute("matchesPage", paginatedMatchPage);
        }
        else {
            int pageNumber = Integer.parseInt(pageParameter);
            PaginatedMatchesViewDto paginatedMatchPage = paginatedMatchPages.get(pageNumber - 1);
            List<MatchRowViewDto> matchesList = paginatedMatchPage.getMatchesList();

            req.setAttribute("matchesList", matchesList);
            req.setAttribute("matchesPage", paginatedMatchPage);
        }
    }

    @Override
    public void init() throws ServletException {
        AppContainer appContainer = (AppContainer) getServletContext().getAttribute("appContainer");
        this.matchesService = appContainer.services().getMatchesService();
    }
}
