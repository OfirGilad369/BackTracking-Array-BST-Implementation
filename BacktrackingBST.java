public class BacktrackingBST implements Backtrack, ADTSet<BacktrackingBST.Node> {
    private Stack stack;
    private Stack redoStack;
    Node root = null;
    Node root_father;

    // Do not change the constructor's signature
    public BacktrackingBST(Stack stack, Stack redoStack) {
        this.stack = stack;
        this.redoStack = redoStack;
        root_father = new Node(-1,null);
    }

    public Node getRoot() {
        return root;
    }
	
    public Node search(int x) {
        return search_sub_tree(root,x);
    }

    private Node search_sub_tree(Node sub_root, int x) {
    	// if the node is null or "x" equals to "root" returning the "root"
        if (sub_root==null || sub_root.key==x)
            return sub_root;
        
        // if "x" bigger then the node keep searching the left side of the tree
        if (sub_root.key > x)
            return search_sub_tree(sub_root.left, x);
        
        // otherwise keep searching the right side of the tree
        return search_sub_tree(sub_root.right, x);
    }

    public void insert(BacktrackingBST.Node z) {
    	// if the tree is empty inserting "z" as the "root"
        Node closest_node;
        if(root == null){
            _set_as_root(z);
        }
        
        // otherwise calling the subfunction to insert the node
        else {
            closest_node = get_closest_node(root, z);
            closest_node.set_node_as_child(z);
        }
        stack.push(new InsertedNodeData(z));
    }

    private Node get_closest_node(Node sub_tree, Node to_find) {
        //performs binary search to find the node which contains the key with the closest value to the value of "to find"
        //subfunction for insert
        if(sub_tree.getKey()<to_find.getKey())
            if(sub_tree.right!=null)
                return get_closest_node(sub_tree.right,to_find);
            else
                return sub_tree;
        if(sub_tree.getKey()>to_find.getKey())
            if(sub_tree.left!=null)
                return get_closest_node(sub_tree.left,to_find);
            else
                return sub_tree;
        return null; //line should never be called
    }

    public void delete(Node x) {
        //different private function was created to handle each case of deleting node from the tree
    	//issue 1: "x" is a leaf 
        if(x.left==null&x.right==null)
            _delete_leaf(x);
        else
        	//issue 2: "x" has 2 children
            if(x.right!=null&&x.left!=null)
                _delete_node_with_two_child(x);
        	//issue 3: "x" has 1 child 
            else
               _delete_node_with_one_child(x);
        
        //updating "x" father to "root" father if "x" equals to the "root"
        if(x.equals(root))
            _set_as_root(root_father.right);
    }

    private void _delete_leaf(Node x) {
    	// subfunction for issue 1: "x" is a leaf 
        Node x_parent = x.parent;
        boolean is_right_child = x.equals(x_parent.right);
        if (is_right_child)
            x_parent.right = null;
        else
            x_parent.left = null;
        stack.push(new DeletedNodeData(x,x.parent,is_right_child,0));
    }

    private void _delete_node_with_one_child(Node x) {
    	// subfunction for issue 1: "x" has 1 child
        boolean is_right_child = x.equals(x.parent.right);
        if (x.equals(x.parent.right))
            x.parent.right = _get_node_singel_child(x);
        else
            x.parent.left = _get_node_singel_child(x);
        x.parent.update_children_parent();
        stack.push(new DeletedNodeData(x,x.parent,is_right_child,1));
    }

    private Node _get_node_singel_child(Node x) {
        if(x.left!=null)
            return x.left;
        return x.right;
    }

    private void _delete_node_with_two_child(Node x) {
    	// subfunction for issue 2: "x" has 2 children
        Node suc = successor(x);
        boolean is_right_child = (suc.parent.right!=null&&suc.parent.right.equals(suc));
        stack.push(new DeletedNodeData(x,x.parent,is_right_child,suc,suc.parent,2));
        suc.switch_places(x,is_right_child);
        delete(x);
        stack.pop();//to pop the data in the stack from the second call of delete
    }

    private void _reverse_delete(DeletedNodeData deleted_node_data) {
    	//different private function was created to reverse each case of deleting node from the tree
        if(deleted_node_data.num_of_childes==0)
            _reverse_delete_leaf(deleted_node_data);
        if(deleted_node_data.num_of_childes==1)
            _reverse_delete_node_with_1_child(deleted_node_data);
        if(deleted_node_data.num_of_childes==2)
            _reverse_delete_node_with_2_children(deleted_node_data);

        if(deleted_node_data.node.parent.equals(root_father))
            _set_as_root(deleted_node_data.node);

        redoStack.push(new InsertedNodeData(deleted_node_data.node));
    }

    private void _set_as_root(Node x) {
        //make x the new tree root
        root =x;
        root_father.right = root;
        if(x!=null)
            root.parent = root_father;
    }
    
    private void _reverse_delete_leaf(DeletedNodeData deleted_node_data) {
        Node parent = deleted_node_data.parent;
        Node node = deleted_node_data.node;
        parent.add_as_son(node);
    }
    
    private void _reverse_delete_node_with_1_child(DeletedNodeData deleted_node_data) {
        _reverse_delete_leaf(deleted_node_data);
        Node node = deleted_node_data.node;
        node.update_children_parent();

    }
    
    private void _reverse_delete_node_with_2_children(DeletedNodeData deleted_node_data) {
        Node node = deleted_node_data.node;
        Node suc = deleted_node_data.successor;
        suc.switch_places(node,deleted_node_data.is_right_child);
        Node suc_parent = deleted_node_data.successor_parent;
        suc_parent.add_as_son(suc);
    }
    
    public Node minimum() {
        return  subtree_min(root);
    }

    private Node subtree_min(Node sub_tree) {
        if(sub_tree == null)
            return null;
        while (sub_tree.left !=null)
            sub_tree=sub_tree.left;
        return sub_tree;
    }

    public Node maximum() {
        return subtree_max(root);
    }

    private Node subtree_max(Node curr_node) {
        if(curr_node == null)
            return null;
        while (curr_node.right !=null)
            curr_node=curr_node.right;
        return curr_node;
    }

    public Node successor(Node x) {
        if (x.right != null)
            return subtree_min(x.right);
        else {
            Node suc=x;
            while (!suc.equals(root)&&suc.parent.right.equals(suc))
                suc = suc.parent;

            if(suc.equals(root))//if x is the maximum
                return null;
            return suc.parent;
        }
    }

    public Node predecessor(Node x) {
        if (x.right != null)
            return subtree_max(x.left);
        else {
            Node pre=x;
            while (!pre.equals(root)&&pre.parent.left.equals(pre))
                pre = pre.parent;
            if(pre.equals(root))//if x is the minimum
                return null;
            return pre.parent;
        }
    }

    @Override
    public void backtrack() {
    	
    	if (!stack.isEmpty()) {
    		Object obj = stack.pop();

    		if(obj instanceof InsertedNodeData)
    			_reverse_insert((InsertedNodeData)obj);

    		if(obj instanceof DeletedNodeData)
    			_reverse_delete((DeletedNodeData)obj);

    		System.out.println("backtracking performed");
    	}
    }

    private void _reverse_insert(InsertedNodeData insert_data) {
        delete(insert_data.node);
        DeletedNodeData deleted_data = (DeletedNodeData)stack.pop();
        redoStack.push(deleted_data);
    }

    private void instert_agine(DeletedNodeData data) {
        //private function for the retrack
        if(data.parent!=root_father)
            data.parent.set_node_as_child(data.node);
        else
            _set_as_root(data.node);
        stack.push(new InsertedNodeData(data.node));
    }

    @Override
    public void retrack() {
    	
    	if (!redoStack.isEmpty()) {
    		Object obj = redoStack.pop();

    		//in case of retrack after backtrack after insert
    		if (obj instanceof InsertedNodeData) {
    			InsertedNodeData data = (InsertedNodeData)obj;
    			delete(data.node);

    		}
            //in case of retrack after backtrack after delete
    		if (obj instanceof DeletedNodeData) {
    			DeletedNodeData data = (DeletedNodeData)obj;
    			instert_agine(data);
    		}
    	}
    }

    public void printPreOrder() {
        String str = get_print_str(root);
        if (str.length()>0)
            str = str.substring(0, str.length() - 1);
        System.out.print(str);
    }

    @Override
    public void print() {
        printPreOrder();
    }

    public String get_print_str(Node sub_tree) {
        if(sub_tree == null)
            return "";
        return sub_tree.key +" "+ get_print_str(sub_tree.left)+get_print_str(sub_tree.right);
    }
    
    public static class Node {
    	//These fields are public for grading purposes. By coding conventions and best practice they should be private.
        public Node left;
        public Node right;
        
        private Node parent;
        private int key;
        private Object value;

        public Node(Node right, Node left) {
            this.right = right;
            this.left = left;
        }

        public Node(int key, Object value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        private void set_node_as_child(Node to_add) {
            if(key>to_add.key)
                left = to_add;
            else
                right = to_add;
            to_add.parent = this;
        }

        private void switch_places(Node x,boolean is_right_child) {
            //switch places between 2 nodes
            if(x.equals(this.parent))
                this.switch_places_with_parent(x,is_right_child);
            else
                if (this.equals(x.parent))
                    x.switch_places_with_parent(this,is_right_child);
                else
                    this.switch_places_none_relatives(x);
        }

        private void switch_places_with_parent(Node my_parent,boolean is_right_child) {
            Node my_children = new Node(right,left);
            if(is_right_child) {
                this.right = my_parent;
                this.left=my_parent.left;
            }
            else{
                this.left = my_parent;
                this.right=my_parent.right;
            }
            //set py parent to be my parent's parent
            this.parent=my_parent.parent;
            if(my_parent.parent.right!=null && my_parent.parent.right.equals(my_parent))
                my_parent.parent.right = this;
            else
                my_parent.parent.left = this;
            my_parent.parent=this;

            my_parent.right=my_children.right;
            my_parent.left=my_children.left;

            my_parent.update_children_parent();
            this.update_children_parent();
        }

        private void update_children_parent() {
            if(this.right !=null)
                this.right.parent=this;
            if(this.left!=null)
                this.left.parent = this;
        }

        private void switch_places_none_relatives(Node x) {
            Node temp = new Node(-10,null);
            temp.copy_pointers(x);
            x.copy_pointers(this);
            this.copy_pointers(temp);

        }

        private void copy_pointers(Node x) {
            this.right = x.right;
            this.left = x.left;
            this.parent = x.parent;

            //change x relatives to be x's relatives
            this.take_his_parent(x);
            this.update_children_parent();
        }

        private void take_his_parent(Node x) {
            this.parent = x.parent;
            if(x.parent.right!=null && x.parent.right.equals(x))
                x.parent.right=this;
            else
                x.parent.left=this;
        }

        private void add_as_son(Node x) {
            if(x.key>this.key)
                this.right = x;
            else
                this.left = x;
            this.update_children_parent();
        }

        public String toString() {
            return "key : " +key;
        }

    }


    class InsertedNodeData {
    	// data structure created to manage data in the stack more easily
    	public BacktrackingBST.Node node;

    	public InsertedNodeData(BacktrackingBST.Node node) {
    		this.node = node;
    	}
    }

	class DeletedNodeData {
    	// data structure created to manage data in the stack more easily
    	public BacktrackingBST.Node node;
    	public BacktrackingBST.Node parent;
    	public boolean is_right_child;
    	public BacktrackingBST.Node successor;  // will be used only if we have deleted node with 2 children
    	public BacktrackingBST.Node successor_parent;  // will be used only if we have deleted node with 2 children
    	public int num_of_childes;

    	public DeletedNodeData(BacktrackingBST.Node node,BacktrackingBST.Node parent,boolean is_right_child,int num_of_childes) {
        	this.node = node;
        	this.parent = parent;
        	this.num_of_childes = num_of_childes;
        	this.is_right_child = is_right_child;
    	}

    	public DeletedNodeData(BacktrackingBST.Node node,BacktrackingBST.Node parent,boolean is_right_child,BacktrackingBST.Node successor,BacktrackingBST.Node successor_parent,int num_of_childes) {
    		this.node = node;
    		this.parent = parent;
    		this.successor=successor;
    		this.num_of_childes = num_of_childes;
    		this.successor_parent = successor_parent;
        	this.is_right_child = is_right_child;
    	}
     
	}
}