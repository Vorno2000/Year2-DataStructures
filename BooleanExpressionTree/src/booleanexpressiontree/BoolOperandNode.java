package booleanexpressiontree;

public class BoolOperandNode extends BoolExpNode {
    public BoolOperandNode(char symbol) {
        super(symbol);
    }
    
    @Override
    public boolean evaluate() {
        return this.symbol == 'T';
    }
}
