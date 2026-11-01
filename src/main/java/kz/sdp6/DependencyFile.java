package kz.sdp6;
import java.util.List;
import java.util.Objects;
public record DependencyFile(String name, List<Dependency> dependencies) implements ProjectElement {
 public record Dependency(String artifact, String version, boolean vulnerable, boolean prohibited) {
  public Dependency { Objects.requireNonNull(artifact); Objects.requireNonNull(version); if(artifact.isBlank()) throw new IllegalArgumentException("Empty artifact"); }
 }
 public DependencyFile { Objects.requireNonNull(name); if(name.isBlank()) throw new IllegalArgumentException("Empty name"); dependencies = List.copyOf(dependencies); }
 @Override public void accept(ProjectVisitor visitor) { visitor.visit(this); }
}
