package intercode.parser;

import intercode.ast.*;
import intercode.lexer.*;
import intercode.visitor.ASTVisitor;


import java.io.IOException;

public class Parser extends ASTVisitor {
    public CompilationUnit cu = null;
    public Lexer lexer = null;
    public Token look = null;

    int level=0;
    String indent="....";

    public Env top=null; // current or top level symbol table
    public BlockStatement enclosingBlock=null; // current BlockStatement
        Word w= null;
        Type type=null;
    public Parser(Lexer lexer) {

        this.lexer = lexer;
        cu = new CompilationUnit();
        move();

        visit(cu);
    }

    public Parser() {

        cu = new CompilationUnit();
        move();

        visit(cu);
    }

    ////////////////////////////////////////
    //  Utility mothods
    ////////////////////////////////////////

    void move() {

        try {
       //         System.out.println(" Gone to lexer");
            look = lexer.scan();

        } catch (IOException e) {

            System.out.println("IOException");
        }
    }


    void error(String s) {

        System.out.println("near line " + lexer.line + ": " + s);
        exit(1);
    }

    void match(int t) {

        try {

            if (look.tag == t) {
     //           System.out.println(" Matched");
                move();
            }else if(look.tag==Tag.EOF){
                error(" Syntax error : \";\"or \"}\" expected");
            }else{
                error(" Syntax error: \""+(char)t+"\"expected");
            }
        } catch (Error e) {


        }
    }
    void exit(int n){
        System.exit(n);
    }
    private boolean opt(int...tags){
        for(int tag:tags)
            if(look.tag==tag)
                return true;
        return false;
    }

    ////////////////////////////////////////

    public void visit(CompilationUnit n) {

        System.out.println(" Compilation Unit");
        n.block = new BlockStatement(null);
        n.block.accept(this);
    }


    public void visit(BlockStatement n) {
        System.out.println(" BlockStatement");
       match('{');
       // set top to the current level symbol table
       n.sTable=top;
       top=new Env(top);
       // set enclosingBlock to the current level BlockStatement
       enclosingBlock=n;

       //n.decls= new Declarations();
       //n.decls.accept(this);

        level++;
        while(opt(Tag.BASICS)){
            DeclarationNode decl= new DeclarationNode();
            n.decls.add(decl);
            decl.accept(this);
        }
        level--;
        level++;
        while(opt(Tag.ID,Tag.IF,Tag.WHILE,Tag.DO,Tag.BREAK)){
            n.stmts.add(parseStatementNode());
        }
        level--;


        match('}');
        top=n.sTable;
        // reseting the enclosing block to the current level symbol table
        enclosingBlock=n.parent;

    }

    public void visit (DeclarationNode n){

       // for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" DeclarationNode"+look.toString());

        n.type= new TypeNode();
        //level++;
        n.type.accept(this);
        //level--;

        n.id = new IdentifierNode();
        n.id.type=n.type.basic;
        //level++;
        n.id.accept(this);
        top.put(n.id.w,n.id);
        match(';');
    }

    public void visit(TypeNode n){
        //for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" TypeNode");

        if(look.toString().equals("int")){ n.basic= Type.Int; }
        else if(look.toString().equals("float")){n.basic=Type.Float;}

        match(Tag.BASICS);
        System.out.println(look.toString()+" in Type");
        if(look.tag=='['){
            System.out.println(" Array found");
            n.array= new ArrayTypeNode();
          //  level++;
            n.array.accept(this);
            //level--;
        }
    }



    public void visit( ArrayTypeNode n){
      //  for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" ArrayTypeNode"+look);
        match('[');
	if(look.tag != Tag.NUM ){
		error("Only Int number");
	}
        n.size=((Num)look).value;
        //for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" ArrayTypeNode"+((Num)look).value);


        match(Tag.NUM);
        match(']');
        // checking multi-dimensional array
        if(look.tag=='['){
            n.type = new ArrayTypeNode();
          //  level++;
            n.type.accept(this);
            //level--;
        }
    }

