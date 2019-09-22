package api;

import comunication.AuthServer;
import comunication.AuthToken;
import comunication.UI;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
public class Controller {
   static comunication.Application application= new comunication.Application();
    static AuthServer authServer = new AuthServer();
    @RequestMapping("/NewToken")
    public String NewToken(
                                    @RequestParam String userid,
                                    @RequestParam String timestamp,
                                    @RequestParam String expirationtimestamp,
                                    @RequestParam String type

                        ) throws NoSuchAlgorithmException, InvalidArgumentException, ProposalException, IOException, ChaincodeEndorsementPolicyParseException {



       return authServer.recordToken(userid, timestamp, expirationtimestamp, type) ;

    }
    @RequestMapping("/GetToken")
    public AuthToken GetUser(
            @RequestParam String id


    ) throws NoSuchAlgorithmException, InvalidArgumentException, ProposalException, IOException, ChaincodeEndorsementPolicyParseException {


        System.out.println(id);
        return application.query(id);

    }

}