package ftp;

import java.rmi.Naming;
import java.rmi.registry.Registry;

public class MainServer {
    /**
     * Instanciación del Server
     * Registrar en el RMIRegistry
     * ...
     */
    public static void main(String[] args) {
        
        if (args.length != 1) {
            System.out.println("1 argument needed: (remote) hostname");
            System.exit(1);
        }
        try {
            String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
            FtpServerInterface remote = (FtpServerInterface) Naming.lookup(rname);
            int bufferlength = 100;
            byte[] buffer = new byte[bufferlength];
            //remote.sendThisBack(buffer);
            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }

}
