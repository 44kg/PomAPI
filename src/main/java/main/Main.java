package main;

import dbService.DBService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.*;

public class Main {
    public static void main(String[] args) throws Exception {
        DBService dbService = new DBService();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new InsertPomServlet(dbService)), "/insert_pom");
        context.addServlet(new ServletHolder(new DependentGavsServlet(dbService)), "/dependent_gavs");
        context.addServlet(new ServletHolder(new MainGavsServlet(dbService)), "/main_gavs");
        context.addServlet(new ServletHolder(new GetPomServlet(dbService)), "/get_pom");
        context.addServlet(new ServletHolder(new GetMostUsedGavsServlet(dbService)), "/get_most_used_gavs");

        Server server = new Server(Integer.parseInt(args[0]));
        server.setHandler(context);

        server.start();
        server.join();
    }
}
