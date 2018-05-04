import java.util.ArrayList;
import java.util.List;


public class RBTree 
{
	private final char redNode = 'r';
    private final char blackNode = 'b';
    public static int nodeCount = 0;
    public final static Node nullNode = new Node(null);
    private Node root = nullNode;
    
    
    public Boolean isTreeEmpty(RBTree Tree)
    {
    	if(Tree.root == nullNode)
    	{
    		return true;
    	}

    	return false;
    }
    public void insertNode(Process prc) 
	{
    	Node newNode = new Node(prc);
        Node tempNode = root;
        if (root == nullNode) 
        {
            root = newNode;
            newNode.setNodeColor(blackNode);
            newNode.setNodeParent(nullNode);
        } 
        else 
        {
        	newNode.setNodeColor(redNode);
            while (tempNode != nullNode) 
            {
            	if (newNode.process.unfairnessScore <= tempNode.process.unfairnessScore ) 
                {
                    if (tempNode.getNodeLeftChild() == nullNode) 
                    {
                    	tempNode.setNodeLeftChild(newNode);
                        newNode.setNodeParent(tempNode);
                        break;
                    } 
                    else 
                    {
                    	tempNode = tempNode.getNodeLeftChild();
                    }
               } 
               else if (newNode.process.unfairnessScore  > tempNode.process.unfairnessScore ) 
               {
                    if (tempNode.getNodeRightChild() == nullNode) 
                    {
                    	tempNode.setNodeRightChild(newNode);
                        newNode.setNodeParent(tempNode);
                        break;
                    } 
                    else 
                    {
                    	tempNode = tempNode.getNodeRightChild();
                    }
                }
            }
            balanceTreeInsert(newNode);
        }
        nodeCount++;
    }
    
    private void balanceTreeInsert(Node node)
    {
        while (node.getNodeParent().getNodeColor() == redNode) 
        {
            Node auntNode = nullNode;

            if (node.getNodeParent() == node.getNodeParent().getNodeParent().getNodeRightChild()) 
            {
            	// node's parent is a right child
            	auntNode = node.getNodeParent().getNodeParent().getNodeLeftChild();
			    if (auntNode != nullNode && auntNode.getNodeColor() == redNode) 
			    {
			    	//Case 1: auntNode is red
			    	node.getNodeParent().setNodeColor(blackNode);
			        auntNode.setNodeColor(blackNode);
			        node.getNodeParent().getNodeParent().setNodeColor(redNode);
			        node = node.getNodeParent().getNodeParent();
			    }
			    else
			    {
				    if (node == node.getNodeParent().getNodeLeftChild()) 
				    {
				    	// Case 2: auntNode is black and node is a left child. Need double rotation
				    	node = node.getNodeParent();
				        rotateRight(node);
				    }
				    // Case 3: auntNode is black and node is right child
				    node.getNodeParent().setNodeColor(blackNode);
				    node.getNodeParent().getNodeParent().setNodeColor(redNode);
				
				    rotateLeft(node.getNodeParent().getNodeParent());
			    }
            } 
            else 
            {
            	// node's parent is a left child
            	auntNode = node.getNodeParent().getNodeParent().getNodeRightChild();
                if (auntNode != nullNode && auntNode.getNodeColor() == redNode) 
                {
                	// Case 1: auntNode is red
                    node.getNodeParent().setNodeColor(blackNode);
                    auntNode.setNodeColor(blackNode);
                    node.getNodeParent().getNodeParent().setNodeColor(redNode);
                    node = node.getNodeParent().getNodeParent();
                }
                else
                {
	                if (node == node.getNodeParent().getNodeRightChild()) 
	                {
	                	//Case 2: auntNode is black and node is right child. Need double rotation
	                    node = node.getNodeParent();
	                    rotateLeft(node);
	                } 
	                //Case 3: auntNode is black and node is left child
	                node.getNodeParent().setNodeColor(blackNode);
	                node.getNodeParent().getNodeParent().setNodeColor(redNode);
	                
	                rotateRight(node.getNodeParent().getNodeParent());
                }
            }
        }
        root.setNodeColor(blackNode);
    }
    
