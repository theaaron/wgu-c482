package model;

/**
 *
 * Model for an InHouse part
 *
 * @author Aaron
 *
 * */

public class InHouse extends Part {

    private int machineId;
    /**
     * Constructs an InHouse Part
     * @param id part id
     * @param name part name
     * @param price part price
     * @param stock part stock
     * @param min part min
     * @param max part max
     * @param machineId part machine id
     * */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }
    /**
     * @return machineId
     * */
    public int getMachineId() {
        return machineId;
    }
    /**
     * @param newMachId  sets new machineId
     * */
    public void setMachineId(int newMachId) {
        this.machineId = newMachId;
    }
}
