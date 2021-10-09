import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class MainClient {
    public static void main(String[] args) {        
        if (args.length < 2) {
            System.out.println("2 argument needed: (remote) hostname and operation");
            System.out.println("la opcion: 'options' enumera el listado de operations disponibles");
            System.exit(1);
        }
        try {
            String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
            final FtpServerInterface remote = (FtpServerInterface) Naming.lookup(rname);

            switch(args[1]){
                case "read":
                    readOpt(remote);
                    break;
                case "write":
                    writeOpt(remote);
                    break;
                case "ejercicio-b":
                    Client.resolucionTresB(remote);
                    break;
                case "ejercicio-5-a":
                    ejercicio5a(remote);
                    break;
                case "timeout":
                    Client.timeout(remote);
                    break;
                case "concurrency":
                    concurrencyOpt(remote);
                    break;
                case "options":
                    System.out.println("opciones:");
                    System.out.println("read");
                    System.out.println("write");
                    System.out.println("ejercicio-b");
                    System.out.println("ejercicio-5-a");
                    System.out.println("timeout");
                    System.out.println("concurrency");
                    break;
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }

    private static void concurrencyOpt(final FtpServerInterface remote) {
        
        final Client clientThread = new Client.Builder().build();
        
        Thread t1 = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(100L);
                    clientThread.write( remote, "out-concurrency", "/pdytr/archivos/concurrency-experiment");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t2 = new Thread(){
            public void run() {
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                clientThread.write( remote, "out-concurrency", "/pdytr/archivos/concurrency-experiment");
            };
        };

        t1.start();
        t2.start();
        System.out.println("Fin experimento");
        
    }

    private static void ejercicio5a(final FtpServerInterface remote) throws Exception {
        long start = System.nanoTime();
        remote.giveMeABoolean();
        long end = System.nanoTime() - start;                
        double miliSeconds = (double)end / 1_000_000.0;
        System.out.println("Tiempo en milisegundos: " + miliSeconds);
    }

    private static void writeOpt(final FtpServerInterface remote) {
        System.out.println("VOY A HACER EL WRITE");
        Client clientWrite = new Client.Builder().build();
        
        Scanner in = new Scanner(System.in);
        System.out.println("Ingrese el nombre del archivo a escrbir:");
        String filename = in.nextLine();
        System.out.println("Ingrese la ruta al archivo con los datos a escribir en el archivo de salida: (ej: /pdytr/archivos/prueba)");
        String sourceFile = in.nextLine();
        

        clientWrite.write(remote, filename, sourceFile);
         
        System.out.println("Fin del write");
    }

    private static void readOpt(final FtpServerInterface remote) {
        System.out.println("VOY A HACER EL READ\n");
        Client client = createClientFromInputForRead();
         
        client.read(remote);
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
