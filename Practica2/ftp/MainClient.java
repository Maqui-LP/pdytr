import java.rmi.Naming;
import java.rmi.registry.Registry;

public class MainClient {
    public static void main(String[] args) {
        
        if (args.length < 8) {
            System.out.println("1 argument needed: (remote) hostname");
            System.exit(1);
        }
        try {
            String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
            FtpServerInterface remote = (FtpServerInterface) Naming.lookup(rname);

            Client client = new Client(args[2], args[3], args[4], args[5], Integer.parseInt(args[6]), Integer.parseInt(args[7]));
           
            System.out.println("VOY A HACER EL READ\n");
            if(args[1].equals("read") ) {
               client.read(remote);
           }
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }
}
