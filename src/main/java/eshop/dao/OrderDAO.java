package eshop.dao;

import eshop.model.Order;

import java.util.List;

public interface OrderDAO {

    void save(Order order);

    List<Order> getAll();
}
