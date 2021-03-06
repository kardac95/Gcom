package gcom.groupmanagement;

import java.io.Serializable;

public class Member implements Serializable{
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

    @Override
    public boolean equals(Object o) {
        Member m = (Member)o;
        return (m.getAddress() + m.getPort()).equals(this.address + this.port);
    }
}
