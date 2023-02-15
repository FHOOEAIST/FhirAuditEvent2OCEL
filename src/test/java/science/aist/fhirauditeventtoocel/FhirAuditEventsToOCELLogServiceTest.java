package science.aist.fhirauditeventtoocel;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


/**
 * <p>Tests {@link FhirAuditEventsToOCELLogService}</p>
 *
 * @author Andreas Pointner
 */
public class FhirAuditEventsToOCELLogServiceTest {

    @Test
    public void testConvertFhirAuditEventsToOCELLog() {
        // given
        FhirAuditEventsToOCELLogService service = new FhirAuditEventsToOCELLogService();
        InputStream resourceAsStream = getClass().getResourceAsStream("/auditEvents.json");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // when
        service.convertFhirAuditEventsToOCELLog(resourceAsStream, outputStream);

        // then
        String res = outputStream.toString(StandardCharsets.UTF_8);
        Assert.assertNotNull(res);
    }
}