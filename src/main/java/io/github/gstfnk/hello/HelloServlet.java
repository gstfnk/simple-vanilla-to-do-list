package io.github.gstfnk.hello;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "Hello", urlPatterns = {"/api"})
public class HelloServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(HelloServlet.class);
    private static final String NAME_PARAM = "name";
    private static final String LANG_PARAM = "lang";

    private HelloService service;

    /**
     * Servlet container needs it
     */
    @SuppressWarnings("unused")
    public HelloServlet() {
        this(new HelloService());
    }

    HelloServlet(HelloService service) {
        this.service = service;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Request with parameters: " + req.getParameterMap());
        var nameParam = req.getParameter(NAME_PARAM);
        var langParam = req.getParameter(LANG_PARAM);
        Integer langId = null;
        try {
            langId = Integer.valueOf(langParam);
        } catch (NumberFormatException e) {
            logger.warn("Non-numeric language id used " + langParam);
        }
        var greeting = service.prepareGreeting(nameParam, langId);
        resp.getWriter().write(greeting);
    }
}
