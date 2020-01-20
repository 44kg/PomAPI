package main;

import dbService.DBService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        DBService dbService = new DBService();
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                String groupId = req.getParameter("groupId");
                String artifactId = req.getParameter("artifactId");
                String version = req.getParameter("version");

                if (groupId == null || artifactId == null || version == null) {
                    resp.setContentType("text/html;charset=utf-8");
                    resp.getWriter().println("Wrong!");
                    resp.setStatus(HttpServletResponse.SC_OK);
                    return;
                }
                resp.setContentType("text/html;charset=utf-8");
                resp.getWriter().println("Added!");
                resp.setStatus(HttpServletResponse.SC_OK);


            }
        }), "/*");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
