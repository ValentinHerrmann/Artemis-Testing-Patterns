package de.tum.cit.aet.test;

import de.tum.cit.aet.wrappers.*;
import static de.tum.cit.aet.TestManager.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Tests for the Driveable interface.
 */
public class TestInterface {

    private static AbstrWrapper<?> abstrWrap;
    private static CarWrapper<?> carWrap;
    private static DrivableWrapper<?> interfaceWrap;

    public static void setAbstrWrap(AbstrWrapper<?> abstrWrap) {
        TestInterface.abstrWrap = abstrWrap;
    }

    public static void setCarWrap(CarWrapper<?> carWrap) {
        TestInterface.carWrap = carWrap;
    }

    public static void setInterfaceWrap(DrivableWrapper<?> interfaceWrap) {
        TestInterface.interfaceWrap = interfaceWrap;
    }

    /**
     * Test that Car implements Driveable interface.
     */
    public static void testCarImplementsInterface() {
        carWrap.verifyInterfaces();
        Assertions.assertThat(carWrap.getClazz().getInterfaces())
            .withFailMessage("Class %s must implement interface %s.",
                           carWrap.getExpectedName(),
                           interfaceWrap.getExpectedName())
            .anySatisfy(iface ->
                Assertions.assertThat(iface.getSimpleName())
                    .isEqualTo(interfaceWrap.getExpectedName())
            );
    }
}

