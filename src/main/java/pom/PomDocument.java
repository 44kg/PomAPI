package pom;

import dbService.dataSets.GavDataSet;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
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

//    public String getOtherCode() throws Exception {
//        Node node = document.getElementsByTagName("project").item(0).cloneNode(true);
//        int n = node.getChildNodes().getLength();
//        for (int i = 0; i < n; i++) {
//            node.removeChild(node.getChildNodes().item(0));
//        }
//        n = node.getAttributes().getLength();
//        for (int i = 0; i < n; i++) {
//            node.getAttributes().item(i).setNodeValue(null);
//        }
//        DOMSource domSource = new DOMSource(node);
//        StringWriter writer = new StringWriter();
//        StreamResult result = new StreamResult(writer);
//        TransformerFactory tf = TransformerFactory.newInstance();
//        Transformer transformer = tf.newTransformer();
//        transformer.transform(domSource, result);
//        return writer.toString();
//    }

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
