package cosc201.a2;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * A diagonal manager that maintains a priority queue of packets based on how far the packets have to travel
 * (the further it has to travel, the higher priority) and just moves the packet of highest priority
 * that's not at its destination to an adjoining cell. If the packet is in a straight line from it's destination it just sends one step
 * in the appropriate direction. If the packet is not in a straight line from it's destination, it will 
 * move diagonally 1 step towards it's destination.
 * 
 * @author Jay Lee
 */

public class DiagonalManager implements PacketManager {

    
public DiagonalManager(Location home){
    this.home = home;
}

private PriorityQueue<Packet> pqueue = new PriorityQueue<Packet>(new sortByPriority());
private Location home;


public class sortByPriority implements Comparator<Packet> {

    public int compare (Packet a, Packet b){
        if(a.distance(a) < b.distance(b)){
            return -1;
        }else if(a.distance(a) > b.distance(b)){
            return 1;
        }else{
        return 0;
    }
}
}




@Override
public int size(){
    return pqueue.size();
}

@Override
public Packet sendPacket(){
    Packet p = null;
    if(this.isEmpty()){
        return null;
    }else{
      p = pqueue.remove();
    }
    if(p.current.row == p.destination.row && p.current.col < p.destination.col){
        p.current.col += 1; //moves right
    }else if(p.current.row == p.destination.row && p.current.col > p.destination.col){
        p.current.col -= 1; //moves left
    }else if(p.current.col == p.destination.col && p.current.row < p.destination.row){
        p.current.row += 1; //moves up
    }else if(p.current.col == p.destination.col && p.current.row > p.destination.row){ 
        p.current.row -= 1;   //moves down
    }else if(p.current.col < p.destination.col && p.current.row < p.destination.row){ 
        p.current.row += 1;   //moves diagonally bottom right
        p.current.col += 1;   
    }else if(p.current.col < p.destination.col && p.current.row > p.destination.row){ 
        p.current.row -= 1;   //moves diagonally up right
        p.current.col += 1; 
    }else if(p.current.col > p.destination.col && p.current.row > p.destination.row){ 
        p.current.row -= 1;   //moves diagonally up left
        p.current.col -= 1; 
    }else{
        p.current.row += 1;   //moves diagonally down left
        p.current.col -= 1; 
      
       }
        return p;
    }


public Location getVertPosition(Packet p){
    if(p.current.row < p.destination.row){
        return new Location(p.current.row+1, p.current.col);
    }else{
        return new Location(p.current.row-1, p.current.col);
    }
}

public Location getHorizPosition(Packet p){
    if(p.current.col < p.destination.col){
        return new Location(p.current.row, p.current.col+1);
    }else{
        return new Location(p.current.row, p.current.col-1);
    }
}



@Override
public void receivePacket(Packet p){
    if (!p.destination.equals(this.home))pqueue.add(p);

}

@Override
public Collection<Packet> packetsHeld() {
    return pqueue;
}

@Override
public PriorityQueue<Packet> priorityPacketsHeld(){
    return null;
}
}
