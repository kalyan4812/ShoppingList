package com.saikalyandaroju.shoppinglist.data.local.db.models;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "shopping_items")
public class ShoppingItem {
    @NonNull
    private String name;
    private String imageUrl;
    private int amount;
    private float price;

    @PrimaryKey(autoGenerate = true)
    private int id;



    public ShoppingItem(@NonNull String name, String imageUrl, int amount, float price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.amount = amount;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
