/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.servlet;

import com.google.gson.Gson;
import db.access.UserAccessor;
import entities.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
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
@WebServlet(name = "UserServlet", urlPatterns = {"/UserServlet", "/UserServlet/Users/*", "/UserServlet/Users/sort*"})
public class UserServlet extends HttpServlet {

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
            if (request.getRequestURI().contains("/UserServlet/Users/sort")) {
                String pathInfo = request.getPathInfo();
                String sub = pathInfo.substring(1);
                String[] list = sub.split(" ");
                String sortBy = list[1];

                List<User> items = UserAccessor.sortUsers(sortBy);

                String jsonData = new Gson().toJson(items);

                out.println(jsonData);
            } else {
                List<User> items = UserAccessor.getAllUsers();

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

            User p = gson.fromJson(jsonData, User.class);

            boolean b = UserAccessor.addUser(p);
            out.println(b);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()) {
            Scanner s = new Scanner(request.getReader());
            String jsonData = s.nextLine();
            boolean b;
            Gson gson = new Gson();
            User p = gson.fromJson(jsonData, User.class);

            if (p.getPassword() == null) {
                b = UserAccessor.updateNoPassUser(p);
            } else {
                b = UserAccessor.updateUser(p);
            }
            out.println(b);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
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
