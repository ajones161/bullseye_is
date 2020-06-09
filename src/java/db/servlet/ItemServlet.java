/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.servlet;

import com.google.gson.Gson;
import db.access.ItemAccessor;
import entities.Item;
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
@WebServlet(name = "ItemServlet", urlPatterns = {"/ItemServlet", "/ItemServlet/Items/*", "/ItemServlet/Items/sort*"})
public class ItemServlet extends HttpServlet {

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
            if (request.getRequestURI().contains("/ItemServlet/Items/sort")) {
                String pathInfo = request.getPathInfo();
                String sub = pathInfo.substring(1);
                String[] list = sub.split(" ");
                String sortBy = list[1];

                List<Item> items = ItemAccessor.sortItem(sortBy);

                String jsonData = new Gson().toJson(items);

                out.println(jsonData);
            } else {
                List<Item> items = ItemAccessor.getAllItems();

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

            Item p = gson.fromJson(jsonData, Item.class);

            boolean b = ItemAccessor.addItm(p);
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
            Item p = gson.fromJson(jsonData, Item.class);

            boolean b = ItemAccessor.updateItm(p);
            out.println(b);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
