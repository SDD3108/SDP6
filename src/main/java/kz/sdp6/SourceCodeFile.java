package kz.sdp6;
import java.util.Objects;
public record SourceCodeFile(String name, int linesOfCode, int complexity, boolean hardcodedCredentials, boolean highRisk) implements ProjectElement {
 public SourceCodeFile { Objects.requireNonNull(name); if (name.isBlank() || linesOfCode < 0 || complexity < 0) throw new IllegalArgumentException("Invalid source file"); }
 @Override public void accept(ProjectVisitor visitor) { visitor.visit(this); }
}
