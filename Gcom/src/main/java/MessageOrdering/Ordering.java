package MessageOrdering;

import GroupManagement.Group;
import GroupManagement.Member;

public interface Ordering {

    abstract void messageGroup(Group group, Member sender, String message);


}
