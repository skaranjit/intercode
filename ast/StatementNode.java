package intercode.ast;

import intercode.visitor.ASTVisitor;

public class StatementNode extends Node {
    //
    //public Node assign;
    public StatementNode stmt;

    public StatementNode(){}

   // public StatementNode(Node assign){
     //   this.assign=assign;
    //}

    public void accept(ASTVisitor v){
        v.visit(this);
    }



}
