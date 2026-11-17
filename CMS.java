import java.util.Scanner;   // only used to read what the user types

// A Node is one order in the line. It holds a token number
// and a link to the next order behind it.
class Node {
    int token;      // the token number of this order
    Node next;      // points to the order behind this one

    Node(int token) {
        this.token = token;
        this.next = null;   // a new order has nobody behind it yet
    }
}

// Our own Queue made from scratch. First in, first out.
class OrderQueue {
    Node front;     // first order in line (will be served first)
    Node rear;      // last order in line (newest order)
    int size;       // how many orders are waiting

    OrderQueue() {
        front = null;   // empty line at the start
        rear = null;
        size = 0;
    }

    // Check if nobody is waiting
    boolean isEmpty() {
        return front == null;
    }

    // ENQUEUE: add a new order at the back of the line
    void enqueue(int token) {
        Node newNode = new Node(token);

        if (isEmpty()) {
            // line was empty, so the new order is both first and last
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;    // old last order now points to the new one
            rear = newNode;         // new order becomes the last one
        }
        size++;
    }

    // DEQUEUE: remove the order at the front of the line
    // returns the token that was removed, or 0 if the line was empty
    int dequeue() {
        if (isEmpty()) {
            return 0;
        }

        int servedToken = front.token;  // remember who we are serving
        front = front.next;             // move front to the next order

        if (front == null) {
            rear = null;    // line became empty, so rear is empty too
        }
        size--;
        return servedToken;
    }

    // SEARCH: find the position of a token in the line
    // position 1 means it is at the front, returns 0 if not found
    int search(int token) {
        Node temp = front;
        int position = 1;

        while (temp != null) {
            if (temp.token == token) {
                return position;
            }
            temp = temp.next;   // move to the next order
            position++;
        }
        return 0;   // reached the end, token not found
    }

    // CANCEL: remove a specific token from anywhere in the line
    // returns true if cancelled, false if token was not found
    boolean cancel(int token) {
        if (isEmpty()) {
            return false;
        }

        // Case 1: the order to cancel is at the front
        if (front.token == token) {
            dequeue();
            return true;
        }

        // Case 2: the order is somewhere behind the front
        Node temp = front;
        while (temp.next != null) {
            if (temp.next.token == token) {
                // if we are removing the last order, rear moves back
                if (temp.next == rear) {
                    rear = temp;
                }
                temp.next = temp.next.next;     // skip over the cancelled order
                size--;
                return true;
            }
            temp = temp.next;
        }
        return false;   // token was not in the line
    }

    // Show all waiting orders
    void display() {
        if (isEmpty()) {
            System.out.println("No orders waiting.");
            return;
        }

        Node temp = front;
        System.out.print("Waiting orders: ");
        while (temp != null) {
            System.out.print("[" + temp.token + "] ");
            temp = temp.next;
        }
        System.out.println();
    }
}

public class Canteen {

    // Shows the small dashboard: previous, current and next order
    static void showDashboard(OrderQueue queue, int lastServed) {
        System.out.println();
        System.out.println("CANTEEN BOARD");

        // Previous order = the last one we served
        if (lastServed == 0) {
            System.out.println("Previous order : None");
        } else {
            System.out.println("Previous order : Token " + lastServed);
        }

        // Current order = the one at the front, to be served now
        if (queue.isEmpty()) {
            System.out.println("Current order  : None");
        } else {
            System.out.println("Current order  : Token " + queue.front.token);
        }

        // Next order = the one right behind the front
        if (queue.front != null && queue.front.next != null) {
            System.out.println("Next order     : Token " + queue.front.next.token);
        } else {
            System.out.println("Next order     : None");
        }

        System.out.println("Orders waiting : " + queue.size);
        System.out.println("===================================");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        OrderQueue queue = new OrderQueue();

        int nextToken = 1;      // token number for the next new order
        int lastServed = 0;     // 0 means nobody has been served yet
        int choice = 0;

        // Keep showing the menu until the user chooses Exit (6)
        while (choice != 6) {
            showDashboard(queue, lastServed);

            System.out.println();
            System.out.println("1. Place new order");
            System.out.println("2. Serve current order");
            System.out.println("3. View all waiting orders");
            System.out.println("4. Search an order");
            System.out.println("5. Cancel an order");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            if (choice == 1) {
                // give the new order the next token number and add it
                queue.enqueue(nextToken);
                System.out.println("Order placed. Your token number is " + nextToken);
                nextToken++;    // next customer gets a bigger number

            } else if (choice == 2) {
                if (queue.isEmpty()) {
                    System.out.println("No orders to serve.");
                } else {
                    lastServed = queue.dequeue();   // current becomes previous
                    System.out.println("Token " + lastServed + " has been served.");
                }

            } else if (choice == 3) {
                queue.display();

            } else if (choice == 4) {
                System.out.print("Enter token number to search: ");
                int token = sc.nextInt();
                int position = queue.search(token);

                if (position == 0) {
                    System.out.println("Token " + token + " is not in the line.");
                } else {
                    System.out.println("Token " + token + " is at position " + position);
                }

            } else if (choice == 5) {
                System.out.print("Enter token number to cancel: ");
                int token = sc.nextInt();

                if (queue.cancel(token)) {
                    System.out.println("Token " + token + " has been cancelled.");
                } else {
                    System.out.println("Token " + token + " is not in the line.");
                }

            } else if (choice == 6) {
                System.out.println("Closing canteen. Thank you!");

            } else {
                System.out.println("Wrong choice. Try again.");
            }
        }

        sc.close();
    }
}