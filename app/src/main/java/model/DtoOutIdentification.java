package model;

public class DtoOutIdentification {
    private String image;
    private String id;

    public DtoOutIdentification(String image, String id){
        this.image = image;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}