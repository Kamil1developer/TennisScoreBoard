package servlet;

import bootstrap.AppContainer;
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
        String filterParameter = req.getParameter("filter_by_player_name");

        if (filterParameter != null && filterParameter.isEmpty()){
            resp.sendRedirect(req.getContextPath() + "/matches");
        }

        else if (filterParameter != null) {
            prepareFilteredMatchesAttributes(req, filterParameter, pageParameter);
            req.setAttribute("filterIsEmpty", false);
            req.setAttribute("filterSubmittedEmpty", false);
            req.getRequestDispatcher("/matches.jsp").forward(req, resp);
        }

        else if (filterParameter == null) {
            prepareAllMatchesPageAttributes(req, pageParameter);
            req.setAttribute("filterSubmittedEmpty", false);
            req.getRequestDispatcher("/matches.jsp").forward(req, resp);
        }
    }

    private void prepareAllMatchesPageAttributes(HttpServletRequest req, String pageParameter){
        Optional<List<PaginatedMatchesViewDto>> optionalList;
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
    }

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
