package intercode.ast;

import intercode.visitor.ASTVisitor;

public class CompilationUnit extends Node {
    public BlockStatement block;
    //public Statements block;
    public CompilationUnit () {

    }

    public CompilationUnit (BlockStatement block) {

        this.block = block ;
    }

    public void accept(ASTVisitor v) {

        v.visit(this);
    }
}
