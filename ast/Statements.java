package intercode.ast;

import intercode.visitor.ASTVisitor;

import javax.swing.plaf.nimbus.State;

public class Statements extends Node {


    public StatementNode  stmt;
    public Statements stmts;
    //public Statements block;
    public Statements () {

    }

    public Statements (StatementNode stmt, Statements stmts) {

        this.stmt = stmt ;
        this.stmts = stmts ;
    }

    public void accept(ASTVisitor v) {

       // v.visit(this);
    }


}
