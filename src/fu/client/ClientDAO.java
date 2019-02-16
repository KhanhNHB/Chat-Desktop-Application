/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fu.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import fu.util.DBUtils;

/**
 *
 * @author hello
 */
public class ClientDAO {

    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    private void closeConnection() throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (stmt != null) {
            stmt.close();
        }
        if (con != null) {
            con.close();
        }
    }

    public boolean createAccount(ClientDTO dto) throws SQLException {
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                String sql = "INSERT INTO dbo.Client(username, password, fullname, state, avarta) VALUES(?,?,?,?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, dto.getUsername());
                stmt.setString(2, dto.getPassword());
                stmt.setString(3, dto.getFullname());
                stmt.setBoolean(4, dto.isState());
                stmt.setBytes(5, dto.getAvarta());
                int row = stmt.executeUpdate();
                if (row > 0) {
                    return true;
                }
            }
        } finally {
            closeConnection();
        }
        return false;
    }

    public ClientDTO checkLogin(String username, String password) throws SQLException {
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                String sql = "SELECT id, username, password, fullname, state, avarta FROM Client WHERE username = ? AND password = ?";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, password);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String pass = rs.getString("password");
                    String fullname = rs.getString("fullname");
                    String user = rs.getString("username");
                    Boolean state = rs.getBoolean("state");
                    byte[] avarta = rs.getBytes("avarta");
                    ClientDTO dto = new ClientDTO(id, user, pass, fullname, state, avarta);
                    return dto;
                }
            }
        } finally {
            closeConnection();
        }
        return null;
    }

    public boolean updateClientImage(ClientDTO dto, byte[] image_person) throws SQLException {
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                String sql = "UPDATE dbo.Client SET avarta = ? WHERE username = ? AND password = ?";
                stmt = con.prepareStatement(sql);
                stmt.setBytes(1, image_person);
                stmt.setString(2, dto.getUsername());
                stmt.setString(3, dto.getPassword());

                int row = stmt.executeUpdate();
                if (row > 0) {
                    return true;
                }
            }
        } finally {
            closeConnection();
        }
        return false;
    }

    public ClientDTO getClientByUsername(String user) throws SQLException {
        try {
            con = DBUtils.getConnection();
            if (con != null) {
                String sql = "SELECT username, password, fullname, state, avarta FROM Client WHERE username = ?";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, user);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String fullname = rs.getString("fullname");
                    boolean state = rs.getBoolean("state");
                    byte[] avarta = rs.getBytes("avarta");
                    ClientDTO dto = new ClientDTO(0, username, password, fullname, state, avarta);
                    return dto;
                }
            }
        } finally {
            closeConnection();
        }
        return null;
    }
}
