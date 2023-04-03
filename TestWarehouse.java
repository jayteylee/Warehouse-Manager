package cosc201.a2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * A randomly managed warehouse. How many packages are successfully delivered?
 * 
 * One thing to note - to model the synchronous behaviour we collect all the
 * deliveries in a queue and only once we have them all do we deliver to their
 * destination. This ensures that we don't get a package from a cell that has
 * only been delivered there in the current round.
 * 
 */
public class TestWarehouse {

  final Random R = new Random();

  double numRounds = 0;
  double distance = 0;
  int rows;
  int cols;
  int totalPackets;
  HashMap<Location, PacketManager> robots = new HashMap<>();
  ArrayList<Packet> packets = new ArrayList<Packet>();
  ArrayList<Location> locations = new ArrayList<Location>();
  int delivered = 0;

  Warehouse w;

  /**
   * Command line arguments - rows then cols then total number of packages
   */
  public double run() {

    w = new Warehouse();

    rows = 100;
    cols = 100;
    totalPackets = rows * cols;

    addRobots();
    addPackets(totalPackets);
    letChaosEnsue();
    distance = MaxDistance(packets);

    System.out.println("Test " + numRounds);
    System.out.println("Test " + distance);

    return distance / numRounds;
  }

  private void addRobots() {
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        w.managers.put(new Location(r, c), new BasicManager(new Location(r, c)));
        //w.managers.put(new Location(r, c), new LoadAwareManager(new Location(r, c),w));
        //w.managers.put(new Location(r, c), new PriorityManager(new Location(r, c),w));
        //w.managers.put(new Location(r, c), new DiagonalManager(new Location(r, c)));
      }
    }
  }

  private void addPackets(int n) {
    for (int i = 0; i < n; i++){
    //  if(R.nextInt(4) == 0){
        sameDestAddPacket();
    //  }else{
     //   totalPackets--;
      //}
    }

  }

  /** random, random case */
  private void randomAddPacket() {
    Packet p = new Packet();
    p.contents = "" + p.ID;
    p.current = randomLocation();
    p.destination = randomLocation();
    if (p.destination.equals(p.current))
      delivered++;
    w.managers.get(p.current).receivePacket(p);
  }

  /**
   * num packets and cells is the same, start upper left and different
   * destinations
   */
  private void upperLeftAddPacket() {
    Packet p = new Packet();
    p.contents = "" + p.ID;
    p.current = new Location(0, 0);
    p.destination = differentLocation();
    if (p.destination.equals(p.current))
      delivered++;
    w.managers.get(p.current).receivePacket(p);
  }

    /**
   * num packets and cells is the same, start random and same
   * destinations
   */
  private void sameDestAddPacket() {
    Packet p = new Packet();
    p.contents = "" + p.ID;
    p.current = differentLocation();
    p.destination = new Location(0, 0);
    if (p.destination.equals(p.current))
      delivered++;
    w.managers.get(p.current).receivePacket(p);
  }

    /**
   * num packets and cells is the same, start middle and different
   * destinations
   */
  private void middleAddPacket() {
    Packet p = new Packet();
    p.contents = "" + p.ID;
    p.current = new Location(rows/2, cols/2);
    p.destination = differentLocation();
    if (p.destination.equals(p.current))
      delivered++;
    w.managers.get(p.current).receivePacket(p);
  }



  /**
   * The number of packets and cells is the same. Each cell contains one packet to
   * begin with, and has a random destination (different packets might have the
   * same
   * destination).
   */
  private void differentRandomAddPacket() {
    Packet p = new Packet();
    p.contents = "" + p.ID;
    p.current = differentLocation();
    p.destination = randomLocation();
    if (p.destination.equals(p.current))
      delivered++;
    w.managers.get(p.current).receivePacket(p);
  }


  /** packets in all different location no overlap */
  private Location differentLocation() {
    Location random = new Location(R.nextInt(rows), R.nextInt(cols));
    while (locations.contains(random)) {
      random = new Location(R.nextInt(rows), R.nextInt(cols));
    }
    locations.add(random);
    return random;
  }

  /** default all random */
  private Location randomLocation() {
    return new Location(R.nextInt(rows), R.nextInt(cols));
  }

  private void letChaosEnsue() {
    System.out.println("Initial configuration");
    System.out.println("Basic Manager");
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        Location l = new Location(r, c);
        System.out.println(w.managers.get(l).packetsHeld());
      }
    }
    // System.out.println("-----------------------");
    int packetsDropped = 0;
    while (packetsDropped + delivered < totalPackets) {
      // System.out.println("Starting a round of deliveries");
      ArrayDeque<Packet> deliveryQueue = new ArrayDeque<>();
      for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
          Packet p = w.managers.get(new Location(r, c)).sendPacket();
          if (p != null) {
            if (isValidLocation(p.current)) {
              if (p.current.equals(p.destination)) {
                delivered++;
                System.out.println("Packet " + p.ID + " has been delivered!");
                p.distanceTraveled++;
                packets.add(p);

                System.out.println("Packet " + p.ID + " traveled " + p.distanceTraveled);
              } else {
                System.out.println("Packet " + p.ID + " is moving to " + p.current);
                deliveryQueue.add(p);
                p.distanceTraveled++;
              }
            } else {
              System.out.println("Packet " + p.ID + " has been dropped!");
              packetsDropped++;
            }
          }
        }
      }
      for (Packet p : deliveryQueue) {
        w.managers.get(p.current).receivePacket(p);
      }
       System.out.println("Round complete");
      numRounds++;
      System.out.println(numRounds);
       System.out.println("---------");
    }
    System.out.println("Packets delivered = " + delivered);
    System.out.println("Packets dropped = " + packetsDropped);
    System.out.println("Number of rounds = " + numRounds);
    System.out.println("Max Distance traveled by a packet = " + MaxDistance(packets));
  }

  private boolean isValidLocation(Location l) {
    return 0 <= l.row && l.row < rows && 0 <= l.col && l.col < cols;
  }

  public double MaxDistance(ArrayList<Packet> p) {
    for (Packet packet : p) {
      if (packet.distanceTraveled > distance) {
        distance = packet.distanceTraveled;
      }
    }
    return distance;
  }
}
