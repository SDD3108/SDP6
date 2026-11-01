package kz.sdp6;
import java.util.Objects;
public record ConfigurationFile(String name, boolean secure, boolean exposedCredentials) implements ProjectElement {
 public ConfigurationFile { Objects.requireNonNull(name); if(name.isBlank()) throw new IllegalArgumentException("Empty name"); }
 @Override public void accept(ProjectVisitor visitor) { visitor.visit(this); }
}
