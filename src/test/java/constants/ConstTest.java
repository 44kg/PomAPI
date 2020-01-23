package constants;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ConstTest {
    public static final String XML_1 = getXmlCode("src/test/java/pomDocumentTests/poms/pom_example1.xml");
    public static final String XML_2 = getXmlCode("src/test/java/pomDocumentTests/poms/pom_example2.xml");
    public static final String XML_3 = getXmlCode("src/test/java/pomDocumentTests/poms/pom_example3.xml");

    public static final String TEXT_1 = "<build><plugins><plugin><groupId>org.apache.maven.plugins</groupId><artifactI";
    public static final String TEXT_2 = "en.compiler.source>1.8</maven.compiler.source><maven.compiler.target>1.8</mave";
    public static final String TEXT_3 = "<test>value</test>";



    private ConstTest() {}

    public static String getXmlCode(String file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileInputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            return new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
