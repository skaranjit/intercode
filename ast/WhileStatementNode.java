package intercode.ast;

import intercode.visitor.ASTVisitor;
import intercode.inter.*;
import java.util.*;
public class WhileStatementNode extends StatementNode {


    public ParenthesesNode cond;
    public StatementNode stmt;
    
    public WhileStatementNode(){

    }

    public WhileStatementNode(ParenthesesNode cond, StatementNode stmt){
        this.cond=cond;
        this.stmt=stmt;
    }

    public void accept(ASTVisitor v){
        v.visit(this);
    }



}
