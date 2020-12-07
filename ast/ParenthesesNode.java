package intercode.ast;

import intercode.visitor.ASTVisitor;

public class ParenthesesNode extends ExprNode {

    public ExprNode expr;
   // public Statements stmts;



    public ParenthesesNode(){}

   // public ParenthesesNode(ExprNode expr){
     //   this.expr=expr;
     //   this.stmts=stmts;
    //}

    public void accept(ASTVisitor v){
        v.visit(this);
    }


}
