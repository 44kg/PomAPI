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
import java.util.Set;

@RunWith(Parameterized.class)
public class GetDependentGavsTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {ConstTest.XML_1, new GavDataSet("log4j", "log4j", "1.2.17"),
                        new GavDataSet("org.freemarker", "freemarker", "2.3.20")},
                {ConstTest.XML_2, new GavDataSet("org.eclipse.jetty", "jetty-server", "9.3.0.M0"),
                        new GavDataSet("org.eclipse.jetty", "jetty-webapp", "9.3.0.M0")},
                {ConstTest.XML_3, new GavDataSet("test1", "test1", "test1"),
                        new GavDataSet("test2", "test2", "test2")}
        });
    }

    private String xmlCode;
    private GavDataSet gav1;
    private GavDataSet gav2;


    public GetDependentGavsTest(String xmlCode, GavDataSet gav1, GavDataSet gav2) {
        this.xmlCode = xmlCode;
        this.gav1 = gav1;
        this.gav2 = gav2;
    }

    private PomDocument pomDocument;

    @Before
    public void init() throws Exception {
        pomDocument = new PomDocument(xmlCode);
    }

    @Test
    public void getDependentGavsTest() {
        Set<GavDataSet> gavs = pomDocument.getDependentGavs();
        Assert.assertTrue(gavs.contains(gav1) && gavs.contains(gav2));
    }
}
