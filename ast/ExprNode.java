package intercode.ast;

import intercode.lexer.Type;
import intercode.visitor.ASTVisitor;
import java.util.*;

public class ExprNode extends Node {
    //   expr--> || | && | ==| !=|<| <=|>=|>
    //
    //
    public Type type=null;
    public List<AssignmentNode> assigns = new ArrayList<AssignmentNode>();

    public ExprNode(){

    }
    public void accept(ASTVisitor v){
        v.visit(this);
    }
}
