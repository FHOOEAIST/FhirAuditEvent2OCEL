package science.aist.fhirauditeventtoocel;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import science.aist.ocel.model.*;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.stream.Stream;

/**
 * <p>Utility class which helps creating sub types of {@link AttributeType}</p>
 *
 * @author Andreas Pointner
 * @since 1.0
 */
@UtilityClass
public class AttributeTypeHelper {

    /**
     * Factory instance used to create the objects
     */
    private static final ObjectFactory FACTORY = new ObjectFactory();

    /**
     * Creates a {@link AttributeStringType} with the given key and value
     *
     * @param key   the key of the {@link AttributeStringType} {@link AttributeStringType#setKey(String)}
     * @param value the value of the {@link AttributeStringType} {@link AttributeStringType#setValue(String)}
     * @return the created {@link AttributeStringType}
     */
    public AttributeStringType string(String key, String value) {
        AttributeStringType attributeStringType = FACTORY.createAttributeStringType();
        attributeStringType.setKey(key);
        attributeStringType.setValue(value);
        return attributeStringType;
    }

    /**
     * Creates a {@link AttributeDateType} with the given key and date
     *
     * @param key  the key of the {@link AttributeDateType} {@link AttributeDateType#setKey(String)}
     * @param date the date of the {@link AttributeDateType} {@link AttributeDateType#setValue(XMLGregorianCalendar)}
     * @return the created {@link AttributeDateType}
     */
    public AttributeDateType date(String key, Date date) {
        AttributeDateType attributeDateType = FACTORY.createAttributeDateType();
        attributeDateType.setKey(key);
        attributeDateType.setValue(dateToGregorianCalendar(date));
        return attributeDateType;
    }

    /**
     * Creates a {@link AttributeListType} and adds all elements from the given {@link Stream} to the element
     *
     * @param key      the key of the {@link AttributeListType} {@link AttributeListType#setKey(String)}
     * @param elements the elements that are added {@link AttributeListType#getStringOrDateOrInt()}
     * @return the created {@link AttributeListType}
     */
    public AttributeListType list(String key, Stream<AttributeType> elements) {
        AttributeListType attributeListType = FACTORY.createAttributeListType();
        attributeListType.setKey(key);
        elements.forEach(attributeListType.getStringOrDateOrInt()::add);
        return attributeListType;
    }

    /**
     * Create a {@link XMLGregorianCalendar} element with the same timestamp as {@link Date}.
     *
     * @param date the data that should be converted
     * @return the resulting {@link XMLGregorianCalendar} element
     */
    @SneakyThrows
    public XMLGregorianCalendar dateToGregorianCalendar(Date date) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    }
}
