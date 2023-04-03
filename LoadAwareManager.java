package cosc201.a2;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.PriorityQueue;


public class LoadAwareManager implements PacketManager {

    private Warehouse w;
    
public LoadAwareManager(Location home, Warehouse w){
    this.home = home;
    this.w  = w;
}

private ArrayDeque<Packet> queue = new ArrayDeque<>();
private Location home;

@Override
public int size(){
    return queue.size();
}

@Override
public Packet sendPacket(){
    Packet p = null;
    if(this.isEmpty()){
        return null;
    }else{
      p = queue.remove();
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
    if (!p.destination.equals(this.home))queue.add(p);
}

@Override
public Collection<Packet> packetsHeld() {
    return queue;
}

@Override
public PriorityQueue<Packet> priorityPacketsHeld(){
    return null;
}

}
    
