package intercode.ast;

import intercode.visitor.ASTVisitor;
import intercode.inter.*;
import intercode.lexer.*;
import java.util.*;

public class StatementNode extends Node {
    //
    //public Node assign;
    public StatementNode stmt;
    public List<AssignmentNode> assigns = new ArrayList<AssignmentNode>();
    
    
    //Label for intermediate Code
    public LabelNode startLabel;
    public LabelNode falseLabel;
    public GotoNode toGoto;
    public StatementNode(){}

   // public StatementNode(Node assign){
     //   this.assign=assign;
    //}

    public void accept(ASTVisitor v){
        v.visit(this);
    }



}
