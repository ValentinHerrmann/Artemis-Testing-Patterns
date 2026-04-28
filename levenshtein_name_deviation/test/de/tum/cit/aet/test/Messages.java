package test;

/**
 * Central message catalog for all assertion and feedback messages shown to students.
 *
 * <h2>Language Switching</h2>
 * <p>All messages are available in German and English. The active language is controlled
 * by {@link TestSettings#LANGUAGE}:</p>
 * <pre>{@code
 * // In TestSettings.java:
 * public static final Language LANGUAGE = Language.DEUTSCH;   // German (default)
 * public static final Language LANGUAGE = Language.ENGLISH;   // English
 * }</pre>
 *
 * <h2>Usage</h2>
 * <p>All fields are format strings. Pass them to {@code String.format()} or AssertJ's
 * {@code withFailMessage()} where %s / %d placeholders are needed:</p>
 * <pre>{@code
 * fail(String.format(Messages.CLASS_NOT_IMPLEMENTED, name, pkg));
 * assertThat(x).withFailMessage(Messages.GETTER_WRONG_VALUE, getter, expected, actual).isEqualTo(actual);
 * }</pre>
 *
 * @see TestSettings#LANGUAGE
 * @see Constants
 */
public class Messages {

    private static final boolean GERMAN = TestSettings.LANGUAGE == TestSettings.Language.DEUTSCH;

    // -------------------------------------------------------------------------
    // Wrapper – general existence states (used in Wrapper.toString())
    // -------------------------------------------------------------------------

    /**
     * Shown when a structural element deviates from the expected signature.
     * Placeholders: (1) parent class name, (2) expected toString, (3) actual toString.
     */
    public static final String WRAPPER_DEVIATION = GERMAN
            ? """
              ⚠️ ABWEICHUNG in %s ⚠️
              Falls möglich wird der tatsächliche Wert für weitere Tests verwendet.
              Erwartet:\t%s
              Tatsächlich:\t%s
              """
            : """
              ⚠️ DEVIATION in %s ⚠️
              If possible actual will be used for further testing.
              Expect:\t%s
              Actual:\t%s
              """;

    /**
     * Shown when a structural element is completely missing.
     * Placeholders: (1) parent class name, (2) expected toString.
     */
    public static final String WRAPPER_MISSING = GERMAN
            ? """
              ❌ FEHLT in %s ❌
              Erwartet:\t%s
              """
            : """
              ❌ MISSING in %s ❌
              Expect:\t%s
              """;

    /**
     * Shown when a structural element has not been checked yet (debug / internal state).
     * Placeholders: (1) parent class name, (2) expected toString.
     */
    public static final String WRAPPER_UNCHECKED = GERMAN
            ? """
              ? NICHT ÜBERPRÜFT in %s ?
              Erwartet:\t%s
              """
            : """
              ? UNCHECKED in %s ?
              Expect:\t%s
              """;

    // -------------------------------------------------------------------------
    // ClassWrapper – toString() intro lines
    // -------------------------------------------------------------------------

    /** Intro line for a deviating class in ClassWrapper.toString(). */
    public static final String CLASS_DEVIATION = GERMAN
            ? "⚠️ ABWEICHUNG ⚠️\nFalls möglich wird der tatsächliche Wert für weitere Tests verwendet"
            : "⚠️ DEVIATION ⚠️\nIf possible actual will be used for further testing";

    /** Intro line for a missing class in ClassWrapper.toString(). */
    public static final String CLASS_MISSING = GERMAN
            ? "❌ FEHLT ❌"
            : "❌ MISSING ❌";

    /** Intro line for an unchecked class in ClassWrapper.toString(). */
    public static final String CLASS_UNCHECKED = GERMAN
            ? "Existenz nicht überprüft"
            : "Existence unchecked";

    // -------------------------------------------------------------------------
    // ClassWrapper – verifyExistence
    // -------------------------------------------------------------------------

