package org.net.demo;


public class Seat {
    
    private String id;
    private int row; 
    private int col;
    private String type;
    private String status; 
    private Double basePrice;
    private String roomID;

 
    public Seat() {}

    
    public String getId() { return id; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public String getStatus() { return status; }
    public Double getBasePrice(){return basePrice ; }
    public String getRoomID()
    {return roomID;}
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}