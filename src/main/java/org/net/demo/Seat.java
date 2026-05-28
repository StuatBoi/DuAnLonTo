package org.net.demo;


public class Seat {
    
    private String id;
    private int row; 
    private int col; 
    private String status; 
    private Double price;
    private String roomID;

 
    public Seat() {}

    
    public String getId() { return id; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public String getStatus() { return status; }
    public Double getPrice(){return price ; }
    public String getRoomID()
    {return roomID;}
}