package db;

import db.interfaces.ICityDatabase;
import exceptions.CityNotFoundException;
import models.City;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;



public class CityDatabase implements ICityDatabase {
    private Connection con = null;

    private HashMap<Integer, City> cityCache = new HashMap<>();


    @Override
    public void connect(String connectionUrl, String user, String pass){
        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(connectionUrl,user,pass);
        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }



    @Override
    public ArrayList<City> getAllCities() throws SQLException {
        ArrayList <City> cities = new ArrayList<>();
        try{
            String sql = "Select id, place_id,city_name FROM cities";
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                String place_id = rs.getString("place_id");
                String city_name = rs.getString("city_name");
                City city = new City(id,place_id,city_name);
                cities.add(city);
                cityCache.put(id,city);
            }
            st.close();
            rs.close();

            return cities;

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public String getCityById(int id){
        try{
            String sql = "Select id,place_id,city_name FROM cities WHERE id=?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1,id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                String place_id = rs.getString("place_id");
                String city_name = rs.getString("city_name");
                City city = new City(id,place_id,city_name);
                cityCache.put(id,city);

                return place_id;

            }else{
                throw new CityNotFoundException("City with Id " + id + " not found.");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        } catch (CityNotFoundException e) {
            System.out.println(e.getMessage());;
        }
        return null;
    }

    @Override
    public String getCityNameById(int id){
        try{
            String sql = "Select id,place_id,city_name FROM cities WHERE id=?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1,id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                String place_id = rs.getString("place_id");
                String city_name = rs.getString("city_name");
                City city = new City(id,place_id,city_name);
                cityCache.put(id,city);

                return city_name;

            }else{
                throw new CityNotFoundException("City with Id " + id + " not found.");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        } catch (CityNotFoundException e) {
            System.out.println(e.getMessage());;
        }
        return null;
    }

}
