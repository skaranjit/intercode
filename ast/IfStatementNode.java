package intercode.ast;


import intercode.inter.LabelNode;
import intercode.visitor.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class IfStatementNode extends StatementNode {

    public ParenthesesNode cond;
    public StatementNode stmt;
    public StatementNode else_stmt;


    // AssignmentNode which will be added before IFStatement for intermediate code
    public List<AssignmentNode> assigns= new ArrayList<AssignmentNode>();

    // label for intermediate code
    public LabelNode falseLabel;

    public IfStatementNode(){}

    public IfStatementNode(ParenthesesNode cond, StatementNode stmt,StatementNode else_stmt){
        this.cond=cond;
        this.stmt=stmt;
        this.else_stmt=else_stmt;
    }

    public void accept(ASTVisitor v){
        v.visit(this);
    }

}
