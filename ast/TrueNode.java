package intercode.ast;

import intercode.visitor.ASTVisitor;

public class TrueNode extends ExprNode {
    public TrueNode(){

    }
    public void accept(ASTVisitor v){
        v.visit(this);
    }
}
