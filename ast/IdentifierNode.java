package intercode.ast;

import intercode.lexer.Type;
import intercode.lexer.Word;
import intercode.visitor.ASTVisitor;

public class IdentifierNode extends ExprNode {
    public String id;
    //public Statements block;
    public Word w;

    public IdentifierNode(Word w, Type type) {
        this.w=w;
        this.type=type;
        this.id = w.lexeme;
    }

    public IdentifierNode (Word w) {

        this.id = w.lexeme ;
        this.w = w;
    }

    public IdentifierNode() {
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
    public void printNode(){
        System.out.println(" "+id.toString());
    }
}
