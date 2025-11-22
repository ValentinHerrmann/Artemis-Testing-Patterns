# 📚 Vehicle Example - Documentation Index

Welcome to the complete documentation for the Vehicle example demonstrating the Levenshtein testing pattern.

## 🎯 Start Here

New to this project? Start with these documents in order:

1. **[README.md](README.md)** - Project overview and structure
2. **[SETUP.md](SETUP.md)** - How to build and run
3. **[TESTS.md](TESTS.md)** - Understanding the test suite

## 📖 Documentation Files

### Overview Documents

| Document | Purpose | Read Time |
|----------|---------|-----------|
| **[README.md](README.md)** | Project overview, structure, and use cases | 5 min |
| **[IMPLEMENTATION_COMPLETE.md](IMPLEMENTATION_COMPLETE.md)** | Complete implementation summary and status | 3 min |
| **[SUMMARY.md](SUMMARY.md)** | Detailed implementation summary with metrics | 10 min |

### Setup & Configuration

| Document | Purpose | Read Time |
|----------|---------|-----------|
| **[SETUP.md](SETUP.md)** | Dependencies, build configuration, and IDE setup | 5 min |
| **[CONSTANTS.md](CONSTANTS.md)** | Constants class reference and usage | 3 min |

### Test Documentation

| Document | Purpose | Read Time |
|----------|---------|-----------|
| **[TESTS.md](TESTS.md)** | Complete test suite documentation | 15 min |
| **[QUICKREF.md](QUICKREF.md)** | Quick reference guide and patterns | 8 min |

### Architecture

| Document | Purpose | Read Time |
|----------|---------|-----------|
| **[ARCHITECTURE.puml](../puml/ARCHITECTURE.puml)** | UML diagram of the complete architecture | Visual |

## 🎓 Learning Paths

### For Students

**Path 1: Understanding the Example**
1. Read [README.md](README.md) - Overview
2. Study source classes in `src/de/tum/cit/aet/`
3. Review [TESTS.md](TESTS.md) - What will be tested
4. Implement your solution
5. Run tests and fix issues

**Path 2: Learning the Pattern**
1. Read [TESTS.md](TESTS.md) - Test structure
2. Study wrapper classes in `test/de/tum/cit/aet/wrappers/`
3. Review [QUICKREF.md](QUICKREF.md) - Common patterns
4. Experiment with modifications

### For Instructors

**Path 1: Quick Review**
1. [IMPLEMENTATION_COMPLETE.md](IMPLEMENTATION_COMPLETE.md) - What's included
2. [TESTS.md](TESTS.md) - Test coverage
3. [CONSTANTS.md](CONSTANTS.md) - Configuration points

**Path 2: Deep Understanding**
1. [SUMMARY.md](SUMMARY.md) - Complete implementation details
2. Study wrapper implementations
3. Review [QUICKREF.md](QUICKREF.md) - Extension patterns
4. View [ARCHITECTURE.puml](../puml/ARCHITECTURE.puml) - Full architecture

**Path 3: Adaptation**
1. [SETUP.md](SETUP.md) - Project structure
2. [CONSTANTS.md](CONSTANTS.md) - Configuration
3. [QUICKREF.md](QUICKREF.md) - Modification patterns
4. Update files as needed

### For Developers

**Path 1: Using the Pattern**
1. [README.md](README.md) - Overview
2. [QUICKREF.md](QUICKREF.md) - Code patterns
3. Study wrapper implementations
4. Apply to your project

**Path 2: Extending the Example**
1. [ARCHITECTURE.puml](../puml/ARCHITECTURE.puml) - System structure
2. [SUMMARY.md](SUMMARY.md) - Implementation details
3. [QUICKREF.md](QUICKREF.md) - Extension patterns
4. Add your features

## 📊 Document Quick Stats

| Document | Lines | Purpose |
|----------|-------|---------|
| README.md | ~115 | Main entry point |
| SETUP.md | ~102 | Build & configuration |
| CONSTANTS.md | ~71 | Constants reference |
| TESTS.md | ~213 | Test documentation |
| QUICKREF.md | ~228 | Quick patterns |
| SUMMARY.md | ~228 | Complete overview |
| IMPLEMENTATION_COMPLETE.md | ~157 | Status summary |
| ARCHITECTURE.puml | ~200 | Visual architecture |
| **Total** | **~1,314** | **8 documents** |

