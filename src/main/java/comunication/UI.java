package comunication;

import com.google.gson.Gson;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Base64;

public class UI {
   static Application App = new Application();
    static AuthServer authServer = new AuthServer();

    static {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println("################################################################");
        System.out.println("GET DATA WITHOUT TOKEN");
        try {
            System.out.println(App.getData("NO KEY"));
        } catch (Exception ex) {

            System.out.println(ex.toString());
        }
        System.out.println("################################################################");

        System.out.println("REDIRECT TO OAUTH PROVIDER  ");
        AuthToken authToken = new AuthToken("", "RandomUser", "Date", "Date+3600", "Owner", "#####");
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(authToken.getUserid().getBytes(StandardCharsets.UTF_8));
        authToken.setId(Base64.getEncoder().encodeToString(hash));
        System.out.println("CREATING TOKEN ---->" + authToken);
        System.out.println("SAVE TOKEN ID ---->" + authToken.getId());
        try {
            System.out.println(AuthServer.recordToken(authToken));
        } catch (InvalidArgumentException | ProposalException | ChaincodeEndorsementPolicyParseException | IOException e) {
            e.printStackTrace();
        }

        System.out.println("################################################################");


        System.out.println("GET DATA USING TOKEN ---->" + authToken.getId());
        try {
            System.out.println(App.getData(authToken.getId()));
        } catch (Exception ex) {

            System.out.println(ex.toString());
        }
        System.out.println("################################################################");

    }
    public void test() throws NoSuchAlgorithmException { System.out.println("################################################################");
        System.out.println("GET DATA WITHOUT TOKEN");
        try {
            System.out.println(App.getData("NO KEY"));
        } catch (Exception ex) {

            System.out.println(ex.toString());
        }
        System.out.println("################################################################");

        System.out.println("REDIRECT TO OAUTH PROVIDER  ");
        AuthToken authToken = new AuthToken("", "RandomUser", "Date", "Date+3600", "Owner", "#####");
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(authToken.getUserid().getBytes(StandardCharsets.UTF_8));
        authToken.setId(Base64.getEncoder().encodeToString(hash));
        System.out.println("CREATING TOKEN ---->" + authToken);
        System.out.println("SAVE TOKEN ID ---->" + authToken.getId());
        try {
            System.out.println(AuthServer.recordToken(authToken));
        } catch (InvalidArgumentException | ProposalException | ChaincodeEndorsementPolicyParseException | IOException e) {
            e.printStackTrace();
        }

        System.out.println("################################################################");


        System.out.println("GET DATA USING TOKEN ---->" + authToken.getId());
        try {
            System.out.println(App.getData(authToken.getId()));
        } catch (Exception ex) {

            System.out.println(ex.toString());
        }
        System.out.println("################################################################");

    }
}
