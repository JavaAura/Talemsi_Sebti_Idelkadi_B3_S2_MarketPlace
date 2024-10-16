package com.market.marketplace.servlet;

import com.market.marketplace.config.ThymeleafConfig;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

public class ProductServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set the content type to HTML
        response.setContentType("text/html;charset=UTF-8");

        // Retrieve the Thymeleaf TemplateEngine instance from the servlet context
        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfig.TEMPLATE_ENGINE_ATTR);

        // Create a WebContext for the current request
        WebContext context = new WebContext(request, response, getServletContext());

        // Render the index.html template and write to response's writer
        templateEngine.process("index", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
