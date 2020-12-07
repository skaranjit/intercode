package intercode.ast;

import intercode.lexer.Type;
import intercode.visitor.ASTVisitor;

public class ExprNode extends Node {
    //   expr--> || | && | ==| !=|<| <=|>=|>
    //
    //
    public Type type=null;
    public ExprNode(){

    }
    public void accept(ASTVisitor v){
        v.visit(this);
    }
}
