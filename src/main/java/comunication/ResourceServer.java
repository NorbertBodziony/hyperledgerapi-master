package comunication;

import java.security.Security;

public class ResourceServer {
    static {
        try
    {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }catch (Exception e)
    {

    }
}
    static AuthServer authServer=new AuthServer();

    public ResourceServer() {
    }

    public static String getData(String id , String key)

    {
        if(authServer.verifyAccess(id,key))
        {
            return "Access Granted Your data";
        }
        else
        {
            return "Access Forbidden";
        }

    }
}
