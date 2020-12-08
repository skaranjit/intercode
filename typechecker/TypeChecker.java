package intercode.typechecker;

import intercode.ast.*;
import intercode.lexer.*;
import intercode.parser.*;
import intercode.visitor.ASTVisitor;

import static java.lang.System.exit;

public class TypeChecker extends ASTVisitor {
    public Parser parser= null;
    public CompilationUnit cu= null;
    public Env top;
    public boolean whileTrue = false;
    int level=0;
    String indent="...";
    public TypeChecker(Parser parser){
        this.parser=parser;
        cu=parser.cu;
        visit(cu);
    }
    public TypeChecker(){
        visit(this.parser.cu);
    }

    //////////////////////////////////

    void error(String s) {

       println(" Syntax Error: " + s);
        exit(1);
    }
    void exit(int n){
        System.exit(n);
    }
    void println(String s){
        System.out.println(s);
    }
    void printSpace(){
        System.out.print("");
    }

    /////////////////////////////////



    public void visit(CompilationUnit n){
        println("*********************");
        System.out.println(" ** TypeChecker Starts ***");
        println(" **********************");
        System.out.println();
        System.out.println(" Compilation Unit");
        n.block.accept(this);
        println(" **********************");
        println("*********TypeChecker Passed***********");
        println(" **********************");
    }
    public void visit(BlockStatement  n){
        println(" Block Statement");
        for(DeclarationNode decl: n.decls)
            decl.accept(this);
        for(StatementNode stmt: n.stmts)
            stmt.accept(this);
        
    }
   /* public void visit(Declarations n){
        if(n.decls!=null){
            n.decl.accept(this);
            n.decls.accept(this);
        }
    }*/
    public void visit(DeclarationNode n){
        System.out.println(" Declaration Node");
        n.type.accept(this);
        n.id.accept(this);
    }
    public void visit(TypeNode n){
        System.out.println(" TypeNode: "+n.basic);
       if( n.array!=null){
           n.array.accept(this);
       }
    }
    public void visit(ArrayTypeNode n){
        System.out.println(" ArrayTypeNode: "+n.size);
        if(n.type!= null){
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

        System.out.println(" Parenthesis Node");
         n.expr.accept(this);
    }
    public void visit(IfStatementNode n){
        println(" IFStatement Node");
        n.cond.accept(this);
        n.stmt.accept(this);
        if(n.else_stmt!=null){
            println(" ELSE CLAUSE");
            n.else_stmt.accept(this);
        }
    }

    public void visit(WhileStatementNode n){
        whileTrue = true;
        println(" While Statement Node");
        n.cond.accept(this);
        n.stmt.accept(this);
        whileTrue = false;
    }
    public void visit(DoWhileStatementNode n){
        boolean tmp = whileTrue;
        System.out.println(" DOWhile Statement Node");
        whileTrue = true;
        n.stmt.accept(this);
        whileTrue = false;
        n.cond.accept(this);
        whileTrue = tmp;
    }
    public void visit(ArrayAccessNode n){
        println(" ArrayAccessNode");
        //n.id.accept(this);
        n.index.accept(this);
    }
    public void visit (ArrayDimsNode n){
        println(" ArrayDimsnOde");
        if(n.size instanceof IdentifierNode){
            if(top.get(((IdentifierNode)n.size).w) != null){
                n.size = (IdentifierNode)top.get(((IdentifierNode)n.size).w);
               
            }
            if (n.size.type != Type.Int) error("Needs to be integer only");
            ((IdentifierNode)n.size).accept(this);
        }
        else if (n.size instanceof NumNode){
            ((NumNode)n.size).accept(this);
        } 
        else{
            error("Can only be integer.");
        }
        if(n.dim!=null){
            n.dim.accept(this);
        }
    }
    public void visit(BreakStatementNode n){
        if(!whileTrue) error("Break node called outside the while or do-while loop"); 
        System.out.println(" BreakStatementNode");

    }
    public void visit(TrueNode n){
        System.out.println(" TrueNode");

    }
    public void visit(FalseNode n){
        System.out.println(" FalseNode");

    }

    public void visit(AssignmentNode n){
        println(" AssignmentNode");
        
        n.id.accept(this);

        IdentifierNode leftId=(IdentifierNode)n.id;
        Type leftType = leftId.type;
     
        

        println(" In Typechecker Assignment's left type is "+leftType);
        Type rightType=null;   
        if(n.right instanceof IdentifierNode){
            ((IdentifierNode)n.right).accept(this);
        }else if(n.right instanceof NumNode){
            ((NumNode)n.right).accept(this);
            rightType = Type.Int;
        }else if(n.right instanceof RealNode){
            ((RealNode)n.right).accept(this);
        }else if(n.right instanceof ArrayAccessNode){
            ((ArrayAccessNode)n.right).accept(this);
        }else if(n.right instanceof ParenthesesNode){
            ((ParenthesesNode)n.right).accept(this);
        }else{
            ((BinExprNode)n.right).accept(this);
            rightType=((BinExprNode)n.right).type;
        }
        rightType = (n.right).type;

        if(leftType==Type.Int){
            println(" left type is int");
        }
        if(leftType== Type.Float && rightType==Type.Int){
            error(" the rhs of assignment is incompatible to left hand side"+leftId.id);
        }
    }

    public void visit(BinExprNode n){
        println(" BinExprNode: "+n.op);
        Type leftType= null;
        ExprNode leftId=null;
        if(n.left instanceof IdentifierNode){
            ((IdentifierNode)n.left).accept(this);
            leftType = n.left.type;
        }else if(n.left instanceof NumNode){
            ((NumNode)n.left).accept(this);
            n.left=(NumNode)n.left;
        }else if(n.left instanceof RealNode){
            ((RealNode)n.left).accept(this);
            n.left=(RealNode)n.left;
        }else if(n.left instanceof ArrayAccessNode){
            ((ArrayAccessNode)n.left).accept(this);
        }else if(n.left instanceof ParenthesesNode){
            ((ParenthesesNode)n.left).accept(this);
        }else{
            ((BinExprNode)n.left).accept(this);
        }

        ExprNode rightId = null;
        Type rightType =null;
        if(n.right!=null){

            if(n.right instanceof IdentifierNode){
                ((IdentifierNode)n.right).accept(this);
                rightId = (IdentifierNode) n.right;
                rightType = rightId.type;
            }else if(n.right instanceof NumNode){
                ((NumNode)n.right).accept(this);
            }else if(n.right instanceof RealNode){
                ((RealNode)n.right).accept(this);
            }else if(n.right instanceof ArrayAccessNode){
                ((ArrayAccessNode)n.right).accept(this);
            }else if(n.right instanceof ParenthesesNode){
                ((ParenthesesNode)n.right).accept(this);
            }else{
                ((BinExprNode)n.right).accept(this);
            }
        }else{
            System.out.println(" n.right==null in BinExprNode"+n.right);
        }
        if(leftType == Type.Float || rightType==Type.Float) {
            n.type = Type.Float;
        }
        else
            n.type = Type.Int;


    }

    public void visit(IdentifierNode n){
        System.out.println("visiting IdentifierNode: "+n.w+" of type: "+n.type);

        if(n.type == null){ error("Variable: " + n.id + " not declared." );} 
        println(n.id);
        if(n.ArrDims != null)
            ((ArrayAccessNode)n.ArrDims).accept(this); 
    }
    public void visit(NumNode n){
        n.printNode();
    }
    public void visit(RealNode n){
        n.printNode();
    }
}
