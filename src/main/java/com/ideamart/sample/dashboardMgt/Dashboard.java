package com.ideamart.sample.dashboardMgt;

/**
 * Created by tharinda on 12/28/16.
 */
public class Dashboard {

    private String date;
    private int reg;
    private int unReg;
    private int pending;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getReg() {
        return reg;
    }

    public void setReg(int reg) {
        this.reg = reg;
    }

    public int getUnReg() {
        return unReg;
    }

    public void setUnReg(int unReg) {
        this.unReg = unReg;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }
}
