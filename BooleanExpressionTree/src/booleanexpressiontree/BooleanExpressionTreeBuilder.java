package booleanexpressiontree;

import java.util.ArrayDeque;
import java.util.Deque;

public class BooleanExpressionTreeBuilder {
    static Deque<BoolExpNode> stack = new ArrayDeque<>();
    
    public static BoolExpNode buildExpressionTree(char[] expression) {
        BoolOperatorNode operatorNode;
        for(int i = 0; i < expression.length; i++) {
            if(expression[i] == 'T' || expression[i] == 'F')
                stack.push(new BoolOperandNode(expression[i]));
            else {
                operatorNode = new BoolOperatorNode(expression[i]);
                if(expression[i] != '!') {
                    operatorNode.rightChild = stack.pop();
                    operatorNode.leftChild = stack.pop();
                    
                }
                else
                    operatorNode.rightChild = stack.pop();
                stack.push(operatorNode);
            }
        }
        return stack.pop();
    }
    
    public static String toInfixString(BoolExpNode node) { // confirmed with Adam Campbell that using this method would not result in penalties
        String infixString = "(";
        
        if(node.leftChild != null) {
            if(node.leftChild.symbol != 'T' && node.leftChild.symbol != 'F') {
                if(node.leftChild.symbol != '!') {
                    infixString += toInfixString(node.leftChild);
                    infixString += node.symbol;
                    
                    if(node.rightChild != null) {
                        if(node.rightChild.symbol != 'T' && node.rightChild.symbol != 'F') {
                            if(node.rightChild.symbol != '!')
                                infixString += toInfixString(node.rightChild);
                            else {
                                infixString += node.rightChild.symbol;
                                if(node.rightChild.rightChild.symbol != 'T' && node.rightChild.rightChild.symbol != 'F')
                                    infixString += toInfixString(node.rightChild.rightChild);
                                else
                                    infixString += node.rightChild.rightChild.symbol;
                            }
                        }
                        else {
                            infixString += node.rightChild.symbol;
                        }
                    }
                    infixString += ")";
                    return infixString;
                }
                else {
                    infixString += node.leftChild.symbol;
                    if(node.leftChild.rightChild.symbol != 'T' && node.leftChild.rightChild.symbol != 'F')
                        infixString += toInfixString(node.leftChild.rightChild);
                    else {
                        infixString += node.leftChild.rightChild.symbol;
                        infixString += node.symbol;
                    }
                        
                }
            } 
            else {
                infixString += node.leftChild.symbol;
                infixString += node.symbol;
                if(node.rightChild != null) {
                    if(node.rightChild.symbol != 'T' && node.rightChild.symbol != 'F') {
                        if(node.rightChild.symbol != '!') 
                            infixString += toInfixString(node.rightChild);
                        else {
                            infixString += node.rightChild.symbol;
                            infixString += node.rightChild.rightChild.symbol;
                        }
                    }
                    else {
                        infixString += node.rightChild.symbol;
                    }
                }
                infixString += ")";
                return infixString;
            }
        }
        if(node.rightChild != null) {
            if(node.rightChild.symbol != 'T' && node.rightChild.symbol != 'F') {
                if(node.rightChild.symbol != '!') 
                    infixString += toInfixString(node.rightChild);
                else {
                    infixString += node.rightChild.symbol;
                    infixString += node.rightChild.rightChild.symbol;
                }
            }
            else {
                infixString += node.rightChild.symbol;
            }
        }
        infixString += ")";
        return infixString;
    }
    
    public static int countNodes(BoolExpNode node) {
        int count = 1;
        
        if(node.leftChild != null) count += countNodes(node.leftChild);
        if(node.rightChild != null) count += countNodes(node.rightChild);
        
        return count;
    }
}