    private void rotateRight(Node node) 
    {
        if (node.getNodeParent() == nullNode) 
        {
        	Node leftNode = root.getNodeLeftChild();
            root.setNodeLeftChild(root.getNodeLeftChild().getNodeRightChild());
            leftNode.getNodeRightChild().setNodeParent(root);
            root.setNodeParent(leftNode);
            leftNode.setNodeRightChild(root);
            leftNode.setNodeParent(nullNode);
            root = leftNode;
        } 
        else 
        {
        	if (node == node.getNodeParent().getNodeLeftChild()) 
            {
                node.getNodeParent().setNodeLeftChild(node.getNodeLeftChild());
            } 
            else 
            {
                node.getNodeParent().setNodeRightChild(node.getNodeLeftChild());
            }

            node.getNodeLeftChild().setNodeParent(node.getNodeParent());
            node.setNodeParent(node.getNodeLeftChild());
            if (node.getNodeLeftChild().getNodeRightChild() != nullNode) 
            {
                node.getNodeLeftChild().getNodeRightChild().setNodeParent(node);
            }
            node.setNodeLeftChild(node.getNodeLeftChild().getNodeRightChild());
            node.getNodeParent().setNodeRightChild(node);
        }
    }
    
    private void rotateLeft(Node node) 
    {
        if (node.getNodeParent() == nullNode) 
        {
        	Node rightNode = root.getNodeRightChild();
            root.setNodeRightChild(rightNode.getNodeLeftChild());
            rightNode.getNodeLeftChild().setNodeParent(root);
            root.setNodeParent(rightNode);
            rightNode.setNodeLeftChild(root);
            rightNode.setNodeParent(nullNode);
            root = rightNode;
        } 
        else 
        {
        	if (node == node.getNodeParent().getNodeLeftChild()) 
            {
                node.getNodeParent().setNodeLeftChild(node.getNodeRightChild());
            } 
            else 
            {
                node.getNodeParent().setNodeRightChild(node.getNodeRightChild());
            }
            node.getNodeRightChild().setNodeParent(node.getNodeParent());
            node.setNodeParent(node.getNodeRightChild());
            if (node.getNodeRightChild().getNodeLeftChild() != nullNode) 
            {
                node.getNodeRightChild().getNodeLeftChild().setNodeParent(node);
            }
            node.setNodeRightChild(node.getNodeRightChild().getNodeLeftChild());
            node.getNodeParent().setNodeLeftChild(node);
        }
    }
    
    private Node getLeftMostNode()
    {
    	if(root != nullNode)
    	{
    		Node leftNode = root;
    		while(leftNode.getNodeLeftChild() != nullNode)
    			leftNode = leftNode.getNodeLeftChild();
    			return leftNode;
		}
    	else
    	{   System.out.println("Empty tree.");
    		return null;
    	}
    }
    
    public Node deleteNode()
    {
    	Node leftMostNode = getLeftMostNode();
        Node tempNode, tempLeft = leftMostNode;
        
        char tempLeftColor = tempLeft.getNodeColor();
        
        tempNode = leftMostNode.getNodeRightChild();  
        replaceNodes(leftMostNode, leftMostNode.getNodeRightChild());  
        if(tempLeftColor == blackNode)
        {
        	balanceTreeDelete(tempNode);
        }
        nodeCount--;
    	return leftMostNode;
    }
    
    private void replaceNodes(Node node1, Node node2){
    	if (node1.getNodeParent() == nullNode)
    	{
            root = node2;
    	}
        else if(node1 == node1.getNodeParent().getNodeLeftChild())
        {
            node1.getNodeParent().setNodeLeftChild(node2);
        }
        else
        {
            node1.getNodeParent().setNodeRightChild(node2);
        }
    	
    	node2.setNodeParent(node1.getNodeParent());
    }
    
