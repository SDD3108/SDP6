package kz.sdp6;
public final class ProjectStatisticsVisitor implements ProjectVisitor {
 private int files, source, tests, dependencies, loc, sumComplexity;
 public int totalFiles(){return files;}
 public int sourceFiles(){return source;}
 public int testFiles(){return tests;}
 public int dependencies(){return dependencies;}
 public int linesOfCode(){return loc;}
 public double averageComplexity(){return source == 0 ? 0.0 : (double)sumComplexity/source;}
 public double testSourceRatio(){return source == 0 ? (tests == 0 ? 0.0 : 1.0) : (double)tests/source;}
 @Override public void visit(SourceCodeFile f){files++;source++;loc+=f.linesOfCode();sumComplexity+=f.complexity();}
 @Override public void visit(TestFile f){files++;tests++;loc+=f.linesOfCode();}
 @Override public void visit(ConfigurationFile f){files++;}
 @Override public void visit(DependencyFile f){files++;dependencies+=f.dependencies().size();}
 @Override public void visit(Directory d){}
 public String report(){return String.format(java.util.Locale.ROOT,"Files: %d | LOC: %d | Tests: %d | Dependencies: %d | Avg complexity: %.2f | Test/source ratio: %.2f",files,loc,tests,dependencies,averageComplexity(),testSourceRatio());}
}
