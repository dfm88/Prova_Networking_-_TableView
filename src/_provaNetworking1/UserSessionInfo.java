package _provaNetworking1;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class UserSessionInfo {

    private Preferences userPref;

    public UserSessionInfo()
    {
        this.userPref = Preferences.userRoot();
    }

    public void settaUserCompleto(int id, String username, String password, boolean isAd)
    {
        setUserSessionID(id);
        setUserSessionName(username);
        setUserSessionPassword(password);
        setUserSessionIsAdmin(isAd);

    }

    public void setUserSessionID(int id)
    {
        userPref.putInt("userIDSession", id);
    }

    public void setUserSessionName(String uName)
    {
        userPref.put("userNameSession",uName);
    }


    public void setUserSessionPassword(String uPass)
    {
        userPref.put("userPasswordSession",uPass);
    }

    public void setUserSessionIsAdmin(Boolean isAd)
    {
         userPref.putBoolean("userIsAdminSession", isAd);

    }

    public String getUserSessionName()
    {
        return userPref.get("userNameSession",null);
    }

    public int getUserSessionID()
    {
        return userPref.getInt("userIDSession",-1);
    }

    public String getUserSessionPassword()
    {
        return userPref.get("userPasswordSession",null);
    }

    public String getUserSessionIsAdmin()
    {
        return (userPref.get("userIsAdminSession",null));
    }

    public void logOutUser() throws BackingStoreException {
        userPref.clear();
    }

    public Preferences getUserPref() {
        return userPref;
    }

    public void setUserPref(Preferences userPref) {
        this.userPref = userPref;
    }
}
