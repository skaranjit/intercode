package intercode.ast;

import intercode.parser.Env;
import intercode.visitor.ASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement extends StatementNode {
  //  public Declarations decls;
   // public Statements stmts;


    public List<DeclarationNode> decls = new ArrayList<DeclarationNode>();
    public List<StatementNode> stmts = new ArrayList<StatementNode>();
    public BlockStatement parent;


    public Env sTable;
    //public Statements block;
    public BlockStatement (BlockStatement parent) {
        this.decls=new ArrayList<DeclarationNode>();
        this.stmts=new ArrayList<StatementNode>();
        this.parent=parent;
    }

    public BlockStatement(List<DeclarationNode>decls,List<StatementNode>stmts,BlockStatement parent) {
        this.decls=decls;
        this.stmts = stmts ;
        this.parent=parent;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }


}

