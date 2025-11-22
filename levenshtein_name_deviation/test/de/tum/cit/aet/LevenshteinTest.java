package de.tum.cit.aet;

import de.tum.cit.aet.levenshtein.*;
//import de.tum.cit.aet.wrappers.AbstrWrapper;
//import de.tum.cit.aet.wrappers.ImplWrapper;
//import de.tum.cit.aet.wrappers.InterfaceWrapper;
import de.tum.in.test.api.*;
import de.tum.in.test.api.jupiter.Hidden;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//@WhitelistClass({TestDriver.class, PreTest.class})
@BlacklistPackage("java.util.stream.*")
@WhitelistPackage("de.tum.cit.aet.levenshtein.*")
@WhitelistPackage("de.tum.cit.aet.*")
//@WhitelistClass(ImplWrapper.class)
//@WhitelistClass(AbstrWrapper.class)
//@WhitelistClass(InterfaceWrapper.class)
@WhitelistClass(TestManager.class)
@WhitelistClass(ConstructorWrapper.class)
@WhitelistClass(MethodWrapper.class)
@WhitelistClass(AttributeWrapper.class)
@WhitelistClass(ClassWrapper.class)
//@WhitelistClass(TestImpl.class)
//@WhitelistClass(TestAbstr.class)
//@WhitelistClass(TestInterface.class)
@WhitelistClass(StructuralLevenshtein.class)
@WhitelistPackage("de.tum.cit.aet.wrappers.*")
@WhitelistPath("target") // mainly for Artemis
@BlacklistPath("target/test-classes") // prevent access to test-related classes and resources
@BlacklistPackage("org.pdfsam.*")
@BlacklistPackage("io.reactivex.*")
@Hidden
@Deadline(TestSettings.SHOW_HIDDEN_AFTER)
@ActivateHiddenBefore(TestSettings.SHOW_HIDDEN_BEFORE)
@MirrorOutput
//@StrictTimeout(3)
@Retention(RUNTIME)
@Target({TYPE, ANNOTATION_TYPE})
public @interface LevenshteinTest
{
}

