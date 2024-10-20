package com.market.marketplace.servlet;

import com.market.marketplace.dao.daoImpl.AdminDaoImpl;
import com.market.marketplace.entities.Admin;
import com.market.marketplace.entities.Client;
import com.market.marketplace.entities.enums.Role;
import com.market.marketplace.service.serviceImpl.AdminServiceImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


public class AdminServlet extends HttpServlet {
    private AdminServiceImpl adminServiceImpl;
    private EntityManagerFactory entityManagerFactory;
    private TemplateEngine templateEngine;


    @Override
    public void init(ServletConfig config) throws ServletException {
        entityManagerFactory = Persistence.createEntityManagerFactory("marketPlace");
        this.templateEngine = new TemplateEngine();
        if (entityManagerFactory == null) {
            throw new ServletException("EntityManagerFactory could not be created.");
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        AdminDaoImpl adminDaoImpl = new AdminDaoImpl(entityManager);
        this.adminServiceImpl = new AdminServiceImpl(adminDaoImpl);
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(config.getServletContext());
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(templateResolver);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tableType = request.getParameter("tableType");
        String emailQuery = request.getParameter("email");

        int page = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
        int size = Integer.parseInt(request.getParameter("size") != null ? request.getParameter("size") : "3");
        if (tableType == null) {
            throw new ServletException("Missing table type parameter.");
        }

        List<Admin> admins = new ArrayList<>();
        List<Admin> superAdmins = new ArrayList<>();
        List<Client> clients = new ArrayList<>();
        long totalSuperAdmins = 0;
        long totalClients = 0;
        long totalAdmins = 0;
        long totalPagesSuperAdmin = 0;
        long totalPagesClients = 0;
        long totalPagesAdmins = 0;

        try {
            switch (tableType) {
                case "admins":
                    if (emailQuery != null && !emailQuery.isEmpty()) {
                        admins = adminServiceImpl.findAdminsByEmail(emailQuery);
                    } else {
                        totalAdmins = adminServiceImpl.countAdmins();
                        admins = adminServiceImpl.findAllAdmins(page, size);
                        totalPagesAdmins =(long) Math.ceil((double) totalAdmins / size);
                    }
                    break;
                case "superAdmin":
                    if (emailQuery != null && !emailQuery.isEmpty()) {
                        superAdmins = adminServiceImpl.findSuperAdminsByEmail(emailQuery);
                    } else {
                        totalSuperAdmins = adminServiceImpl.countSuperAdmins();
                        superAdmins = adminServiceImpl.findSuperAdmins(page, size);
                       totalPagesSuperAdmin = (long) Math.ceil((double) totalSuperAdmins / size);
                    }
                    break;
                case "clients":
                    if (emailQuery != null && !emailQuery.isEmpty()) {
                        clients = adminServiceImpl.findClientsByEmail(emailQuery);
                    } else {
                        totalClients = adminServiceImpl.countClients();
                        clients = adminServiceImpl.findAllClients(page, size);
                        totalPagesClients = (long) Math.ceil((double) totalClients / size);
                    }
                    break;
                default:
                    throw new ServletException("Invalid table type requested.");
            }
        } catch (Exception e) {
            throw new ServletException("Error retrieving data from the database", e);
        }

        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());




        switch (tableType) {
            case "admins":
                context.setVariable("admins", admins);
                context.setVariable("totalAdmins", totalAdmins);
                context.setVariable("totalPages", totalPagesAdmins);
                context.setVariable("currentPage", page);
                context.setVariable("pageSize", size);
                break;
            case "superAdmin":
                context.setVariable("superAdmins", superAdmins);
                context.setVariable("totalSuperAdmins", totalSuperAdmins);
                context.setVariable("totalPages", totalPagesSuperAdmin);
                context.setVariable("currentPage", page);
                context.setVariable("pageSize", size);
                break;
            case "clients":
                context.setVariable("clients", clients);
                context.setVariable("totalClients", totalClients);
                context.setVariable("totalPages", totalPagesClients);
                context.setVariable("currentPage", page);
                context.setVariable("pageSize", size);
                break;
        }

        response.setContentType("text/html;charset=UTF-8");
        templateEngine.process("admin/" + tableType, context, response.getWriter());
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if (action == null) {
                response.sendRedirect(request.getContextPath() + "/"); // Redirect to admins page
                return;
            }

            switch (action) {
                case "addAdminNormal":
                    handleAddAdminNormal(request, response);
                    break;
                case "updateAdminNormal":
                    handleUpdateAdminNormal(request, response);
                    break;
                case "deleteAdminNormal":
                    handleDeleteAdminNormal(request, response);
                    break;
                case "addSuperAdmin":
                    handleAddSuperAdmin(request, response);
                    break;
                case "updateSuperAdmin":
                    handleUpdateSuperAdmin(request, response);
                    break;
                case "deleteSuperAdmin":
                    handleDeleteSuperAdmin(request, response);
                    break;
                case "deleteClients":  // New case for deleting clients
                    handleDeleteClients(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/");
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error processing form submission", e);
        }

    }

    public void handleAddAdminNormal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        Admin admin = new Admin();
        admin.setFirstName(firstname);
        admin.setLastName(lastname);
        admin.setEmail(email);
        admin.setPassword(hashedPassword);
        admin.setRole(Role.ADMIN);

        adminServiceImpl.addAdminNormal(admin); // Call service to save admin

        response.sendRedirect(request.getContextPath() + "/admin?tableType=admins");
    }

