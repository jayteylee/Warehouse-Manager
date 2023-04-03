package cosc201.a2;

import java.math.BigInteger;


/**
 * Skeleton code for Part 1 of Assignment 2 in COSC201. Methods to be
 * filled in according to the specifications in the javadoc and/or the 
 * assignment PDF. Please check if it's at all unclear to you what is required.
 * 
 * @author <your name and ID number here>
 */
public class RouteCounter {

  public static final boolean DAMAGED = true;
  public static final boolean SOUND   = false; // SOUND means UNDAMAGED

  private int rows;
  private int cols;
  private boolean[][] damaged;

  /**
   * This constructor builds a grid given the rows and columns, with 
   * no damage (since boolean defaults to false, i.e., SOUND)
   * 
   * @param rows the number of rows
   * @param cols the number of columns
   */
  public RouteCounter(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
    this.damaged = new boolean[rows][cols];
  }

  /**
   * This constructor constructs the grid given the description as
   * a 2D-array of its damaged cells. Note that damaged[r][c] being true
   * means that location at row r and column c is damaged.
   * 
   * @param damaged the array describing damage locations
   */
  public RouteCounter(boolean[][] damaged) {
    this(damaged.length, damaged[0].length);
    for(int r = 0; r < rows; r++) {
      for(int c = 0; c < cols; c++) {
        this.damaged[r][c] = damaged[r][c];
      }
    }
  }

  /**
   * Make the given location damaged (you may assume the location
   * is valid for the grid).
   *
   * @param row the row index
   * @param col the column index
   */
  public void damage(int row, int col) {
    damaged[row][col] = DAMAGED;
  }

  /**
   * Repair the given location if necessary (you may assume the location
   * is valid for the grid).
   *
   * @param row the row index
   * @param col the column index
   */
  public void repair(int row, int col) {
    damaged[row][col] = SOUND;
  }

  /**
   * Counts the number of routes from the upper left to the lower right
   * of the corresponding grid (not using any damaged nodes).
   * 
   * @return the number of routes
   */
  public long countRoutes() {
    // Add a proper implementation of this method (4 points)
    // Note that this method might be called, then some locations
    // damaged or repaired, and then it might be called again. The
    // second call should recompute the routes based on the new
    // status of all locations.
    long[][] counts = new long[rows][cols];

    int a = 0;
      while(damaged[a][0] != DAMAGED){
        counts[a][0] = 1;
        a++;
      if(a == rows){
        break;
      }
      }
      for(int x = a; x < rows; x++){
      counts[x][0] = 0;
      }
    
      int b = 0;
      while(damaged[0][b] != DAMAGED){
        counts[0][b] = 1;
        b++;
      if(b == cols){
        break;
      }
      }
      for(int y = b; y < cols; y++){
      counts[0][y] = 0;
      }


    for(int r = 1; r < rows; r++) {
      for(int c = 1; c < cols; c++) {
        if(damaged[r][c] == DAMAGED){
          counts[r][c] = 0;
        }else{
        counts[r][c] = counts[r-1][c] + counts[r][c-1];
      }
    }
    }
    return counts[rows-1][cols-1];
  }

  /**
   * Counts the number of routes from the upper left to the lower right
   * of the corresponding grid (not using any damaged nodes) using
   * BigInteger to avoid overflow.
   * 
   * @return the number of routes as a BigInteger
   */
  public BigInteger bigCountRoutes() {
    // Add a proper implementation of this method (1 point)
    // See above for additional notes.

    BigInteger[][] counts = new BigInteger[rows][cols];

    int a = 0;
      while(damaged[a][0] != DAMAGED){
        counts[a][0] = BigInteger.valueOf(1);
        a++;
      if(a == rows){
        break;
      }
      }
      for(int x = a; x < rows; x++){
      counts[x][0] = BigInteger.valueOf(0);
      }
    
      int b = 0;
      while(damaged[0][b] != DAMAGED){
        counts[0][b] = BigInteger.valueOf(1);
        b++;
      if(b == cols){
        break;
      }
      }
      for(int y = b; y < cols; y++){
      counts[0][y] = BigInteger.valueOf(0);
      }


    for(int r = 1; r < rows; r++) {
      for(int c = 1; c < cols; c++) {
        if(damaged[r][c] == DAMAGED){
          counts[r][c] = BigInteger.valueOf(0);
        }else{
        counts[r][c] = counts[r-1][c].add(counts[r][c-1]);
      }
    }
    }
    return counts[rows-1][cols-1];
  }

  
  
public String toString(){
    StringBuilder result = new StringBuilder();
    for(int r = 0; r < rows; r++) {
      for(int c = 0; c < cols; c++) {
        if(damaged[r][c] == DAMAGED){
          result.append("D ");  
        }
        else{
          result.append("S ");   
        }
          
      }
      result.append("\n");
    }
    return result.toString();
  }
}
