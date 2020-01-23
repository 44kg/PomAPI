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
public class GetModelVersionTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {ConstTest.XML_1, "4.0.0"},
                {ConstTest.XML_2, "3.0.0"},
                {ConstTest.XML_3, "modelVersion"}
        });
    }

    private String xmlCode, result;

    public GetModelVersionTest(String xmlCode, String result) {
        this.xmlCode = xmlCode;
        this.result = result;
    }

    private PomDocument pomDocument;

    @Before
    public void init() throws Exception {
        pomDocument = new PomDocument(xmlCode);
    }

    @Test
    public void getModelVersionTest() {
        Assert.assertEquals(pomDocument.getModelVersion(), result);
    }
}
