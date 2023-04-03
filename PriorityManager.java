package cosc201.a2;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;


public class PriorityManager implements PacketManager {

    private Warehouse w;
    
public PriorityManager(Location home, Warehouse w){
    this.home = home;
    this.w  = w;
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
        p.current.col += 1;
    }else if(p.current.row == p.destination.row && p.current.col > p.destination.col){
        p.current.col -= 1;
    }else if(p.current.col == p.destination.col && p.current.row < p.destination.row){
        p.current.row += 1;
    }else if(p.current.col == p.destination.col && p.current.row > p.destination.row){ 
        p.current.row -= 1;   
    }else{
        if(w.getManager(getVertPosition(p)).size() < w.getManager(getHorizPosition(p)).size() || w.getManager(getVertPosition(p)).size() == w.getManager(getHorizPosition(p)).size()){
            p.current.row = getVertPosition(p).row;
            p.current.col = getVertPosition(p).col;
        }else if(w.getManager(getVertPosition(p)).size() > w.getManager(getHorizPosition(p)).size()){
            p.current.row = getHorizPosition(p).row;
            p.current.col = getHorizPosition(p).col;
        }
       // if(w.getManager(new Location(p.current.row+1,p.current.col)).size() < w.getManager(new Location(p.current.row,p.current.col+1)).size()){ //south of current)
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
