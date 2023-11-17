/**
 * This class represents nodes in the list.
 * It includes a value, and because this is a doubly linked list, a reference to both the next and previous nodes.
 */
class Node
{
    /**
     * Note that the constructor only takes the value as an argument.
     * The references will be handled by methods in DoublyLinkedList.
     * @param value
     */
    constructor(value)
    {
        this.value = value;
        this.next = null;
        this.prev = null;
    }
}

/**
 * Defines the behaviour and state of each linked list.
 * There are four methods, each corresponding to the typical linked list operations.
 */
class DoublyLinkedList
{
    constructor()
    {
        this.head = null;
        this.tail = null;
    }

    /**
     * Adds a node to the end (tail) of the list.
     * If the list is empty, the node will be added, and it will be treated as its own neighbouring nodes.
     * Otherwise, the node will be added to the end of the list.
     * @param value
     */
    push(value)
    {
        const newNode = new Node(value);
        //empty list
        //I decided to keep the handling for this condition for the sake of completeness
        if (!this.head)
        {

            this.head = this.tail = newNode;
        }
        else
        {
            //gets the node at the end of the list, and uses it to add the node to the end.
            this.tail.next = newNode;
            newNode.prev = this.tail;
            this.tail = newNode;
        }
    }

    /**
     * Removes and returns the node at the end of the list.
     * Returns null if the list is empty.
     * @returns {*|null}
     */
    pop()
    {
        //empty list
        if (!this.tail)
        {
            return null;
        }
        const poppedNode = this.tail;
        //sets tail pointer to second-to-last node
        this.tail = this.tail.prev;
        //removes node by nulling its reference
        if (this.tail)
        {
            this.tail.next = null;
        }
        //used if there's only one node in the list
        else
        {
            this.head = null;
        }
        return poppedNode.value;
    }

    /**
     * Removes and returns the node at the front of the list.
     * Effectively an inversion of pop(), using references to head instead of tail.
     * @returns {*|null}
     */
    shift()
    {
        //empty list
        if (!this.head)
        {
            return null;
        }
        const shiftedNode = this.head;
        //changes head pointer to node after head
        this.head = this.head.next;
        //nulls the head
        if (this.head)
        {
            this.head.prev = null;
        }
        //only one node in the list
        else
        {
            this.tail = null;
        }
        return shiftedNode.value;
    }

    /**
     * Inserts a value at the front of the list (head).
     * @param value
     */
    unshift(value)
    {
        const newNode = new Node(value);
        //used if the list is empty
        if (!this.head)
        {
            this.head = this.tail = newNode;
        }
        //adds to the front of the list
        else
        {
            this.head.prev = newNode;
            newNode.next = this.head;
            this.head = newNode;
        }
    }
}

let theList = new DoublyLinkedList();
theList.push(6);
theList.push(12);
//theList.push(18);
theList.unshift(33);
console.log(theList.pop());
