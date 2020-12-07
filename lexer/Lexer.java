package intercode.lexer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

public class Lexer {
    public int line = 1;
    private char peek = ' ';

    private FileInputStream inputStream;
    private BufferedInputStream bin;
    Hashtable<String, Word> words = new Hashtable<String, Word>();
    // private Hashtable words = new Hashtable() ;

    public Lexer() {



        reserve(new Word("if",Tag.IF));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("break", Tag.BREAK));

        reserve(Word.True);
        reserve(Word.False);
        reserve(Type.Int);
        reserve(Type.Char);
        reserve(Type.Bool);
        reserve(Type.Float);
        reserve(Word.eof);

        setupIOStream();
    }

    void reserve(Word w) {
//System.out.println("a+");
        words.put(w.lexeme, w);
    }

    void setupIOStream() {
        try {

            inputStream = new FileInputStream("input.txt");
            bin = new BufferedInputStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    void readch() throws IOException {
        peek = (char) bin.read();
    }
// to return boolean
    boolean readch(char c) throws IOException{
     readch();
        if(peek!=c)return false;
        peek=' ';


        return true;
    }


    public Token scan() throws IOException {

        // System.out.println("scan() in Lexer") ;

        for (; ; readch()) {

            if (peek == ' ' || peek == '\t')
                continue;
            else if (peek == '\n')
                line = line + 1;
            else
                break;
        }

        switch (peek){


            case '&':
                if(readch('&'))return Word.and;
                else return new Token('&');

            case '|':
                if(readch('|'))return Word.or;
                else return new Token('|');
            case'=':
                if(readch('='))return Word.eq;
                else return new Token('=');
            case'!':
                if(readch('='))return Word.ne;
                else return new Token('!');
            case '<':
                if(readch('='))return Word.le;
                else return new Token('<');
            case '>':
                if(readch('='))return Word.and;
                else return new Token('>');
            default:
                //System.out.println(" in peek");
                break;
        }

        //System.out.println(" Out of Switch "+peek);
        if (Character.isDigit(peek)) {
            //System.out.println("In Digit");
            int v = 0;

            do {

                v = 10 * v + Character.digit(peek, 10);
                readch();

            } while (Character.isDigit(peek));

           if(peek!='.') return new Num(v);
           float x=v;float d=10;
           for(;;){
               readch();
               if(!Character.isDigit(peek))break;
               x=x+Character.digit(peek,10)/d;d=d*10;
           }
           return new Real(x);
        }

        if (Character.isLetter(peek)) {
            //System.out.println(" In letter");
            StringBuffer b = new StringBuffer();

            do {
                //System.out.println(peek);
                b.append(peek);
                //System.out.println(peek);
                readch();
            } while (Character.isLetterOrDigit(peek));

            String s = b.toString();
            //System.out.println("s: " + s) ;
            Word w = (Word)words.get(s);

            if (w != null)
                return w;

            w = new Word(s, Tag.ID);
            words.put(s, w);

           // System.out.println("w: " + w.toString());

            return w;
        }
       
        if((int)peek==65535){
            System.out.println("@@@@@@@@@@@@ EOF reached");
            return Word.eof;
        }


// if none condition is met
        //System.out.println(" at last");
        Token t = new Token(peek);
        //System.out.println("t: "+t.toString());
        peek = ' ';

        return t;
    }
}
