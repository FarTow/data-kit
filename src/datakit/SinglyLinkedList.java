package datakit;

import java.util.Iterator;
import java.util.Objects;

/**
 * Singly Linked List Data Structure
 * @param <T> type for data stored
 */
public class SinglyLinkedList<T> implements Iterable<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    // Constructors

    /**
     * Initialize this list to be empty
     */
    public SinglyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Initialize this list to the values of some array
     * @param vals array to take values from
     */
    public SinglyLinkedList(T[] vals) {
        this();

        size = vals.length;

        if (size > 0) {
            head = new Node<>(vals[0]);

            if (size == 1) {
                tail = head;
            } else {
                Node<T> prevNode = head;

                for (int i = 1; i < vals.length - 1; i++) {
                    prevNode.next = new Node<>(vals[i]);
                    prevNode = prevNode.next;
                }

                prevNode.next = new Node<>(vals[vals.length - 1]);
                tail = prevNode.next;
            }

        }
    }

    // Add

    /**
     * Insert a node in the beginning of the list
     * @param data new data to insert in list
     */
    public void addFirst(T data) {
        if (size == 0 || size == 1) {
            add(data);
        }

        head = new Node<>(data, head);
        size++;
    }

    /**
     * Same method as {@link #add(T)}
     */
    public void addLast(T data) {
        add(data);
    }

    /**
     * Append a new node to the end of this list
     * @param data new data to append to list
     */
    public void add(T data) {
        Node<T> newNode = new Node<>(data);

        if (size == 0) {
            head = newNode;
        } else if (size == 1) {
            head.next = newNode;
        } else {
            tail.next = newNode;
        }

        tail = newNode;
        size++;
    }

    /**
     * Insert a new node to the list at a desired index
     * @param index index to insert node at
     * @param data new data to insert into list
     */
    public void add(int index, T data) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("Violation of pre-condition: " +
                    "index must be within the list");
        }

        if (size == 0 || size == 1 || index == size) {
            add(data);
        } else if (index == 0) {
            addFirst(data);
        } else {
            Node<T> prevNode = getNode(index - 1);
            prevNode.next = new Node<>(data, prevNode.next);
            size++;
        }
    }

    // Add All

    /**
     * Append a singly linked list to the start of this list
     * @param other singly linked list to append to this list
     */
    public void addAllFirst(SinglyLinkedList<T> other) {
        if (other.size > 0) {
            if (size == 0) {
                addAll(other);
            } else {
                other.tail.next = head;
                head = other.head;
                size += other.size;
            }
        }
    }

    /**
     * Same method as {@link #addAll(SinglyLinkedList)}
     */
    public void addAllLast(SinglyLinkedList<T> other) {
        addAll(other);
    }

    /**
     * Append a singly linked list to the end of this list
     * @param other singly linked list to append to this list
     */
    public void addAll(SinglyLinkedList<T> other) {
        if (other.size > 0) {
            if (size == 0) {
                head = other.head;
            } else {
                tail.next = other.head;
            }

            tail = other.tail;
            size += other.size;
        }
    }

    /**
     * Append a singly linked list inside of this list
     * @param index index to insert list at
     * @param other singly linked list to insert
     */
    public void addAll(int index, SinglyLinkedList<T> other) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("Violation of pre-condition: " +
                    "index must be within the list");
        }

        if (other.size > 0) {
            if (size == 0) {
                addAll(other);
            } else if (index == 0) {
                addAllFirst(other);
            } else if (index == size - 1) {
                addAll(other);
            } else {
                Node<T> startNode = getNode(index - 1);
                other.tail.next = startNode.next;
                startNode.next = other.head;
                size += other.size;
            }
        }
    }

    // Remove

    /**
     * Remove the first node in this list
     * @return the first node in this list
     */
    public T removeFirst() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Unable to remove first on empty list");
        }

        if (size == 1) {
            return removeOnlyNode();
        }

        T data = head.data;
        head = head.next;

        size--;
        return data;
    }

    /**
     * Remove any amount of nodes from the start of this list
     * @param numRemoved number of nodes to remove from front
     */
    public void removeFirstNodes(int numRemoved) {
        if (numRemoved < 0 || numRemoved > size) {
            throw new IllegalArgumentException("Violation of precondition: " +
                    "impossible to remove desired amount of nodes");
        }

        if (numRemoved == size) {
            clear();
        } else if (numRemoved > 0) {
            head = getNode(numRemoved);
            size -= numRemoved;
        }
    }

    /**
     * Remove the last node in this list
     * @return the last node in this list
     */
    public T removeLast() {
        if (size == 0) {
            throw new IndexOutOfBoundsException("Unable to remove last on empty list");
        }

        if (size == 1) {
            return removeOnlyNode();
        } else if (size == 2) {
            T data = tail.data;

            head.next = null;
            tail = head;

            size--;
            return data;
        } else {
            T data = tail.data;
            Node<T> newTail = getNode(size - 2);
            newTail.next = null;
            tail = newTail;

            size--;
            return data;
        }
    }

    /**
     * Remove all nodes past a certain index
     * @param numRemoved number of nodes to remove from tail
     */
    public void removeLastNodes(int numRemoved) {
        if (numRemoved < 0 || numRemoved > size) {
            throw new IllegalArgumentException("Violation of precondition: " +
                    "impossible to remove desired amount of nodes");
        }

        if (numRemoved == size) {
            clear();
        } else {
            tail = getNode(size - numRemoved - 1);
            tail.next = null;
            size -= numRemoved;

            if (size == 1) {
                tail = head;
            }
        }
    }

    /**
     * Remove a node at a specified index in this list
     * @param index index of node to be removed
     * @return the data in the node of the index that was removed
     */
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("Violation of pre-condition: " +
                    "index must be within the list");
        }

        if (index == 0) {
            return removeFirst();
        } else if (index == size - 1) {
            return removeLast();
        } else {
            Node<T> prevNode = getNode(index - 1);
            T data = prevNode.next.data;
            prevNode.next = prevNode.next.next;

            size--;
            return data;
        }
    }

    /**
     * Remove the values between specified indices
     * <br>pre: indices are within list
     * @param startIndex first index of nodes to remove (inclusive)
     * @param endIndex last index of nodes to remove (exclusive)
     */
    public void removeRange(int startIndex, int endIndex) {
        if (startIndex < 0 || startIndex > size - 1 || endIndex < startIndex || endIndex > size) {
            throw new IllegalArgumentException("Violation of pre-condition: " +
                    "indices are within list");
        }

        if (startIndex == 0) {
            removeFirstNodes(endIndex);
        } else if (endIndex == size) {
            removeLastNodes(size - startIndex);
        } else {
            Node<T> startNode = getNode(startIndex - 1);
            startNode.next = getNode(endIndex);
            size -= (endIndex - startIndex);
        }
    }

    /**
     * Remove all nodes from this list
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    // Helper method to remove the only node in the list
    // Should ONLY be called when the size is 1
    private T removeOnlyNode() {
        T data = head.data;
        head = null;
        size--;
        return data;
    }

    // Setters

    /**
     * @param data value to set for first node
     */
    public void setFirst(T data) {
        head.data = data;
    }

    /**
     * @param data value to set for last node
     */
    public void setLast(T data) {
        tail.data = data;
    }

    /**
     * @param index index of node to set value of
     * @param data value to set for node
     */
    public void set(int index, T data) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("Violation of pre-condition: " +
                    "index must be within the list");
        }

        if (index == 0) {
            setFirst(data);
        } else if (index == size - 1) {
            setLast(data);
        } else {
            getNode(index).data = data;
        }
    }

    // Getters

    /**
     * @return first node in list
     */
    public T getFirst() {
        return size == 0 ? null : head.data;
    }

    /**
     * @return last node in list
     */
    public T getLast() {
        return size == 0 ? null : tail.data;
    }

    /**
     * @param index of node to retrieve
     * @return the data of node at index
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("Violation of pre-condition: " +
                    "index must be within the list");
        }

        if (index == 0) {
            return getFirst();
        } else if (index == size - 1) {
            return getLast();
        } else {
            return getNode(index).data;
        }
    }

    /**
     * @return size of this list
     */
    public int size() {
        return size;
    }

    /**
     * @return whether or not this list is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    // Overrides

    /**
     * @return a string of the contents of this list in sequential order in an array format
     */
    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder("[");

        Node<T> currNode = head;

        while (currNode != null) {
            returnString.append(currNode.data);
            currNode = currNode.next;

            if (currNode != null) {
                returnString.append(", ");
            }
        }

        returnString.append("]");
        return returnString.toString();
    }

    /**
     * Two lists are considered equal if they are the same size and have the same values
     * @param obj object to compare to
     * @return if an object is equal to this list
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof SinglyLinkedList)) {
            return false;
        }

        SinglyLinkedList<?> objAsList = (SinglyLinkedList<?>) obj;

        if (objAsList.size != size) {
            return false;
        }

        Iterator<?> objListIterator = objAsList.iterator();
        Iterator<T> thisListIterator = iterator();

        while (thisListIterator.hasNext()) {
            T thisNode = thisListIterator.next();
            Object otherNode = objListIterator.next();

            if (!thisNode.equals(otherNode)) {
                return false;
            }
        }

        return true;
    }

    /**
     * @return the hash code of this list
     */
    @Override
    public int hashCode() {
        return Objects.hash(head, tail, size);
    }

    /**
     * @return an iterator to traverse the nodes that compose this list
     */
    @Override
    public Iterator<T> iterator() {
        return new SLLIterator();
    }

    // Helper

    private Node<T> getNode(int index) {
        int currIndex = 0;
        Node<T> currNode = head;

        while (currIndex < index) {
            currNode = currNode.next;
            currIndex++;
        }

        return currNode;
    }

    // Sub-Classes

    /**
     * Single Linked Node
     * @param <T> type for data stored
     */
    private static class Node<T> {
        private T data;
        private Node<T> next;

        // Constructors

        /**
         * Initialize this node to be empty
         */
        public Node() {
            this.data = null;
            next = null;
        }

        /**
         * Initialize the node's data with no next node
         * @param data data to store in this node
         */
        public Node(T data) {
            this.data = data;
            next = null;
        }

        /**
         * Initialize the node's data and next node
         * @param data data to store in this node
         * @param next node to point to
         */
        public Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }

        // Setters

        /**
         * Set the data for this node
         * @param data new data for this node
         */
        public void setData(T data) {
            this.data = data;
        }

        /**
         * Set the next node for this node
         * @param next new next node for this node
         */
        public void setNext(Node<T> next) {
            this.next = next;
        }

        // Getters

        /**
         * @return data
         */
        public T getData() {
            return data;
        }

        /**
         * @return next node
         */
        public Node<T> getNext() {
            return next;
        }

        // Overrides

        /**
         * @return the data stored in this node as a string
         */
        @Override
        public String toString() {
            return data.toString();
        }

        /**
         * Two nodes are considered equal if they contain equivalent data
         * @param obj object to compare to
         * @return if an object is equal to this node
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Node)) {
                return false;
            }

            Node<?> objAsNode = (Node<?>) obj;

            return objAsNode.getData().equals(data);
        }

        /**
         * @return the hash code of this node
         */
        @Override
        public int hashCode() {
            return Objects.hash(data, next);
        }
    }

    /**
     * List iterator for this Singly Linked List
     */
    private class SLLIterator implements Iterator<T> {
        private Node<T> currNode;
        private int nodesPassed;

        public SLLIterator() {
            currNode = null;
        }

        @Override
        public boolean hasNext() {
            return nodesPassed < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new IllegalStateException("Unable to go to next node if it doesn't exist");
            }

            if (nodesPassed == 0) {
                currNode = head;
            } else {
                currNode = currNode.next;
            }

            nodesPassed++;

            return currNode.data;
        }

        @Override
        public void remove() {
            if (currNode == null) {
                throw new IllegalStateException("Unable to remove node when current node is null");
            }

            if (currNode == head) {
                head = head.next;
                currNode = head;
            } else {
                Node<T> prevNode = head;

                while (prevNode.next != currNode) {
                    prevNode = prevNode.next;
                }

                prevNode.next = currNode.next;
                currNode = prevNode;

                if (nodesPassed == size) {
                    tail = prevNode;
                }
            }

            nodesPassed--;
            size--;
        }
    }
}
