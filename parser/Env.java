package intercode.parser;

import intercode.ast.IdentifierNode;
import intercode.lexer.Token;

import javax.net.ssl.SSLEngineResult;
import java.util.Hashtable;

public class Env {
    private Hashtable table;
    public Env prev; // point to parent symbol table

    public Env (Env n){
        table=new Hashtable();
        prev=n;
    }
    public void put(Token w, IdentifierNode i){
        table.put(w,i);
    } //

   public IdentifierNode get(Token w){
        for(Env e=this;e!=null;e=e.prev){
            IdentifierNode found=(IdentifierNode)(e.table.get(w));
            if(found!=null){
                return found;
            }

        }
       return null;
    }

}
