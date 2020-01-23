package pomDocumentTests;

import constants.ConstTest;
import dbService.dataSets.GavDataSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pom.PomDocument;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class GetMainGavTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {ConstTest.XML_1, new GavDataSet("org.44kg", "PomAPI", "1.0")},
                {ConstTest.XML_2, new GavDataSet("L1.1", "L1.1", "1.0")},
                {ConstTest.XML_3, new GavDataSet("groupId", "artifactId", "version")}
        });
    }

    private String xmlCode;
    private GavDataSet result;

    public GetMainGavTest(String xmlCode, GavDataSet result) {
        this.xmlCode = xmlCode;
        this.result = result;
    }

    private PomDocument pomDocument;

    @Before
    public void init() throws Exception {
        pomDocument = new PomDocument(xmlCode);
    }

    @Test
    public void getMainGavTest() {
        Assert.assertEquals(pomDocument.getMainGav(), result);
    }
}
