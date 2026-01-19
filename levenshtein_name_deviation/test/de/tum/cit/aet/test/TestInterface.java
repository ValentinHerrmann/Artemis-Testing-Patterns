package de.tum.cit.aet.test;

import static de.tum.cit.aet.TestManager.*;
import static de.tum.cit.aet.levenshtein.Utils.saveCast;

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
     * Test MAX_SPEED constant.
     */
    public static void testMaxSpeedConstant() {
        driveableInterface().maxSpeed().verifyExistence(true);

        Object maxSpeed = driveableInterface().maxSpeed().getValue(null);
        double dMaxSpeed = (double)saveCast(maxSpeed, double.class);
        Assertions.assertThat(dMaxSpeed)
            .withFailMessage("Interface constant %s should be 200.0",
                           driveableInterface().maxSpeed().getExpectedName())
            .isEqualTo(200.0, Assertions.within(0.001));
    }
}

