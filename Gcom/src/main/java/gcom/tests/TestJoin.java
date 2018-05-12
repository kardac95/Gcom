package gcom.tests;

import gcom.groupmanagement.GroupManager;
import gcom.groupmanagement.Member;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TestJoin implements Serializable{
    public static void main(String[] args) {
        String myName = args[0];
        String myPort = args[1];
        System.out.println(myName);
        String groupNameToJoin = "FanBoys";

        String localIp = null;
        try {
            InetAddress ipAddr = InetAddress.getLocalHost();
            localIp = ipAddr.getHostAddress();
            System.out.println(ipAddr.getHostAddress());
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
        Member me = new Member(myName, localIp, myPort);

        GroupManager gm = new GroupManager(me);
        if(args.length == 3) {
            gm.createGroup(args[2]);
        } else {
            gm.joinGroupRequest(new Member("kalle", localIp, "1337"), groupNameToJoin);
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("Write message");
        while(sc.hasNextLine()) {
            String m = sc.nextLine();
            gm.messageGroup(m, me, groupNameToJoin);
        }
    }
}
