package intercode.inter;

import intercode.ast.IdentifierNode;
import intercode.lexer.Tag;
import intercode.lexer.Type;
import intercode.lexer.Word;
import intercode.visitor.ASTVisitor;

public class TempNode extends IdentifierNode {

    public static int num=0;
    public TempNode(){

    }
    public static IdentifierNode newTemp(){
        num++;
        return new IdentifierNode(new Word("t"+num, Tag.ID),null);
    }

   public void accept(ASTVisitor v){
        v.visit(this);
   }

}
