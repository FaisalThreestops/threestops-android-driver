package com.delivx.adapter;

import com.delivx.pojo.AssignedAppointments;
import com.delivx.utility.Utility;

import java.util.Comparator;

public class SortSlotStartTimeCmp implements Comparator<AssignedAppointments> {

    @Override
    public int compare(AssignedAppointments o1, AssignedAppointments o2) {

//       return o1.getStoreId().compareTo(o2.getStoreId());
        return Utility.getDay(Long.parseLong(o1.getSlotStartTime())).compareTo(Utility.getDay(Long.parseLong(o2.getSlotStartTime())));


    }
}
