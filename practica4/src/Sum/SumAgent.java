import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import jade.core.*;
import jade.domain.FIPAAgentManagement.MissingArgument;

public class SumAgent extends Agent {
    private final String manchine = "Main-Container";
    private Location origin = null;
    private Integer sum = null;
    private String filePath = "/temp/file";

    public void printAgentPresentation() {
        System.out.println("Agente con nombre local: " + getLocalName());
        System.out.println("y mi nombre es: " + getName());
        System.out.println("Localizacion: " + this.origin.getID() + " Location Name: " + this.origin.getName());
    }

    public void setup(){
        Object[] args = getArguments();
        this.getOpt(args);
        this.origin = here();
        this.printAgentPresentation();

        try{
            ContainerID destination = new ContainerID(this.manchine, null);
            System.out.println("Migrando agente a: " + destination.getID());
            doMove(destination);
        }catch(Exception e){
            System.out.println("Error: No fue posible migrar el agente...:-(");
            System.out.println(e.getMessage());
        }
    }

    private void getOpt(Object[] args) {
        try{
           if(args != null && args.length != 0){
                this.filePath = (String) args[0];
            }else{
               throw new MissingArgument("No suministro el path al archivo");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void afterMove(){
        Location actual = here();
        System.out.println("Location after move: " + actual.getName() + " id: " + actual.getID());

        if(!actual.getName().equals(origin.getName())){
            try{
                List<String> numbers = Files.readAllLines(Paths.get(this.filePath),Charset.forName("utf8"));
                //List<String> numbers = Stream.of("1","2","3","4","5","6","7","8","9").collect(Collectors.toList());

                int result = 0;

                for(String num: numbers){
                    result += Integer.parseInt(num);
                }

                sum = result;

            }catch(NumberFormatException e){
                System.out.println("Not a number ~(>_<)~");
            }catch(IOException e){
                System.out.println("El archivo no existe ~(º-º)~");
            }catch(Exception e){
                System.out.println("Ups... algo salio mal...(-_-\")");
                e.printStackTrace();
            }
            ContainerID destination = new ContainerID(this.origin.getName(), null);
            doMove(destination);

        }else{
            System.out.println(String.format("Sum: %d", this.sum));
        }
    }
}