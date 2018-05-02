package GroupManagement;

public class Member {
    private String name;
    private String address;
    private String port;

    public Member(String name, String address, String port) {
        this.name = name;
        this.address = address;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPort() {
        return port;
    }
}
