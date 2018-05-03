package communication.rmi;
import com.sun.xml.internal.bind.v2.model.util.ArrayInfoUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;



public class Main {

    static Integer[] hostPorts = new Integer[]{1337, 1338, 1339, 1440};
    public static void main(String args[]) {
        int myPort = Integer.parseInt(args[0]);
        Node n = new Node(myPort);
        Integer[] otherHosts = ArrayUtils.removeAllOccurences(hostPorts, myPort);
        Arrays.stream(otherHosts).forEach(System.out::println);



        Scanner sc = new Scanner(System.in);
        System.out.println("Write message");
        while(sc.hasNextLine()) {
            Arrays.stream(otherHosts).forEach(i -> {
                n.connectToNode(i);
            });
            String message = sc.nextLine();
            n.unReliableMulticast(message, ArrayUtils.remove(otherHosts, (int)(Math.random()*3)));
        }
    }

}
