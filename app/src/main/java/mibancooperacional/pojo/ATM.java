package mibancooperacional.pojo;

import java.io.Serializable;

public class ATM implements Serializable {
    private int id;
    private String name, address, postalCode, location;
    private float latitude, longitude;

    public ATM(int id, String name, String address, String postalCode, String location, float latitude, float longitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ATM() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "ATM{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", location='" + location + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}