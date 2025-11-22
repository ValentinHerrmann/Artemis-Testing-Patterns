# Setup Instructions

## Project Structure

```
code_example_01/
├── src/
│   └── de/tum/cit/aet/
│       ├── Driveable.java           (Interface)
│       ├── AbstractVehicle.java     (Abstract Class)
│       └── Car.java                 (Implementation)
├── test/
│   └── de/tum/cit/aet/
│       └── VehicleStructuralTest.java
└── README.md
```

## Dependencies Required

To run the structural tests, you need:

1. **JUnit 5** (Jupiter API)
   - `org.junit.jupiter:junit-jupiter-api`
   - `org.junit.jupiter:junit-jupiter-engine`

2. **Levenshtein Wrapper Library**
   - The wrapper classes must be available:
     - `ClassWrapper`
     - `InterfaceWrapper`
     - `ConcreteClassWrapper`
     - `AttributeWrapper`
     - `MethodWrapper`
     - `ConstructorWrapper`
     - `StructuralLevenshtein`

## Build Configuration

### Gradle (build.gradle)

```gradle
dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.9.0'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.9.0'
    
    // Add the levenshtein wrapper library
    implementation project(':levenshtein-wrappers')
    // OR
    implementation files('libs/levenshtein-wrappers.jar')
}

sourceSets {
    main {
        java {
            srcDirs = ['src']
        }
    }
    test {
        java {
            srcDirs = ['test']
        }
    }
}
```

### Maven (pom.xml)

```xml
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.9.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.9.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>

<build>
    <sourceDirectory>src</sourceDirectory>
    <testSourceDirectory>test</testSourceDirectory>
</build>
```

## Running the Tests

```bash
# With Gradle
./gradlew test

# With Maven
mvn test

# Direct Java
javac -cp junit-jupiter-api.jar:levenshtein-wrappers.jar src/de/tum/cit/aet/*.java
javac -cp junit-jupiter-api.jar:levenshtein-wrappers.jar:src test/de/tum/cit/aet/*.java
java -cp junit-platform-console-standalone.jar:levenshtein-wrappers.jar:src:test \
     org.junit.platform.console.ConsoleLauncher \
     --select-class de.tum.cit.aet.VehicleStructuralTest
```

## IDE Setup

### IntelliJ IDEA

1. Mark `src` directory as **Sources Root**
2. Mark `test` directory as **Test Sources Root**
3. Add JUnit 5 library to project
4. Add levenshtein-wrappers library to classpath

### Eclipse

1. Configure source folders:
   - Add `src` as source folder
   - Add `test` as test source folder
2. Add JUnit 5 to build path
3. Add levenshtein-wrappers JAR to build path

## Notes

- The package path errors shown by the IDE are cosmetic - they appear because the IDE expects a specific directory structure
- The code will compile and run correctly once dependencies are properly configured
- The Levenshtein wrapper classes need to be built separately (from the main levenshtein_name_deviation project)

