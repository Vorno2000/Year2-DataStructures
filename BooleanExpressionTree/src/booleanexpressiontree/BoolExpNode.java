package booleanexpressiontree;

public abstract class BoolExpNode {
    protected char symbol;
    
    public BoolExpNode leftChild;
    public BoolExpNode rightChild;
    
    public BoolExpNode(char symbol) {
        symbol = Character.toUpperCase(symbol);
        if(symbol == '!' || symbol == '&' || symbol == '^' || symbol == '|' || symbol == 'T' || symbol == 'F')
            this.symbol = symbol;
        else
            throw new IllegalArgumentException(symbol+" is not a Valid Symbol!");
    }
    
    public boolean evaluate() {
        if(symbol == 'T' || symbol == 'F')
            return symbol == 'T';
        else {
            switch(this.symbol) {
                case('!'):
                    return !rightChild.evaluate();
                case('&'):
                    return leftChild.evaluate() & rightChild.evaluate();
                case('^'):
                    return leftChild.evaluate() ^ rightChild.evaluate();
                case('|'):
                    return leftChild.evaluate() | rightChild.evaluate();
                default:
                    throw new IllegalArgumentException();
            }
        }
            
    }
    
    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}
