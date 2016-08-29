
public class StringStack {

	private Node head;
	
	public String stack(){
		head = null;
		return "";
	}
	

	public boolean isEmpty() {
		if(head == null){
			return true;
		}else{
			return false;
		}
	}


	public String pop() {
		if(isEmpty()){
			throw new StackEmptyException("can't pop from empty list");
		}
		String val = head.item;
		head = head.next;
		return val;
	}
	

	public String peek() {
		return head.item;
	}


	public void push(String item) {
		Node add = new Node(item);
		add.next = head;
		head = add;
	}


	public void popAll() {
		head = null;
		}
		
		
	public static void main(String args[]){
		StringStack mystack = new StringStack();
		mystack.push("1");
		mystack.push("5");
		mystack.push("3");
		System.out.println(mystack.peek());
		System.out.println(mystack.pop());
		System.out.println(mystack.peek());
		mystack.pop();
		mystack.pop();
		try {
			mystack.pop();
		} catch(StackEmptyException ex){
		System.out.println(ex);
		}
	}
}

