package kz.sdp6;
public final class QualityGateVisitor implements ProjectVisitor {
 private final ProjectStatisticsVisitor statistics = new ProjectStatisticsVisitor();
 private final SecurityAuditVisitor security = new SecurityAuditVisitor();
 @Override public void visit(SourceCodeFile f){statistics.visit(f);security.visit(f);}
 @Override public void visit(TestFile f){statistics.visit(f);security.visit(f);}
 @Override public void visit(ConfigurationFile f){statistics.visit(f);security.visit(f);}
 @Override public void visit(DependencyFile f){statistics.visit(f);security.visit(f);}
 @Override public void visit(Directory d){}
 public boolean securityPass(){return security.criticalIssues()==0;}
 public boolean complexityPass(){return statistics.averageComplexity()<=10;}
 public boolean testsPass(){return statistics.testSourceRatio()>=0.40;}
 public boolean passed(){return securityPass()&&complexityPass()&&testsPass();}
 private String status(boolean pass){return pass?"PASS":"FAIL";}
 public String report(){return "Security: "+status(securityPass())+"\nComplexity: "+status(complexityPass())+"\nTests: "+status(testsPass())+"\nFINAL RESULT: "+(passed()?"PASSED":"FAILED");}
}
