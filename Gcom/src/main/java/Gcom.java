import GroupManagement.GroupManager;
import GroupManagement.Member;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Gcom {
    private GroupManager GM;
    private Member member;

    public Gcom(String memberName, String port) {
        this.GM = new GroupManager();
        try {
            this.member = new Member(memberName, InetAddress.getLocalHost().getHostAddress(), port);
        } catch (UnknownHostException e) {
            System.err.println("Failed to fetch host address.");
            e.printStackTrace();
        }
    }

    public boolean joinGroup(String groupName) {
        GM.joinGroup(groupName, member);
        return false;
    }

    public boolean leaveGroup() {
        return false;
    }


}
