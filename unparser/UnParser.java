package intercode.unparser;

import intercode.ast.*;
import intercode.inter.InterCode;
import intercode.lexer.*;
import intercode.parser.*;
import intercode.typechecker.*;
import intercode.visitor.*;
import intercode.inter.*;
import java.io.*;
import java.util.jar.Pack200;

public class UnParser extends ASTVisitor {

  //  public TypeChecker typechecker= null;
    public InterCode inter=null;
    public CompilationUnit cu= null;
    public File tempFile;
    public FileWriter tempFileWriter;
    public Parser parser;

    public UnParser(InterCode inter)
    {
        try
        {
            tempFile = new File("output.txt");
            tempFileWriter = new FileWriter(tempFile);
            System.out.println("\n** Output.txt created successfully! ** ");
        }
        catch (IOException e)
        {

        }

      //  this.typechecker = typechecker;
        //visit(this.typechecker.cu);

        this.inter=inter;
        cu=inter.cu;
        visit(cu);


        try
        {
            tempFileWriter.close();
        }
        catch (IOException e)
        {

        }
    }

    public UnParser (){
        visit(this.inter.cu);
    }







/// Utility Method
    ///

  void print(String s)
    {
        try
        {
            tempFileWriter.append(s);
        }
        catch (IOException e)
        {

        }
    }

    void println(String s)
    {
        try
        {
            tempFileWriter.append(s + "\n");
        }
        catch (IOException e)
        {

        }
    }

    void printSpace()
    {
        System.out.print(" ");
    }

    int indent = 0;

    void indentUp()
    {
        indent++;
    }

    void indentDown()
    {
        indent--;
    }

    void printIndent()
    {
        String s = "";
        for (int i = 0; i < indent; i++)
        {
            s += "   ";
        }
        print(s);
    }
////////////////////////////////////////////////////////////////////////////
///////             Utility Methods Ends                         ///////////
///////////////////////////////////////////////////////////////////////////
    public void visit(CompilationUnit n){
        n.block.accept(this);
    }

    public void visit(BlockStatement n){
        println("{");
        indentUp();
       //n.decls.accept(this);
        indentDown();

        println(" Blockstatement");
        for(DeclarationNode decl:n.decls)
            decl.accept(this);



        indentUp();
        //n.stmts.accept(this);
        for(StatementNode stmt: n.stmts)
            stmt.accept(this);
        indentDown();
        printIndent();
        indentDown();
        println("}");
    }


   /* public void visit(Declarations n){
        if(n.decls!=null){
            n.decl.accept(this);
            n.decls.accept(this);
        }
    }*/
    public void visit(DeclarationNode n){

        n.type.accept(this);
        print(" ");
        n.id.accept(this);
        println(";");


    }

    public void visit(TypeNode n){
        printIndent();
        print(n.basic.toString());
        if(n.array!=null){
            n.array.accept(this);
        }
    }
    public void visit(ArrayTypeNode n){
        print("[");
        print(""+n.size);
        print("]");

        if(n.type!=null){
            n.type.accept(this);
        }
    }

    /*public void visit(Statements n){
        if(n.stmts!=null){
            n.stmt.accept(this);
            n.stmts.accept(this);
        }
    }*/


    public void visit(ParenthesesNode n){
//         print("(");
        (n.expr).accept(this);
//         print(")");
    }

    public void visit(IfStatementNode n){

        for(AssignmentNode assign:n.assigns)
            assign.accept(this);

        printIndent();
        print("IfFalse ");
        n.cond.accept(this);

        println(" goto "+n.falseLabel.id);
        indentUp();
        n.toGoto.accept(this);
        indentDown();
        println(n.falseLabel.id+":");        
        if(n.else_stmt!=null) n.else_stmt.accept(this);

    }

