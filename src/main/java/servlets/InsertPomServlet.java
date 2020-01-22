package servlets;

import com.google.gson.Gson;
import dbService.DBService;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import pom.PomDocument;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class InsertPomServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(InsertPomServlet.class);

    DBService dbService;

    public InsertPomServlet(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String xmlCode;
        try (InputStream inputStream = request.getInputStream()) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            xmlCode = new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "Reading request body error.", e);
            Servlet.sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        PomDocument pomDocument;
        try {
            pomDocument = new PomDocument(xmlCode);
        } catch (ParserConfigurationException | SAXException e) {
            LOGGER.log(Level.ERROR, "XML parsing error.", e);
            Servlet.sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        dbService.insertPom(pomDocument.getProjectAttributes(), pomDocument.getModelVersion(),
                pomDocument.getOtherCode(), pomDocument.getMainGav(), pomDocument.getDependentGavs());

        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();
        map.put("insert", "true");

        try {
            Servlet.sendResponse(response, gson.toJson(map), Servlet.JSON_CONTENT_TYPE, HttpServletResponse.SC_OK);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "Response sending error.", e);
            Servlet.sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
