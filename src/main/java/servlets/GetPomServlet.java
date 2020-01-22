package servlets;

import dbService.DBService;
import dbService.dataSets.PomDataSet;
import freemarker.template.TemplateException;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import pom.PomDocument;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetPomServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(GetPomServlet.class);

    DBService dbService;

    public GetPomServlet(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String groupId = request.getParameter(PomDocument.GROUP_ID);
        String artifactId = request.getParameter(PomDocument.ARTIFACT_ID);
        String version = request.getParameter(PomDocument.VERSION);

        if (groupId == null || artifactId == null || version == null) {
            Servlet.sendResponse(response, HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        PomDataSet pomDataSet = dbService.getPom(groupId, artifactId, version);
        if (pomDataSet == null) {
            Servlet.sendResponse(response, HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        try {
            String pom = PomDocument.assembleXML(pomDataSet);
            Servlet.sendResponse(response, pom, Servlet.XML_CONTENT_TYPE, HttpServletResponse.SC_OK);
        } catch (IOException | TemplateException e) {
            LOGGER.log(Level.ERROR, "Response sending error.", e);
            Servlet.sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
