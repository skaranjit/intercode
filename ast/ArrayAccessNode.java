package intercode.ast;

import intercode.visitor.ASTVisitor;

public class ArrayAccessNode extends ExprNode {

    //a[i] = k+j

    public IdentifierNode id;
    public ExprNode index;

    public ArrayAccessNode(){

    }
    public ArrayAccessNode(IdentifierNode id, ExprNode e){
        this.id=id;
        this.index=e;
    }
    public void accept(ASTVisitor v){
        v.visit(this);
    }

}
