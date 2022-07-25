package science.aist.fhirauditeventtoocel;

import lombok.AllArgsConstructor;
import org.hl7.fhir.r5.model.AuditEvent;
import science.aist.gtf.transformation.Transformer;
import science.aist.gtf.transformation.renderer.TransformationRender;
import science.aist.ocel.model.LogType;

import java.util.Collection;

@AllArgsConstructor
public class FhirAuditEventsToOCELLogTransformer implements Transformer<Collection<AuditEvent>, LogType> {

    private TransformationRender<LogType, LogType, Collection<AuditEvent>, Collection<AuditEvent>> renderer;

    @Override
    public LogType applyTransformation(Collection<AuditEvent> auditEvents) {
        return renderer.renderElement(auditEvents, auditEvents);
    }
}
