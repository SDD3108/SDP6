package kz.sdp6;
import java.util.Objects;
public record TestFile(String name, String targetModule, int linesOfCode, int testCases) implements ProjectElement {
 public TestFile { Objects.requireNonNull(name); Objects.requireNonNull(targetModule); if (name.isBlank() || targetModule.isBlank() || linesOfCode < 0 || testCases < 0) throw new IllegalArgumentException("Invalid test file"); }
 @Override public void accept(ProjectVisitor visitor) { visitor.visit(this); }
}
