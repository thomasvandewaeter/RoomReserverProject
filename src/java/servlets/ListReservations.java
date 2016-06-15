/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entities.Reservation;
import entities.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sessionbeans.ReservationFacade;
import sessionbeans.UserFacade;

/**
 *
 * @author Matteus
 */
@WebServlet(name = "ListReservations", urlPatterns = {"/ListReservations"})
public class ListReservations extends HttpServlet {

    ReservationFacade reservationFacade;
    UserFacade userFacade;
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
            try {
                List<Reservation> list = reservationFacade.getAllReservations();
                if(list != null && !list.isEmpty()){
                    Iterator<Reservation> it = list.iterator();
                    while(it.hasNext()){
                        System.out.println(it.next().getUser().getEmail());
                    }
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            
            try {
                List<User> list = userFacade.getAllUsers();
                if(list != null && !list.isEmpty()){
                    Iterator<User> it = list.iterator();
                    while(it.hasNext()){
                        System.out.println(it.next().getEmail());
                    }
                } else {
                    System.out.println("*** No users found ***");
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            
            
            
            out.println("<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<title>Servlet ListReservations</title>"
                    + "</head>"
                    + "<body>"
                    + "<h1>Servlet ListReservations at " + request.getContextPath() + "</h1>");
            for(int i = 0; i<5; i++){
                out.println("<div><b>" + i + "</b></div>");
            }
            out.println("</body>"
                    + "</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
