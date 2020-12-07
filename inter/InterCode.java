package intercode.inter;

import intercode.ast.*;
import intercode.parser.Parser;
import intercode.typechecker.TypeChecker;
import intercode.visitor.ASTVisitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class InterCode extends ASTVisitor {

    public TypeChecker checker= null;
   
    //public Parser parser;
    public CompilationUnit cu= null;
    //List of Assignments for the Binary Expressions.
    public List<AssignmentNode> BinassignList = new ArrayList<AssignmentNode>();

    int level=0;
   // String indent="...";


   public InterCodeGen(TypeChecker checker)
    {
            this.checker = checker;
            this.cu = checker.cu;
            visit(cu);
    }
    public InterCodeGen()
    {
            visit(this.checker.cu);
    }

 //////////////////////////////////////////////////////////////////////////////////////////////////
  ////                                                                                          ////
  ////                                    UTILITY METHODS                                      //// 
  ////                                                                                         ////
  /////////////////////////////////////////////////////////////////////////////////////////////////
    void error(String s)
    {
        println(s);
        exit(1);
    }

    void exit(int n)
    {
        System.exit(n);
    }

    void print(String s)
    {
        System.out.print(s);
    }

    void println(String s)
    {
        System.out.println(s);
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
    public void visit (CompilationUnit n)
    {
        println("Intercode Generator starts");

        n.block.accept(this);
        println("*************End of the InterCode Generator*************");
    }

    public void visit (BlockStatementNode n)
    {
	    
	for(DeclarationNode decl : n.decls)
	     decl.accept(this);
	for(StatementNode stmt : n.stmts){
		BinassignList = new ArrayList<AssignmentNode>();
	    stmt.accept(this);
	}
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

        System.out.println(" If Statementnode");
        n.cond.accept(this);
        IdentifierNode temp= TempNode.newTemp();
        ParenthesesNode cond=n.cond;
        ExprNode expr= null;


        if(cond.expr instanceof BinExprNode){
            expr=(BinExprNode)cond.expr;
        }else if(cond.expr instanceof TrueNode){
            expr=(TrueNode)cond.expr;
        }else if(cond.expr instanceof FalseNode){
            expr=(FalseNode)cond.expr;
        }

        AssignmentNode assign= new AssignmentNode(temp,expr);
        n.assigns.add(assign);

        // replace n.cond with temp
        n.cond.expr= temp;

        // create truelabel and falselabel
        n.falseLabel=LabelNode.newLabel();


        n.stmt.accept(this);

        if(n.else_stmt!=null){
            println(" Else Clause");
            n.else_stmt.accept(this);
        }

    }

    public void visit(WhileStatementNode n){
        printIndent();
        print("while (");
        n.cond.accept(this);
        print(")");
        indentUp();
        n.stmt.accept(this);
        indentDown();
    }
    public void visit(DoWhileStatementNode n){
        printIndent();
        println("do");
        indentUp();
        n.stmt.accept(this);
        indentDown();
        printIndent();
        print("while (");
        n.cond.accept(this);
        println(");");
    }

    public void visit(ArrayAccessNode n){
        n.id.accept(this);
        n.index.accept(this);
    }
    public void visit(ArrayDimsNode n){
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
        }else if(n.right instanceof ArrayAccessNode){
            ((ArrayAccessNode)n.right).accept(this);
        }else if(n.right instanceof BinExprNode){
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
        }else if(n.left instanceof ArrayAccessNode){
            ((ArrayAccessNode)n.left).accept(this);
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
            }else if(n.right instanceof ArrayAccessNode){
                ((ArrayAccessNode)n.right).accept(this);
            }else if(n.right instanceof BinExprNode){
                ((BinExprNode)n.right).accept(this);
            }else {

            }



        }



    }

    public void visit(IdentifierNode n){
        print(n.id);
    }
    public void visit(NumNode n){
        print(""+n.value);
    }
    public void visit(RealNode n){
        print(" "+n.value);
    }

    //////// Visit method for Intermediate code
    ////

    public void visit(GotoNode n){

    }
    public void visit(LabelNode n){

    }

    public void visit(TempNode n){

    }




}
