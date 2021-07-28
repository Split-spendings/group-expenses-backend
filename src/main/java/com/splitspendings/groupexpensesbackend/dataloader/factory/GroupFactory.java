package com.splitspendings.groupexpensesbackend.dataloader.factory;

import com.splitspendings.groupexpensesbackend.model.Group;
import org.springframework.stereotype.Component;

@Component
public class GroupFactory implements Factory<Group> {

    public static final String GROUP_NAME = "group";

    private int next_serial_number = 1;

    @Override
    public Group generate() {
        int serial_number = next_serial_number;
        Group group = new Group();
        group.setName(format(GROUP_NAME, serial_number));
        next_serial_number++;
        return group;
    }
}