    /**
     * Shown when the expected class is missing or deviates.
     * Placeholders: (1) class name, (2) package name.
     */
    public static final String CLASS_NOT_IMPLEMENTED = GERMAN
            ? """
              Klasse %s im Paket %s ist nicht wie erwartet implementiert.
              --> Weitere Details in den Struktur-Tests.
              --> Dies kann dazu führen, dass nachfolgende Tests fehlschlagen.
              """
            : """
              Class %s in package %s is not implemented as expected.
              --> See structural Tests for details about this.
              --> This may lead subsequent tests to fail.
              """;

    // -------------------------------------------------------------------------
    // ClassWrapper – getObj (instantiation failures)
    // -------------------------------------------------------------------------

    /**
     * Shown when trying to directly instantiate an abstract class.
     * Placeholder: (1) class name.
     */
    public static final String CLASS_CANNOT_INSTANTIATE_ABSTRACT = GERMAN
            ? "Abstrakte Klasse %s kann nicht direkt instanziiert werden.\nDies kann dazu führen, dass nachfolgende Tests fehlschlagen."
            : "Cannot instantiate abstract class %s directly.\nThis may lead subsequent tests to fail.";

    /**
     * Shown when trying to directly instantiate an interface.
     * Placeholder: (1) interface name.
     */
    public static final String CLASS_CANNOT_INSTANTIATE_INTERFACE = GERMAN
            ? "Interface %s kann nicht direkt instanziiert werden.\nDies kann dazu führen, dass nachfolgende Tests fehlschlagen."
            : "Cannot instantiate interface %s directly.\nThis may lead subsequent tests to fail.";

    /**
     * Shown when instantiating a class via its constructor fails.
     * Placeholder: (1) class name.
     */
    public static final String CLASS_INSTANTIATION_FAILED = GERMAN
            ? "Das Erstellen von Instanzen der Klasse %s ist fehlgeschlagen. Der Konstruktor ist möglicherweise nicht korrekt implementiert.\nDies kann dazu führen, dass nachfolgende Tests fehlschlagen."
            : "Creating instances of class %s failed. Constructor may not be implemented correctly.\nThis may lead subsequent tests to fail.";

    /**
     * Shown when creating a ByteBuddy dynamic subclass instance fails.
     * Placeholders: (1) class name, (2) error message.
     */
    public static final String CLASS_SUBCLASS_INSTANTIATION_FAILED = GERMAN
            ? "Das Erstellen von Instanzen einer Unterklasse von %s ist fehlgeschlagen. Der Konstruktor ist möglicherweise nicht korrekt implementiert. Dies kann dazu führen, dass nachfolgende Tests fehlschlagen. Fehler: %s"
            : "Creating instances of a subclass of %s failed. Constructor may not be implemented correctly. This might also cause subsequent tests to fail. Error: %s";

    // -------------------------------------------------------------------------
    // ClassWrapper – testGetter
    // -------------------------------------------------------------------------

    /**
     * Shown when a getter method does not return the expected attribute value.
     * Placeholders: (1) getter signature, (2) expected value, (3) actual value.
     */
    public static final String GETTER_WRONG_VALUE = GERMAN
            ? """
              Getter '%s' gibt nicht den Attributwert zurück.
              Erwartet: %s
              Tatsächlich: %s"""
            : """
              Getter '%s' does not return the attribute's value.
              Expected: %s
              Actual: %s""";

    // -------------------------------------------------------------------------
    // MethodWrapper
    // -------------------------------------------------------------------------

    /**
     * Shown when the expected method is missing or deviates.
     * Placeholders: (1) method signature, (2) class name.
     */
    public static final String METHOD_NOT_IMPLEMENTED = GERMAN
            ? """
              Methode %s in Klasse %s ist nicht wie erwartet implementiert.
              --> Weitere Details in den Struktur-Tests.
              --> Dies kann dazu führen, dass nachfolgende Tests fehlschlagen."""
            : """
              Method %s in class %s is not implemented as expected.
              --> See structural Tests for details about this.
              --> This may lead subsequent tests to fail.""";

