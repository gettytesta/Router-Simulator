/* Getty Testa
* 115217416
* Recitation 01
*/

import java.util.InputMismatchException;
import java.util.Scanner;

public class Simulator {
    static final int MAX_PACKETS = 3;
    private Router dispatcher;
    private Router[] routers;
    private int totalServiceTime;
    private int totalPacketsArrived;
    private int packetsDropped;
    private double arrivalProb;
    private int numIntRouters;
    private int maxBufferSize;
    private int minPacketSize;
    private int maxPacketSize;
    private int bandwidth;
    private int duration;

    /*
     * This will represent a queue for routers being sent to the destination
     * Router. The timeArrive field of the packet will represent the index of the
     * Router in routers[].
     */
    private Router destQueue = new Router();

    /**
     * Empty constructor for the Simulator class. Sets all fields to 1.
     */
    public Simulator() {
        this(1, 1, 1, 1, 1, 1, 1);
    }

    /**
     * Overloaded constructor for the Simulator class. Sets fields based
     * on the user's input.
     * 
     * @param numIntRouters the number of routers in the simulation
     * @param arrivalProb   the probability of a Packet arriving
     * @param maxBufferSize the max number of Packets a Router can hold
     * @param minPacketSize the minimum Packet size
     * @param maxPacketSize the maximum Packet size
     * @param bandwidth     the number of Packets that the dest Router can accept
     *                      per time
     * @param duration      the number of simulation units
     */
    public Simulator(int numIntRouters, double arrivalProb, int maxBufferSize, int minPacketSize, int maxPacketSize,
            int bandwidth, int duration) {
        this.numIntRouters = numIntRouters;
        this.arrivalProb = arrivalProb;
        this.maxBufferSize = maxBufferSize;
        this.minPacketSize = minPacketSize;
        this.maxPacketSize = maxPacketSize;
        this.bandwidth = bandwidth;
        this.duration = duration;
        routers = new Router[numIntRouters];
        for (int i = 0; i < numIntRouters; i++) {
            routers[i] = new Router();
        }
        dispatcher = new Router();
    }

    /**
     * Prompts the user for input values and runs the simulation.
     * 
     * @param args the command line arguments for the program
     */
    public static void main(String[] args) {
        boolean isTerminated = false;
        Scanner inputScanner = new Scanner(System.in);
        while (!isTerminated) {
            System.out.println("Starting simulator...\n");
            try {
                System.out.print("Enter the number of Intermediate routers: ");
                int numRouters = inputScanner.nextInt();
                if (numRouters < 1) {
                    throw new IllegalArgumentException();
                }
                System.out.print("\nEnter the arrival probability of a packet: ");
                double probability = inputScanner.nextDouble();
                if (probability < 0) {
                    throw new IllegalArgumentException();
                }
                System.out.print("\nEnter the maximum buffer size of a router: ");
                int maxBuffer = inputScanner.nextInt();
                if (maxBuffer < 0) {
                    throw new IllegalArgumentException();
                }
                System.out.print("\nEnter the minimum size of a packet: ");
                int minSize = inputScanner.nextInt();
                if (minSize < 0) {
                    throw new IllegalArgumentException();
                }
                System.out.print("\nEnter the maximum size of a packet: ");
                int maxSize = inputScanner.nextInt();
                if (maxSize < 0) {
                    throw new IllegalArgumentException();
                }
                System.out.print("\nEnter the bandwidth size: ");
                int bandwidthSize = inputScanner.nextInt();
                if (bandwidthSize < 0) {
                    throw new IllegalArgumentException();
                }
                System.out.print("\nEnter the simulation duration: ");
                int simDuration = inputScanner.nextInt();
                if (simDuration < 0) {
                    throw new IllegalArgumentException();
                }
                inputScanner.nextLine();

                Simulator userSimulation = new Simulator(numRouters, probability, maxBuffer, minSize, maxSize,
                        bandwidthSize, simDuration);

                userSimulation.simulate();
            } catch (InputMismatchException ime) {
                System.out.println("Invalid input!\n");
                inputScanner.nextLine();
            } catch (IllegalArgumentException iae) {
                System.out.println("Invalid simulation value!\n");
                inputScanner.nextLine();
            }

            do {
                System.out.print("Do you want to try another simulation? (y/n): ");
                String userInput = inputScanner.nextLine();
                if (userInput.toUpperCase().equals("N")) {
                    isTerminated = true;
                    break;
                } else if (userInput.toUpperCase().equals("Y")) {
                    break;
                }
            } while (true);
        }
        inputScanner.close();
        System.out.println("\nProgram terminating successfully...");
    }

