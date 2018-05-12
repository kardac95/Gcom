package gcom.communication.rmi;


public class Main {
    public static void main(String args[]) {/*
        String localIp = null;
        try {
            InetAddress ipAddr = InetAddress.getLocalHost();
            localIp = ipAddr.getHostAddress();
            System.out.println(ipAddr.getHostAddress());
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }

        System.out.println(localIp);

        //Integer[] hostPorts = new Integer[]{1337, 1338, 1339, 1340};
        Member[] members = new Member[]{
                new Member("Kalle", localIp, "1337"),
                new Member("Felix", localIp, "1338"),
                new Member("Kg", localIp, "1339"),
                new Member("JJ", localIp, "1340"),

        };
        int myPort = Integer.parseInt(args[0]);

        Node n = new Node(myPort);
        System.out.println("my port: " + myPort);
        Member[] otherHosts = new Member[members.length-1];
        int i= 0;
        Member me = null;
        for (Member member:members) {
            if(Integer.parseInt(member.getPort()) != myPort) {
                otherHosts[i] = member;
                i++;
            } else {
                me = member;
            }
        }

        Group g = new Group("test");

        Arrays.stream(otherHosts).forEach(System.out::println);

        Scanner sc = new Scanner(System.in);
        System.out.println("Write message");
        while(sc.hasNextLine()) {
            Arrays.stream(otherHosts).forEach(m -> {
                System.out.println("Connect to " + m);
                n.connectToNode(m);
            });
            String message = sc.nextLine();
            Message m = new Message(g, me, message, "message", null);
            n.unReliableMulticast(m, otherHosts);

            while(n.getInQueue().peek() != null) {
                System.out.println(n.getInQueue().poll().getMessage());
            }
        }
    */}

}
