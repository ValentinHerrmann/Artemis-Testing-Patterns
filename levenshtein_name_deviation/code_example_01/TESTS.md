# Vehicle Test Suite - Levenshtein Pattern

This test suite demonstrates the Levenshtein structural testing pattern with comprehensive wrapper classes and behavioral tests.

## Structure

### Wrapper Classes (`test/de/tum/cit/aet/wrappers/`)

#### 1. DriveableInterfaceWrapper
Wraps the `Driveable` interface and defines expected methods:
- `startEngine()` → boolean
- `accelerate(double)` → void
- `brake(double)` → void
- `getCurrentSpeed()` → double

**Uses Constants:**
- `Constants.interfaceName()` → "Driveable"
- `Constants.interfaceMethod()` → "startEngine"
- `Constants.speedType()` → double.class

#### 2. AbstractVehicleWrapper
Wraps the `AbstractVehicle` abstract class and defines:

**Attributes:**
- manufacturer (String, protected)
- model (String, protected)
- yearOfManufacture (int, protected)
- currentSpeed (double, protected)
- engineRunning (boolean, protected)

**Constructor:**
- AbstractVehicle(String, String, int)

**Methods:**
- getManufacturer(), getModel(), getYearOfManufacture()
- isEngineRunning(), getVehicleInfo()
- calculateFuelConsumption() (abstract)
- getMaxSpeed() (abstract)

**Uses Constants:**
- `Constants.abstractClass()` → "AbstractVehicle"
- `Constants.abstractClassAttribute()` → "manufacturer"
- `Constants.yearType()` → int.class
- `Constants.speedType()` → double.class
- `Constants.overwrittenMethod()` → "calculateFuelConsumption"
- `Constants.calcReturnType()` → double.class

#### 3. CarWrapper
Wraps the `Car` concrete class with full inheritance hierarchy:

**Inherits from:** AbstractVehicleWrapper  
**Implements:** DriveableInterfaceWrapper

**Additional Attributes:**
- numberOfDoors (int, private)
- fuelType (String, private)
- engineCapacity (double, private)
- maxSpeed (double, private)
- fuelConsumptionRate (double, private)

**Constructor:**
- Car(String, String, int, int, String, double)

**Implements all interface methods + overrides abstract methods**

**Uses Constants:**
- `Constants.concreteClass()` → "Car"
- `Constants.concreteClassAttribute()` → "numberOfDoors"
- `Constants.doorType()` → int.class
- `Constants.engineCapacityType()` → double.class

## Test Class (`VehicleTest.java`)

### Structural Tests (Order 1)
Uses `StructuralLevenshtein.structuralTestFactory()` to automatically generate dynamic tests for:
- Interface structure validation
- Abstract class structure validation
- Concrete class structure validation
- Inheritance hierarchy verification
- Method signature verification
- Attribute type and modifier verification

**Detail Level:** FULL (most comprehensive)

### Behavioral Tests (Order 2-12)

| Test | Description | Constants Used |
|------|-------------|----------------|
| **testCarCreation** | Constructor validation | `concreteClass()` |
| **testAbstractVehicleAttributes** | Inherited attribute access | `abstractClassAttribute()` |
| **testCarSpecificAttributes** | Car-specific attribute | `concreteClassAttribute()` |
| **testStartEngine** | Interface method behavior | `interfaceMethod()` |
| **testAcceleration** | Speed increase logic | `speedType()` |
| **testBraking** | Speed decrease logic | - |
| **testCalculateFuelConsumption** | Abstract method override | `overwrittenMethod()`, `calcReturnType()` |
| **testGetMaxSpeed** | Abstract method override | - |
| **testIsElectric** | Car-specific method | - |
| **testGetVehicleInfo** | Inherited method override | - |
| **testCompleteDrivingScenario** | Integration test | `interfaceMethod()`, `overwrittenMethod()` |

## Running the Tests

### Prerequisites
1. Copy levenshtein framework classes to classpath:
   - `ClassWrapper`, `MethodWrapper`, `AttributeWrapper`, `ConstructorWrapper`
   - `GenericClassWrapper`, `Wrapper`, `WrapperProperty`
   - `StructuralLevenshtein`, `LevenshteinUtils`

2. Ensure JUnit 5 is available

3. Compile source classes first:
```bash
javac code_example_01/src/de/tum/cit/aet/*.java
```

### Run Tests
```bash
# Compile test classes
javac -cp .:junit-jupiter-api.jar:levenshtein-framework.jar \
  code_example_01/test/de/tum/cit/aet/*.java \
  code_example_01/test/de/tum/cit/aet/wrappers/*.java

# Run tests
java -cp .:junit-platform-console-standalone.jar:levenshtein-framework.jar \
  org.junit.platform.console.ConsoleLauncher \
  --select-class de.tum.cit.aet.VehicleTest
```

Or with Gradle:
```bash
./gradlew test --tests VehicleTest
```

## Key Features Demonstrated

### 1. **Constants-Driven Testing**
All expected values come from the `Constants` class, making tests maintainable and adaptable to different variants.

### 2. **Levenshtein Name Deviation**
The wrappers automatically handle minor naming deviations:
- "startEngine" vs "startEngne" (typo)
- "manufacturer" vs "manufcturer" (missing 'a')
- "numberOfDoors" vs "numDoors" (abbreviation)

### 3. **Structural Factory Pattern**
`StructuralLevenshtein.structuralTestFactory()` generates comprehensive tests automatically:
- Class existence
- Modifiers (public, abstract, etc.)
- Inheritance relationships
- Method signatures
- Attribute types
- Constructor parameters

### 4. **Wrapper-Based Invocation**
Tests invoke methods through wrappers, enabling:
- Reflection-based testing
- Type-safe method calls
- Automatic deviation handling
- Clear error messages

### 5. **Comprehensive Coverage**
- **Structural:** Interface, abstract class, concrete class
- **Behavioral:** All methods and edge cases
- **Integration:** Complete workflow scenarios

## Expected Output

### Structural Tests
```
✓ Driveable - Class Existence
✓ Driveable - Class Modifiers
✓ Driveable - Method: startEngine
✓ Driveable - Method: accelerate
✓ AbstractVehicle - Class Existence
✓ AbstractVehicle - Attributes: manufacturer, model, yearOfManufacture
✓ AbstractVehicle - Constructor
✓ Car - Class Existence
✓ Car - Inheritance from AbstractVehicle
✓ Car - Implements Driveable
✓ Car - All methods present
```

### Behavioral Tests
```
✓ Car Creation - Constructor test
✓ Abstract Class Attributes - Manufacturer attribute test
✓ Concrete Class Attributes - NumberOfDoors attribute test
✓ Interface Method - startEngine() test
✓ Interface Method - accelerate() and getCurrentSpeed() test
✓ Interface Method - brake() test
✓ Abstract Method Override - calculateFuelConsumption() test
✓ Abstract Method Override - getMaxSpeed() test
✓ Car Specific Method - isElectric() test
✓ Inherited Method - getVehicleInfo() test
✓ Integration Test - Complete driving scenario
```

## Customization

To adapt for different exercises:
1. Update `Constants.java` with new class/method names
2. Modify wrapper classes to match new structure
3. Add/remove behavioral tests as needed
4. Adjust Levenshtein distance threshold in `TestSettings`

## Benefits

- **Student-Friendly:** Clear error messages with deviation information
- **Maintainable:** Constants-based configuration
- **Flexible:** Handles naming variations automatically
- **Comprehensive:** Structural + behavioral validation
- **Scalable:** Easy to add new test cases

