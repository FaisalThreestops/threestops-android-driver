package com.delivx.pojo.Cancel;

public class Reasons {
    private String reasons;

    private String res_id;

    private String _id;

    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getReasons ()
    {
        return reasons;
    }

    public void setReasons (String reasons)
    {
        this.reasons = reasons;
    }

    public String getRes_id ()
    {
        return res_id;
    }

    public void setRes_id (String res_id)
    {
        this.res_id = res_id;
    }

    public String get_id ()
    {
        return _id;
    }

    public void set_id (String _id)
    {
        this._id = _id;
    }
}
