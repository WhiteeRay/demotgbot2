package models;

public class City {
    private int id;
    private String place_id;
    private String city_name;


    public City(int id, String place_id, String city_name) {
        this.id = id;
        this.place_id = place_id;
        this.city_name = city_name;
    }
    public City(String place_id){
        this.place_id = place_id;
    }
    public int getId() {
        return id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void displayInfo(){
        System.out.println(id +  ". " + city_name);
    }
}
