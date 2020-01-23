package pomDocumentTests;

import org.junit.Test;
import org.xml.sax.SAXException;
import pom.PomDocument;

import javax.xml.parsers.ParserConfigurationException;

public class ConstructorTest {

    @Test(expected = NullPointerException.class)
    public void constructorTest1() throws ParserConfigurationException, SAXException {
        PomDocument pomDocument = new PomDocument(null);
    }

    @Test(expected = SAXException.class)
    public void constructorTest2() throws ParserConfigurationException, SAXException {
        PomDocument pomDocument = new PomDocument("");
    }

    @Test(expected = SAXException.class)
    public void constructorTest3() throws ParserConfigurationException, SAXException {
        PomDocument pomDocument = new PomDocument("AnyString");
    }
}
