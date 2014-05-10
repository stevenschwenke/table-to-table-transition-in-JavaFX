
import javafx.beans.property.SimpleStringProperty;

public class UserFX {

    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty userId = new SimpleStringProperty();


    public UserFX(String nameUndAbteilungFormatiert, String userId) {
        super();
        this.name.set(nameUndAbteilungFormatiert);
        this.userId.set(userId);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        UserFX other = (UserFX) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (userId == null) {
            if (other.userId != null) {
                return false;
            }
        } else if (!userId.equals(other.userId)) {
            return false;
        }
        return true;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String newValue) {
        this.name.set(newValue);
    }

    public SimpleStringProperty userIdProperty() {
        return userId;
    }

    public String getUserId() {
        return userId.get();
    }

    public void setUserId(String newValue) {
        this.userId.set(newValue);
    }
}
