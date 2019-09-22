package comunication;

public class AuthToken {
    String id;
    String userid;
    String timestamp;
    String expirationtimestamp;
    String type;
    String key;

    public AuthToken(String id, String userid, String timestamp, String expirationtimestamp, String type, String key) {
        this.id = id;
        this.userid = userid;
        this.timestamp = timestamp;
        this.expirationtimestamp = expirationtimestamp;
        this.type = type;
        this.key = key;
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                "id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", expirationtimestamp='" + expirationtimestamp + '\'' +
                ", type='" + type + '\'' +
                ", id='" + key + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getExpirationtimestamp() {
        return expirationtimestamp;
    }

    public void setExpirationtimestamp(String expirationtimestamp) {
        this.expirationtimestamp = expirationtimestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
