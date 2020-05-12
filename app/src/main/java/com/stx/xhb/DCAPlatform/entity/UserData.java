package com.stx.xhb.DCAPlatform.entity;


public class UserData {
    private String acount;
    private String passwd;
    private String image;

    public UserData(String acount, String passwd, String image) {
        this.acount = acount;
        this.passwd = passwd;
        this.image=image;
    }


    public String getAcount() {
        return acount;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getImage()  {return image;}

    public void setAcount(String acount){this.acount=acount;}
    public void setPasswd(String passwd){this.passwd=passwd;}
    public void setImage(String image){this.image=image;}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserData)) return false;

        UserData userData = (UserData) o;

        if (!getAcount().equals(userData.getAcount())) return false;
        return getPasswd().equals(userData.getPasswd());

    }

    @Override
    public int hashCode() {
        int result = getAcount().hashCode();
        result = 31 * result + getPasswd().hashCode();
        return result;
    }
}
