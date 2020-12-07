package intercode;

import intercode.inter.InterCode;
import intercode.lexer.Lexer;
import intercode.parser.Parser;
import intercode.typechecker.TypeChecker;
import intercode.unparser.TreePrinter;
import intercode.unparser.UnParser;

public class Main {

    public static void main(String[] args) {
	Lexer lexer=new Lexer();
	Parser parser= new Parser(lexer);
	TreePrinter tree= new TreePrinter(parser);
	TypeChecker checker= new TypeChecker(parser);
    InterCode inter= new InterCode(checker);
	UnParser unParser = new UnParser(inter);
    }
}
