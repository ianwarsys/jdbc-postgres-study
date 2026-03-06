package main.java;

import main.db.DB;
import main.java.entities.Order;
import main.java.entities.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.*;

public class Main {
    public static void main(String[] args) throws SQLException {

        String sql = "select * from tb_order " +
                " INNER JOIN tb_order_product ON tb_order.id = tb_order_product.order_id" +
                " INNER JOIN tb_product ON tb_order_product.product_id = tb_product.id";
        ResultSet rs;

        Map<Long, Order> orders_map = new HashMap<>();
        Map<Long, Product> products_map = new HashMap<>();

        try (PreparedStatement ps = DB.getConnection().prepareStatement(sql)) {
            rs = ps.executeQuery();

            while (rs.next()) {


                //
                // Holds the order id of the tb_order_product
                Long order_id = rs.getLong("order_id");

                // If there isn't an Order object mapped from this table row it will be created a new respective instance and added to a map data structure of mapped orders
                if (orders_map.get(order_id) == null) {
                    Order order = instantiateOrder(rs);
                    orders_map.put(order_id, order);
                }

                // Holds the product id of the tb_order_product
                Long product_id = rs.getLong("product_id");

                //If there isn't a Product object mapped from this table row it will be created a new respective instance and added to a map data structure of mapped products
                if (products_map.get(product_id) == null) {
                    Product product = instantiateProduct(rs);
                    products_map.put(product.getId(), product);
                }

                orders_map.get(order_id).getProducts().add(products_map.get(product_id));
            }

            orders_map.keySet().forEach(key -> {
                Order order = orders_map.get(key);
                System.out.println(order);
                order.getProducts().forEach(System.out::println);
                System.out.println();
            });


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Order instantiateOrder(ResultSet rs) throws SQLException {
        Long id = rs.getLong("order_id");
        Double latitude = rs.getDouble("latitude");
        Double longitude = rs.getDouble("longitude");
        Instant moment = rs.getTimestamp("moment").toInstant();
        Status status = Status.values()[rs.getInt("status")];


        return new Order(id, latitude, longitude, moment, status);

    }

    public static Product instantiateProduct(ResultSet rs) throws SQLException {
        Long id = rs.getLong("product_id");
        String name = rs.getString("name");
        Double price = rs.getDouble("price");
        String description = rs.getString("description");
        String image_url = rs.getString("image_uri");

        return new Product(id, name, price, description, image_url);
    }
}
