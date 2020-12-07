package intercode.unparser;

import intercode.ast.*;
import intercode.lexer.Num;
import intercode.lexer.Real;
import intercode.lexer.Tag;
import intercode.parser.Parser;
import intercode.visitor.ASTVisitor;
import jdk.nashorn.internal.ir.Assignment;

public class TreePrinter extends ASTVisitor {

    public Parser parser = null;
    int level= 0;
   // String indent= ".....";
    public TreePrinter(Parser parser){
        this.parser=parser;
        visit(this.parser.cu);

    }
    public TreePrinter(){
    visit(this.parser.cu);
    }


    //utility
    void print(String s){
        System.out.print(s);
    }
    void println(String s){
        System.out.println(s);
    }
    void printSpace(){
        System.out.print(" ");
    }

    int indent=0;
    void indentUp(){
        indent++;
    }
    void indentDown(){
        indent--;
    }
    void printIndent(){
        String s = "";
        for (int i =0;i<indent;i++){
            s+="  ";
        }
        print(s);
    }
    void printDotDotDot(){
        String s="";
        for(int i=0;i<indent;i++){
            s+="...";
            print(s);
        }
    }
//


    public void visit(CompilationUnit n){
        System.out.println(" Tree Printer Starts here");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(" Compilation Unit");
        indentUp();
        n.block.accept(this);
        indentDown();
    }

    public void visit(BlockStatement n){
        printDotDotDot();
        System.out.println(" Block Statement");
        indentUp();
       // n.decls.accept(this);

       for(DeclarationNode decl: n.decls){

           decl.accept(this);
       }




        indentDown();
        indentUp();
        //n.stmts.accept(this);

       for(StatementNode stmt: n.stmts){
            stmt.accept(this);}
        //StatementNode stmt= new StatementNode();

       /* stmt=n.stmts.get(0);
        if(stmt instanceof WhileStatementNode){
            ((WhileStatementNode)stmt).accept(this);
        }*/
        indentDown();
    }
    /*public void visit(Declarations n){
        if(n.decls!=null){
            n.decl.accept(this);
            n.decls.accept(this);
        }
    }*/

    public void visit(DeclarationNode n){
        println("Declaration Node");
        indentUp();
        n.type.accept(this);
        indentDown();
        indentUp();
        n.id.accept(this);
        indentDown();
    }
    public void visit(TypeNode n){
        printDotDotDot();
        println(" TypeNode");
        if(n.array!=null){
            indentUp();
            n.array.accept(this);
            indentDown();
        }
    }

    public void visit(ArrayTypeNode n){
        System.out.println(" ArryType node");

        if(n.type!=null){
            indentUp();
            n.type.accept(this);
            indentDown();
        }
    }

   /* public void visit(Statements n){
        if(n.stmts!=null){
       // n.stmt.assign.accept(this);
            n.stmts.accept(this);
        }
    }*/

    public void visit(ParenthesesNode n){
        printDotDotDot();
        System.out.println("ParenthesisNode");
        indentUp();
       n.expr.accept(this);
        indentDown();
    }

    public void visit(IfStatementNode n){
        printDotDotDot();
        println("IFStatement");
        indentUp();
        n.cond.accept(this);
        indentDown();

        indentUp();
        //n.stmt.assign.accept(this);
        indentDown();

        if(n.else_stmt!=null){
            printIndent();
          //  n.else_stmt.assign.accept(this);
            indentDown();
        }
    }
    public void visit(WhileStatementNode n){
        printDotDotDot();
        println(" WhileStatement");

        indentUp();
        n.cond.accept(this);
        indentDown();

        indentUp();
         n.stmt.accept(this);
         indentDown();
    }

    public void visit(DoWhileStatementNode n){
        printDotDotDot();
        System.out.println("Do While Statement");
        indentUp();
       // n.stmt.assign.accept(this);
        indentDown();

        indentUp();
        n.cond.accept(this);
        indentDown();


    }
    public void  visit(ArrayAccessNode n){
        printDotDotDot();
        println(" ArrayAccessNode");

        indentUp();
        n.id.accept(this);
        indentDown();

        indentUp();
        n.index.accept(this);
        indentDown();

    }

    public void visit(ArrayDimsNode n){
        printDotDotDot();
        System.out.println(" array dims node");
        indentUp();
        n.size.accept(this);
        indentDown();
        // checking if multi-dimensional array
        if(n.dim!=null){
            indentUp();
            n.dim.accept(this);
            indentDown();

        }
    }
    public void visit(BreakStatementNode n){
        printDotDotDot();
        System.out.println(" BreakStatement Node");
    }
    public void visit(TrueNode n){
        printDotDotDot();
        println(" TRUENODE");
    }
    public void visit(FalseNode n){
        printDotDotDot();
        println(" FalseNODE");
    }





    public void visit(AssignmentNode n){
        println(" Assignment node");
        n.id.accept(this);
        indentDown();

        print("=");
        indentUp();


        if(n.right instanceof IdentifierNode){
            ((IdentifierNode)n.right).accept(this);
        }else if(n.right instanceof NumNode){
            ((NumNode)n.right).accept(this);
        }else if(n.right instanceof RealNode){
            ((RealNode)n.right).accept(this);
        }else if(n.right instanceof ArrayAccessNode){
            ((ArrayAccessNode)n.right).accept(this);
        }
//        else{
//            ((BinExprNode)n.right).accept(this);
//        }

        indentUp();

    }

    public void visit(BinExprNode n){
        println(" BinExprNode");

        indentUp();



        if (n.left instanceof IdentifierNode){
            ((IdentifierNode)n.left).accept(this);
        }else if(n.left instanceof NumNode){
            ((NumNode)n.left).accept(this);
        }else if(n.left instanceof RealNode){
            ((RealNode)n.left).accept(this);
        }else if(n.left instanceof ArrayAccessNode){
            ((ArrayAccessNode)n.left).accept(this);
        }else{
            ((BinExprNode)n.left).accept(this);
        }



        if(n.right!=null){
            if(n.right instanceof IdentifierNode){
                ((IdentifierNode)n.right).accept(this);
            }else if(n.right instanceof NumNode){
                ((NumNode)n.right).accept(this);
            }else if(n.right instanceof RealNode){
                ((RealNode)n.right).accept(this);
            }else if(n.right instanceof ArrayAccessNode){
                ((ArrayAccessNode)n.right).accept(this);
            }else{
                ((BinExprNode)n.right).accept(this);
            }
        }else{}
    }


    public void visit(IdentifierNode n){
        printDotDotDot();
        n.printNode();
    }
    public void visit(NumNode n){
        printDotDotDot();
        n.printNode();
    }
    public void visit(RealNode n){
        printDotDotDot();
        n.printNode();
    }

}
