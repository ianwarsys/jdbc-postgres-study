package main.java;

import main.db.DB;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {

        String sql = "select * from tb_product";
        ResultSet rs;
        try(PreparedStatement ps = DB.getConnection().prepareStatement(sql)){
            rs = ps.executeQuery();

            while(rs.next()) {
                System.out.println(rs.getString("name"));
                System.out.println(rs.getDouble("price"));
                System.out.println(rs.getString("description"));
                System.out.println(rs.getString("image_uri"));

                System.out.println();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
