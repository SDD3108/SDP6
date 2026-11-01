package kz.sdp6;
import java.util.ArrayList;
import java.util.List;
public final class TechnicalDebtVisitor implements ProjectVisitor {
 private final List<String> warnings = new ArrayList<>();
 private int points;
 public int debtPoints(){return points;}
 public List<String> warnings(){return List.copyOf(warnings);}
 @Override public void visit(SourceCodeFile f){if(f.complexity()>10){int excess=f.complexity()-10;points+=excess;warnings.add(f.name()+": complexity exceeds 10 by "+excess);}if(f.linesOfCode()>300){points+=2;warnings.add(f.name()+": large source file");}}
 @Override public void visit(TestFile f){if(f.testCases()==0){points+=3;warnings.add(f.name()+": no test cases");}}
 @Override public void visit(ConfigurationFile f){}
 @Override public void visit(DependencyFile f){for(var d:f.dependencies())if(d.version().isBlank()){points+=2;warnings.add(d.artifact()+": version not pinned");}}
 @Override public void visit(Directory d){}
 public String report(){return "Technical debt points: "+points+"\n"+(warnings.isEmpty()?"No technical debt warnings":String.join("\n",warnings));}
}
