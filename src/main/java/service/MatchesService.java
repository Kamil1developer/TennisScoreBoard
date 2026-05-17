package service;

import dao.MatchDao;
import dao.PlayerDao;
import dto.page.MatchesOnPageViewDto;
import dto.view.MatchOverViewDto;
import dto.view.MatchRowViewDto;
import dto.view.PaginatedMatchesViewDto;
import entity.Match;
import entity.Player;

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

    public Optional<List<PaginatedMatchesViewDto>> showMatches(){
        Optional<List<Match>> optionalMatches = matchDao.findAll();
        if (optionalMatches.isPresent()){
            List<List<MatchRowViewDto>> matchPages = new LinkedList<>();

            List<Match> matches = optionalMatches.get();
            for (int i = 0; i < matches.size(); i++){
                List<MatchRowViewDto> matchesOnPage = getMatches(matches, i);
                matchPages.add(matchesOnPage);
            }
            List<PaginatedMatchesViewDto> paginatedMatchPages = buildPaginatedMatchesView(matchPages);

            return Optional.of(paginatedMatchPages);
        }
        return  Optional.empty();
    }

    private List<PaginatedMatchesViewDto> buildPaginatedMatchesView(List<List<MatchRowViewDto>> matchPages){
        List<PaginatedMatchesViewDto> paginatedMatchPages = new LinkedList<>();

        int totalPages = matchPages.size();
        for (int i = 0; i < matchPages.size(); i++){

            int pageNumber = i + 1;
            if (pageNumber == 1){
                PaginatedMatchesViewDto paginatedMatchPage = new PaginatedMatchesViewDto(
                        matchPages.get(i),
                        pageNumber,
                        false,
                        false
                );

                paginatedMatchPages.add(paginatedMatchPage);
            }
            else if (pageNumber > 1 && pageNumber < totalPages){
                PaginatedMatchesViewDto paginatedMatchPage = new PaginatedMatchesViewDto(
                        matchPages.get(i),
                        pageNumber,
                        true,
                        true
                );

                paginatedMatchPages.add(paginatedMatchPage);
            }
            else if (pageNumber > 1 && pageNumber == totalPages){
                PaginatedMatchesViewDto paginatedMatchPage = new PaginatedMatchesViewDto(
                        matchPages.get(i),
                        pageNumber,
                        true,
                        false
                );

                paginatedMatchPages.add(paginatedMatchPage);
            }
            else if (pageNumber == 1 && pageNumber < totalPages){
                PaginatedMatchesViewDto paginatedMatchPage = new PaginatedMatchesViewDto(
                        matchPages.get(i),
                        pageNumber,
                        false,
                        true
                );

                paginatedMatchPages.add(paginatedMatchPage);
            }
        }

        return paginatedMatchPages;
    }



    private List<MatchRowViewDto> getMatches(List<Match> matches, int i) {
        List<MatchRowViewDto> matchesOnPage = new LinkedList<>();
        for(int j = 0; j <= 5; j++) {
            Match match = matches.get(i);
            Player firstPlayer = match.getFirstPlayerId();
            Player secondPlayer = match.getSecondPlayerId();
            Player winner = match.getWinnerId();

            MatchRowViewDto matchRowViewDto = new MatchRowViewDto(
                    firstPlayer.getName(),
                    secondPlayer.getName(),
                    winner.getName()
            );

            matchesOnPage.add(matchRowViewDto);
        }

        return matchesOnPage;
    }
}
