import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class MainClient {
    public static void main(String[] args) {        
        if (args.length < 2) {
            System.out.println("2 argument needed: (remote) hostname and operation");
            System.exit(1);
        }
        try {
            String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
            FtpServerInterface remote = (FtpServerInterface) Naming.lookup(rname);
            //Client client = new Client(args[2], args[3], args[4], args[5], Integer.parseInt(args[6]), Integer.parseInt(args[7]));

            if(args[1].equals("read") ) {
                
                System.out.println("VOY A HACER EL READ\n");
                Client client = createClientFromInputForRead();
            
                client.read(remote);
            
            } else if(args[1].equals("write")){
                System.out.println("VOY A HACER EL WRITE");
                Client clientWrite = new Client.Builder().build();
                
                Scanner in = new Scanner(System.in);
                System.out.println("Ingrese el nombre del archivo a escrbir:");
                String filename = in.nextLine();
                System.out.println("Ingrese el contenido a introducir en el archivo:");
                String text = in.nextLine();

                clientWrite.write(remote, filename, text.getBytes());
            
                System.out.println("Fin del write");
            
            }
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }

    private static Client createClientFromInputForRead(){
        Scanner in = new Scanner(System.in);
        System.out.println("Ingrese el nombre del archivo a leer:");
        String sourceFilename = in.nextLine();
        System.out.println("Ingrese el path de origen:");
        String sourcePath = in.nextLine();
        System.out.println("Ingrese el nombre de archivo destino:");
        String outputFilename = in.nextLine();
        System.out.println("Ingrese el path destino.");
        String outputPath = in.nextLine();
        System.out.println("Ingrese los bytes a leer:");
        int chuncks = Integer.parseInt(in.nextLine());
        System.out.println("Ingrese la posicion inicial:");
        int pos = Integer.parseInt(in.nextLine());
        return  new Client.Builder()
                            .sourceFilename(sourceFilename)
                            .sourcePath(sourcePath)
                            .outputFilename(outputFilename)
                            .outputPath(outputPath)
                            .bytesToRead(chuncks)
                            .initialPositio(pos)
                            .build();

    }  

}
