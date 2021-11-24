package booleanexpressiontree;

public class BoolOperatorNode extends BoolExpNode {
    
    public BoolOperatorNode(char symbol) {
        super(symbol);
    }
    
    @Override
    public boolean evaluate() {
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
