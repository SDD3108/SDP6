package kz.sdp6;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class VisitorTest {
 private SourceCodeFile source(String name, int complexity){return new SourceCodeFile(name,100,complexity,false,false);}
 private TestFile test(String module){return new TestFile(module.replace(".java","Test.java"),module,40,4);}
 @Test void countsAllFiveElementTypes(){var d=new Directory("root").add(source("A.java",3)).add(test("A.java")).add(new ConfigurationFile("app.yml",true,false)).add(new DependencyFile("pom.xml",List.of(new DependencyFile.Dependency("a","1",false,false))));var s=new ProjectStatisticsVisitor();d.accept(s);assertEquals(4,s.totalFiles());assertEquals(1,s.dependencies());}
 @Test void nestedDirectoriesAreTraversed(){var d=new Directory("root").add(new Directory("src").add(new Directory("nested").add(source("A.java",5))));var s=new ProjectStatisticsVisitor();d.accept(s);assertEquals(1,s.totalFiles());}
 @Test void emptyDirectoryIsSafe(){var d=new Directory("empty");var s=new ProjectStatisticsVisitor();d.accept(s);assertEquals(0,s.totalFiles());assertEquals(0.0,s.averageComplexity());assertEquals(0.0,s.testSourceRatio());}
 @Test void credentialsAreDetected(){var s=new SecurityAuditVisitor();new SourceCodeFile("A.java",5,2,true,false).accept(s);assertEquals(1,s.criticalIssues());assertTrue(s.report().contains("hardcoded"));}
 @Test void vulnerableDependencyIsDetected(){var s=new SecurityAuditVisitor();new DependencyFile("pom.xml",List.of(new DependencyFile.Dependency("bad","1",true,false))).accept(s);assertEquals(75,s.score());}
 @Test void insecureConfigIsDetected(){var s=new SecurityAuditVisitor();new ConfigurationFile("app.yml",false,true).accept(s);assertEquals(2,s.criticalIssues());}
 @Test void statisticsCalculateAverageAndRatio(){var d=new Directory("root").add(source("A.java",6)).add(source("B.java",10)).add(test("A.java"));var s=new ProjectStatisticsVisitor();d.accept(s);assertEquals(8,s.averageComplexity());assertEquals(.5,s.testSourceRatio());assertEquals(240,s.linesOfCode());}
 @Test void qualityGatePasses(){var d=new Directory("root").add(source("A.java",8)).add(test("A.java"));var q=new QualityGateVisitor();d.accept(q);assertTrue(q.passed());assertTrue(q.report().contains("PASSED"));}
 @Test void qualityGateFailsWhenTestsAbsent(){var q=new QualityGateVisitor();source("A.java",2).accept(q);assertFalse(q.passed());assertFalse(q.testsPass());}
 @Test void qualityGateFailsForComplexity(){var d=new Directory("root").add(source("A.java",20)).add(test("A.java"));var q=new QualityGateVisitor();d.accept(q);assertFalse(q.complexityPass());}
 @Test void qualityGateFailsSecurity(){var q=new QualityGateVisitor();new SourceCodeFile("A.java",10,2,true,false).accept(q);assertFalse(q.securityPass());}
 @Test void complianceDetectsAllRuleTypes(){var d=new Directory("root").add(new SourceCodeFile("A.java",10,2,true,false)).add(new ConfigurationFile("app.yml",true,true)).add(new DependencyFile("pom.xml",List.of(new DependencyFile.Dependency("bad","",false,true))));var c=new ComplianceVisitor();d.accept(c);assertEquals(5,c.violations().size());}
 @Test void complianceAllowsCoveredModules(){var d=new Directory("root").add(source("A.java",3)).add(test("A.java"));var c=new ComplianceVisitor();d.accept(c);assertTrue(c.passed());}
 @Test void testBeforeProductionIsStillRecognized(){var d=new Directory("root").add(test("A.java")).add(source("A.java",3));var c=new ComplianceVisitor();d.accept(c);assertTrue(c.passed());}
 @Test void technicalDebtDetectsComplexityLargeFilesAndEmptyTests(){var d=new Directory("root").add(new SourceCodeFile("A.java",401,14,false,false)).add(new TestFile("ATest.java","A.java",0,0));var v=new TechnicalDebtVisitor();d.accept(v);assertEquals(9,v.debtPoints());}
 @Test void testOnlyProjectHasNoDivisionByZero(){var s=new ProjectStatisticsVisitor();test("A.java").accept(s);assertEquals(1.0,s.testSourceRatio());}
 @Test void directoryRejectsSelfReference(){var a=new Directory("a");assertThrows(IllegalArgumentException.class,()->a.add(a));}
 @Test void dependencyListIsImmutable(){var f=new DependencyFile("pom.xml",List.of(new DependencyFile.Dependency("a","1",false,false)));assertThrows(UnsupportedOperationException.class,()->f.dependencies().clear());}
 @Test void inputValidationRejectsNegativeLoc(){assertThrows(IllegalArgumentException.class,()->new SourceCodeFile("A.java",-1,0,false,false));}
 @Test void emptyProjectFailsTestsGate(){var q=new QualityGateVisitor();new Directory("empty").accept(q);assertFalse(q.passed());}
}
