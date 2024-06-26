package com.example.calendar.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cafes")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)//автоинкремент
    private Long idCafe;

    @Column(columnDefinition="varchar(1000)")
    private String description;

    @OneToOne
    private Rating rating;

    private int views;
    //private float rating;

    private String linkPhoto, name, address;

    private float money;

    private Time open, close;

    @ManyToMany
    private List<Dish> dishes;

    public static Iterable<Room> returnCommon(List<Room> nowDishesContains, List<Room> addDishesContains) {

        nowDishesContains.removeIf(cafe -> !addDishesContains.contains(cafe));
        return nowDishesContains;
    }

    public static List<Room> cafesToRemove(List<Room> cafes, List<Dish> dishList) {
        List<Room> cafesToRemove = new ArrayList<>();

        for (Room cafe: cafes){

            boolean a = false;
            for(Dish dish: dishList) {
                if (cafe.getDishes().contains(dish)) {a = true; break;}
            }
            if (!a) cafesToRemove.add(cafe);
        }

        return cafesToRemove;
    }

    public void updateMoney(){
        for(Dish dish: dishes){
            this.money += dish.getPrice();
        }
        this.money /= this.dishes.size();
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public Time getOpen() {
        return open;
    }

    public void setOpen(Time open) {
        this.open = open;
    }

    public Time getClose() {
        return close;
    }

    public void setClose(Time close) {
        this.close = close;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }


    public Long getIdCafe() {
        return idCafe;
    }

    public void setIdCafe(Long idCafe) {
        this.idCafe = idCafe;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        if(rating==null) return 0;
        return rating.getRating();
    }

    public float getAtmosphere() {
        if(rating==null) return 0;
        return rating.getAtmosphere();
    }

    public float getCookery() {
        if(rating==null) return 0;
        return rating.getCookery();
    }

    public float getService() {
        if(rating==null) return 0;
        return rating.getService();
    }

    public float getStaff() {
        if(rating==null) return 0;
        return rating.getStaff();
    }

    public float getPrice() {
        if(rating==null) return 0;
        return rating.getPrice();
    }

    public void setRating(Rating rating, int count) {
        //if(this.rating==null) this.initRating(new Rating());
        this.rating.setRating((this.rating.getRating() * count + rating.getRating())/(count+1));
        this.rating.setService((this.rating.getService() * count + rating.getService())/(count+1));
        this.rating.setStaff((this.rating.getStaff() * count + rating.getStaff())/(count+1));
        this.rating.setPrice((this.rating.getPrice() * count + rating.getPrice())/(count+1));
        this.rating.setAtmosphere((this.rating.getAtmosphere() * count + rating.getAtmosphere())/(count+1));
        this.rating.setCookery((this.rating.getCookery() * count + rating.getCookery())/(count+1));
    }

    public void initRating(Rating rating){
        this.rating = rating;
    }

    public String getLinkPhoto() {
        return linkPhoto;
    }

    public void setLinkPhoto(String linkPhoto) {
        this.linkPhoto = linkPhoto;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Room() {
    }

    public Room(String description, String linkPhoto, String name, String address) {
        this.description = description;
        this.linkPhoto = linkPhoto;
        this.name = name;
        this.address = address;
        this.views = 0;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void oneMore()
    {
        this.views+=1;
    }

    public void addDish(Dish dish) {
//        if(categories.contains(dish.getCategory()))
//        {dishes.add(dish);
//         makeUniqCat();}
//        else dishes.add(dish);

        if(!dishes.contains(dish)) dishes.add(dish);


    }



    public void makeUniqDish(){
        for (Dish dish: dishes) {
            if(dishes.indexOf(dish)!=dishes.lastIndexOf(dish)) dishes.remove(dish);
        }
    }

    public void delDish(Dish dish) { if(dishes.contains(dish)) dishes.remove(dish);    }


    @Override
    public String toString() {
        return "Cafe{" +
                " name='" + name + '\'' +
                '}';
    }

    public double getAverageDinnerCost() {
        double sum = 0.0;
        for (Dish d : dishes) {
            sum += d.getPrice();
        }
        double average = sum / dishes.size();
        BigDecimal roundedAverage = BigDecimal.valueOf(average).setScale(2, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
    }
}
