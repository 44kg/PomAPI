package servlets;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Servlet {
    public static final String JSON_CONTENT_TYPE = "text/json;charset=utf-8";
    public static final String XML_CONTENT_TYPE = "text/xml;charset=utf-8";

    private Servlet() {}

    public static void sendResponse(HttpServletResponse response, String content, String contentType, int status) throws IOException {
        response.setContentType(contentType);
        response.getWriter().println(content);
        response.setStatus(status);
    }

    public static void sendResponse(HttpServletResponse response, int status) {
        response.setStatus(status);
    }
}
