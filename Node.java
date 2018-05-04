
public class Node {
	private Node left;
    private Node right;
    private Node parent;
    private char color;
    private int count;
    
    public Process process = null;
    
    public Node(Process p) 
    {
    	this.left = RBTree.nullNode;
        this.right = RBTree.nullNode;
        this.parent = RBTree.nullNode;
        this.color = 'b';
        this.process = p;
    } 
    
    public char getNodeColor()
    {
        return color;
    }
    
    public void setNodeColor(char nodeColor)
    {
        this.color = nodeColor;
    }
    
    public int getNodeCount()
    { 
    	return count; 
    }
    
    public void setNodeCount(int nodeCount)
    {
        this.count = nodeCount;
    }
    
    public Node getNodeParent()
    {
        return parent;
    }
    
    public void setNodeParent(Node parentNode)
    {
        this.parent = parentNode;
    }
    
    public Node getNodeLeftChild() 
    {
        return left;
    }
    
    public void setNodeLeftChild(Node leftChild) 
    {
        this.left = leftChild;
    }
    
    public Node getNodeRightChild() 
    {
        return right;
    }
    
    public void setNodeRightChild(Node rightChild) 
    {
        this.right = rightChild;
    }
}
