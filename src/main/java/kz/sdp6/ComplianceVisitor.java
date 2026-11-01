package kz.sdp6;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public final class ComplianceVisitor implements ProjectVisitor {
 private final Set<String> productionModules = new HashSet<>(), testedModules = new HashSet<>();
 private final List<String> violations = new ArrayList<>();
 public List<String> violations() {
  List<String> result = new ArrayList<>(violations);
  for(String module:productionModules) if(!testedModules.contains(module)) result.add("Missing tests for: "+module);
  result.sort(String::compareTo); return List.copyOf(result);
 }
 public boolean passed(){return violations().isEmpty();}
 @Override public void visit(SourceCodeFile f){productionModules.add(f.name());if(f.hardcodedCredentials()) violations.add("Credentials in: "+f.name());}
 @Override public void visit(TestFile f){if(f.testCases()>0) testedModules.add(f.targetModule());}
 @Override public void visit(ConfigurationFile f){if(f.exposedCredentials()) violations.add("Exposed credentials in: "+f.name());}
 @Override public void visit(DependencyFile f){for(var d:f.dependencies()){if(d.version().isBlank())violations.add("Unspecified version: "+d.artifact());if(d.prohibited()) violations.add("Prohibited dependency: "+d.artifact());}}
 @Override public void visit(Directory d){}
 public String report(){return "Compliance: "+(passed()?"PASS":"FAIL")+"\n"+(passed()?"No violations":String.join("\n",violations()));}
}
