# Quick Reference - Levenshtein Pattern Example

## File Overview

| File | Purpose | Key Features |
|------|---------|--------------|
| **Constants.java** | Centralized test configuration | Single DEFAULT variant, all class/method names |
| **DriveableInterfaceWrapper.java** | Interface wrapper | Defines 4 expected interface methods |
| **AbstractVehicleWrapper.java** | Abstract class wrapper | 5 attributes, 7 methods (2 abstract) |
| **CarWrapper.java** | Concrete class wrapper | Full inheritance + interface implementation |
| **VehicleTest.java** | Test suite | Structural factory + 12 behavioral tests |

## Constants Reference

```java
// Class names
Constants.abstractClass()           // "AbstractVehicle"
Constants.concreteClass()           // "Car"
Constants.interfaceName()           // "Driveable"

// Member names
Constants.abstractClassAttribute()  // "manufacturer"
Constants.concreteClassAttribute()  // "numberOfDoors"
Constants.interfaceMethod()         // "startEngine"
Constants.overwrittenMethod()       // "calculateFuelConsumption"

// Types
Constants.yearType()                // int.class
Constants.speedType()               // double.class
Constants.doorType()                // int.class
Constants.engineCapacityType()      // double.class
Constants.calcReturnType()          // double.class
```

## Test Execution Flow

```
1. @BeforeAll: Initialize wrappers
   ├── DriveableInterfaceWrapper()
   ├── AbstractVehicleWrapper()
   └── CarWrapper(abstractVehicle, driveable)

2. @TestFactory: Structural tests (Order 1)
   └── StructuralLevenshtein.structuralTestFactory(...)
       ├── Tests class existence
       ├── Tests modifiers
       ├── Tests inheritance
       ├── Tests methods
       └── Tests attributes

3. @Test: Behavioral tests (Order 2-12)
   ├── testCarCreation()
   ├── testAbstractVehicleAttributes()
   ├── testCarSpecificAttributes()
   ├── testStartEngine()
   ├── testAcceleration()
   ├── testBraking()
   ├── testCalculateFuelConsumption()
   ├── testGetMaxSpeed()
   ├── testIsElectric()
   ├── testGetVehicleInfo()
   └── testCompleteDrivingScenario()
```

## Wrapper Usage Pattern

### Creating a Wrapper
```java
public class MyClassWrapper extends GenericClassWrapper<Object> {
    public MyClassWrapper() {
        super(
            Constants.concreteClass(),      // Expected name
            "de.tum.cit.aet",              // Package
            superClassWrapper,              // Parent class wrapper
            new ClassWrapper<?>[]{...},     // Interface wrappers
            "public"                        // Modifiers
        );
        
        // Add attributes
        addAttribute(new AttributeWrapper<>(this, "name", String.class, "private"));
        
        // Add constructor
        addConstructor(new ConstructorWrapper<>(this, new Class<?>[]{String.class}, "public"));
        
        // Add methods
        addMethod(new MethodWrapper<>(this, "getName", String.class, new Class<?>[]{}, "public"));
    }
}
```

### Using Wrapper in Tests
```java
// Create instance
Object obj = wrapper.getConstructorWrappers().get(0).invoke("arg1", "arg2");

// Access attribute
String value = wrapper.getAttributeByName("attributeName").getValue(obj);

// Invoke method
ReturnType result = wrapper.getMethodByName("methodName").invoke(obj, args...);
```

## Key Benefits

| Feature | Benefit |
|---------|---------|
| **Constants-driven** | Easy to adapt for different exercises |
| **Levenshtein tolerance** | Handles minor naming deviations automatically |
| **Structural factory** | Generates comprehensive tests automatically |
| **Wrapper invocation** | Type-safe reflection with clear errors |
| **Behavioral tests** | Validates actual functionality, not just structure |
| **Ordered execution** | Structural tests run first, then behavioral |

## Common Test Patterns

### Pattern 1: Attribute Test
```java
@Test
void testAttribute() {
    Object obj = wrapper.getConstructorWrappers().get(0).invoke(...);
    Type value = wrapper.getAttributeByName(Constants.attributeName()).getValue(obj);
    assertEquals(expected, value);
}
```

### Pattern 2: Method Test
```java
@Test
void testMethod() {
    Object obj = wrapper.getConstructorWrappers().get(0).invoke(...);
    ReturnType result = wrapper.getMethodByName(Constants.methodName()).invoke(obj, args);
    assertNotNull(result);
}
```

### Pattern 3: State Change Test
```java
@Test
void testStateChange() {
    Object obj = wrapper.getConstructorWrappers().get(0).invoke(...);
    
    // Initial state
    Type before = wrapper.getMethodByName("getter").invoke(obj);
    
    // Trigger change
    wrapper.getMethodByName("setter").invoke(obj, newValue);
    
    // Verify change
    Type after = wrapper.getMethodByName("getter").invoke(obj);
    assertNotEquals(before, after);
}
```

## Extending the Example

### Add New Wrapper Class
1. Create wrapper class extending `GenericClassWrapper`
2. Define expected structure in constructor
3. Use `Constants` for all names and types
4. Add to structural test factory list

### Add New Test
1. Use `@Test` annotation
2. Set appropriate `@Order`
3. Use wrapper methods for invocation
4. Use Constants for all expected values
5. Add descriptive `@DisplayName`

### Adapt for Different Exercise
1. Update source classes in `src/`
2. Modify `Constants.java` values
3. Adjust wrapper class definitions
4. Update/add behavioral tests as needed
5. Run tests to validate

## Troubleshooting

| Issue | Solution |
|-------|----------|
| "Class not found" | Check package name and class name in Constants |
| "Method not found" | Verify method name spelling and parameter types |
| "Attribute not found" | Check attribute name and ensure it's not private (unless accessing via wrapper) |
| "Levenshtein deviation" | Check TestSettings.CLASS_NAME_DEVIATION_THRESHOLD |
| Compilation errors | Ensure levenshtein framework is in classpath |

## Performance Tips

- Use `DetailLevel.MINIMAL` for faster structural tests
- Cache wrapper instances in `@BeforeAll`
- Order tests from simple to complex
- Group related tests together
- Use `@TestFactory` for similar test patterns

