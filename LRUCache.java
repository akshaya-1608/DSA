import java.util.*;

class LRUCache {
    CDLL ll;
    int capacity;
    int size;
    Map<Integer, CDLLNode> mp;

    public LRUCache(int cap) {
        ll = new CDLL();
        this.capacity = cap;
        this.size = 0;
        mp = new HashMap<>();
    }

    int get(int k) {
        if (mp.containsKey(k)) {
            CDLLNode node = mp.get(k);
            ll.moveToFront(node);
            return node.val;
        } else {
            return -1; // Key not found
        }
    }

    void put(int k, int v) {
        if (mp.containsKey(k)) { // Update existing key
            CDLLNode node = mp.get(k);
            node.val = v;
            ll.moveToFront(node);
        } else { // Insert new key-value pair
            if (size < capacity) {
                CDLLNode nn = ll.insAtBegin(k, v);
                mp.put(k, nn);
                size++;
            } else { // Evict least recently used (LRU)
                int delKey = ll.delLast();
                mp.remove(delKey);
                CDLLNode nn = ll.insAtBegin(k, v);
                mp.put(k, nn);
            }
        }
    }
}

class CDLLNode {
    int key, val;
    CDLLNode prev, next;

    public CDLLNode(int k, int v) {
        this.key = k;
        this.val = v;
        this.prev = this.next = null;
    }
}

class CDLL {
    CDLLNode head;

    public CDLL() {
        head = null;
    }

    CDLLNode insAtBegin(int k, int v) {
        CDLLNode nn = new CDLLNode(k, v);
        nn.next = nn;
        nn.prev = nn;
        if (head == null) {
            head = nn;
            return nn;
        }
        CDLLNode last = head.prev;
        nn.next = head;
        head.prev = nn;
        last.next = nn;
        nn.prev = last;
        head = nn;
        return nn;
    }

    int delLast() {
        if (head == null) return -1;
        CDLLNode last = head.prev;
        int ret = last.key;
        if (last == head) {
            head = null;
            return ret;
        }
        last.prev.next = head;
        head.prev = last.prev;
        return ret;
    }

    void moveToFront(CDLLNode nodeToMove) {
        if (nodeToMove == head) return;
        nodeToMove.prev.next = nodeToMove.next;
        nodeToMove.next.prev = nodeToMove.prev;
        CDLLNode last = head.prev;
        nodeToMove.next = head;
        head.prev = nodeToMove;
        last.next = nodeToMove;
        nodeToMove.prev = last;
        head = nodeToMove;
    }
}

// Main class to take user input and interact with LRU Cache
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter cache capacity: ");
        int capacity = sc.nextInt();
        LRUCache cache = new LRUCache(capacity);

        while (true) {
            System.out.println("\nChoose operation: 1. Get  2. Put  3. Exit");
            int choice = sc.nextInt();

            if (choice == 1) { // Get operation
                System.out.print("Enter key to fetch: ");
                int key = sc.nextInt();
                int result = cache.get(key);
                System.out.println("Value: " + (result == -1 ? "Key not found" : result));
            } 
            else if (choice == 2) { // Put operation
                System.out.print("Enter key: ");
                int key = sc.nextInt();
                System.out.print("Enter value: ");
                int value = sc.nextInt();
                cache.put(key, value);
                System.out.println("Inserted: (" + key + ", " + value + ")");
            } 
            else if (choice == 3) { // Exit
                System.out.println("Exiting...");
                break;
            } 
            else {
                System.out.println("Invalid choice! Try again.");
            }
        }
        sc.close();
    }
}
