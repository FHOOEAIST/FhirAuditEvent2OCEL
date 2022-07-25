package science.aist.fhirauditeventtoocel.renderer;

import lombok.AllArgsConstructor;
import org.hl7.fhir.r5.model.AuditEvent;
import org.hl7.fhir.r5.model.Reference;
import science.aist.fhirauditeventtoocel.AttributeTypeHelper;
import science.aist.gtf.transformation.renderer.TransformationRender;
import science.aist.ocel.model.ObjectFactory;
import science.aist.ocel.model.ObjectType;

import java.util.Collection;

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
        objectType.getStringOrDateOrInt().add(AttributeTypeHelper.string("id", currentElement.getReference()));
        objectType.getStringOrDateOrInt().add(AttributeTypeHelper.string("type", currentElement.getReferenceElement().getResourceType()));
        return objectType;
    }
}
