package servlets;

import com.google.gson.Gson;
import dbService.DBService;
import dbService.dataSets.GavDataSet;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import pom.PomDocument;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DependentGavsServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(DependentGavsServlet.class);

    DBService dbService;

    public DependentGavsServlet(DBService dbService) {
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

        Set<GavDataSet> gavDataSets = dbService.getDependentGavs(groupId, artifactId, version);
        if (gavDataSets.size() == 0) {
            Servlet.sendResponse(response, HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        List<Map<String, String>> list = new ArrayList<>();
        for (GavDataSet gav : gavDataSets) {
            list.add(gav.toMap());
        }

        String json = new Gson().toJson(list);

        try {
            Servlet.sendResponse(response, json, Servlet.JSON_CONTENT_TYPE, HttpServletResponse.SC_OK);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "Response sending error.", e);
            Servlet.sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
