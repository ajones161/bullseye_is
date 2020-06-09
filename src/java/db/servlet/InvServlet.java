/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.servlet;

import db.access.InvAccessor;
import com.google.gson.Gson;
import entities.InvItem;
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
@WebServlet(name = "InvServlet", urlPatterns = {"/InvServlet", "/InvServlet/invItems/*", "/InvServlet/tLine/*", "/InvServlet/invItems/sort*"})
public class InvServlet extends HttpServlet {

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
            if (uri.contains("/InvServlet/invItems/sort")) {
                String pathInfo = request.getPathInfo();
                String sub = pathInfo.substring(1);
                String[] list = sub.split(" ");
                String id = list[1];
                String sortBy = list[2];

                List<InvItem> items = InvAccessor.sortInv(id, sortBy);

                String jsonData = new Gson().toJson(items);

                out.println(jsonData);
            } else if (uri.contains("/InvServlet/tLine/")) {
                String pathInfo = request.getPathInfo();
                String sub = pathInfo.substring(1);
                
                List<InvItem> items = InvAccessor.getOrderItems(sub);
                String jsonData = new Gson().toJson(items);

                out.println(jsonData);
            } else if (uri.contains("/InvServlet/invItems/pointOfSale")) {
                String pathInfo = request.getPathInfo();
                String sub = pathInfo.substring(1);
                String[] list = sub.split("/");
                String id = list[1];
                
                List<InvItem> items = InvAccessor.pointOfSale(id);
                String jsonData = new Gson().toJson(items);

                out.println(jsonData);
            }
            else {
                String pathInfo = request.getPathInfo();
                String id = pathInfo.substring(1);

                List<InvItem> items = InvAccessor.getAllInv(id);

                String jsonData = new Gson().toJson(items);

                out.println(jsonData);
            }
        } catch (SQLException ex) {
            Logger.getLogger(InvServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            String uri = request.getRequestURI();
            if (uri.contains("/InvServlet/invItems/create")) {
                String pathInfo = request.getPathInfo();
                String sub = pathInfo.substring(1);
                String[] list = sub.split("/");
                String id = list[1];
                
                InvAccessor.createSale(id);
            }
            else if (uri.contains("/InvServlet/invItems/fillSale")) {
                String pathInfo = request.getPathInfo();
                String sub = pathInfo.substring(1);
                String[] list = sub.split("/");
                String loc = list[1];
                int itm = Integer.parseInt(list[2]);
                int quant = Integer.parseInt(list[3]);
                
                InvAccessor.fillSale(loc, itm, quant);
            } else {
                Scanner s = new Scanner(request.getReader());
            String jsonData = s.nextLine();

            Gson gson = new Gson();
            InvItem p = gson.fromJson(jsonData, InvItem.class);

            boolean b = InvAccessor.updateInv(p);
            out.println(b);
            }
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
