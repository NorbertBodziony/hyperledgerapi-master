package comunication;

public class ID {
    String id;

    @Override
    public String toString() {
        return "ID{" +
                "id='" + id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ID(String id) {
        this.id = id;
    }
}