    /**
     * Shown when invoking a method throws an unexpected exception.
     * Placeholders: (1) method signature, (2) class name.
     */
    public static final String METHOD_INVOCATION_EXCEPTION = GERMAN
            ? "Der Aufruf der Methode %s auf Klasse %s hat eine Ausnahme geworfen."
            : "Calling method %s on class %s threw an exception.";

    // -------------------------------------------------------------------------
    // AttributeWrapper
    // -------------------------------------------------------------------------

    /**
     * Shown when the expected attribute is missing or deviates.
     * Placeholders: (1) attribute name, (2) class name.
     */
    public static final String ATTRIBUTE_NOT_IMPLEMENTED = GERMAN
            ? """
              Attribut %s in Klasse %s ist nicht wie erwartet implementiert.
              --> Weitere Details in den Struktur-Tests.
              --> Dies kann dazu führen, dass nachfolgende Tests fehlschlagen."""
            : """
              Attribute %s in class %s is not implemented as expected.
              --> See structural Tests for details about this.
              --> This may lead subsequent tests to fail.""";

    /**
     * Shown when reading the value of an attribute fails.
     * Placeholders: (1) attribute name, (2) class name, (3) cause / exception message.
     */
    public static final String ATTRIBUTE_ACCESS_FAILED = GERMAN
            ? "Zugriff auf den Wert des Attributs '%s' in %s nicht möglich. Ursache: %s"
            : "Could not access value of attribute '%s' in %s. Cause: %s";

    /**
     * Shown when trying to set the value of a final attribute.
     * Placeholders: (1) attribute name, (2) class name.
     */
    public static final String ATTRIBUTE_FINAL_CANNOT_SET = GERMAN
            ? "Der Wert des Attributs %s in %s kann nicht gesetzt werden, da es als final deklariert ist (sollte es aber nicht sein)."
            : "Cannot set value of attribute %s in %s because it is declared final (but should not be).";

    /**
     * Shown when setting the value of an attribute fails for an unexpected reason.
     * Placeholders: (1) attribute name, (2) class name.
     */
    public static final String ATTRIBUTE_SET_FAILED = GERMAN
            ? "Wert des Attributs '%s' in %s konnte nicht gesetzt werden."
            : "Could not set value of attribute '%s' in %s.";

    // -------------------------------------------------------------------------
    // ConstructorWrapper
    // -------------------------------------------------------------------------

    /**
     * Shown when the expected constructor is missing or deviates.
     * Placeholders: (1) constructor signature, (2) class name.
     */
    public static final String CONSTRUCTOR_NOT_IMPLEMENTED = GERMAN
            ? """
              Konstruktor %s in Klasse %s ist nicht wie erwartet implementiert.
              --> Weitere Details in den Struktur-Tests.
              --> Dies kann dazu führen, dass nachfolgende Tests fehlschlagen.
              """
            : """
              Constructor %s in class %s is not implemented as expected.
              --> See structural Tests for details about this.
              --> This may lead subsequent tests to fail.
              """;

    /**
     * Shown when invoking a constructor throws an unexpected exception.
     * Placeholders: (1) constructor signature, (2) class name, (3) exception message.
     */
    public static final String CONSTRUCTOR_INVOCATION_FAILED = GERMAN
            ? "Konstruktor '%s' in Klasse %s konnte nicht aufgerufen werden: %s"
            : "Failed to invoke constructor '%s' in class %s: %s";

    // -------------------------------------------------------------------------
    // Utils
    // -------------------------------------------------------------------------

    /** Thrown internally when a value is null where null is not permitted. */
    public static final String NULL_NOT_ALLOWED = GERMAN
            ? "Ein Wert war null, der nicht null sein darf"
            : "A value was null that is not allowed to be null";
}
