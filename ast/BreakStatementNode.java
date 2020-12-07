package intercode.ast;

import intercode.visitor.ASTVisitor;

public class BreakStatementNode extends StatementNode {

    public BreakStatementNode(){

    }
    public void accept(ASTVisitor v){
        v.visit(this);
    }

}
