package science.aist.fhirauditeventtoocel.renderer;

import lombok.AllArgsConstructor;
import org.hl7.fhir.r5.model.AuditEvent;
import org.hl7.fhir.r5.model.Reference;
import science.aist.gtf.transformation.renderer.TransformationRender;
import science.aist.jack.stream.FilterStreamUtils;
import science.aist.ocel.model.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
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
                .flatMap(ReferenceObjectTypeRenderer::getReferenceStream)
                .flatMap(s -> s)
                .filter(x -> ReferenceObjectTypeRenderer.extractKey(x) != null)
                .filter(FilterStreamUtils.distinctByKeys(ReferenceObjectTypeRenderer::extractKey))
                .map(ref -> objectRenderer.renderElement(currentElement, ref))
                .forEach(objectsType.getObject()::add);
        logType.getObjects().add(objectsType);

        return logType;
    }
}
