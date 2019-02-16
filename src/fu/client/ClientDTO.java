/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fu.client;

/**
 *
 * @author hello
 */
public class ClientDTO {

    private int id;
    private String username;
    private String password;
    private String fullname;
    private boolean state;
    private byte[] avarta;

    public ClientDTO() {
    }

    public ClientDTO(int id, String username, String password, String fullname, boolean state, byte[] avarta) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.state = state;
        this.avarta = avarta;
    }

 

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the fullname
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * @param fullname the fullname to set
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * @return the state
     */
    public boolean isState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(boolean state) {
        this.state = state;
    }

    /**
     * @return the avarta
     */
    public byte[] getAvarta() {
        return avarta;
    }

    /**
     * @param avarta the avarta to set
     */
    public void setAvarta(byte[] avarta) {
        this.setAvarta(avarta);
    }

    @Override
    public String toString() {
        return fullname;
    }
}