    public StatementNode parseStatementNode(){
      //  for(int i=0;i<level;i++){System.out.println(indent);}
        System.out.println(" look in parseStatement is "+look.toString());
        StatementNode stmt= new StatementNode();
        switch (look.tag){
            case Tag.ID:
              stmt = new AssignmentNode();
                ((AssignmentNode)stmt).accept(this);
                return stmt;

            case Tag.IF:

                stmt= new IfStatementNode();
                ((IfStatementNode)stmt).accept(this);
                return  stmt;


            case Tag.WHILE:
                stmt= new WhileStatementNode();
                ((WhileStatementNode)stmt).accept(this);
                return stmt;
            case Tag.DO:
                stmt= new DoWhileStatementNode();
                ((DoWhileStatementNode)stmt).accept(this);
                return stmt;
            case Tag.BREAK:
                stmt= new BreakStatementNode();
                ((BreakStatementNode)stmt).accept(this);
                return stmt;
            case '{':
                stmt = new BlockStatement(enclosingBlock);
                ((BlockStatement)stmt).accept(this);
            default:return null;


        }

    }






    public void visit(WhileStatementNode n){
     //   for(int i=0;i<level;i++){System.out.println(indent);}
        System.out.println("WhileStatementNode");

        match(Tag.WHILE);
       // for(int i=0;i<level;i++){System.out.println(indent);}
        System.out.println("WHILE");

        n.cond= new ParenthesesNode();
        //level++;
        n.cond.accept(this);
        //level--;
            System.out.println("in While look is"+look.toString());
        if(look.tag=='{'){
            n.stmt= new BlockStatement(null);
          //  level++;
            n.stmt.accept(this);
           // level--;
        }else{
            n.stmt=parseStatementNode();
        }
    }

