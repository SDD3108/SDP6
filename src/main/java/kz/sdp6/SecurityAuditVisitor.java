package kz.sdp6;
import java.util.ArrayList;
import java.util.List;
public final class SecurityAuditVisitor implements ProjectVisitor {
 private final List<String> findings = new ArrayList<>();
 private int critical;
 public List<String> findings() { return List.copyOf(findings); }
 public int criticalIssues() { return critical; }
 public int score() { return Math.max(0, 100 - critical * 25); }
 private void issue(String message) { findings.add(message); critical++; }
 @Override public void visit(SourceCodeFile f) { if(f.hardcodedCredentials()) issue(f.name()+": hardcoded credentials"); if(f.highRisk()) issue(f.name()+": high-risk source file"); }
 @Override public void visit(TestFile f) {}
 @Override public void visit(ConfigurationFile f) { if(!f.secure()) issue(f.name()+": insecure configuration"); if(f.exposedCredentials()) issue(f.name()+": exposed credentials"); }
 @Override public void visit(DependencyFile f) { for(var dep:f.dependencies()) if(dep.vulnerable()) issue(dep.artifact()+": vulnerable dependency"); }
 @Override public void visit(Directory d) {}
 public String report() { return "Security score: "+score()+"/100 | critical: "+critical+"\n"+(findings.isEmpty()?"No security findings":String.join("\n", findings)); }
}
