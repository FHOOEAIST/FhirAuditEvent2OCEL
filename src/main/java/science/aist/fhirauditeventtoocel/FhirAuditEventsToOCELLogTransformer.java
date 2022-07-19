package science.aist.fhirauditeventtoocel;

import org.hl7.fhir.r5.model.AuditEvent;
import science.aist.gtf.transformation.Transformer;
import science.aist.ocel.model.LogType;

import java.util.Collection;

public class FhirAuditEventsToOCELLogTransformer implements Transformer<Collection<AuditEvent>, LogType> {
    @Override
    public LogType applyTransformation(Collection<AuditEvent> auditEvents) {
        return null;
    }
}
