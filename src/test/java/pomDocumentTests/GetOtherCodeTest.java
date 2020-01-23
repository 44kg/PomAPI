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
public class GetOtherCodeTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {ConstTest.XML_1, ConstTest.TEXT_1},
                {ConstTest.XML_2, ConstTest.TEXT_2},
                {ConstTest.XML_3, ConstTest.TEXT_3}
        });
    }

    private String xmlCode, result;

    public GetOtherCodeTest(String xmlCode, String result) {
        this.xmlCode = xmlCode;
        this.result = result;
    }

    private PomDocument pomDocument;

    @Before
    public void init() throws Exception {
        pomDocument = new PomDocument(xmlCode);
    }

    @Test
    public void getOtherCodeTest() {
        Assert.assertTrue(pomDocument.getOtherCode().contains(result));
    }
}
