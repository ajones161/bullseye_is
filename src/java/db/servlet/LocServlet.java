/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.servlet;

import com.google.gson.Gson;
import db.access.LocAccessor;
import entities.Location;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ajone
 */
@WebServlet(name = "LocServlet", urlPatterns = {"/LocServlet", "/LocServlet/Locations/*", "/LocServlet/Locations/sort*", "/LocServlet/LocID/*"})
public class LocServlet extends HttpServlet {

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
            if (request.getRequestURI().contains("/LocServlet/Locations/sort")) {
                String pathInfo = request.getPathInfo();
                String sub = pathInfo.substring(1);
                String[] list = sub.split(" ");
                String sortBy = list[1];

                List<Location> items = LocAccessor.sortLoc(sortBy);

                String jsonData = new Gson().toJson(items);

                out.println(jsonData);
            } else if (request.getRequestURI().contains("/LocServlet/LocID/")) {
                List<Location> items = LocAccessor.getID();

                String jsonData = new Gson().toJson(items);

                out.println(jsonData);
            } else if (request.getRequestURI().contains("/LocServlet/Locations/")) {
                String pathInfo = request.getPathInfo();
                String sub = pathInfo.substring(1);
                if ("".equals(sub)) {
                    List<Location> items = LocAccessor.getAllLoc();

                    String jsonData = new Gson().toJson(items);

                    out.println(jsonData);
                } else {
                    Location loc = LocAccessor.getSpecific(sub);
                    String jsonData = new Gson().toJson(loc);

                    out.println(jsonData);
                }
            } else {

            }
        } catch (SQLException ex) {
            Logger.getLogger(LocServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {

            Scanner s = new Scanner(request.getReader());
            String jsonData = s.nextLine();

            Gson gson = new Gson();

            Location p = gson.fromJson(jsonData, Location.class);

            boolean b = LocAccessor.addLoc(p);
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
            Location p = gson.fromJson(jsonData, Location.class);

            boolean b = LocAccessor.updateLoc(p);
            out.println(b);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
