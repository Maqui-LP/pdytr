import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.Location;
import jade.domain.FIPAAgentManagement.MissingArgument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FTPAgent extends Agent {

    private Location origin;

    private String method;
    private String sourcePath;
    private String destinationPath;
    private String files;

    private byte[] file = null;
    private long fileSize;
    private int currentSize = 0;

    @Override
    protected void setup() {

        Object[] args = getArguments();
        //this.getOpt(args);
        this.origin = here();

        try{
            ContainerID destination = new ContainerID("Main-Container",null);
            System.out.println("Migrating the agent " + destination.getID());
            doMove(destination);
        }catch(Exception e){
            System.out.println("ERROR: It was not posible to migrate the agent!");
            System.out.println("Causa: " + e.getCause());;
        }
    }

    @Override
    protected void afterMove() {
        Location here = here();

        switch (this.method) {
            case "write":
                this.writeAfterMove(here);
                break;
            case "read":
                this.readAfterMove(here);
                break;
            default:
                throw new IllegalArgumentException("ERROR: " + this.method + " is not a legal option.");
        }
    }

    private void writeAfterMove(Location here){
        if(!here.getName().equals(this.origin.getName())){
            FTPCommand.write(this.destinationPath,this.file,this.currentSize,this.fileSize);
            doMove(new ContainerID(this.origin.getName(),null));
        }else if(this.fileSize > this.currentSize){
            this.file = FTPCommand.read(this.sourcePath,this.currentSize, this.currentSize,this.fileSize);
            this.currentSize += this.file.length;
            System.out.println(String.format("%d of %d readed",this.file.length, this.fileSize));
            doMove(new ContainerID("Main-Container", null));
        }else {
            System.out.println("The file " + this.sourcePath + " was written in the remote directory " + this.destinationPath);
        }
    }

    private void readAfterMove(Location here){
        if(!here.getName().equals(this.origin.getName())){
            this.file = FTPCommand.read(this.destinationPath, this.currentSize, this.currentSize, this.fileSize);
            this.currentSize += this.file.length;
            System.out.println("Reading " + this.file.length + " bytes of " + this.fileSize);
            doMove(new ContainerID(this.origin.getName(),null));
            return;
        }

        try{
            Files.write(Paths.get(this.sourcePath),this.file, StandardOpenOption.CREATE,StandardOpenOption.APPEND);
            System.out.println(String.format("Writing %d bytes of %d",this.file.length,this.fileSize));
        }catch(IOException e){
            System.out.println("Error al crear/escribir el archivo, causa: " + e.getCause());
        }finally {
            if(this.fileSize > this.currentSize){
                System.out.println("Bytes left");
                doMove(new ContainerID("Main-Container",null));
            }else{
                System.out.println("The file " + this.sourcePath + " was readed correctly and successfuly loaded in " + this.destinationPath);
            }
        }
    }

    private void getOpt(Object[] args)throws IOException{

        if(args.length != 3){
            throw new RuntimeException("ERROR 3 argumentos son necesarios: comando, directorio local y directorio remoto.");
        }

        this.method = (String) args[0];
        this.sourcePath = (String) args[1];
        this.destinationPath = (String) args[2];

        switch (this.method){
            case "write":
                this.file = FTPCommand.read(this.destinationPath,this.currentSize,this.currentSize,this.fileSize);
                this.currentSize += this.file.length;
                this.fileSize = Files.size(Paths.get(this.destinationPath));
                break;
            case "read":
                this.fileSize = Files.size(Paths.get(this.destinationPath));
                break;
            default:
                throw new IllegalArgumentException("ERROR: el argumento :" + (String)args[0] + " no es una opcion permitida");
        }
    }
}
