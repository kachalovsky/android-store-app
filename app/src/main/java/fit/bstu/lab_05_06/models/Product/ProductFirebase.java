package fit.bstu.lab_05_06.models.Product;

/**
 * Created by andre on 13.11.2017.
 */

public class ProductFirebase {
    private String name;
    private Double price;
    private Integer count;
    private Boolean isSaved = false;
    private String imgPath;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    private String userEmail;
    private String identifier;

    public ProductFirebase() {}

    public ProductFirebase(String identifier, String name, Double price, Integer count, Boolean isSaved, String imgPath) {
        this.identifier = identifier;
        this.name = name;
        this.price = price;
        this.count = count;
        this.isSaved = isSaved;
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getSaved() {
        return isSaved;
    }

    public void setSaved(Boolean saved) {
        isSaved = saved;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

}
