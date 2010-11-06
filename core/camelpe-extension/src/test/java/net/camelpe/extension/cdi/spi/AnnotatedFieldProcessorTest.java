/**
 * 
 */
package net.camelpe.extension.cdi.spi;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Set;

import javax.enterprise.inject.ResolutionException;

import net.camelpe.extension.cdi.spi.bean_samples.BeanHavingEndpointInjectAndInjectAnnotatedField;
import net.camelpe.extension.cdi.spi.bean_samples.BeanHavingEndpointInjectAnnotatedField;
import net.camelpe.extension.cdi.spi.bean_samples.BeanHavingNoEndpointInjectAnnotatedField;

import org.junit.Test;

/**
 * <p>
 * Test {@link AnnotatedFieldProcessor}.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class AnnotatedFieldProcessorTest {

    /**
     * Test method for
     * {@link net.camelpe.extension.cdi.spi.AnnotatedFieldProcessor#ensureNoConflictingAnnotationsPresentOn(java.lang.Class)}
     * .
     */
    @Test
    public final void assertThatEnsureNoConflictingAnnotationsPresentOnCorrectlyRecognizesNoConflict() {
        AnnotatedFieldProcessor
                .ensureNoConflictingAnnotationsPresentOn(BeanHavingEndpointInjectAnnotatedField.class);
    }

    /**
     * Test method for
     * {@link net.camelpe.extension.cdi.spi.AnnotatedFieldProcessor#ensureNoConflictingAnnotationsPresentOn(java.lang.Class)}
     * .
     */
    @Test(expected = ResolutionException.class)
    public final void assertThatEnsureNoConflictingAnnotationsPresentOnCorrectlyRecognizesAConflict() {
        AnnotatedFieldProcessor
                .ensureNoConflictingAnnotationsPresentOn(BeanHavingEndpointInjectAndInjectAnnotatedField.class);
    }

    /**
     * Test method for
     * {@link net.camelpe.extension.cdi.spi.AnnotatedFieldProcessor#hasCamelInjectAnnotatedFields(java.lang.Class)}
     * .
     */
    @Test
    public final void assertThatHasCamelInjectAnnotatedFieldsRecognizesThatNoCamelInjectAnnotationIsPresentOnAnyField() {
        final boolean answer = AnnotatedFieldProcessor
                .hasCamelInjectAnnotatedFields(BeanHavingNoEndpointInjectAnnotatedField.class);

        assertFalse("AnnotatedFieldProcessor.hasCamelInjectAnnotatedFields("
                + BeanHavingNoEndpointInjectAnnotatedField.class.getName()
                + ") should have recognized that no field is annotated with "
                + "@EndpointInject on the supplied class, yet it didn't",
                answer);
    }

    /**
     * Test method for
     * {@link net.camelpe.extension.cdi.spi.AnnotatedFieldProcessor#hasCamelInjectAnnotatedFields(java.lang.Class)}
     * .
     */
    @Test
    public final void assertThatHasCamelInjectAnnotatedFieldsRecognizesEndpointInjectAnnotationOnField() {
        final boolean answer = AnnotatedFieldProcessor
                .hasCamelInjectAnnotatedFields(BeanHavingEndpointInjectAnnotatedField.class);

        assertTrue("AnnotatedFieldProcessor.hasCamelInjectAnnotatedFields("
                + BeanHavingEndpointInjectAnnotatedField.class.getName()
                + ") should have recognized a field annotated with "
                + "@EndpointInject on the supplied class, yet it didn't",
                answer);
    }

    /**
     * Test method for
     * {@link net.camelpe.extension.cdi.spi.AnnotatedFieldProcessor#camelInjectAnnotatedFieldsIn(java.lang.Class)}
     * .
     */
    @Test
    public final void assertThatCamelInjectAnnotatedFieldsInReturnsEnpointInjectAnnotatedField() {
        final Set<Field> camelInjectAnnotatedFields = AnnotatedFieldProcessor
                .camelInjectAnnotatedFieldsIn(BeanHavingEndpointInjectAnnotatedField.class);

        assertEquals("AnnotatedFieldProcessor.hasCamelInjectAnnotatedFields("
                + BeanHavingEndpointInjectAnnotatedField.class.getName()
                + ") should have returned exactly one field annotated with "
                + "@EndpointInject on the supplied class, yet it didn't", 1,
                camelInjectAnnotatedFields.size());
    }

}
