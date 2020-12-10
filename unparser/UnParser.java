package assign7.unparser;

import assign7.ast.*;
import assign7.parser.*;
import assign7.typechecker.TypeChecker;
import assign7.visitor.*;
import assign7.intercode.*;
import java.io.*;



public class Unparser extends ASTVisitor
{

    public File tempFile;
    public FileWriter tempFileWriter;
    public InterCodeGen inter;

    public Unparser(InterCodeGen inter)
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

        this.inter = inter;
        visit(this.inter.cu);

        try
        {
            tempFileWriter.close();
        }
        catch (IOException e)
        {

        }
    }

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

    public void visit(CompilationUnit n)
    {
        n.block.accept(this);
    }

    public void visit(BlockStatementNode n)
    {
        for(DeclarationNode decl: n.decls) 
	        decl.accept(this);
        
        for(StatementNode stmt : n.stmts){
            indentUp();
	        stmt.accept(this);
            indentDown();
        }
        
        printIndent();
        println("}");
    }

    public void visit(DeclarationNode n)
    {
            n.type.accept(this);
            print(" ");
            n.id.accept(this);

            println(" ;");
    }

    public void visit(TypeNode n)
    {
        printIndent();
	if (n.array != null)
        {
	    for (AssignmentNode a: n.array.assigns)
	    	a.accept(this);
            
        }
        print(n.basic.toString());

        if(n.array != null)
            n.array.accept(this);
    }

    public void visit(ArrayTypeNode n)
    {   
        
        print("[");
        (n.size).accept(this);
//         if(n.size instanceof IdentifierNode) print("" + ((IdentifierNode)n.size).w);
//         if(n.size instanceof NumNode) print("" + ((NumNode)n.size).value);
//         print("" +n.size);
        print("]");

        if(n.type != null)
            n.type.accept(this);
    }

    public void visit(StatementNode n)
    {
        n.stmt.accept(this);
    }

    public void visit(AssignmentNode n)
    {
    	
        printIndent();
         n.left.accept(this);
         print(" = ");
         if (n.right instanceof  IdentifierNode)
             ((IdentifierNode)n.right).accept(this);
         else if (n.right instanceof NumNode)
            ((NumNode)n.right).accept(this);
         else if (n.right instanceof RealNode)
	    ((RealNode)n.right).accept(this);
 	else if (n.right instanceof BooleanNode)
		((BooleanNode)n.right).accept(this);
        else {
             ((BinExprNode)n.right).accept(this);
	    
		
	}
	//else{ n.right.accept(this);}

        println(";");
    }

    public void visit(BinExprNode n)
    {
    	
        if (n.left instanceof IdentifierNode)
        {
            ((IdentifierNode)n.left).accept(this);
        }
        else if (n.left instanceof NumNode)
        {
            ((NumNode)n.left).accept(this);
        }
        else if (n.left instanceof RealNode)
        {
            ((RealNode)n.left).accept(this);
        }
        else if (n.left instanceof BooleanNode)
        {
            ((BooleanNode)n.left).accept(this);
        }
        else if (n.left instanceof ParenNode)
        {
            ((ParenNode)n.left).accept(this);
        } else if(n.left instanceof BinExprNode){
            ((BinExprNode)n.left).accept(this);
	}else {
	
	}

        if (n.op != null)
        {
            print(" " + n.op.toString() + " ");
        }

        if (n.right != null)
        {
            if (n.right instanceof IdentifierNode)
            {
                ((IdentifierNode) n.right).accept(this);
            }
            else if (n.right instanceof NumNode)
            {
                ((NumNode) n.right).accept(this);
            }
            else if (n.right instanceof RealNode)
            {
                ((RealNode) n.right).accept(this);
            }
            else if (n.right instanceof BooleanNode)
            {
                ((BooleanNode) n.right).accept(this);
            }
            else if (n.right instanceof ParenNode)
            {
                ((ParenNode) n.right).accept(this);
            } else if(n.right instanceof BinExprNode){
            	((BinExprNode)n.right).accept(this);
            }else{ }
        }
    }

    public void visit(IdentifierNode n)
    {
    	
        if (n.array != null)
        {
	     println("");
	    for (AssignmentNode a: n.array.assigns){
	    	printIndent();
	    	a.accept(this);
	    }
            
        }
	print(n.id);
	if (n.array != null)
        { 
	    n.array.accept(this); 
        }

    }

    public void visit(ArrayIDNode n)
    {
        print("[");
        n.node.accept(this);
        print("]");
        if (n.array != null)
        {
            n.array.accept(this);
        }
    }

    public void visit(NumNode n)
    {
        print("" + n.value);
    }

    public void visit(RealNode n)
    {
        print("" + n.value);
    }

    public void visit(BreakNode n)
    {
        println("Break: goto "+ n.bLabel.id);
    }

    public void visit(ConditionalNode n)
    {
    	for (AssignmentNode assign : n.assigns)
		assign.accept(this);
        printIndent();
        print("ifFalse ");
       n.condition.accept(this);
        println(" goto " + n.falseLabel.id);
        n.IfGoto.accept(this);
        
	println(n.falseLabel.id+":");
        // if (n.elseStmt != null)
        // {
        //     n.elseStmt.accept(this);
        // }
    }
    public void visit(WhileNode n)
    {
        //printIndent();
        for (AssignmentNode assign : n.assigns)
		assign.accept(this);
      //  printIndent();
        println(n.startLabel.id + ": WhileStatement");
        printIndent();
        print("iffalse ");
       n.condition.accept(this);
        println(" goto " + n.falseLabel.id);
        n.wGoto.accept(this);
        println("goto "+n.startLabel.id);
        println(n.falseLabel.id+":");

        indentUp();
        //n.stmt.accept(this);
        indentDown();
    }
    public void visit(GotoNode n){
        n.stmt.accept(this);
    }
    public void visit(DoWhileNode n)
    {
        for (AssignmentNode assign : n.assigns)
		    assign.accept(this);
      //  printIndent();
        println(n.startLabel.id + ": Do Statement");
       
        printIndent();
        n.dGoto.accept(this);
        printIndent();
        print("iffalse ");
        n.condition.accept(this);
        println(" goto " + n.falseLabel.id);
      
        println("goto "+n.startLabel.id);
        println(n.falseLabel.id+":");
       
    }
    public void visit(BooleanNode n)
    {
        print(n.bool.toString());
    }
    public void visit(ParenNode n)
    {
        print("(");
        n.node.accept(this);
        print(")");
    }
}