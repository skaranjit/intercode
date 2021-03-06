package intercode.ast;

import intercode.visitor.ASTVisitor;
import intercode.inter.*;
public class DoWhileStatementNode extends StatementNode {

    public StatementNode stmt;
    public ParenthesesNode cond;

    public DoWhileStatementNode(){
    }
    public DoWhileStatementNode(StatementNode stmt,ParenthesesNode cond){
        this.cond=cond;
        this.stmt=stmt;
    }
    public void accept(ASTVisitor v){
        v.visit(this);
    }

}
