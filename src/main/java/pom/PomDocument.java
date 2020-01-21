package pom;

import dbService.dataSets.GavDataSet;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class PomDocument {
    private static final Logger LOGGER = LogManager.getLogger(PomDocument.class);

    private Document document;

    public PomDocument(String xmlCode) {
        document = parseFromString(xmlCode);
    }

    public PomDocument(File file) {
        document = parseFromFile(file.getName());
    }

    public String getProjectAttributes() {
        Node rootNode = document.getDocumentElement();
        NamedNodeMap map = rootNode.getAttributes();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < map.getLength(); i++) {
            if (map.item(i).getNodeType() == Node.ATTRIBUTE_NODE) {
                builder.append(map.item(i).getNodeName()).append("=").append(map.item(i).getTextContent()).append(" ");
            }
        }
        return builder.toString();
    }

    public String getModelVersion() {
        NodeList model = document.getElementsByTagName("modelVersion");
        return model.item(0).getTextContent();
    }

    public GavDataSet getMainGav() {
        String groupId = document.getElementsByTagName("groupId").item(0).getTextContent();
        String artifactId = document.getElementsByTagName("artifactId").item(0).getTextContent();
        String version = document.getElementsByTagName("version").item(0).getTextContent();
        return new GavDataSet(groupId, artifactId, version);
    }

    public Set<GavDataSet> getDependentGavs() {
        Set<GavDataSet> gavsSet = new HashSet<>();
        NodeList groupIdList = document.getElementsByTagName("groupId");
        NodeList artifactIdList = document.getElementsByTagName("artifactId");
        NodeList versionList = document.getElementsByTagName("version");
        for (int i = 1; i < groupIdList.getLength(); i++) {
            gavsSet.add(new GavDataSet(groupIdList.item(i).getTextContent(), artifactIdList.item(i).getTextContent(),
                    versionList.item(i).getTextContent()));
        }
        return gavsSet;
    }

    public String getOtherCode() {
        Node node = document.cloneNode(true).getChildNodes().item(0);
        removeTag(node, "dependencies");
        removeTag(node, "modelVersion");
        removeTag(node, "groupId");
        removeTag(node, "artifactId");
        removeTag(node, "version");
        removeTextNodes(node);

        NodeList nodeList = node.getChildNodes();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < nodeList.getLength(); i++) {
            builder.append(getString(nodeList.item(i)));
        }
        return builder.toString();
    }

    private static void removeTextNodes(Node node) {
        int i = 0;
        while (node.getChildNodes().item(i) != null) {
            if (node.getChildNodes().item(i).getNodeName().equalsIgnoreCase("#text")
                    && node.getChildNodes().item(i).getTextContent().trim().equals("")) {
                node.removeChild(node.getChildNodes().item(i));
            } else {
                i++;
            }
        }
        NodeList nodeList = node.getChildNodes();
        if (nodeList.getLength() > 0) {
            int n = node.getChildNodes().getLength();
            for (int j = 0; j < n; j++) {
                removeTextNodes(nodeList.item(j));
            }
        }
    }

    private static void removeTag(Node node, String tagName) {
        int i = 0;
        while (node.getChildNodes().item(i) != null) {
            if (node.getChildNodes().item(i).getNodeName().equalsIgnoreCase(tagName)) {
                node.removeChild(node.getChildNodes().item(i));
            } else {
                i++;
            }
        }
    }

    private static String getString(Node node) {
        DOMSource domSource = new DOMSource(node);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
        } catch (TransformerException e) {
            LOGGER.log(Level.ERROR, "", e);
        }
        return writer.toString();
    }

    private static Document parseFromString(String xmlCOde) {
        try (InputStream inputStream = new ByteArrayInputStream(xmlCOde.getBytes(StandardCharsets.UTF_8))) {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            document.setXmlStandalone(true);
            return document;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOGGER.log(Level.ERROR, "Parsing error", e);
            return null;
        }
    }

    private static Document parseFromFile(String uri) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(uri);
            document.setXmlStandalone(true);
            return document;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOGGER.log(Level.ERROR, "Parsing error", e);
            return null;
        }
    }
}
