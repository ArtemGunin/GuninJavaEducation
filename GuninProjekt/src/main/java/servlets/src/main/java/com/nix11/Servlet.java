package com.nix11;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/address")
public class Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestRepository requestRepository = RequestRepository.getInstance();
        Request newRequest = new Request(request.getRemoteAddr(), request.getHeader("User-Agent"));
        requestRepository.save(newRequest);
        request.setAttribute("currentRequest", newRequest);
        request.setAttribute("repository", requestRepository.getAll());
        response.setContentType("text/html");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
