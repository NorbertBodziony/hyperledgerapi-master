package comunication;

import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Base64;

public class PerformanceTest {

        static {
            try
            {
                Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            }catch (Exception e)
            {

            }
        }
        static Application App=new Application();
        static AuthServer authServer=new AuthServer();

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidArgumentException, ProposalException, IOException, ChaincodeEndorsementPolicyParseException {

        AuthToken authToken=new AuthToken("","RandomUser","Date","Date+3600","Owner","#####");
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(authToken.getUserid().getBytes(StandardCharsets.UTF_8));
        authToken.setId(Base64.getEncoder().encodeToString(hash));
        {
            long startTime = System.currentTimeMillis();
            for (int i=0;i<300 ;i++) {
                authServer.recordToken(authToken);
            }
            long estimatedTime = System.currentTimeMillis() - startTime;
            System.out.println(estimatedTime/300000+"T/s");
        }
    }
}
