/* Getty Testa
* 115217416
* Recitation 01
*/

public class Packet {
    static int packetCount = 0;
    private int id;
    private int packetSize;
    private int timeArrive;
    private int timeToDest;

    /**
     * Empty constructor for the Packet class. Sets fields to 0.
     */
    public Packet() {
        this(0, 0);
    }

    /**
     * Overloaded constructor for the Packet class. Sets fields based on
     * user's input.
     * 
     * @param packetSize the size of the packet
     * @param timeArrive the time value when the Packet arrived
     */
    public Packet(int packetSize, int timeArrive) {
        id = packetCount;
        this.packetSize = packetSize;
        this.timeArrive = timeArrive;
        timeToDest = packetSize / 100;
    }

    /**
     * Gets the id of the Packet.
     * 
     * @return the id of the Packet
     */
    public int getId() {
        return id;
    }

    /**
     * Changes the id of the Packet based on the user's input.
     * 
     * @param id the new id for the Packet
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the size of the Packet.
     * 
     * @return the size of the Packet
     */
    public int getPacketSize() {
        return packetSize;
    }

    /**
     * Changes the size of the Packet based on the user's input.
     * 
     * @param packetSize the new size of the Packet
     */
    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }

    /**
     * Gets the time the Packet arrived.
     * 
     * @return the time the Packet arrived
     */
    public int getTimeArrive() {
        return timeArrive;
    }

    /**
     * Changes the time the Packet arrived based on the user's input.
     * 
     * @param timeArrive the new time that the Packet arrived at
     */
    public void setTimeArrive(int timeArrive) {
        this.timeArrive = timeArrive;
    }

    /**
     * Gets the time to the destination router.
     * 
     * @return the time to the destination router
     */
    public int getTimeToDest() {
        return timeToDest;
    }

    /**
     * Changes the time to the destination router based on the user's input.
     * 
     * @param timeToDest the new time to the destination
     */
    public void setTimeToDest(int timeToDest) {
        this.timeToDest = timeToDest;
    }

    /**
     * Creates and returns the String representation of the Packet
     * 
     * @return the String representation of the Packet
     */
    public String toString() {
        return "[" + id + ", " + timeArrive + ", " + timeToDest + "]";
    }
}