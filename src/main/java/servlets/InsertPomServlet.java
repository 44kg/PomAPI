package servlets;

import dbService.DBService;
import dbService.dataSets.GavDataSet;
import dbService.dataSets.PomDataSet;
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
import java.util.*;

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

        GavDataSet mainGav = pomDocument.getMainGav();
        Set<GavDataSet> dependentGavs = pomDocument.getDependentGavs();

        long id = dbService.insertPom(pomDocument.getProjectAttributes(), pomDocument.getModelVersion(),
                pomDocument.getOtherCode(), mainGav, dependentGavs);

        PomDataSet pomDataSet = dbService.getPomById(id);
        String json = pomDataSet.toJson();

        try {
            Servlet.sendResponse(response, json, Servlet.JSON_CONTENT_TYPE, HttpServletResponse.SC_OK);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "Response sending error.", e);
            Servlet.sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
