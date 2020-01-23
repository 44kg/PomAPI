package pomDocumentTests;

import constants.ConstTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pom.PomDocument;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class GetProjectAttributesTest {
    public static final String ATTR = "xmlns=\"http://maven.apache.org/POM/4.0.0\" " +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
            "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\" ";

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {ConstTest.XML_1, ATTR},
                {ConstTest.XML_2, "xmlns=\"http://maven.apache.org/POM/4.0.0\" "},
                {ConstTest.XML_3, ""}
        });
    }

    private String xmlCode, result;

    public GetProjectAttributesTest(String xmlCode, String result) {
        this.xmlCode = xmlCode;
        this.result = result;
    }

    private PomDocument pomDocument;

    @Before
    public void init() throws Exception {
        pomDocument = new PomDocument(xmlCode);
    }

    @Test
    public void getProjectAttributesTest() {
        Assert.assertEquals(pomDocument.getProjectAttributes(), result);
    }
}
