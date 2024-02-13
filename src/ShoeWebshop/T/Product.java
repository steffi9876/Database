package ShoeWebshop.T;

import java.util.List;

public class Product {
    private int index;
    private int id;
    private String color;
    private int price;
    private int size;
    private int balance;

    public Product(int id, String color, int price, int size, int balance) {
        this.id = id;
        this.color = color;
        this.price = price;
        this.size = size;
        this.balance = balance;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

}
