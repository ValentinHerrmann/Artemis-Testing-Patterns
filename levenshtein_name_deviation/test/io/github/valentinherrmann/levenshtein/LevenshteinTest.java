package io.github.valentinherrmann.levenshtein;

import de.tum.cit.aet.test.TestManager;
//import de.tum.cit.aet.wrappers.AbstrWrapper;
//import de.tum.cit.aet.wrappers.ImplWrapper;
//import de.tum.cit.aet.wrappers.InterfaceWrapper;
import de.tum.cit.aet.wrappers.AbstrWrapper;
import de.tum.cit.aet.wrappers.CarWrapper;
import de.tum.cit.aet.wrappers.DrivableWrapper;
import de.tum.in.test.api.*;
import de.tum.in.test.api.jupiter.Hidden;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static de.tum.cit.aet.test.TestSettings.TIMEOUT_SECONDS;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//@WhitelistClass({TestDriver.class, PreTest.class})
@BlacklistPackage("java.util.stream.*")
@WhitelistPackage("de.tum.cit.aet.levenshtein.*")
@WhitelistPackage("de.tum.cit.aet.*")
@WhitelistPackage("net.bytebuddy.**")
@AddTrustedPackage("net.bytebuddy.**")
@AddTrustedPackage("de.tum.cit.aet.test.**")
@WhitelistClass(TestManager.class)
@WhitelistClass(ConstructorWrapper.class)
@WhitelistClass(MethodWrapper.class)
@WhitelistClass(AttributeWrapper.class)
@WhitelistClass(ClassWrapper.class)
@WhitelistClass(StructuralLevenshtein.class)
@WhitelistClass(AbstrWrapper.class)
@WhitelistClass(DrivableWrapper.class)
@WhitelistClass(CarWrapper.class)
@WhitelistPackage("de.tum.cit.aet.wrappers.*")
@WhitelistPackage("de.tum.cit.aet.test.*")
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath("target/test-classes") // prevent access to test-related classes and resources
@BlacklistPackage("org.pdfsam.*")
@BlacklistPackage("io.reactivex.*")
@MirrorOutput
@StrictTimeout(TIMEOUT_SECONDS)
@Retention(RUNTIME)
@Target({TYPE, ANNOTATION_TYPE})
public @interface LevenshteinTest
{
}

