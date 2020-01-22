package pom;

import dbService.dataSets.GavDataSet;
import dbService.dataSets.PomDataSet;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PomDocument {
    private static final Logger LOGGER = LogManager.getLogger(PomDocument.class);

    public static final String TEMPLATE_FILE = "src/main/java/templates/pom_template.xml";

    public static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    public static final String PROJECT_ATTRIBUTES = "projectAttributes";
    public static final String MODEL_VERSION = "modelVersion";
    public static final String GROUP_ID = "groupId";
    public static final String ARTIFACT_ID = "artifactId";
    public static final String VERSION = "version";
    public static final String OTHER_CODE = "otherCode";
    public static final String DEPENDENCIES = "dependencies";

    private Document document;

    public PomDocument(String xmlCode) throws ParserConfigurationException, SAXException {
        document = parseFromString(xmlCode);
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
        NodeList model = document.getElementsByTagName(MODEL_VERSION);
        return model.item(0).getTextContent();
    }

    public GavDataSet getMainGav() {
        String groupId = document.getElementsByTagName(GROUP_ID).item(0).getTextContent();
        String artifactId = document.getElementsByTagName(ARTIFACT_ID).item(0).getTextContent();
        String version = document.getElementsByTagName(VERSION).item(0).getTextContent();
        return new GavDataSet(groupId, artifactId, version);
    }

    public Set<GavDataSet> getDependentGavs() {
        Set<GavDataSet> gavsSet = new HashSet<>();
        NodeList groupIdList = document.getElementsByTagName(GROUP_ID);
        NodeList artifactIdList = document.getElementsByTagName(ARTIFACT_ID);
        NodeList versionList = document.getElementsByTagName(VERSION);
        for (int i = 1; i < groupIdList.getLength(); i++) {
            gavsSet.add(new GavDataSet(groupIdList.item(i).getTextContent(), artifactIdList.item(i).getTextContent(),
                    versionList.item(i).getTextContent()));
        }
        return gavsSet;
    }

    public String getOtherCode() {
        Node node = document.cloneNode(true).getChildNodes().item(0);
        removeTag(node, DEPENDENCIES);
        removeTag(node, MODEL_VERSION);
        removeTag(node, GROUP_ID);
        removeTag(node, ARTIFACT_ID);
        removeTag(node, VERSION);
        removeTextNodes(node);

        NodeList nodeList = node.getChildNodes();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < nodeList.getLength(); i++) {
            builder.append(getString(nodeList.item(i)));
        }
        return builder.toString().replace(XML_HEAD, "");
    }

    public static String assembleXML(PomDataSet pomDataSet) throws IOException, TemplateException {
        Map<String, String> variables = new HashMap<>();
        variables.put(PROJECT_ATTRIBUTES, pomDataSet.getProjectAttributes());
        variables.put(MODEL_VERSION, pomDataSet.getModelVersion());
        variables.put(GROUP_ID, pomDataSet.getGavDataSet().getGroupId());
        variables.put(ARTIFACT_ID, pomDataSet.getGavDataSet().getArtifactId());
        variables.put(VERSION, pomDataSet.getGavDataSet().getVersion());
        variables.put(OTHER_CODE, pomDataSet.getOtherCode());
        variables.put(DEPENDENCIES, assembleXmlGav(pomDataSet.getGavDataSet().getDependentGavs()));

        Writer stream = new StringWriter();
        Template template = new Configuration().getTemplate(TEMPLATE_FILE);
        template.process(variables, stream);
        return stream.toString();
    }

    private static String assembleXmlGav(Set<GavDataSet> gavDataSets) {
        StringBuilder builder = new StringBuilder();
        for (GavDataSet gav : gavDataSets) {
            builder.append("<").append(DEPENDENCIES).append(">");
            builder.append("<").append(GROUP_ID).append(">").append(gav.getGroupId())
                    .append("</").append(GROUP_ID).append(">");
            builder.append("<").append(ARTIFACT_ID).append(">").append(gav.getArtifactId())
                    .append("</").append(ARTIFACT_ID).append(">");
            builder.append("<").append(VERSION).append(">").append(gav.getVersion())
                    .append("</").append(VERSION).append(">");
            builder.append("</").append(DEPENDENCIES).append(">");
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
            LOGGER.log(Level.ERROR, "POM document transforming to string error", e);
        }
        return writer.toString();
    }

    private static Document parseFromString(String xmlCOde) throws ParserConfigurationException, SAXException {
        try (InputStream inputStream = new ByteArrayInputStream(xmlCOde.getBytes(StandardCharsets.UTF_8))) {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            document.setXmlStandalone(true);
            return document;
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "XML parsing error", e);
            return null;
        }
    }
}
