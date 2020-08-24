public class MyLinkedList<T> {
    // Double LinkedList
    public static class Node<T>{
        public Node<T> next;
        public Node<T> previous;
        public T value;

        public Node(T val){
            value = val;
        }

        public boolean hasNext(){
            return this.next == null;
        }
    }

    public Node<T> head;
    public Node<T> tail;
    public int length;

    public MyLinkedList(Node<T> node){
        head = node;
        tail = node;
        length = 0;
        while(tail != null && tail.next != null){
            tail = tail.next;
            length ++;
        }
        length ++;
    }

    public void add(Node<T> newNode){
        if(head == null){
            head = newNode;
            tail = newNode;
            head.previous = null;
            head.next = null;
        }else{
            newNode.previous = tail;
            tail.next = newNode;
            tail = newNode;
        }
        length ++;
    }

    public void delete(Node<T> node){
        if(node == head){
            head = head.next;
            head.previous = null;
        }else{
            node.next.previous = node.previous;
            node.previous.next = node.next;
        }
        length --;
    }

    public int getLength(Node<T> node){
        if(node == head) return length;
        int length = 0;
        while(node != null){
            length ++;
            node = node.next;
        }
        return length;
    }

    public static void main(String[] args){
        Node<Integer> node1 = new Node<Integer>(1);
        Node<Integer> node2 = new Node<Integer>(2);
//        Node<Integer> head = node1;
        MyLinkedList<Integer> linkedList = new MyLinkedList<>(node1);
        int len = linkedList.getLength(node1);
        System.out.println(len);
        linkedList.add(node2);
        len = linkedList.getLength(node1);
        System.out.println(len);
    }
}
