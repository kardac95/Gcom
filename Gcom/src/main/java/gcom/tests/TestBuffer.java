package gcom.tests;

import gcom.messageordering.VectorClock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TestBuffer {
    public static void main(String[] args) {
        List <VectorClock> buffer= new ArrayList();

        for (int i = 10; i > 0; i--) {
            VectorClock v = new VectorClock("1");
            for (int j = 0; j < i; j++) {
                v.inc();
            }
            buffer.add(v);
        }
        System.out.println("Unsorted list");
        for (int i = 0; i < buffer.size(); i++) {
            System.out.println(buffer.get(i).getValue("1"));
        }
        //buffer.sort((m1,m2) -> (int)(m1.getValue("1") - m2.getValue("1")));
        buffer.sort((m1,m2) -> m1.isBefore(m2) ? -1:1);

        System.out.println("Sorted list");
        for (int i = 0; i < buffer.size(); i++) {
            System.out.println(buffer.get(i).getValue("1"));
        }

        /*System.out.println("start retard test");
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("Hej");
        list.add("då");
        list.add("ok");
        list.add("YO");
        System.out.println(list.size());
        list.forEach(item -> {
            System.out.println(item);
            list.remove(item);
        });
        System.out.println(list.size());
        */

    }
}
