package io.github.valentinherrmann.levenshtein;

import io.github.valentinherrmann.test.TestManager;
//import io.github.valentinherrmann.wrappers.AbstrWrapper;
//import io.github.valentinherrmann.wrappers.ImplWrapper;
//import io.github.valentinherrmann.wrappers.InterfaceWrapper;
import io.github.valentinherrmann.wrappers.AbstrWrapper;
import io.github.valentinherrmann.wrappers.CarWrapper;
import io.github.valentinherrmann.wrappers.DrivableWrapper;
import de.tum.in.test.api.*;
import de.tum.in.test.api.jupiter.Hidden;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static io.github.valentinherrmann.test.TestSettings.TIMEOUT_SECONDS;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//@WhitelistClass({TestDriver.class, PreTest.class})
@BlacklistPackage("java.util.stream.*")
@WhitelistPackage("io.github.valentinherrmann.levenshtein.*")
@WhitelistPackage("io.github.valentinherrmann.*")
@WhitelistPackage("net.bytebuddy.**")
@AddTrustedPackage("net.bytebuddy.**")
@AddTrustedPackage("io.github.valentinherrmann.test.**")
@WhitelistClass(TestManager.class)
@WhitelistClass(ConstructorWrapper.class)
@WhitelistClass(MethodWrapper.class)
@WhitelistClass(AttributeWrapper.class)
@WhitelistClass(ClassWrapper.class)
@WhitelistClass(StructuralLevenshtein.class)
@WhitelistClass(AbstrWrapper.class)
@WhitelistClass(DrivableWrapper.class)
@WhitelistClass(CarWrapper.class)
@WhitelistPackage("io.github.valentinherrmann.wrappers.*")
@WhitelistPackage("io.github.valentinherrmann.test.*")
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

