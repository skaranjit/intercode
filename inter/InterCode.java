package intercode.inter;

import intercode.ast.*;
import intercode.parser.Parser;
import intercode.typechecker.TypeChecker;
import intercode.visitor.ASTVisitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class InterCode extends ASTVisitor {

    public TypeChecker checker= null;
   
    //public Parser parser;
    public CompilationUnit cu= null;
    //List of Assignments for the Binary Expressions.
    public List<AssignmentNode> BinassignList = new ArrayList<AssignmentNode>();
    ExprNode lhs = null;
    ExprNode last = null;
    ExprNode temp1 = null;
    //For Break Statement Node:
    private LabelNode globalLabel;
    int level=0;
   // String indent="...";


   public InterCode(TypeChecker checker)
    {
            this.checker = checker;
            this.cu = checker.cu;
            visit(cu);
    }
    public InterCode()
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

    public void visit (BlockStatement n)
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
        //BinassignList = new ArrayList<AssignmentNode>();
        IdentifierNode temp= TempNode.newTemp();
        ParenthesesNode cond=n.cond;
        ExprNode expr= null;
        if(cond.expr instanceof BinExprNode){
            expr=(BinExprNode)cond.expr;
            expr = (BinExprNode)cond.expr;

        }else if(cond.expr instanceof TrueNode){
            expr=(TrueNode)cond.expr;
        }else if(cond.expr instanceof FalseNode){
            expr=(FalseNode)cond.expr;
        }

        AssignmentNode assign= new AssignmentNode(temp,expr);
        for (AssignmentNode a : BinassignList){
            n.assigns.add(a);
        }
        n.assigns.add(assign);

        // replace n.cond with temp
        n.cond.expr= temp;

        // create truelabel and falselabel
        n.falseLabel=LabelNode.newLabel();
        //n.stmt.accept(this);
        n.toGoto = new GotoNode(n.falseLabel,n.stmt);
        n.toGoto.accept(this);
        if(n.else_stmt!=null){
            println(" Else Clause");
            n.else_stmt.accept(this);
        }

    }
    
    public void visit(WhileStatementNode n){
        printIndent();
        println("While Statement");
        //BinassignList = new ArrayList<AssignmentNode>();
        n.startLabel = LabelNode.newLabel();
        n.cond.accept(this);
        IdentifierNode temp = TempNode.newTemp();
        ParenthesesNode cond=n.cond;
        ExprNode expr = null;
        if(cond.expr instanceof BinExprNode){
            expr = (BinExprNode)cond.expr;
    // 		//((BinExprNode)expr).accept(this);
            expr = BinassignList.get(BinassignList.size()-1).id;
        } else if(cond.expr instanceof TrueNode){
            expr=(TrueNode)cond.expr;
        }else if(cond.expr instanceof FalseNode){
            expr=(FalseNode)cond.expr;
        }
        AssignmentNode assign = new AssignmentNode(temp, expr);
        for(AssignmentNode assign1 : BinassignList){
            n.assigns.add(assign1);
        }
        n.assigns.add(assign);
        n.cond.expr= temp;
        n.falseLabel = LabelNode.newLabel();
	    globalLabel = n.falseLabel;
        //n.stmt.accept(this);
        n.toGoto = new GotoNode(n.falseLabel, n.stmt);
	    n.toGoto.accept(this);
       // n.stmt.accept(this);
    }

    public void visit(DoWhileStatementNode n){
        printIndent();
        println("do");
        indentUp();
        //BinassignList = new ArrayList<AssignmentNode>();
        n.startLabel = LabelNode.newLabel();
        n.toGoto = new GotoNode(n.startLabel, n.stmt);
        n.toGoto.accept(this);
        println(n.startLabel.id + ": ");
        print("iffalse ");
        //n.stmt.accept(this);
        indentDown();
        printIndent();
        n.cond.accept(this);
        IdentifierNode temp = TempNode.newTemp();
        ParenthesesNode cond=n.cond;
        ExprNode expr = null;
        if(cond.expr instanceof BinExprNode){
            expr = (BinExprNode)cond.expr;
    // 		//((BinExprNode)expr).accept(this);
            expr = BinassignList.get(BinassignList.size()-1).id;
        } else if(cond.expr instanceof TrueNode){
            expr=(TrueNode)cond.expr;
        }else if(cond.expr instanceof FalseNode){
            expr=(FalseNode)cond.expr;
        }
	AssignmentNode assign = new AssignmentNode(temp, expr);
        for(AssignmentNode assign1 : BinassignList){
            n.assigns.add(assign1);
        }
        n.assigns.add(assign);
        n.cond.expr= temp;

        n.falseLabel = LabelNode.newLabel();
        println(" goto " + n.falseLabel.id);
        println("goto "+n.startLabel.id);
        print(";");
        println("");
    }

    public void visit(ArrayAccessNode n){
        //n.id.accept(this);
        n.index.accept(this);
    }
    public void visit(ArrayDimsNode n){
        print("[");
        n.size.accept(this);
        IdentifierNode temp = TempNode.newTemp();
        ExprNode expr = null;
        if(n.size instanceof BinExprNode){
            expr = (BinExprNode)n.size;
    // 		//((BinExprNode)expr).accept(this);
            expr = BinassignList.get(BinassignList.size()-1).id;  
        }
        else if(n.size instanceof IdentifierNode){
            expr = ((IdentifierNode)n.size);
        }else if(n.size instanceof NumNode){
            expr= (NumNode)n.size;
        }      
        print("]");
        if(n.dim!= null){
            n.dim.accept(this);
        }
    }


    public void visit(AssignmentNode n){
        printIndent();
        n.id.accept(this);
        List<AssignmentNode> temp1 = new ArrayList<AssignmentNode>();
	    temp1 = BinassignList;
	    //BinassignList = new ArrayList<AssignmentNode>();
        print(" = ");
        n.right.accept(this);
        // if(n.right instanceof ParenthesesNode){
        //     ((ParenthesesNode)n.right).accept(this);
        // }
        // if(n.right instanceof IdentifierNode){
        //     ((IdentifierNode)n.right).accept(this);
        // }
        // else if (n.right instanceof NumNode){
        //     ((NumNode)n.right).accept(this);
        // }else if(n.right instanceof RealNode){
        //     ((RealNode)n.right).accept(this);
        // }else if(n.right instanceof ArrayAccessNode){
        //     ((ArrayAccessNode)n.right).accept(this);
        // }else if(n.right instanceof BinExprNode){
        //     ((BinExprNode)n.right).accept(this);
        // }


        println(";");
        BinassignList = temp1;
    }

    public void visit(BreakStatementNode n){
        printIndent();
        n.falseLabel = globalLabel;
        println(" goto " + n.falseLabel);
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
        if(n.left != null){
            IdentifierNode temp = TempNode.newTemp();
            if(!BinassignList.isEmpty()) n.id =(IdentifierNode)lhs;
            temp1 = new BinExprNode(n.op,n.id,n.right);
            AssignmentNode assign = new AssignmentNode(temp, temp1);
            lhs = temp;
            BinassignList.add(assign);
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

    //////// Visit method for Intermediate code
    ////

    
    public void visit(LabelNode n){

    }

    public void visit(TempNode n){

    }
    public void visit(GotoNode n){
        n.stmt.accept(this);
    }





}
