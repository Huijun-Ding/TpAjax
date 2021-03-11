package miage.tpajax.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import miage.tpajax.bd.Bd;

public class ServletValidation extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        /*----- Lecture de la requête en UTF-8 -----*/
        request.setCharacterEncoding("UTF-8");

        /*----- Type de la réponse -----*/
        response.setContentType("application/xml;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try ( PrintWriter out = response.getWriter()) {
            /*----- Ecriture de la page XML -----*/
            out.println("<?xml version=\"1.0\"?>");

            /*----- Récupération des paramètres -----*/
            String mot = request.getParameter("zone");
            String method = request.getParameter("method");
            if (method.equals("valider")){
                if (!mot.equals("")){
                    try {
                        /*----- Lecture de liste de mots dans la BD -----*/
                        Boolean existe = Bd.VerifierMot(mot);
                        System.out.println(existe);
                        if (existe) {
                            out.println("<existe>Existe!</existe>");
                        } else {
                            out.println("<existe>N'existe pas!</existe>");
                        }
                    } catch (ClassNotFoundException | SQLException ex) {
                        out.println("<existe>Erreur - " + ex.getMessage() + "</existe>");
                    }
                }else {
                    out.println("<existe></existe>");
                }

            }else if (method.equals("ajouter")){
                if (!mot.equals("")){
                    try {
                        /*----- appeler la fonction d'insertion de mot -----*/
                        int nb = Bd.ajouterMot(mot);
                        if (nb == 1) {
                            out.println("<insertion>Insertion réussie!</insertion>");
                        } else {
                            out.println("<insertion>Insertion échouée!</insertion>");
                        }
                    } catch (ClassNotFoundException | SQLException ex) {
                        out.println("<insertion>Erreur - " + ex.getMessage() + "</insertion>");
                    }
                }
            }

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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ServletValidation.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ServletValidation.class.getName()).log(Level.SEVERE, null, ex);
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
