package intercode.ast;

import intercode.visitor.ASTVisitor;

public class ArrayDimsNode extends ExprNode {
    //
    //      a[i] might be a[i][e][f]
    // Arraydims helps to parse multi-dimensional array
    //
    public ExprNode size;
    public ArrayDimsNode dim;

    public ArrayDimsNode(){}

    public ArrayDimsNode(ExprNode size, ArrayDimsNode dim){
        this.size=size;
        this.dim=dim;
    }

    public void accept(ASTVisitor v){
        v.visit(this);
    }
}
