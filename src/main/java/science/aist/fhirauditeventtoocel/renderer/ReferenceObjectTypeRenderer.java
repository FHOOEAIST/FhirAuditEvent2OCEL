package science.aist.fhirauditeventtoocel.renderer;

import lombok.AllArgsConstructor;
import org.hl7.fhir.r5.model.AuditEvent;
import org.hl7.fhir.r5.model.Reference;
import science.aist.fhirauditeventtoocel.AttributeTypeHelper;
import science.aist.gtf.transformation.renderer.TransformationRender;
import science.aist.ocel.model.ObjectFactory;
import science.aist.ocel.model.ObjectType;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * <p>Renderer to create {@link ObjectType}s out of {@link Reference}s</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@AllArgsConstructor
public class ReferenceObjectTypeRenderer implements TransformationRender<ObjectType, ObjectType, Collection<AuditEvent>, Reference> {

    private final ObjectFactory factory;

    @Override
    public ObjectType renderElement(Collection<AuditEvent> auditEvents, Reference currentElement) {
        return mapProperties(createElement(), auditEvents, currentElement);
    }

    @Override
    public ObjectType createElement() {
        return factory.createObjectType();
    }

    @Override
    public ObjectType mapProperties(ObjectType objectType, Collection<AuditEvent> auditEvents, Reference currentElement) {
        objectType.getStringOrDateOrInt().add(AttributeTypeHelper.string("id", extractKey(currentElement)));
        if (currentElement.hasReferenceElement() && currentElement.getReferenceElement().hasResourceType()) {
            objectType.getStringOrDateOrInt().add(AttributeTypeHelper.string("type", currentElement.getReferenceElement().getResourceType()));
        }
        return objectType;
    }

    public static String extractKey(Reference currentElement) {
        if (currentElement.hasReference()) {
            return currentElement.getReference();
        }
        if (currentElement.hasIdentifier()) {
            return currentElement.getIdentifier().getValue();
        }
        return null;
    }

    public static Stream<Stream<Reference>> getReferenceStream(AuditEvent ae) {
        return Stream.of(ae.getBasedOn().stream(),
                Stream.of(ae.getEncounter()),
                ae.getAgent().stream().filter(AuditEvent.AuditEventAgentComponent::hasWho).map(AuditEvent.AuditEventAgentComponent::getWho),
                ae.getEntity().stream().filter(AuditEvent.AuditEventEntityComponent::hasWhat).map(AuditEvent.AuditEventEntityComponent::getWhat),
                Stream.of(ae.getPatient()));
    }
}
