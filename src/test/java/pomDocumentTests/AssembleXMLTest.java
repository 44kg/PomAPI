package pomDocumentTests;

import dbService.dataSets.GavDataSet;
import dbService.dataSets.PomDataSet;
import org.junit.Assert;
import org.junit.Test;
import pom.PomDocument;

public class AssembleXMLTest {

    @Test (expected = NullPointerException.class)
    public void assembleXMLNullTest() throws Exception {
        PomDocument.assembleXML(null);
    }

    @Test
    public void assembleXMLTest() throws Exception {
        PomDataSet pomDataSet = new PomDataSet("text1", "text2", "text3");
        pomDataSet.setGavDataSet(new GavDataSet("mainGav", "mainGav", "mainGav"));
        pomDataSet.getGavDataSet().addDependentGav(new GavDataSet("depGav", "depGav", "depGav"));
        String result = PomDocument.assembleXML(pomDataSet);
        Assert.assertTrue(result.contains("text1") && result.contains("text2") && result.contains("text3")
                && result.contains("mainGav") && result.contains("depGav"));
    }
}
