package intercode.ast;

import intercode.visitor.ASTVisitor;

public class DoWhileStatementNode extends StatementNode {

    public StatementNode stmt;
    public ParenthesesNode cond;

    public GotoNode toGoto;
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
