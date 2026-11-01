package kz.sdp6;
import java.util.List;
public final class Main {
 public static Directory exampleProject(){
  return new Directory("fintech-project")
   .add(new Directory("src")
    .add(new SourceCodeFile("PaymentService.java",220,14,true,true))
    .add(new SourceCodeFile("UserService.java",145,7,false,false)))
   .add(new Directory("tests").add(new TestFile("PaymentServiceTest.java","PaymentService.java",120,8)))
   .add(new Directory("config").add(new ConfigurationFile("application.properties",false,true)))
   .add(new DependencyFile("pom.xml",List.of(new DependencyFile.Dependency("spring-web","6.2.1",false,false),new DependencyFile.Dependency("legacy-json","",true,true))));
 }
 public static void main(String[] args){
  Directory project=exampleProject();
  SecurityAuditVisitor security=new SecurityAuditVisitor();
  ProjectStatisticsVisitor statistics=new ProjectStatisticsVisitor();
  QualityGateVisitor quality=new QualityGateVisitor();
  ComplianceVisitor compliance=new ComplianceVisitor();
  TechnicalDebtVisitor debt=new TechnicalDebtVisitor();
  project.accept(security);project.accept(statistics);project.accept(quality);project.accept(compliance);project.accept(debt);
  System.out.println("=== Smart Code Inspection: "+project.name()+" ===");
  System.out.println("\n[SECURITY]\n"+security.report());
  System.out.println("\n[STATISTICS]\n"+statistics.report());
  System.out.println("\n[QUALITY GATE]\n"+quality.report());
  System.out.println("\n[COMPLIANCE]\n"+compliance.report());
  System.out.println("\n[TECHNICAL DEBT]\n"+debt.report());
 }
}
