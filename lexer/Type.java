package intercode.lexer;


public class Type extends Word {

    public int width=0;


    public static final Type Int= new Type("int", Tag.BASICS,4);
    public static final Type Float= new Type("float", Tag.BASICS,8);
    public static final Type Char= new Type("char", Tag.BASICS,1);
    public static final Type Bool= new Type("bool", Tag.BASICS,1);

    // w used for storage size
    public Type(String s, int tag, int w) {
        super(s, tag);
        width=w;
    }

    public static boolean numeric(Type p){
        if(p==Type.Char || p==Type.Int || p== Type.Float){
            return  true;
        }else{
            return  false;
        }
    }
    public static Type max(Type p1, Type p2){
        if(!numeric(p1) || !numeric(p2)){
            return null;
        }else if(p1== Type.Float||p2==Type.Float){
            return Type.Float;
        }else if(p1==Type.Int || p2== Type.Int){
            return  Type.Int;
        }else{
            return Type.Char;
        }
    }




}
