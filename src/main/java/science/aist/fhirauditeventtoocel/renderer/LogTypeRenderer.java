package science.aist.fhirauditeventtoocel.renderer;

import lombok.AllArgsConstructor;
import org.hl7.fhir.r5.model.AuditEvent;
import org.hl7.fhir.r5.model.Reference;
import science.aist.gtf.transformation.renderer.TransformationRender;
import science.aist.jack.stream.FilterStreamUtils;
import science.aist.ocel.model.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * <p>Renderer for the {@link LogType}</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@AllArgsConstructor
public class LogTypeRenderer implements TransformationRender<LogType, LogType, Collection<AuditEvent>, Collection<AuditEvent>> {

    private final ObjectFactory factory;

    private final TransformationRender<EventType, EventType, Collection<AuditEvent>, AuditEvent> eventRenderer;

    private final TransformationRender<ObjectType, ObjectType, Collection<AuditEvent>, Reference> objectRenderer;

    @Override
    public LogType renderElement(Collection<AuditEvent> auditEvents, Collection<AuditEvent> currentElement) {
        return mapProperties(createElement(), auditEvents, currentElement);
    }

    @Override
    public LogType createElement() {
        return factory.createLogType();
    }

    @Override
    public LogType mapProperties(LogType logType, Collection<AuditEvent> auditEvents, Collection<AuditEvent> currentElement) {
        EventsType eventsType = factory.createEventsType();
        currentElement.stream()
                .map(ae -> eventRenderer.renderElement(currentElement, ae))
                .forEach(eventsType.getEvent()::add);
        logType.getEvents().add(eventsType);

        ObjectsType objectsType = factory.createObjectsType();
        currentElement.stream()
                .flatMap(ae -> Stream.of(ae.getBasedOn().stream(), Stream.of(ae.getEncounter()), ae.getAgent().stream().map(AuditEvent.AuditEventAgentComponent::getWho)))
                .flatMap(s -> s)
                .filter(FilterStreamUtils.distinctByKeys(Reference::getReference))
                .sorted(Comparator.<Reference, String>comparing(r -> r.getReferenceElement().getResourceType()).thenComparing(r -> r.getReferenceElement().getIdPartAsLong()))
                .map(ref -> objectRenderer.renderElement(currentElement, ref))
                .forEach(objectsType.getObject()::add);
        logType.getObjects().add(objectsType);

        return logType;
    }
}
