package de.tum.cit.aet;

import java.io.FileWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Constants {

    public enum Variant {
        DEFAULT
    }

    public static String abstractClass(){
        return switch (TestSettings.variant) {
            case DEFAULT -> "AbstractVehicle";
        };
    }

    public static String concreteClass(){
        return switch (TestSettings.variant) {
            case DEFAULT -> "Car";
        };
    }

    public static String concreteClassAttribute() {
        return switch (TestSettings.variant) {
            case DEFAULT -> "numberOfDoors";
        };
    }

    public static String interfaceName(){
        return switch (TestSettings.variant) {
            case DEFAULT -> "Driveable";
        };
    }

    public static String overwrittenMethod() {
        return switch (TestSettings.variant) {
            case DEFAULT -> "calculateFuelConsumption";
        };
    }

    public static String abstractClassAttribute() {
        return switch (TestSettings.variant) {
            case DEFAULT -> "manufacturer";
        };
    }

    public static String interfaceMethod() {
        return switch (TestSettings.variant) {
            case DEFAULT -> "startEngine";
        };
    }

    public static Class<?> yearType() {
        return switch (TestSettings.variant) {
            case DEFAULT -> int.class;
        };
    }

    public static Class<?> speedType() {
        return switch (TestSettings.variant) {
            case DEFAULT -> double.class;
        };
    }

    public static Class<?> doorType() {
        return switch (TestSettings.variant) {
            case DEFAULT -> int.class;
        };
    }

    public static Class<?> engineCapacityType() {
        return switch (TestSettings.variant) {
            case DEFAULT -> double.class;
        };
    }

    public static Class<?> calcReturnType() {
        return switch (TestSettings.variant) {
            case DEFAULT -> double.class;
        };
    }



    public static void main(String[] args)  {
        try {
            String filename = "VariantOverview.csv";

            List<String> methodNames = new ArrayList<>();
            List<List<String>> rows = new ArrayList<>();

            // Sammle alle statischen Methoden, die einen String oder Class<?> zurückgeben
            for (Method method : Constants.class.getDeclaredMethods()) {
                if (Modifier.isStatic(method.getModifiers())
                        && method.getParameterCount() == 0
                        && !method.getName().equals("exportVariantsToCSV")
                        && (method.getReturnType() == String.class || method.getReturnType() == Class.class)) {
                    methodNames.add(method.getName());
                }
            }


            // Sammle Werte für jede Variante
            for (Variant v : Variant.values()) {
                List<String> row = new ArrayList<>();
                row.add(v.name());

                for (String methodName : methodNames) {
                    try {
                        Method method = Constants.class.getDeclaredMethod(methodName);
                        Object result = method.invoke(null);
                        row.add(result.toString());
                    } catch (NoSuchMethodException e) {
                        row.add("N/A");
                    }
                }
                rows.add(row);
            }

            // Schreibe CSV
            try (FileWriter writer = new FileWriter(filename)) {
                writer.write("Variant;" + String.join(";", methodNames) + "\n");
                for (List<String> row : rows) {
                    writer.write(String.join(";", row) + "\n");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
