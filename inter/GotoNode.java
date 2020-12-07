package intercode.inter;
import intercode.ast.*;
import java.util.*;
public class GotoNode {

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
