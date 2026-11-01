# Assignment 6 — Smart Code Inspection & Compliance Engine

Java 17 / Maven / JUnit 5 implementation of the **Visitor Pattern**. It checks a realistic fintech project using five independent operations without putting inspection logic in the model classes.

## Run

```bash
mvn test
mvn exec:java
```

`Main` builds `fintech-project/src`, `tests`, `config`, and `pom.xml` and executes five visitors against the **same project structure**. The demo intentionally contains findings so the reports show both successful and failed checks.

## Architecture

- `ProjectElement` defines `accept(ProjectVisitor)`.
- `SourceCodeFile`, `TestFile`, `ConfigurationFile`, `DependencyFile`, `Directory` implement it.
- `ProjectVisitor` defines five overloaded `visit(...)` methods.
- `SecurityAuditVisitor`, `ProjectStatisticsVisitor`, `QualityGateVisitor`, `ComplianceVisitor`, and `TechnicalDebtVisitor` implement operations.
- `Directory.accept()` calls `visitor.visit(this)` then recursively visits each child. There is no element-type switch in inspection operations.

## Rules and definitions

| Visitor | Rules |
| --- | --- |
| SecurityAudit | Hardcoded credentials, high-risk source, insecure configuration, exposed configuration credentials, vulnerable dependencies. Security score = max(0, 100 - 25 × critical findings). |
| ProjectStatistics | Number of non-directory files, source files, test files, LOC in source and test files, dependencies, arithmetic mean of source complexity, test file/source file ratio. |
| QualityGate | Security critical findings = 0, average complexity ≤ 10, test/source ratio ≥ 0.40; all three must pass. |
| Compliance | No exposed credentials, dependency versions set, every production source file has a test file with positive test cases targeted to its name, no prohibited dependencies. |
| TechnicalDebt | +1 point per complexity point exceeding 10, +2 for source above 300 LOC, +3 for test file with zero test cases, +2 for dependency without version. |

When there are no source files, average complexity is `0`; ratio is `0` if there are no tests and `1` if only test files exist. The choice prevents division by zero; an empty project fails the tests gate. The sample does not scan actual files—it inspects structured project metadata.

## Questions for defense

**1. What problem does Visitor solve?** Multiple unrelated operations (security, statistics, quality, compliance, debt) work on the same heterogeneous model without adding methods for each operation to every file class.

**2. Where are Element and Visitor?** `ProjectElement` is Element; the five project element classes are ConcreteElements. `ProjectVisitor` is Visitor; the five inspection classes are ConcreteVisitors.

**3. What is double dispatch?** First `element.accept(visitor)` selects the concrete element implementation; then `visitor.visit(this)` selects the overloaded `visit` for that concrete element type. For example `SourceCodeFile.accept` invokes `visit(SourceCodeFile)`.

**4. Why not place operations directly into Elements?** That mixes security/compliance/reporting concerns with data representation and forces editing all element classes whenever a new operation is added.

**5. Is a new Visitor or Element easier?** A Visitor is easier: implement the existing `ProjectVisitor` methods, leave Elements unchanged. A new element type requires updating the visitor interface and every concrete visitor.

**6. When is Visitor a bad choice?** When new element types are added frequently, or when visitors need heavy access to internal element state, causing unstable interfaces or tight coupling.

**7. Visitor vs Strategy?** Strategy selects one interchangeable algorithm for a job, while Visitor defines type-specific operations over a stable structure of heterogeneous elements via double dispatch.

**8. SOLID principles?** SRP: Element data separate from inspection logic. OCP: new operations via new visitors without modifying concrete elements. DIP: clients program against `ProjectVisitor` / `ProjectElement` interfaces. Trade-off: new element classes require edits to all visitors.

### Visitor limitation challenge: `DatabaseMigrationFile`

To add `DatabaseMigrationFile`, create a concrete `ProjectElement` class with `accept()` calling `visitor.visit(this)`, add `visit(DatabaseMigrationFile)` to `ProjectVisitor`, and implement that new method in **all five** visitors (`SecurityAuditVisitor`, `ProjectStatisticsVisitor`, `QualityGateVisitor`, `ComplianceVisitor`, `TechnicalDebtVisitor`). Update the demo and tests if the type should be exercised. Java compilation will catch missing visit implementations. Existing element classes do not need modification.

## Diagrams

- [Class diagram](docs/class-diagram.md)
- [Sequence diagram](docs/sequence-diagram.md)

These are Mermaid diagrams that GitHub renders directly.

## Tests

`VisitorTest` provides 20 JUnit 5 tests covering element types, nested/empty directories, statistics, vulnerabilities, gate PASS/FAIL, compliance violations and successful compliance, TechnicalDebtVisitor, invalid data, immutability and direct directory self-reference rejection.
