package servlet;

import bootstrap.AppContainer;
import dto.NewMatchRequestDto;
import exceptions.NewMatchValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.NewMatchService;
import validator.NewMatchValidator;

import java.io.IOException;

@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {
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
            newMatchService.checkPlayerExists(requestDto);
        }
        catch (NewMatchValidationException e){
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/new-match.jsp").forward(req,resp);
        }




    }
    @Override
    public void init(){
        AppContainer appContainer = (AppContainer) getServletContext().getAttribute("appContainer");
        newMatchService = appContainer.services().getNewMatchService();
    }
}
