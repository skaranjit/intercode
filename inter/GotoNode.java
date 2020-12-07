package intercode.inter;
import intercode.ast.*;
import intercode.visitor.*;
import java.util.*;
public class GotoNode extends StatementNode {

    public LabelNode gotoLabel = null;
    
    public GotoNode(LabelNode label, StatementNode stmts)
    {
        this.gotoLabel = label;
        this.stmt = stmts;
    }
    public GotoNode(){ 
    }
    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }
}