    /**
     * Runs the simuation and determines the average time it takes for a
     * Packet to arrive at the destination Router.
     * 
     * @return the average time it takes to arrive to the destination Router
     */
    public double simulate() {
        Packet.packetCount = 0;
        for (int i = 1; i <= duration; i++) {
            System.out.printf("Time: %d\n", i);
            for (int j = 0; j < MAX_PACKETS; j++) {
                if (Math.random() < arrivalProb) {
                    Packet.packetCount++;
                    Packet arrivedPacket = new Packet(randInt(minPacketSize, maxPacketSize), i);
                    dispatcher.enqueue(arrivedPacket);
                    System.out.printf("Packet %d arrives at dispatcher with size %d.\n", arrivedPacket.getId(),
                            arrivedPacket.getPacketSize());
                }
            }
            if (dispatcher.size() == 0) {
                System.out.println("No packets arrived.");
            }
            while (!dispatcher.isEmpty()) {
                int sendTo = Router.sendPacketTo(routers, maxBufferSize);
                if (sendTo == -1) {
                    System.out.printf("Network is congested. Packet %d is dropped.\n", dispatcher.dequeue().getId());
                    packetsDropped++;
                } else {
                    System.out.printf("Packet %d sent to Router %d.\n", dispatcher.peek().getId(), sendTo + 1);
                    routers[sendTo].enqueue(dispatcher.dequeue());
                }
            }
            for (int j = 0; j < numIntRouters; j++) {
                Packet currentRouter = routers[j].peek();
                if (routers[j].peek() == null) {
                    continue;
                }
                currentRouter.setTimeToDest(currentRouter.getTimeToDest() - 1);
                if (currentRouter.getTimeToDest() <= 0) {
                    // Packet represents the index of the Router
                    destQueue.enqueue(new Packet(0, j));
                }
            }

            for (int j = 0; j < bandwidth && !destQueue.isEmpty(); j++) {
                // This represents the index of a Router in routers[]
                int index = destQueue.dequeue().getTimeArrive();
                Packet leavingPacket = routers[index].dequeue();
                int timeChange = i - leavingPacket.getTimeArrive();
                System.out.printf("Packet %d has successfully reached its destination: +%d\n", leavingPacket.getId(),
                        timeChange);
                totalServiceTime += timeChange;
                totalPacketsArrived++;
            }
            for (int j = 0; j < numIntRouters; j++) {
                System.out.println("R" + (j + 1) + ": " + routers[j].toString());
            }
            System.out.println();
        }
        System.out.println("Simulation ending...");
        if (totalPacketsArrived == 0) {
            System.out.println("No packets ever arrived...");
            return 0;
        } else {
            System.out.printf("Total service time: %d\n", totalServiceTime);
            System.out.printf("Total packets served: %d\n", totalPacketsArrived);
            System.out.printf("Average service time per packet: %.02f\n",
                    (double) totalServiceTime / totalPacketsArrived);
            System.out.printf("Total packets dropped: %d\n\n", packetsDropped);
            return totalServiceTime / totalPacketsArrived;
        }
    }

    /**
     * Generates a random integer between the two input values.
     * 
     * @param minVal the minimum value of the random number
     * @param maxVal the maximum value of the ranodm number
     * @return the random number
     */
    private static int randInt(int minVal, int maxVal) {
        if (minVal == maxVal) {
            return minVal;
        }
        return (int) (Math.random() * (maxVal - minVal + 1)) + minVal;
    }

}