    public void visit(WhileStatementNode n){
        printIndent();
        println(n.startLabel.id + ": WhileStatement");
        printIndent();
        for (AssignmentNode assign : n.assigns)
		    assign.accept(this);
        print("iffalse ");
        n.cond.accept(this);
        println(" goto " + n.falseLabel.id);
        indentUp();
        n.toGoto.accept(this);
        println("goto "+n.startLabel.id);
        indentDown();
        println(n.falseLabel.id+":");        indentUp();

    }
    public void visit(DoWhileStatementNode n){
         printIndent();
         
        println("visiting DO Statements");
        n.startLabel = LabelNode.newLabel();
        n.toGoto = new GotoNode(n.startLabel, n.stmt);
        n.toGoto.accept(this);
        for (AssignmentNode assign : n.assigns)
		    assign.accept(this);
        println(n.startLabel.id + ": DoWhile");
         indentDown();
         printIndent();
         print("iffalse ");
         n.cond.accept(this);
         println(" goto " + n.falseLabel.id);
      
         println("goto "+n.startLabel.id);
         println(n.falseLabel.id+":");
    }

    public void visit(ArrayAccessNode n){
        n.index.accept(this);
    }
    public void visit(ArrayDimsNode n){
            println("");
            indentUp();
            printIndent();
            for(AssignmentNode a : n.assigns)
                a.accept(this);
            indentDown();
            printIndent();
            print("[");
                n.size.accept(this);
            print("]");
            if(n.dim!= null){
                n.dim.accept(this);
            }
    }

    public void visit(AssignmentNode n){
        printIndent();
        n.id.accept(this);
        print(" = ");
        if(n.right instanceof ParenthesesNode){
            ((ParenthesesNode)n.right).accept(this);
        }
        if(n.right instanceof IdentifierNode){
            ((IdentifierNode)n.right).accept(this);
        }
        else if (n.right instanceof NumNode){
            ((NumNode)n.right).accept(this);
        }else if(n.right instanceof RealNode){
            ((RealNode)n.right).accept(this);
        }else if(n.right instanceof TrueNode){
            ((TrueNode)n.right).accept(this);
        }else if(n.right instanceof FalseNode){
            ((FalseNode)n.right).accept(this);
        }else{
            ((BinExprNode)n.right).accept(this);
        }    
        println(";");           
    }

    public void visit(BreakStatementNode n){
        printIndent();
        println("break;");
    }

    public void visit(TrueNode n){
        print("true");
    }

    public void visit(FalseNode n){
        print("false");
    }

    public void visit(BinExprNode n){
        if(n.left instanceof ParenthesesNode){
            ((ParenthesesNode)n.left).accept(this);
        }
        if(n.left instanceof IdentifierNode){
            ((IdentifierNode)n.left).accept(this);
        }
        else if (n.left instanceof NumNode){
            ((NumNode)n.left).accept(this);
        }else if(n.left instanceof RealNode){
            ((RealNode)n.left).accept(this);
        }else if(n.left instanceof BinExprNode){
            ((BinExprNode)n.left).accept(this);
        }else {

        }
        if(n.op!=null){
            print(" "+ n.op.toString()+ " ");
        }
        if(n.right!=null){
            if(n.right instanceof ParenthesesNode){
                ((ParenthesesNode)n.right).accept(this);
            }
            if(n.right instanceof IdentifierNode){
                ((IdentifierNode)n.right).accept(this);
            }
            else if (n.right instanceof NumNode){
                ((NumNode)n.right).accept(this);
            }else if(n.right instanceof RealNode){
                ((RealNode)n.right).accept(this);
            }else if(n.right instanceof BinExprNode){
                ((BinExprNode)n.right).accept(this);
            }else {

            }
        }
    }

    public void visit(IdentifierNode n){
        print(n.id);
        if(n.ArrDims != null)
            ((ArrayAccessNode)n.ArrDims).accept(this);
    }
    public void visit(NumNode n){
        print(""+n.value);
    }
    public void visit(RealNode n){
        print(" "+n.value);
    }
    public void visit(GotoNode n){
        n.stmt.accept(this);
    }

}
