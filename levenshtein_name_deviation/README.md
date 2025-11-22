## Levenshtein Name Deviation Testing Pattern

## Problem Statement:

Testing student code with test cases that require exact name matching (e.g., class names, method names) can lead to 
issues when students deviate slightly from the expected names. Such deviations may result in test failures, even if 
the underlying logic is correct. This pattern addresses the challenge of creating robust tests that can accommodate 
minor naming variations while still effectively validating the intended functionality.

In learning exercises where students are expected to implement specific classes or methods, it is common for them 
to make small naming deviations, such as typos or slight alterations. Rigid name matching in tests can lead to:
* **Increased frustration:** Students may become frustrated when their correct logic is marked incorrect due to naming issues.
* **Reduced learning effectiveness:** The focus shifts from understanding concepts to worrying primarily about exact names.

In exams, such naming deviations can be particularly problematic, as they may unfairly penalize students for minor mistakes
and will lead to increased workload for instructors who need to manually review complaints and re-evaluate submissions.

### Rationale:

This pattern aims to create a more forgiving testing environment that still maintains the integrity of the assessment. 
* **Fully compatible with Exercise Variants Pattern:** As the pattern uses Strings for finding names and Types via Reflection, it is fully compatible with the Exercise Variants Pattern described [here](../exercise_variants/README.md).
* **Improves student experience:** By allowing for minor naming deviations, students can focus on learning and applying concepts rather than being overly concerned with exact names and get less frustrated.
* **Reduces instructor workload:** Minimizes the need for manual reviews of submissions due to naming issues.

### Try it out
Tests will be run using the command `./gradlew clean test`.

### Pattern Description:

Overview of the Pattern Structure (details below):
![your-UML-diagram-name](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/ValentinHerrmann/Artemis-Testing-Patterns/refs/heads/develop/levenshtein_name_deviation/puml/de.tum.cit.aet.puml)



The `de.tum.cit.aet.levenshtein` package contains Wrappers classes for structure elements which shall be inherited for
the classes to test in `de.tum.cit.aet.wrappers`. 
![your-UML-diagram-name](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/ValentinHerrmann/Artemis-Testing-Patterns/refs/heads/develop/levenshtein_name_deviation/puml/de.tum.cit.aet.levenshtein.puml)

`de.tum.cit.aet.wrappers` contains one Wrapper class per class to test. For definitely existing classes for which a 
Class<?> is available the `GenericClassWrapper` can be used and no dedicated Wrapper class is necessary.
![your-UML-diagram-name](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/ValentinHerrmann/Artemis-Testing-Patterns/refs/heads/develop/levenshtein_name_deviation/puml/de.tum.cit.aet.wrappers.puml)
