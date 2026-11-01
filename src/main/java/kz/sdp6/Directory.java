package kz.sdp6;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
public final class Directory implements ProjectElement {
 private final String name;
 private final List<ProjectElement> children = new ArrayList<>();
 public Directory(String name) { if(name == null || name.isBlank()) throw new IllegalArgumentException("Empty directory name"); this.name=name; }
 public String name() { return name; }
 public List<ProjectElement> children() { return Collections.unmodifiableList(children); }
 public Directory add(ProjectElement child) {
  Objects.requireNonNull(child);
  if(child == this) throw new IllegalArgumentException("Directory cannot contain itself");
  children.add(child); return this;
 }
 @Override public void accept(ProjectVisitor visitor) { visitor.visit(this); for(ProjectElement element:children) element.accept(visitor); }
}
