package intercode.ast;

import intercode.lexer.Token;
import intercode.visitor.ASTVisitor;
import java.util.*;
import intercode.inter.*;


public class BinExprNode extends ExprNode {
    public IdentifierNode id;
    public ExprNode left;
    public ExprNode right;
   // public Statements st;
    public Token op;

    public BinExprNode(){

    }

    public BinExprNode(Token op,ExprNode a,ExprNode b){
       this.op= op;
       this.left = a;
       this.right = b;
    }
    public BinExprNode(IdentifierNode id){
        this.id= id;
    }
    public void accept(ASTVisitor v){
        v.visit(this);
    }

}
