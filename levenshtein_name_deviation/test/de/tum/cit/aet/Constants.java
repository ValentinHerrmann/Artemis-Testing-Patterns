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
        return "AbstractVehicle";
    }

    public static String concreteClass(){
        return "Car";
    }

    public static String concreteClassAttribute() {
        return "numberOfDoors";
    }

    public static String interfaceName(){
        return "Driveable";
    }

    public static String overwrittenMethod() {
        return "calculateFuelConsumption";
    }

    public static String abstractClassAttribute() {
        return "manufacturer";
    }

    public static String interfaceMethod() {
        return "startEngine";
    }

    public static Class<?> yearType() {
        return int.class;
    }

    public static Class<?> speedType() {
        return double.class;
    }

    public static Class<?> doorType() {
        return int.class;
    }

    public static Class<?> engineCapacityType() {
        return double.class;
    }

    public static Class<?> calcReturnType() {
        return double.class;
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
