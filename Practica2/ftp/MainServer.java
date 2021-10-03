import java.rmi.Naming;
import java.rmi.registry.Registry;

public class MainServer {
    /**
     * Instanciacion del Server
     * Registrar en el RMIRegistry
     * ...
     */
    public static void main(String[] args) {
        
        if (args.length != 1) {
            System.out.println("1 argument needed: (remote) hostname");
            System.exit(1);
        }
        try {
            Server server = new Server();
            
            String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
            Naming.rebind(rname, server);
            System.out.println("Done");

        } catch (Exception e) {
            e.printStackTrace();
        }    
    }

}
