/* Getty Testa
* 115217416
* Recitation 01
*/

import java.util.LinkedList;

public class Router extends LinkedList<Packet> {
    /**
     * Constructor for the Router class.
     */
    public Router() {
        super();
    }

    /**
     * Enqueues a new Packet to the router's queue.
     * 
     * @param p the new Packet for Router
     */
    public void enqueue(Packet p) {
        super.add(p);
    }

    /**
     * Dequeues the first Packet from the router's queue.
     * 
     * @return the dequeued Packet from the Router
     */
    public Packet dequeue() {
        return super.poll();
    }

    /**
     * Checks if the queue in the Router is empty.
     * 
     * @return true if the router is empty, false otherwise
     */
    public boolean isEmpty() {
        if (size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Creates and returns the String representation of the Router
     * 
     * @return the String representation of the Router
     */
    public String toString() {
        String formattedString = "{";
        if (this.size() == 0) {
            return formattedString + "}";
        } else if (this.size() < 4) {
            formattedString += this.get(0).toString();
            for (int i = 1; i < this.size(); i++) {
                formattedString += ", ";
                formattedString += this.get(i).toString();
            }
            return formattedString + "}";
        } else {
            formattedString += this.get(1).toString();
            formattedString += ", ";
            formattedString += this.get(2).toString();
            return formattedString + ", ... ... ...}";
        }

    }

    /**
     * Determines the index of the most free Router in the routers[].
     * 
     * @param routers       the array of Routers to search through
     * @param maxBufferSize the maximum amount of Packets a Router can hold
     * @return the index of the most free Router, -1 if all routers are full
     * @throws CongestedNetworkException if all routers are full
     */
    public static int sendPacketTo(Router[] routers, int maxBufferSize) {
        try {
            int openRouterIndex = 0;
            boolean fullNetworkCheck = true;
            for (int i = 0; i < routers.length; i++) {
                if (routers[i].size() != maxBufferSize) {
                    fullNetworkCheck = false;
                }
                if (routers[i].size() < routers[openRouterIndex].size()) {
                    openRouterIndex = i;
                }
            }
            if (fullNetworkCheck) {
                throw new CongestedNetworkException();
            }
            return openRouterIndex;
        } catch (CongestedNetworkException cne) {
            return -1;
        }
    }
}
