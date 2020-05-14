package com.example.snapandeat;

public class Report implements java.io.Serializable{

    private int id;
    private int userId;
    private String restaurant;
    private String calories;
    private String carbs;
    private String fats;
    private String proteins;
    private String date;
    private String itemName;
    private String imageUri;

    public Report(){

    }

    public Report(int id, int userId, String restaurant, String calories, String carbs,
                  String fats, String proteins, String date, String itemName, String imageUri){
        this.id = id;
        this.userId = userId;
        this.restaurant = restaurant;
        this.calories = calories;
        this.carbs = carbs;
        this.fats = fats;
        this.proteins = proteins;
        this.date = date;
        this.itemName = itemName;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getFats() {
        return fats;
    }

    public void setFats(String fats) {
        this.fats = fats;
    }

    public String getProteins() {
        return proteins;
    }

    public void setProteins(String proteins) {
        this.proteins = proteins;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
