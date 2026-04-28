package levenshtein;
import wrappers.*;
import de.tum.in.test.api.*;
import de.tum.in.test.api.jupiter.Hidden;
import test.TestSettings;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//@WhitelistClass({TestDriver.class, PreTest.class})
@BlacklistPackage("java.util.stream.*")
@WhitelistPackage(".levenshtein.*")
@WhitelistPackage(".*")
@WhitelistPackage("net.bytebuddy.**")
@AddTrustedPackage("net.bytebuddy.**")
@AddTrustedPackage(".test.**")
@WhitelistPackage(".wrappers.*")
@WhitelistPackage(".test.*")
/*
@WhitelistClass(TestManager.class)
@WhitelistClass(ConstructorWrapper.class)
@WhitelistClass(MethodWrapper.class)
@WhitelistClass(AttributeWrapper.class)
@WhitelistClass(ClassWrapper.class)
@WhitelistClass(StructuralLevenshtein.class)
@WhitelistClass(MainWrapper.class)
*/
@WhitelistPackage(".wrappers.*")
@WhitelistPackage(".test.*")
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath("target/test-classes") // prevent access to test-related classes and resources
@BlacklistPackage("org.pdfsam.*")
@BlacklistPackage("io.reactivex.*")
@MirrorOutput
@StrictTimeout(TestSettings.TIMEOUT_SECONDS)
@Retention(RUNTIME)
@Target({TYPE, ANNOTATION_TYPE})
public @interface LevenshteinTest
{
}

