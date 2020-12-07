package intercode.ast;

import intercode.visitor.ASTVisitor;

public class AssignmentNode extends StatementNode {
    public IdentifierNode id;
    public ExprNode right;
    //public Statements block;
    public AssignmentNode () {

    }

    public AssignmentNode (IdentifierNode identifierNode, ExprNode right) {

        this.id = identifierNode ;
        this.right = right ;

    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
}
