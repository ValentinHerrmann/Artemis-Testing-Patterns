# Code Example 01: Vehicle System

This example demonstrates the capabilities of the Levenshtein testing pattern with a vehicle hierarchy.

## 📚 Documentation

- **[SETUP.md](SETUP.md)** - Dependencies and build configuration
- **[CONSTANTS.md](CONSTANTS.md)** - Overview of Constants class
- **[TESTS.md](TESTS.md)** - Detailed test suite documentation

## 📁 Project Structure

```
code_example_01/
├── src/de/tum/cit/aet/           # Classes under test
│   ├── Driveable.java            # Interface
│   ├── AbstractVehicle.java      # Abstract base class
│   └── Car.java                  # Concrete implementation
├── test/de/tum/cit/aet/
│   ├── VehicleTest.java          # Main test class
│   └── wrappers/                 # Wrapper classes
│       ├── DriveableInterfaceWrapper.java
│       ├── AbstractVehicleWrapper.java
│       └── CarWrapper.java
└── docs/
    ├── README.md                 # This file
    ├── SETUP.md                  # Setup instructions
    ├── CONSTANTS.md              # Constants reference
    └── TESTS.md                  # Test documentation
```

## Structure

### Interface: `Driveable`
- Defines the contract for driveable vehicles
- Methods:
  - `boolean startEngine()`
  - `void accelerate(double speedIncrease)`
  - `void brake(double brakingForce)`
  - `double getCurrentSpeed()`

### Abstract Class: `AbstractVehicle`
- Base class for all vehicles
- **Attributes** (protected):
  - `manufacturer: String`
  - `model: String`
  - `yearOfManufacture: int`
  - `currentSpeed: double`
  - `engineRunning: boolean`
- **Constructor**: `AbstractVehicle(String manufacturer, String model, int yearOfManufacture)`
- **Concrete Methods**:
  - `getManufacturer()`, `getModel()`, `getYearOfManufacture()`
  - `isEngineRunning()`
  - `getVehicleInfo()`
- **Abstract Methods**:
  - `calculateFuelConsumption()`
  - `getMaxSpeed()`

### Concrete Class: `Car`
- Extends `AbstractVehicle`
- Implements `Driveable`
- **Additional Attributes** (private):
  - `numberOfDoors: int`
  - `fuelType: String`
  - `engineCapacity: double`
  - `maxSpeed: double`
  - `fuelConsumptionRate: double`
- **Constructor**: `Car(String manufacturer, String model, int yearOfManufacture, int numberOfDoors, String fuelType, double engineCapacity)`
- **Implements all interface methods**
- **Overrides abstract methods**
- **Additional methods**: `getNumberOfDoors()`, `getFuelType()`, `getEngineCapacity()`, `isElectric()`

## What This Example Demonstrates

### 1. **Interface Testing**
The test verifies that `Driveable` interface exists with the correct:
- Method signatures
- Return types
- Parameter types
- Modifiers (public abstract)

### 2. **Abstract Class Testing**
The test verifies that `AbstractVehicle` has:
- Correct attributes with proper visibility (protected)
- Constructor with correct parameters
- Concrete method implementations
- Abstract method declarations
- Proper class modifiers (public abstract)

### 3. **Implementation Testing**
The test verifies that `Car`:
- Extends the correct superclass
- Implements the correct interface
- Has all required attributes (including inherited)
- Has proper constructor
- Implements all interface methods
- Overrides all abstract methods
- Has additional specific methods

### 4. **Levenshtein Distance Tolerance**
The pattern can tolerate minor naming deviations:
- If a student writes `accelrate` instead of `accelerate`
- If they write `numberOfDoors` as `numDoors` or `doorCount`
- The test can still match these with warnings instead of failures

### 5. **Comprehensive Structural Validation**
- Verifies inheritance hierarchy
- Checks method signatures precisely
- Validates attribute types and modifiers
- Tests constructor parameters
- Ensures interface contract compliance

## Running the Test

The `VehicleStructuralTest` class uses the Levenshtein pattern to:
1. Define expected structure using wrappers
2. Generate dynamic tests with `structuralTestFactory()`
3. Compare actual implementation against expected structure
4. Provide detailed feedback on structural deviations

## Use Cases

This pattern is ideal for:
- **Programming exercises** where students implement specific structures
- **Code reviews** to ensure architectural compliance
- **Refactoring validation** to maintain structural contracts
- **API compatibility testing** across versions

## 🧪 Test Suite Overview

### Wrapper Classes
The test suite includes three wrapper classes that follow the Levenshtein pattern:

1. **DriveableInterfaceWrapper** - Wraps the Driveable interface
2. **AbstractVehicleWrapper** - Wraps the AbstractVehicle abstract class  
3. **CarWrapper** - Wraps the Car concrete class with full inheritance

### Test Types

#### Structural Tests (Automated)
- ✅ Class existence and modifiers
- ✅ Inheritance hierarchy validation
- ✅ Interface implementation verification
- ✅ Method signatures and return types
- ✅ Attribute types and visibility
- ✅ Constructor parameters

#### Behavioral Tests (12 Tests)
- ✅ Constructor validation
- ✅ Attribute access (inherited and specific)
- ✅ Interface method implementation
- ✅ Abstract method overrides
- ✅ State management
- ✅ Integration scenarios

### Constants Integration
All tests use the `Constants` class from `/test/de/tum/cit/aet/Constants.java`:
- `abstractClass()` → "AbstractVehicle"
- `concreteClass()` → "Car"
- `interfaceName()` → "Driveable"
- `abstractClassAttribute()` → "manufacturer"
- `concreteClassAttribute()` → "numberOfDoors"
- And more... (see [CONSTANTS.md](CONSTANTS.md))

## 🚀 Quick Start

1. Review the class structure in `src/`
2. Check wrapper implementations in `test/wrappers/`
3. Run `VehicleTest.java` to see structural and behavioral tests
4. Modify `Constants.java` to adapt for different exercises

## 📖 Learn More

See [TESTS.md](TESTS.md) for detailed test documentation and expected output.

