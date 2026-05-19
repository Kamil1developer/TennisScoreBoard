package service;

import dao.MatchDao;
import dao.PlayerDao;
import dto.page.MatchesOnPageViewDto;
import dto.view.MatchOverViewDto;
import dto.view.MatchRowViewDto;
import dto.view.PaginatedMatchesViewDto;
import entity.Match;
import entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MatchesService {
    private final PlayerDao playerDao;
    private final MatchDao matchDao;
    public MatchesService(PlayerDao playerDao, MatchDao matchDao){
        this.playerDao = playerDao;
        this.matchDao = matchDao;
    }

    private record MatchPaginationContext(
            List<Match> matches,
            List<MatchRowViewDto> matchesOnPage,
            List<List<MatchRowViewDto>> matchPages
    ) {}

    public Optional<List<PaginatedMatchesViewDto>> showMatches(){
        Optional<List<Match>> optionalMatches = matchDao.findAll();
        if (optionalMatches.isPresent()){

            List<PaginatedMatchesViewDto> paginatedMatchPages = new LinkedList<>();
            List<List<MatchRowViewDto>> pages = new LinkedList<>();
            List<MatchRowViewDto> matchesOnPage = new LinkedList<>();
            List<Match> matches = optionalMatches.get();

            for (int i = 0; i < matches.size(); i++){
                MatchPaginationContext context = new MatchPaginationContext(matches, matchesOnPage, pages);
                addMatchToPage(context, i);
            }

            if (!matchesOnPage.isEmpty()) {
                pages.add(new ArrayList<>(matchesOnPage));

                matchesOnPage.clear();
            }

            if (!pages.isEmpty()){
                MatchPaginationContext context = new MatchPaginationContext(matches, matchesOnPage, pages);
                buildPaginatedMatchesView(context, paginatedMatchPages);
                matchesOnPage.clear();
            }
            return Optional.of(paginatedMatchPages);
        }
        return  Optional.empty();
    }

    private void buildPaginatedMatchesView(MatchPaginationContext context, List<PaginatedMatchesViewDto> paginatedMatchPages){
        int matches = context.matches.size();
        int matchPages = context.matchPages.size();
        int totalPages;

        totalPages = matches / matchPages;

        for (int i = 0; i < totalPages; i++){

            int pageNumber = i + 1;
            if (pageNumber == 1){
                PaginatedMatchesViewDto paginatedMatchPage = new PaginatedMatchesViewDto(
                        context.matchPages.get(i),
                        pageNumber,
                        false,
                        false
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
            else if (pageNumber == 1 && pageNumber < totalPages){
                PaginatedMatchesViewDto paginatedMatchPage = new PaginatedMatchesViewDto(
                        context.matchPages.get(i),
                        pageNumber,
                        false,
                        true
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
}
