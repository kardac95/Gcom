package GroupManagement;

public class Member {
    //variable:identifier
    private String name;
    //variable:IP address;
    private String address;

    public Member(String name, String address) {
        this.name = name;
        this.address = address;
    }

    //method:getID
    public String getName() {
        return name;
    }

    //method:getIP
    public String getAddress() {
        return address;
    }
}
