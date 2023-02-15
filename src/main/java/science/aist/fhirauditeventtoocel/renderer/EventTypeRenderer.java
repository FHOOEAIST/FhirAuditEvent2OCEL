package science.aist.fhirauditeventtoocel.renderer;

import lombok.AllArgsConstructor;
import org.hl7.fhir.r5.model.AuditEvent;
import org.hl7.fhir.r5.model.Reference;
import science.aist.fhirauditeventtoocel.AttributeTypeHelper;
import science.aist.gtf.transformation.renderer.TransformationRender;
import science.aist.ocel.model.EventType;
import science.aist.ocel.model.ObjectFactory;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * <p>Renderer for {@link EventType}</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@AllArgsConstructor
public class EventTypeRenderer implements TransformationRender<EventType, EventType, Collection<AuditEvent>, AuditEvent> {

    private final ObjectFactory factory;

    @Override
    public EventType renderElement(Collection<AuditEvent> auditEvents, AuditEvent currentElement) {
        return mapProperties(createElement(), auditEvents, currentElement);
    }

    @Override
    public EventType createElement() {
        return factory.createEventType();
    }

    @Override
    public EventType mapProperties(EventType eventType, Collection<AuditEvent> auditEvents, AuditEvent currentElement) {
        eventType.getStringOrDateOrInt().add(AttributeTypeHelper.string("id", currentElement.getId()));
        // TODO for the moment we just use the first code
        eventType.getStringOrDateOrInt().add(AttributeTypeHelper.string("activity", currentElement.getCode().getCodingFirstRep().getDisplay()));
        eventType.getStringOrDateOrInt().add(AttributeTypeHelper.date("timestamp", currentElement.getOccurredDateTimeType().getValue()));
        eventType.getStringOrDateOrInt().add(AttributeTypeHelper.list("omap", Stream.of(
                currentElement.getBasedOn().stream(),
                        Stream.of(currentElement.getEncounter()),
                        currentElement.getAgent().stream().map(AuditEvent.AuditEventAgentComponent::getWho),
                        Stream.of(currentElement.getPatient())
                )
                .flatMap(s -> s)
                .filter(Reference::hasReference)
                .map(Reference::getReference)
                .map(ref -> AttributeTypeHelper.string("object-id", ref))
        ));
        return eventType;
    }
}
