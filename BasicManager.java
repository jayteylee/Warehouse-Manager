package cosc201.a2;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.PriorityQueue;

public class BasicManager implements PacketManager {

public BasicManager(Location home){
    this.home = home;
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
    while (!this.isEmpty()) {
      p = queue.remove();
      if (!p.destination.equals(this.home)) break; 
      // We have found a packet that needs to move
    }
      if (p == null || p.destination.equals(this.home)) return null; 
      // No packets to move
    
    if(p.current.row == p.destination.row && p.current.col < p.destination.col){
        p.current.col += 1;
    }else if(p.current.row == p.destination.row && p.current.col > p.destination.col){
        p.current.col -= 1;
    }else if(p.current.row < p.destination.row){
        p.current.row += 1;
    }else{
        p.current.row -= 1;
    }
    return p;
}

@Override
public void receivePacket(Packet p){
    queue.add(p);
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