    private void balanceTreeDelete(Node node)
    {
    	while(node != root && node.getNodeColor() == blackNode)
    	{
    		Node broNode = nullNode;
            if(node == node.getNodeParent().getNodeLeftChild())
            {
                broNode = node.getNodeParent().getNodeRightChild();
                if(broNode.getNodeColor() == redNode)
                {
					// Case 1: broNode is red
                    broNode.setNodeColor(blackNode);
                    node.getNodeParent().setNodeColor(redNode);
                    rotateLeft(node.getNodeParent());
                    broNode = node.getNodeParent().getNodeRightChild();
                }
                if(broNode.getNodeLeftChild().getNodeColor() == blackNode && broNode.getNodeRightChild().getNodeColor() == blackNode)
                {
					// Case 2: broNode is black, and both of broNode's children are black
                    broNode.setNodeColor(redNode);
                    node = node.getNodeParent();
                }
                else
                {
                    if(broNode.getNodeRightChild().getNodeColor() == blackNode)
                    {
						// Case 3: broNode is black and left child of broNode is red and right child is black
                        broNode.getNodeLeftChild().setNodeColor(blackNode);
                        broNode.setNodeColor(blackNode);
                        rotateRight(broNode);
                        broNode = node.getNodeParent().getNodeRightChild();
                    }
					// Case 4: broNode is black and the right child of w is red
                    broNode.setNodeColor(node.getNodeParent().getNodeColor());
                    node.getNodeParent().setNodeColor(blackNode);
                    broNode.getNodeRightChild().setNodeColor(blackNode);
                    rotateLeft(node.getNodeParent());
                    node = root;
                }
            }
            else
            {
                broNode = node.getNodeParent().getNodeLeftChild();
                if(broNode.getNodeColor() == redNode)
                {
					// Case 1: broNode is red
                    broNode.setNodeColor(blackNode);
                    node.getNodeParent().setNodeColor(redNode);
                    rotateRight(node.getNodeParent());
                    broNode = node.getNodeParent().getNodeLeftChild();
                }
                if(broNode.getNodeRightChild().getNodeColor() == blackNode && broNode.getNodeLeftChild().getNodeColor() == blackNode)
                {
					// Case 2: broNode is black and both of broNode's children are black
                    broNode.setNodeColor(redNode);
                    node = node.getNodeParent();
                }
                else
                {
                    if(broNode.getNodeLeftChild().getNodeColor() == blackNode)
                    {
						// Case 3: broNode is black and right child of broNode is red and left child is black
                        broNode.getNodeRightChild().setNodeColor(blackNode);
                        broNode.setNodeColor(redNode);
                        rotateLeft(broNode);
                        broNode = node.getNodeParent().getNodeLeftChild();
                    }
					// Case 4: broNode is black and the left child of broNode is red
                    broNode.setNodeColor(node.getNodeParent().getNodeColor());
                    node.getNodeParent().setNodeColor(blackNode);
                    broNode.getNodeLeftChild().setNodeColor(blackNode);
                    rotateRight(node.getNodeParent());
                    node = root;
                }
            }
        }
    	node.setNodeColor(blackNode);
    }
    
    public List<String> printRBtree()
    {
    	List<String> snapshotRBTree = new ArrayList<String>();
    	Node rootRBtree = root;
    	snapshotRBTree = printTree(rootRBtree, snapshotRBTree);
    	return snapshotRBTree;
    }
    
    public List<String> printTree(Node node, List<String> snapshotRBTree) {
        if (node == nullNode) {
            return snapshotRBTree;
        }
        String RBTNode;
        printTree(node.getNodeLeftChild(), snapshotRBTree);
        RBTNode = "'P" + node.process.prcId + " " + node.process.unfairnessScore + " " + node.getNodeColor() + "'";
        snapshotRBTree.add(RBTNode);
        printTree(node.getNodeRightChild(), snapshotRBTree);
        return snapshotRBTree; 
    }
    
}
