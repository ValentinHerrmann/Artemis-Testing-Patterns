package de.tum.cit.aet.levenshtein;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DynamicTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructuralLevenshtein {

    public enum DetailLevel {
        ONE_FOR_EVERYTHING,
        ONE_PER_CLASS,
        ONE_PER_MEMBER_CATEGORY,
        ONE_PER_MEMBER
    }

    public static void structuralTestTemplate(List<? extends Wrapper<?>> wrappers) {
        List<String> msg = new ArrayList<>();
        for(Wrapper<?> wrap : wrappers) {
            switch (wrap.getOverallExistence()) {
                case MISSING:
                case DEVIATES:
                    msg.add(wrap.toString());
                    break;
                default:
                    break;
            }
        }
        Assertions.assertThat(msg).withFailMessage(String.join("\n", msg)).isEmpty();
    }



    public static List<DynamicTest> structuralTestFactory(DetailLevel detailsLevel, ClassWrapper<?>... classWrappers) {
        List<DynamicTest> tests = new ArrayList<>();
        Map<String,List<Wrapper<?>>> wrappers = new HashMap<>();
        String key;
        switch (detailsLevel) {
            case ONE_FOR_EVERYTHING:
                key = "Structural[all]";
                wrappers.put(key,new ArrayList<>());
                for (ClassWrapper<?> classWrap : classWrappers) {
                    wrappers.get(key).add(classWrap);
                    wrappers.get(key).addAll(classWrap.getConstructorWrappers());
                    wrappers.get(key).addAll(classWrap.getAttributeWrappers());
                    wrappers.get(key).addAll(classWrap.getMethodsWrappers());
                }
                break;
            case ONE_PER_CLASS:
                for (ClassWrapper<?> classWrap : classWrappers) {
                    key = "Structural[" + classWrap.getExpectedName() + "]";
                    wrappers.put(key,new ArrayList<>());
                    wrappers.get(key).add(classWrap);
                    wrappers.get(key).addAll(classWrap.getConstructorWrappers());
                    wrappers.get(key).addAll(classWrap.getAttributeWrappers());
                    wrappers.get(key).addAll(classWrap.getMethodsWrappers());
                }
                break;
            case ONE_PER_MEMBER_CATEGORY:
                for (ClassWrapper<?> classWrap : classWrappers) {
                    key = "[" + classWrap.getExpectedName() + "]";
                    wrappers.put("Class"+key, List.of(classWrap));
                    wrappers.put("Constructors"+key, new ArrayList<>(classWrap.getConstructorWrappers()));
                    wrappers.put("Attributes"+key, new ArrayList<>(classWrap.getAttributeWrappers()));
                    wrappers.put("Methods"+key, new ArrayList<>(classWrap.getMethodsWrappers()));
                }
                break;
            case ONE_PER_MEMBER:
                throw new RuntimeException("Detail level not yet supported: " + detailsLevel);
                /*
                for (ClassWrapper<?> classWrap : classWrappers) {

                    key = "[" + classWrap.getExpectedName() + "]";
                    wrappers.put("Class"+key,List.of(classWrap));
                    for (Wrapper<?> constructor : classWrap.getConstructorWrappers()) {
                        key = String.format("Constructors[%s(%s)]", classWrap.getExpectedName(), constructor);
                        wrappers.put("Constructors"+key, List.of(constructor));
                    }
                    for (Wrapper<?> attribute : classWrap.getAttributeWrappers()) {
                        key = String.format("Attributes[%s.%s]", classWrap.getExpectedName(), attribute.getExpectedName());
                        wrappers.put(key, List.of(attribute));
                    }
                    for (Wrapper<?> method : classWrap.getMethodsWrappers()) {
                        key = String.format("Methods[%s.%s]", classWrap.getExpectedName(), method.getExpectedName());
                        wrappers.put(key,List.of(method));
                    }
                }
                break;
                */
            default:
                throw new IllegalArgumentException("Unknown DetailLevel: " + detailsLevel);
        }

        for(Map.Entry<String, List<Wrapper<?>>> x : wrappers.entrySet()) {
            tests.add(DynamicTest.dynamicTest(
                    String.format("struct%s", x.getKey()),
                    () -> StructuralLevenshtein.structuralTestTemplate(x.getValue())
            ));
        }
        return tests;
    }
}
