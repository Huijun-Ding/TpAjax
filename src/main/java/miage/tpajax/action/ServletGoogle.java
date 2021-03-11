package miage.tpajax.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import miage.tpajax.bd.Bd;

public class ServletGoogle extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /*----- Lecture de la requête en UTF-8 -----*/
        request.setCharacterEncoding("UTF-8");

        /*----- Type de la réponse -----*/
        response.setContentType("application/xml;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            /*----- Ecriture de la page XML -----*/
            out.println("<?xml version=\"1.0\"?>");
            out.println("<liste_mot>");

            /*----- Récupération des paramètres -----*/
            String mot_begin = request.getParameter("mot_begin");
            if (!mot_begin.equals("")) {
                try {
                    /*----- Lecture de liste de mots dans la BD -----*/
                    ArrayList<String> mots = Bd.LireMots(mot_begin);

                    for (String mot : mots) {
                        out.println("<mot><![CDATA[" + mot + "]]></mot>");
                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    out.println("<mot>Erreur - " + ex.getMessage() + "</mot>");
                }
            } else {
                out.println("<mot><![CDATA[]]></mot>");
            }
            out.println("</liste_mot>");
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
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