## 🔍 Find What You Need

### I want to...

**...understand what this project is**
→ Read [README.md](README.md)

**...set up the project**
→ Read [SETUP.md](SETUP.md)

**...understand the tests**
→ Read [TESTS.md](TESTS.md)

**...see code examples**
→ Read [QUICKREF.md](QUICKREF.md)

**...configure for my exercise**
→ Read [CONSTANTS.md](CONSTANTS.md)

**...see implementation details**
→ Read [SUMMARY.md](SUMMARY.md)

**...check if it's complete**
→ Read [IMPLEMENTATION_COMPLETE.md](IMPLEMENTATION_COMPLETE.md)

**...understand the architecture**
→ View [ARCHITECTURE.puml](../puml/ARCHITECTURE.puml)

## 🎯 Key Concepts

### Constants-Driven Testing
All expected values (class names, method names, types) are centralized in `Constants.java`. See [CONSTANTS.md](CONSTANTS.md).

### Wrapper Pattern
Each class under test has a corresponding wrapper that defines its expected structure. See wrapper classes in `test/wrappers/`.

### Structural Test Factory
`StructuralLevenshtein.structuralTestFactory()` auto-generates comprehensive validation tests. See [TESTS.md](TESTS.md).

### Levenshtein Distance
The pattern tolerates minor naming deviations using Levenshtein distance calculation. See [TESTS.md](TESTS.md).

## 📁 Source Code Structure

```
code_example_01/
├── src/de/tum/cit/aet/              # Classes under test
│   ├── Driveable.java               # Interface (33 lines)
│   ├── AbstractVehicle.java         # Abstract class (82 lines)
│   └── Car.java                     # Implementation (127 lines)
│
├── test/de/tum/cit/aet/             # Test suite
│   ├── VehicleTest.java             # Main tests (260 lines)
│   └── wrappers/                    # Wrapper classes
│       ├── DriveableInterfaceWrapper.java      (54 lines)
│       ├── AbstractVehicleWrapper.java         (128 lines)
│       └── CarWrapper.java                     (203 lines)
│
└── docs/                            # Documentation (this folder)
    ├── INDEX.md                     # This file
    ├── README.md                    # Overview
    ├── SETUP.md                     # Setup guide
    ├── CONSTANTS.md                 # Constants reference
    ├── TESTS.md                     # Test docs
    ├── QUICKREF.md                  # Quick reference
    ├── SUMMARY.md                   # Complete summary
    ├── IMPLEMENTATION_COMPLETE.md   # Status report
    └── ARCHITECTURE.puml            # Architecture diagram
```

## 🔗 External References

### Related Files in Project
- `/test/de/tum/cit/aet/Constants.java` - Constants class (updated for Vehicle example)
- `/test/de/tum/cit/aet/levenshtein/` - Levenshtein framework classes

### Dependencies
- JUnit 5 (Jupiter API)
- Levenshtein Framework (ClassWrapper, MethodWrapper, etc.)

## ✨ Quick Links

- **View Architecture:** Open [ARCHITECTURE.puml](../puml/ARCHITECTURE.puml) in PlantUML viewer
- **Run Tests:** See instructions in [SETUP.md](SETUP.md)
- **Modify Constants:** Edit `/test/de/tum/cit/aet/Constants.java`
- **Add Tests:** See patterns in [QUICKREF.md](QUICKREF.md)

## 📮 Document Maintenance

| Document | Last Updated | Maintainer | Status |
|----------|--------------|------------|--------|
| README.md | 2025-11-22 | Auto | ✅ Current |
| SETUP.md | 2025-11-22 | Auto | ✅ Current |
| CONSTANTS.md | 2025-11-22 | Auto | ✅ Current |
| TESTS.md | 2025-11-22 | Auto | ✅ Current |
| QUICKREF.md | 2025-11-22 | Auto | ✅ Current |
| SUMMARY.md | 2025-11-22 | Auto | ✅ Current |
| IMPLEMENTATION_COMPLETE.md | 2025-11-22 | Auto | ✅ Current |
| ARCHITECTURE.puml | 2025-11-22 | Auto | ✅ Current |

---

## 🎉 Ready to Go!

All documentation is complete and ready to use. Start with [README.md](README.md) and follow your learning path above.

**Questions?** Check the relevant documentation file or review the source code.

**Need help?** Review [QUICKREF.md](QUICKREF.md) for common patterns and solutions.