    private void handleAddSuperAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Hash the password using BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        Admin superAdmin = new Admin();
        superAdmin.setFirstName(firstname);
        superAdmin.setLastName(lastname);
        superAdmin.setEmail(email);
        superAdmin.setPassword(hashedPassword);
        superAdmin.setRole(Role.ADMIN);// Store hashed password

        adminServiceImpl.addSuperAdmin(superAdmin); // Call service to save superAdmin

        response.sendRedirect(request.getContextPath() + "/admin?tableType=superAdmin"); // Redirect to list admins
    }

    private void handleUpdateSuperAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("superadminId"));
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");


        Admin superAdmin = new Admin();
        superAdmin.setId(id);
        superAdmin.setFirstName(firstname);
        superAdmin.setLastName(lastname);
        superAdmin.setEmail(email);
        superAdmin.setAccessLevel(1);
        if (password != null && !password.isEmpty()) {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            superAdmin.setPassword(hashedPassword);
        }

        adminServiceImpl.updateSuperAdmin(superAdmin);

        response.sendRedirect(request.getContextPath() + "/admin?tableType=superAdmin");
    }

    public void handleDeleteAdminNormal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int adminId = Integer.parseInt(request.getParameter("adminId"));

        try {
            adminServiceImpl.deleteAdminNormal(adminId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/admin?tableType=admins");
    }

    public void handleDeleteSuperAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int superAdminId = Integer.parseInt(request.getParameter("superAdminId"));

        try {
            adminServiceImpl.deleteSuperAdmin(superAdminId);
        } catch (Exception e) {
            e.printStackTrace();

        }

        response.sendRedirect(request.getContextPath() + "/admin?tableType=superAdmin");
    }

    public void handleUpdateAdminNormal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int adminId = Integer.parseInt(request.getParameter("adminId"));
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Admin admin = new Admin();
        admin.setId(adminId);
        admin.setFirstName(firstName);
        admin.setLastName(lastName);
        admin.setEmail(email);
        admin.setAccessLevel(0); // Normal admin level

        if (password != null && !password.isEmpty()) {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            admin.setPassword(hashedPassword);
        }

        try {
            adminServiceImpl.updateAdminNormal(admin); // Update the normal admin
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Redirect to the admin management page
        response.sendRedirect(request.getContextPath() + "/admin?tableType=admins");
    }

    private void handleDeleteClients(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int clientId = Integer.parseInt(request.getParameter("clientId"));

        try {
            adminServiceImpl.deleteClientById(clientId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/admin?tableType=clients");
    }


    @Override
    public void destroy() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
