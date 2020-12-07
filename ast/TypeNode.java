package intercode.ast;

import intercode.lexer.Type;
import intercode.visitor.ASTVisitor;

public class TypeNode extends Node {

    // type--> Type[NUM] | basic;

    public Type basic;
    public ArrayTypeNode array= null; // by default, array type is null

    public TypeNode(){

    }
    public TypeNode(Type t, ArrayTypeNode at){
        this.basic=t;
        this.array=at;
    }
    public void accept(ASTVisitor v){
        v.visit(this);
    }




}
