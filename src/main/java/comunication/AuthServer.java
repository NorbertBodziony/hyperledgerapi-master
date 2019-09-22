package comunication;
import java.io.*;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;

import java.util.*;
import java.util.concurrent.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.*;
import org.hyperledger.fabric.sdk.security.*;


public class AuthServer {

    final static String PATH_CRYPTO_CONFIG = "/home/bless/fabric-samples/first-network/crypto-config"; // do not change this line!

    public AuthServer() {
    }


    static   HFClient client ;
    static   Channel channel;

    static {
        try {

            Security.addProvider(new BouncyCastleProvider());
            client = getClient();
            channel = getChannel(client);

        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (TransactionException e) {
            e.printStackTrace();
        } catch (ProposalException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {

            String response=query("67");
             System.out.println(response);
          //    AuthToken authToken=new AuthToken("67","PABLO","69","9999","piccasso","#####");
          //   recordToken(authToken);
            //authToken=new AuthToken("92","Token","11","admin");
          //  recordToken(client,channel,authToken);
           //  query("67");

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
    static String query(String x) throws ProposalException, InvalidArgumentException {

        QueryByChaincodeRequest qpr = client.newQueryProposalRequest();
        ChaincodeID id = ChaincodeID.newBuilder().setName("mycc1").build();
        qpr.setChaincodeID(id);

        qpr.setFcn("query");
        qpr.setArgs(new String[]{ x});
        Collection<ProposalResponse> res = channel.queryByChaincode(qpr);
        String s="";
        for (ProposalResponse pres : res) {
            s += new String(pres.getChaincodeActionResponsePayload());

        }
        return s;
    }

    static void deleteToken(String x) throws InvalidArgumentException, ProposalException, ChaincodeEndorsementPolicyParseException, IOException {

        TransactionProposalRequest req = client.newTransactionProposalRequest();
        ChaincodeID cid = ChaincodeID.newBuilder().setName("mycc1").build();
        req.setChaincodeID(cid);
        req.setFcn("deleteToken");
        req.setArgs(new String[]{x});
        final Collection<ProposalResponse> responses = channel.sendTransactionProposal(req, channel.getPeers());

        CompletableFuture<BlockEvent.TransactionEvent> txFuture = channel.sendTransaction(responses, client.getUserContext());

        BlockEvent.TransactionEvent event;
        try {
            event = txFuture.get();
            System.out.println(event.toString());
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        } catch (ExecutionException ex) {
            System.out.println(ex.toString());
        }

    }
    static public String recordToken(AuthToken authToken) throws InvalidArgumentException, ProposalException, ChaincodeEndorsementPolicyParseException, IOException {

        TransactionProposalRequest req = client.newTransactionProposalRequest();
        ChaincodeID cid = ChaincodeID.newBuilder().setName("mycc1").build();
        req.setChaincodeID(cid);
        req.setFcn("recordToken");
        req.setArgs(new String[]{authToken.id,authToken.userid,authToken.timestamp,authToken.expirationtimestamp,authToken.type,authToken.key});
        final Collection<ProposalResponse> responses = channel.sendTransactionProposal(req, channel.getPeers());

        CompletableFuture<BlockEvent.TransactionEvent> txFuture = channel.sendTransaction(responses, client.getUserContext());

        BlockEvent.TransactionEvent event;
        try {
            event = txFuture.get();

        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        } catch (ExecutionException ex) {
            System.out.println(ex.getMessage());
        }
        return "Success";
    }
    public static String recordToken(String userid, String timestamp, String expirationtimestamp, String type) throws InvalidArgumentException, ProposalException, ChaincodeEndorsementPolicyParseException, IOException, NoSuchAlgorithmException {

        TransactionProposalRequest req = client.newTransactionProposalRequest();
        ChaincodeID cid = ChaincodeID.newBuilder().setName("mycc1").build();
        req.setChaincodeID(cid);
        req.setFcn("recordToken");

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(userid.getBytes(StandardCharsets.UTF_8));
        String id=(Base64.getEncoder().encodeToString(hash));

        hash = digest.digest(id.getBytes(StandardCharsets.UTF_8));
        String key=(Base64.getEncoder().encodeToString(hash));

        req.setArgs(new String[]{id,userid,timestamp,expirationtimestamp,type,key});
        final Collection<ProposalResponse> responses = channel.sendTransactionProposal(req, channel.getPeers());

        CompletableFuture<BlockEvent.TransactionEvent> txFuture = channel.sendTransaction(responses, client.getUserContext());

        BlockEvent.TransactionEvent event;
        try {
            event = txFuture.get();
//
       } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
       } catch (ExecutionException ex) {
           System.out.println(ex.getMessage());
        }
        Gson g = new GsonBuilder().disableHtmlEscaping().create();
        ID IDJson =new ID(id);

        return g.toJson(IDJson);
    }

    static HFClient getClient() {
        HFClient client = null;
        try {
            client = getHfClient();

            try {
                client.setUserContext(new User() {

                    public String getName() {
                        return "PeerAdmin";
                    }

                    public Set<String> getRoles() {
                        return null;
                    }

                    public String getAccount() {
                        return null;
                    }

                    public String getAffiliation() {
                        return null;
                    }

                    public Enrollment getEnrollment() {
                        return new Enrollment() {
                            public PrivateKey getKey() { // load admin private id
                                PrivateKey privateKey = null;
                                try {
                                    String k = validFile(PATH_CRYPTO_CONFIG + "/peerOrganizations/org2.example.com/users/Admin@org2.example.com/msp/keystore");
                                    File privateKeyFile = findFileSk(k);
                                    privateKey = getPrivateKeyFromBytes(toByteArray(new FileInputStream(privateKeyFile)));
                                } catch (FileNotFoundException ex) {
                                    System.out.println(ex.toString());
                                } catch (IOException ex) {
                                    System.out.println(ex.toString());
                                } catch (Exception ex) {
                                    System.out.println(ex.toString());
                                }
                                return privateKey;
                            }

                            public String getCert() {// read admin client certificate

                                String certificate = null;
                                try {
                                    String k = validFile(PATH_CRYPTO_CONFIG + "/peerOrganizations/org2.example.com/users/Admin@org2.example.com/msp/signcerts/Admin@org2.example.com-cert.pem");
                                    File certificateFile = new File(k);
                                    certificate = new String(toByteArray(new FileInputStream(certificateFile)), "UTF-8");
                                } catch (UnsupportedEncodingException ex) {
                                    System.out.println(ex.toString());
                                } catch (FileNotFoundException ex) {
                                    System.out.println(ex.toString());
                                } catch (IOException ex) {
                                    System.out.println(ex.toString());
                                }
                                return certificate;
                            }
                        };
                    }

                    public String getMspId() {
                        return "Org2MSP";
                    }
                });
            } catch (InvalidArgumentException ex) {
                System.out.println(ex.toString());
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        return client;
    }

    static boolean verifyAccess(String id ,String key)
    {
        System.out.println("VERIFY ACCESS");
        try {
            Gson gson=new Gson();
            String a= query(id);
            AuthToken authToken=gson.fromJson(a,AuthToken.class);
            if (authToken.getKey().equals("#####"))
            {
                return true;
            }
        } catch (ProposalException e) {
            e.printStackTrace();
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }
    static Channel getChannel(HFClient client) throws InvalidArgumentException, TransactionException, ProposalException {
        Channel channel = client.newChannel("mychannel");
        Properties ordererProperties = new Properties();
        ordererProperties.setProperty("pemFile", validFile(PATH_CRYPTO_CONFIG + "/ordererOrganizations/example.com/orderers/orderer.example.com/tls/server.crt"));
        ordererProperties.setProperty("trustServerCertificate", "true"); // testing

        ordererProperties.setProperty("hostnameOverride", "orderer.example.com");
        ordererProperties.setProperty("sslProvider", "openSSL");
        ordererProperties.setProperty("negotiationType", "TLS");
        ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTime", new Object[]{5L, TimeUnit.MINUTES});
        ordererProperties.put("grpc.NettyChannelBuilderOption.keepAliveTimeout", new Object[]{8L, TimeUnit.SECONDS});
        channel.addOrderer(client.newOrderer("orderer.example.com", "grpcs://localhost:7050", ordererProperties));  // use the network orderer container URL

        Properties peerProperties;
        peerProperties = new Properties();
        peerProperties.setProperty("pemFile", validFile(PATH_CRYPTO_CONFIG + "/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/server.crt"));
        peerProperties.setProperty("trustServerCertificate", "true"); // testing                                                                                                                       
        peerProperties.setProperty("hostnameOverride", "peer0.org2.example.com");
        peerProperties.setProperty("sslProvider", "openSSL");
        peerProperties.setProperty("negotiationType", "TLS");
        peerProperties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 9000000);

        channel.addPeer(client.newPeer("peer0.org2.example.com", "grpcs://127.0.0.1:9051", peerProperties));

        channel.initialize();

        return channel;
    }

    static HFClient getHfClient() throws Exception {
        CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
        HFClient client = HFClient.createNewInstance();
        client.setCryptoSuite(cryptoSuite);
        return client;
    }

    // *******************************************************************************
    // ************************* helper functions ************************************
    // *******************************************************************************
    static PrivateKey getPrivateKeyFromBytes(byte[] data) throws IOException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        final Reader pemReader = new StringReader(new String(data));

        final PrivateKeyInfo pemPair;
        PEMParser pemParser = new PEMParser(pemReader);
        pemPair = (PrivateKeyInfo) pemParser.readObject();

        PrivateKey privateKey = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getPrivateKey(pemPair);

        return privateKey;
    }

    static File findFileSk(String directorys) {

        File directory = new File(directorys);

        File[] matches;
        matches = directory.listFiles();

        if (null == matches) {
            throw new RuntimeException(String.format("Matches returned null does %s directory exist?", directory.getAbsoluteFile().getName()));
        }

        if (matches.length != 1) {
            throw new RuntimeException(String.format("Expected in %s only 1 sk file but found %d", directory.getAbsoluteFile().getName(), matches.length));
        }

        return matches[0];
    }

    static byte[] toByteArray(InputStream input) throws IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = input.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();
    }

    static String validFile(String s) {
        if (directoryExists(s) || fileExists(s)) {

        } else {
            System.out.println(s + " not existing!!");
        }
        return s;
    }

    static boolean directoryExists(String filePathString) {
        File f = new File(filePathString);
        if (f.exists() && f.isDirectory()) {
            return true;
        }
        return false;
    }

    static boolean fileExists(String filePathString) {
        File f = new File(filePathString);
        if (f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;
    }
}