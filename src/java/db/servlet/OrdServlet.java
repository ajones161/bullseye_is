/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.servlet;

import com.google.gson.Gson;
import db.access.OrdAccessor;
import entities.Order;
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
@WebServlet(name = "OrdServlet", urlPatterns = {"/OrdServlet", "/OrdServlet/Orders", "/OrdServlet/Orders/*", "/OrdServlet/Orders/Filter*"})
public class OrdServlet extends HttpServlet {

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
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            String uri = request.getRequestURI();
            if (uri.contains("/OrdServlet/Orders/out")) {
                List<Order> items = OrdAccessor.getOutstanding();

                String jsonData = new Gson().toJson(items);

                out.println(jsonData);

            } else if (!uri.contains("/OrdServlet/Orders/out") && !uri.contains("/OrdServlet/Orders/Filter")) {
                List<Order> items = OrdAccessor.getAll();

                String jsonData = new Gson().toJson(items);

                out.println(jsonData);
            } else if (uri.contains("/OrdServlet/Orders/Filter") || uri.contains("/OrdServlet/Orders/Out/Filter")) {
                String type;

                String pathInfo = request.getPathInfo();
                String sub = pathInfo.substring(1);
                String[] list = sub.split("/");
                String location = list[1];

                if (uri.contains("/OrdServlet/Orders/Out/Filter")) {
                    type = "out";
                } else {
                    type = "reg";
                }
                List<Order> items = OrdAccessor.getFiltered(type, location);
                String jsonData = new Gson().toJson(items);

                out.println(jsonData);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrdServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            String uri = request.getRequestURI();
            if (!uri.contains("/OrdServlet/Orders/Update")) {
                Scanner s = new Scanner(request.getReader());
                String jsonData = s.nextLine();

                Gson gson = new Gson();
                Order p = gson.fromJson(jsonData, Order.class);

                boolean b = OrdAccessor.updateOrd(p);
                out.println(b);
            } else if (uri.contains("/OrdServlet/Orders/Update/Loss")) {
                String pathInfo = request.getPathInfo();
                String sub = pathInfo.substring(1);
                String[] list = sub.split("/");
                boolean b = OrdAccessor.emptyLoss(list[2]);
            } else if (uri.contains("/OrdServlet/Orders/Update/FillLoss")) {
                String pathInfo = request.getPathInfo();
                String sub = pathInfo.substring(1);
                String[] list = sub.split("/");
                int ID = Integer.parseInt(list[2]);
                String notes = list[3];
                int amount = Integer.parseInt(list[4]);
                String locationID = list[5];
                OrdAccessor.fillLoss(ID, notes, amount, locationID);
            } else if (uri.contains("OrdServlet/Orders/Update/CHECKWH")) {
                String pathInfo = request.getPathInfo();
                String sub = pathInfo.substring(1);
                
                String[] list = sub.split("/");
                
                int blyat = Integer.parseInt(list[2]);
                OrdAccessor.checkWarehouse(blyat);
            }
            else if (uri.contains("/OrdServlet/Orders/Update/CreateReturn")) {
                String pathInfo = request.getPathInfo();
                String sub = pathInfo.substring(1);
                
                String[] list = sub.split("/");
                
                String locationID = list[2];
                int ID = Integer.parseInt(list[3]);
                int amount = Integer.parseInt(list[4]);
                String type = list[5];
                
                OrdAccessor.createReturn(locationID, ID, amount, type); 
            } 
            else if (uri.contains("/OrdServlet/Orders/Update")) {
                String pathInfo = request.getPathInfo();
                String sub = pathInfo.substring(1);
                String[] list = sub.split("/");
                String location = list[1];
                String ftn = list[2];
                boolean b = OrdAccessor.createOrd(location, ftn);
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
