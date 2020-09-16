package com.driver.threestops.app.main.help_center.zendeskpojo;

import java.io.Serializable;

/**
 * <h>SpinnerRowItem</h>
 * Created by Ali on 12/29/2017.
 */

public class SpinnerRowItem implements Serializable
{
    private int colorId;
    private String priority;

    public SpinnerRowItem(int colorId, String priority) {
        this.colorId = colorId;
        this.priority = priority;
    }

    public int getColorId() {
        return colorId;
    }

    public String getPriority() {
        return priority;
    }
}
