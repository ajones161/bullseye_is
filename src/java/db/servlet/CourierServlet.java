/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.servlet;

import db.access.CourierAccessor;
import com.google.gson.Gson;
import entities.Courier;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ajone
 */
@WebServlet(name = "CourierServlet", urlPatterns = {"/CourierServlet", "/CourierServlet/Couriers/*", "/CourierServlet/Couriers/sort*"})
public class CourierServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            String uri = request.getRequestURI();
            if (request.getRequestURI().contains("/CourierServlet/Couriers/sort")) {
                String pathInfo = request.getPathInfo();
                String sub = pathInfo.substring(1);
                String[] list = sub.split(" ");
                String sortBy = list[1];
                
                List<Courier> items = CourierAccessor.sortCour(sortBy);

                String jsonData = new Gson().toJson(items);

                out.println(jsonData);
            } else {
                List<Courier> items = CourierAccessor.getAllCouriers();

                String jsonData = new Gson().toJson(items);

                out.println(jsonData);
            }
        }
    }
    
        @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {

            Scanner s = new Scanner(request.getReader());
            String jsonData = s.nextLine();

            Gson gson = new Gson();

            Courier p = gson.fromJson(jsonData, Courier.class);

            boolean b = CourierAccessor.addItm(p);
            out.println(b);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            Scanner s = new Scanner(request.getReader());
            String jsonData = s.nextLine();

            Gson gson = new Gson();
            Courier p = gson.fromJson(jsonData, Courier.class);

            boolean b = CourierAccessor.updateItm(p);
            out.println(b);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
