package science.aist.fhirauditeventtoocel;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r5.model.AuditEvent;
import org.hl7.fhir.r5.model.Bundle;
import science.aist.fhirauditeventtoocel.renderer.EventTypeRenderer;
import science.aist.fhirauditeventtoocel.renderer.LogTypeRenderer;
import science.aist.fhirauditeventtoocel.renderer.ReferenceObjectTypeRenderer;
import science.aist.gtf.transformation.Transformer;
import science.aist.ocel.model.LogType;
import science.aist.ocel.model.ObjectFactory;
import science.aist.ocel.model.XMLRepository;
import science.aist.ocel.model.impl.LogRepository;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.stream.Collectors;

public class FhirAuditEventsToOCELLogService {
    private final ObjectFactory factory;
    private final Transformer<Collection<AuditEvent>, LogType> transformer;

    /**
     * Configures the transformers and renderers
     */
    public FhirAuditEventsToOCELLogService() {
        factory = new ObjectFactory();
        transformer = new FhirAuditEventsToOCELLogTransformer(
                new LogTypeRenderer(
                        factory,
                        new EventTypeRenderer(factory),
                        new ReferenceObjectTypeRenderer(factory)
                )
        );
    }

    /**
     * Use the transformer to transform from a {@link Collection} of {@link AuditEvent}s into a {@link LogType}
     *
     * @param auditEvents the collection of audit events
     * @return the resulting ocel log
     */
    public LogType convertFhirAuditEventsToOCELLog(Collection<AuditEvent> auditEvents) {
        return transformer.applyTransformation(auditEvents);
    }

    /**
     * Loads an audit bundle from an input stream and output the resulting ocel log into a given output stream
     *
     * @param auditBundleInputStream an input stream to a bundle of audit logs
     * @param logOutputStream        and output stream where the resulting log should be written to
     */
    public void convertFhirAuditEventsToOCELLog(InputStream auditBundleInputStream, OutputStream logOutputStream) {
        // Create fhir context and parse
        FhirContext ctx = FhirContext.forR5();
        IParser parser = ctx.newJsonParser();

        // read the bundle from the input stream
        Bundle auditEventBundle = parser.parseResource(Bundle.class, auditBundleInputStream);
        // convert the bundle resources into audit events
        Collection<AuditEvent> auditEvents = auditEventBundle.getEntry().stream().map(Bundle.BundleEntryComponent::getResource).map(AuditEvent.class::cast).collect(Collectors.toList());

        // convert the audit events into a log
        LogType logType = convertFhirAuditEventsToOCELLog(auditEvents);

        // write the xes log on the output stream
        XMLRepository<LogType> repository = new LogRepository();
        repository.save(factory.createLog(logType), logOutputStream);
    }
}
