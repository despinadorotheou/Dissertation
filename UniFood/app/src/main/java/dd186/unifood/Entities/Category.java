package dd186.unifood.Entities;

public class Category {

    private int id;
    private String category;

    public Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Category() {
    }

}
