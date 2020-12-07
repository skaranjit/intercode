package intercode.ast;

import intercode.visitor.ASTVisitor;

public class FalseNode extends ExprNode {
public FalseNode(){

}


public void accept(ASTVisitor v){
    v.visit(this);
}
}
