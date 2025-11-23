package de.tum.cit.aet.test;

import de.tum.cit.aet.wrappers.*;
import static de.tum.cit.aet.TestManager.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Tests for the Driveable interface.
 * Simplified to match the new example structure.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestInterface {

    /**
     * Test that the interface exists and is properly defined.
     */
    public static void testInterfaceExists() {
        driveableInterface().verifyExistence(true);
    }

    /**
     * Test MAX_SPEED constant.
     */
    public static void testMaxSpeedConstant() {
        driveableInterface().maxSpeed().verifyExistence(true);

        Object maxSpeed = driveableInterface().maxSpeed().getValue(null);
        Assertions.assertThat((double)maxSpeed)
            .withFailMessage("Interface constant %s should be 200.0",
                           driveableInterface().maxSpeed().getExpectedName())
            .isEqualTo(200.0, Assertions.within(0.001));
    }

    /**
     * Test start method signature.
     */
    public static void testStartMethodExists() {
        driveableInterface().startMethod().verifyExistence(true);
    }

    /**
     * Test getSpeed method signature.
     */
    public static void testGetSpeedMethodExists() {
        driveableInterface().getSpeedMethod().verifyExistence(true);
    }

    /**
     * Test that Car implements Driveable interface.
     */
    public static void testCarImplementsInterface() {
        carImpl().verifyInterfaces();
        Assertions.assertThat(carImpl().getClazz().getInterfaces())
            .withFailMessage("Class %s must implement interface %s.",
                           carImpl().getExpectedName(),
                           driveableInterface().getExpectedName())
            .anySatisfy(iface ->
                Assertions.assertThat(iface.getSimpleName())
                    .isEqualTo(driveableInterface().getExpectedName())
            );
    }
}