    public void visit(ParenthesesNode n)
    {
        System.out.println(" Parenthesis Node"+look.toString());
	    match('(');
	    ExprNode rhs_assign = null;
        if (look.tag == Tag.ID)
        {
            rhs_assign = new IdentifierNode();
            ((IdentifierNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.NUM)
        {
            rhs_assign = new NumNode();
            ((NumNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.REAL)
        {
            rhs_assign = new RealNode();
            ((RealNode)rhs_assign).accept(this);
        }
	    else if (look.tag == Tag.TRUE){
	        rhs_assign = new TrueNode();
	        ((TrueNode)rhs_assign).accept(this);
	    }
        else if (look.tag == '(')
        {
            rhs_assign = new ParenthesesNode();
            ((ParenthesesNode)rhs_assign).accept(this);
        }
        if (look.tag == ')')
        {
            n.expr = rhs_assign;
        }
        else{
            n.expr = (BinExprNode) parseBinExprNode(rhs_assign,0);
        }

        match(')');
        n.type = rhs_assign.type;
    }

    ExprNode parseArrayAccessNode(IdentifierNode id){
      //  for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" parse array access node");
        ExprNode index= new ArrayDimsNode();
        //level++;
        index.accept(this);
        //level--;
        return new ArrayAccessNode(id,index);

    }
    ExprNode parseBinExprNode(ExprNode lhs, int precedence){
	while (getPrecedence(look.tag) >= precedence)
        {
            System.out.println("InParseBinExprNode");
            Token token_op = look;
            int op = getPrecedence(look.tag);
            move();
            ExprNode rhs = null;
            if (look.tag == Tag.ID)
            {
                rhs = new IdentifierNode();
                ((IdentifierNode)rhs).accept(this);
            }
            else if (look.tag == Tag.NUM)
            {
                rhs = new NumNode();
                ((NumNode)rhs).accept(this);
            }
            else if (look.tag == Tag.REAL)
            {
                rhs = new RealNode();
                ((RealNode)rhs).accept(this);
            }
            else if (look.tag == '(')
            {
                rhs = new ParenthesesNode();
                ((ParenthesesNode)rhs).accept(this);
            }
            
        

            System.out.println(" operator"+look);

            while (getPrecedence(look.tag) > op)
            {
                rhs = parseBinExprNode(rhs, getPrecedence(look.tag));
            }
            lhs = new BinExprNode(token_op, lhs, rhs);
        }
        return lhs;
    }

    int getPrecedence(int op){
        switch(op){
            case '*':case '/':case '%': return 12; // multiplicative
            case '+': case'-':return 11;  // additive
            case '>':case '<': return 9;
            case Tag.LE: case Tag.GE: return 9;
            case Tag.EQ: case Tag.NE: return 8;
            default:
                return -1;
        }
    }
    public void visit(AssignmentNode n){
     //   for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" In Assignment Node");
        n.id = new IdentifierNode();
        (n.id).accept(this);
	// ExprNode lhs = null;
	// if(look.tag=='['){
		    
	//           lhs= parseArrayAccessNode(n.id);
    //     }
	System.out.println(look.toString());
	if(look.tag == '='){
		match('=');
		 System.out.println(" Operator =");

	}
        else error("Invalid Declarations");
        ExprNode rhs_assign = null;
        if (look.tag == Tag.ID)
        {
            rhs_assign = new IdentifierNode();
            ((IdentifierNode)rhs_assign).accept(this);
	    // if(look.tag=='['){
        //         //***PROBLEM***// ((IdentifierNode)n.expr)
        //         rhs_assign=parseArrayAccessNode((IdentifierNode)rhs_assign);
        //     }
        }
        else if (look.tag == Tag.NUM)
        {
            rhs_assign = new NumNode();
            ((NumNode)rhs_assign).accept(this);
        }
        else if (look.tag == Tag.REAL)
        {
            rhs_assign = new RealNode();
            ((RealNode)rhs_assign).accept(this);
        }
        else if (look.tag == '(')
        {
            rhs_assign = new ParenthesesNode();
            ((ParenthesesNode)rhs_assign).accept(this);
        }
        if (look.tag == ';')
        {
            n.right = rhs_assign;
        }
        else
            n.right = (BinExprNode) parseBinExprNode(rhs_assign, 0);
        match(';');
    }


    public void visit(BreakStatementNode n){
       // for (int i=0;i<level;i++){System.out.println();}
        System.out.println(" BreakStatement Node");

        match(Tag.BREAK);
        match(';');

    }




    public void visit(IfStatementNode n){


        System.out.println(" IF Statement Node");

        match(Tag.IF);

        n.cond= new ParenthesesNode();
        n.cond.accept(this);

        if(look.tag=='{'){
            n.stmt= new BlockStatement(null);
            n.stmt.accept(this);
        }else{
            n.stmt=parseStatementNode();
        }

        if(look.tag==Tag.ELSE){
            match(Tag.ELSE);
            if(look.tag=='{'){
                n.else_stmt= new BlockStatement(null);
                n.else_stmt.accept(this);
            }else{
                n.else_stmt=parseStatementNode();
            }
        }



    }





    public void visit(DoWhileStatementNode n){
       // for(int i=0;i<level;i++){System.out.println(indent);}
        System.out.println("DoWhileStatementNode");

        match(Tag.DO);
        //for(int i=0;i<level;i++){System.out.println(indent);}
        System.out.println("Do");

        if(look.tag== '{'){
          n.stmt= new BlockStatement(null);
            //level++;
            n.stmt.accept(this);
            //level--;
        }else{
            n.stmt=parseStatementNode();
        }

        match(Tag.WHILE);
        //for(int i=0;i<level;i++){System.out.println(indent);}
        System.out.println("Do's While");
        n.cond= new ParenthesesNode();
        //level++;
        n.cond.accept(this);
        //level--;
        match(';');
    }

    public void visit (BinExprNode n) {
        // (i[a]+j)
        //  for(int i=0; i<level;i++){ System.out.print(indent); }
//         System.out.println(" In BinExprNode");
// //
//        if (look.tag == '(') {
//            n.left = new ParenthesesNode();
//            //     level++;
//            ((ParenthesesNode) n.left).accept(this);
//            //   level--;
//        } else if (look.tag == Tag.ID) {
//            n.left = new IdentifierNode();
//            // level++;
//            ((IdentifierNode) n.left).accept(this);
//            //level--;

//         //    if (look.tag == '[') {
//         //        n.left = parseArrayAccessNode(((IdentifierNode) n.left));
//         //    }
//        } else if (look.tag == Tag.NUM) {
//            n.left = new NumNode();
//            //level++;
//            ((NumNode) n.left).accept(this);
//            //level--;
//        } else if (look.tag == Tag.REAL) {
//            n.left = new RealNode();
//            //level++;
//            ((RealNode) n.left).accept(this);
//            //level--;

//        }


//        //for(int i=0; i<level;i++){ System.out.print(indent); }
//        System.out.println(" &&&& BinExpr In operator" + look);
//        System.out.println(" &&&& BinExpr n.left" + n.left);

//         //level++;
//         // Build AST for binary expression with the operator precedence
//        BinExprNode binary = (BinExprNode) parseBinExprNode(n.left, 0);
//        n.op = binary.op;
//        n.right = binary.right;
//        // level--;*/
    }
    public void visit(ArrayAccessNode n){

    }

    public void visit(ArrayDimsNode n){
            System.out.println(" ArrayDimsNode");
            match('[');
            ExprNode index= null;
            if(look.tag=='('){
                index=new ParenthesesNode();
                //level++;
                ((ParenthesesNode)index).accept(this);
                //level--
            }else if(look.tag==Tag.ID){
                index=new IdentifierNode();
               // level++;
                //****PROBLEM***********
                ((IdentifierNode)index).accept(this);
                //level--;
            }else if(look.tag==Tag.NUM){
                index= new NumNode();
                //level++;
                ((NumNode)index).accept(this);
                //level--;
            }


            if(look.tag!=']'){
                level++; //*** Need to caste
                index= (ExprNode) parseBinExprNode(index,0);
                level--;
            }
            match(']');
            n.size=index;


            if(look.tag=='['){
                n.dim= new ArrayDimsNode();
            //    level++;
                n.dim.accept(this);
              //  level--;
            }

        }




    public void visit(TrueNode n){
       // for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" In TrueNode");
        if(look.tag!=Tag.TRUE){
            error(" Syntax error:\"true\"needed");
        }
        match(Tag.TRUE);
    }

    public void visit(FalseNode n){
       // for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" In FalseNode");
        if(look.tag!=Tag.FALSE){
            error(" Syntax error:\"false\"needed");
        }
        match(Tag.FALSE);
    }
    public void visit(NumNode n){
        n.value= ((Num)look).value;

        if(look.tag!=Tag.NUM){
            error(" Syntax error:integer number needed instead of"+n.value);
        }
        match(Tag.NUM);
        n.type=Type.Int;
       // for(int i=0; i<level;i++){ System.out.print(indent); }
        n.printNode();
        //System.out.println(" "+n.literal);
    }
    public void visit(RealNode n){
        n.value=((Real)look).value;
        if(look.tag!=Tag.REAL){
            error(" Syntax error: Real number needed instead of "+n.value);
        }
        match(Tag.REAL);
        n.type = Type.Float;
       // for(int i=0; i<level;i++){ System.out.print(indent); }
        n.printNode();
    }
    public void visit(IdentifierNode n){
        System.out.println(" In Identifier Node");
        n.id= look.toString();
        n.w=(Word)look;
        if((IdentifierNode)top.get(n.w) != null){
            n.type = top.get(n.w).type;
        }
        System.out.println(" ********n.type"+n.type);

        if(look.tag!=Tag.ID){
            error(" Syntax error: Identifier or variable needed "+n.id);
        }
         match(Tag.ID);
       
        if(look.tag=='['){

//             // had to parse
           n.ArrDims =  parseArrayAccessNode(n);
       }
       
        // for(int i=0; i<level;i++){ System.out.print(indent); }
        n.printNode();
    }

}

