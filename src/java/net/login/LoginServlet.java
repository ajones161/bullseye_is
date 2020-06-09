package net.login;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import entities.User;

/**
 *
 * @author ajone
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private LoginDao loginDao;

    @Override
    public void init() {
        loginDao = new LoginDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            String username = request.getParameter("username");
            String password = request.getParameter("password");
            LoginBean loginBean = new LoginBean();
            loginBean.setUsername(username);
            loginBean.setPassword(password);

            Connection connection = null;

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bullseye?useSSL=false", "root", "root");

            PreparedStatement preparedStatement = connection.prepareStatement("select * from user where userid = ?");
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            String userID = null;
            String locationID = null;
            String roleID = null;
            Boolean active = null;

            while (rs.next()) {
                userID = rs.getString("userID");
                locationID = rs.getString("locationID");
                roleID = rs.getString("roleID");
                active = rs.getBoolean("active");
            }
            User currentUser = new User(userID, locationID, roleID, active);
            try {
                if (loginDao.validate(loginBean)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("uName", username);
                    session.setAttribute("user", currentUser);
                    session.setAttribute("Error", "");
                    if(!"Driver".equals(roleID)) {
                        response.sendRedirect("beHome.jsp");
                    }
                    else {
                        response.sendRedirect("webPortal.jsp");
                    }
                } else {
                    HttpSession session = request.getSession();
                    session.setAttribute("uName", username);
                    session.setAttribute("Error", "Username or password did not match our records. Please try again.");
                    response.sendRedirect("index.jsp");
                }
            } catch (ClassNotFoundException e) {
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
