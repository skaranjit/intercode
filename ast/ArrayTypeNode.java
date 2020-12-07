package intercode.ast;

import intercode.lexer.Type;
import intercode.visitor.ASTVisitor;

public class ArrayTypeNode extends TypeNode {


    // Type---> Type [NUM] | basic
    public TypeNode type;
    public int size=1;

    public ArrayTypeNode(){}

    public ArrayTypeNode( int size, TypeNode t){
        this.type=t;
        this.size=size;
    }
    public void accept(ASTVisitor v){
        v.visit(this);
    }



}
