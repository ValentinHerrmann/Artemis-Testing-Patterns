# Constants Overview for Vehicle Example

This Constants class provides centralized access to the expected class, interface, and member names for the Vehicle structural testing pattern.

## Variant

- **DEFAULT**: The single variant for the Vehicle example (AbstractVehicle, Car, Driveable)

## Constants Reference

### Class Names

| Method | Return Value | Description |
|--------|--------------|-------------|
| `abstractClass()` | "AbstractVehicle" | Name of the abstract base class |
| `concreteClass()` | "Car" | Name of the concrete implementation class |
| `interfaceName()` | "Driveable" | Name of the interface |

### Attribute Names

| Method | Return Value | Description |
|--------|--------------|-------------|
| `abstractClassAttribute()` | "manufacturer" | Example attribute from AbstractVehicle |
| `concreteClassAttribute()` | "numberOfDoors" | Example attribute from Car |

### Method Names

| Method | Return Value | Description |
|--------|--------------|-------------|
| `interfaceMethod()` | "startEngine" | Example method from Driveable interface |
| `overwrittenMethod()` | "calculateFuelConsumption" | Abstract method overridden in Car |

### Type Definitions

| Method | Return Type | Description |
|--------|-------------|-------------|
| `yearType()` | `int.class` | Type for yearOfManufacture parameter |
| `speedType()` | `double.class` | Type for speed-related values |
| `doorType()` | `int.class` | Type for numberOfDoors attribute |
| `engineCapacityType()` | `double.class` | Type for engineCapacity attribute |
| `calcReturnType()` | `double.class` | Return type for calculateFuelConsumption() |

## Usage Example

```java
// In a test class
String expectedClassName = Constants.abstractClass(); // "AbstractVehicle"
Class<?> expectedType = Constants.speedType(); // double.class

ClassWrapper<?> wrapper = new ConcreteClassWrapper<>(
    expectedClassName,
    "de.tum.cit.aet",
    null,
    new ClassWrapper<?>[]{},
    "public abstract"
);
```

## CSV Export

Run the `main()` method to generate a CSV file (`VariantOverview.csv`) with all constant values:

```bash
cd /path/to/test/de/tum/cit/aet
javac Constants.java
java Constants
```

This will create a file showing all constants for each variant in CSV format.

