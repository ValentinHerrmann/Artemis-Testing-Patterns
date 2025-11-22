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


### Pattern Description:

@startuml
skinparam classAttributeIconSize 0
hide circle
/'
hide private members
hide protected members
hide package members
'/

package  "levenshtein" {


abstract class Wrapper<T> {
~ name : WrapperProperty<String>
~ modifiers : WrapperProperty<String>
~ parentClassWrapper : ClassWrapper<T>
~ existence : Existence
--
+ Wrapper(parentClass: ClassWrapper<T>, expectedName: String, expectedModifiers: String)
+ verifyExistence(throwAssertion : boolean) : void
+ verifyExistence(failMessage : String, throwAssertion : boolean) : void
+ getOverallExistence() : Existence
+ getParentClassWrapper(): ClassWrapper<T>
+ getExpectedName(): String
+ toString(): String
+ {abstract} expectedToString(): String
+ {abstract} actualToString(): String

~ findWithDeviation(): void
~ verifyModifiers(modifierBitmask: int): void
~ verifyType(typeWrapperProperty: WrapperProperty<Class<?>>, actualType: Class<?>): void


~ {abstract} parseExistence(): void
~ parseExistence(properties: WrapperProperty<?>): void

}

class AttributeWrapper<T,V> {
- field : Field

~ type : WrapperProperty<Class<?>>
--
+AttributeWrapper(parentClass : ClassWrapper<T>, expectedName : String, expectedType : Class<V>, modifiers : String)

+verifyExistence(throwAssertion: boolean): void

+getValue() : V
+getValue(obj : Object) : V
+setValue(value : V) : void
+setValue(value : V, obj : Object) : void

+expectedToString(): String
+actualToString(): String
}



class ConstructorWrapper {
~ paramTypes : Class<?>[]
- constructor : Constructor<T>
  --
+ ConstructorWrapper(parentClass : ClassWrapper<T>, paramTypes : Class<?>[], modifiers : String)
+ verifyExistence(throwAssertion : boolean): void
+ invoke(args : Object...) : T

~ findWithDeviation(): void
~ parseExistence(): void
+ expectedToString(): String
+ actualToString(): String
  }


class MethodWrapper {
- paramTypes : Class<?>[]
- returnType : WrapperProperty<Class<?>>
  -method: Method
  --
  +MethodWrapper(parentClass: ClassWrapper<T>, expectedName: String, expectedReturnType: Class<R>, paramTypes: Class<?>[], modifiers: String)
  +verifyExistence(throwAssertion: boolean): void
  #findWithDeviation(): void
  #parseExistence(): void
  +invoke(params : Object...) : R
  +invoke(objWrapper: ClassWrapper<?>, params: Object): R
  +expectedToString(): String
  +actualToString(): String
  }


class GenericClassWrapper {
-clazz: Class<T>
--
+GenericClassWrapper(clz: Class<T>)
+getClazz(): Class<T>
+getObj(): Object
}

abstract class ClassWrapper {
-expectedPackage: String
-clazz: Class<T>
#obj: T
~superClassWrapper: WrapperProperty<ClassWrapper<?>>
  ~interfaceWrappers: WrapperProperty<ClassWrapper<?>[]>
--
+ClassWrapper(expectedName: String, expectedPackage: String, superClassWrapper: ClassWrapper<?>, interfaceWrappers: ClassWrapper<?>[], modifiers: String)
+verifyExistence(throwAssertion: boolean): void
+getClazz(): Class<T>
+getObj(): Object
#findWithDeviation(): void
+verifySuperClass(): void
+verifyInterfaces(): void
-{static} generateClassNameVariations(className: String): String[]
-{static} isClassNameWithinClassTestDeviation(expectedClassName: String, actualClassName: String): boolean
+getAttributeWrappers(): List<Wrapper<T>>
+getMethodsWrappers(): List<Wrapper<T>>
+getConstructorWrappers(): List<Wrapper<T>>
#parseExistence(): void
+expectedToString(): String
+actualToString(): String
+equals(other: Object): boolean
+toString(): String
}

class WrapperProperty {
~expected: T
~actual: T
~existence: Existence
--
+WrapperProperty(expected: T)
+toString(): String
}











ConstructorWrapper -[hidden]-> AttributeWrapper
AttributeWrapper -[hidden]-> MethodWrapper
MethodWrapper -[hidden]-> ClassWrapper



ConstructorWrapper -l|> Wrapper
AttributeWrapper -l|> Wrapper
MethodWrapper -l|> Wrapper
ClassWrapper -u|> Wrapper

GenericClassWrapper -up-|> ClassWrapper

/'
AttributeWrapper ..> WrapperProperty

ConstructorWrapper .up.> ClassWrapper
AttributeWrapper .up.> ClassWrapper
MethodWrapper .up.> ClassWrapper


Wrapper *-- ClassWrapper
ClassWrapper o-- ClassWrapper


AttributeWrapper *-left- WrapperProperty
Wrapper -left-> WrapperProperty
Wrapper o-left- WrapperProperty
MethodWrapper *-left- WrapperProperty
ClassWrapper -left-> WrapperProperty
ClassWrapper o-left- WrapperProperty

'/

}
@enduml