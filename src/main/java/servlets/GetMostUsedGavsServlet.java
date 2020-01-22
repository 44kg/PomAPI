package servlets;

import com.google.gson.Gson;
import dbService.DBService;
import dbService.dataSets.GavDataSet;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetMostUsedGavsServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(GetMostUsedGavsServlet.class);

    DBService dbService;

    public GetMostUsedGavsServlet(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String amount = request.getParameter("amount");
        Set<GavDataSet> gavDataSets;
        if (amount == null) {
            gavDataSets = dbService.getMostUsedGavs(10);
        } else {
            gavDataSets = dbService.getMostUsedGavs(Integer.parseInt(amount));
        }
        if (gavDataSets.size() == 0) {
            Servlet.sendResponse(response, HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        List<Map<String, String>> list = new ArrayList<>();
        for (GavDataSet gav : gavDataSets) {
            list.add(gav.getMap());
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
