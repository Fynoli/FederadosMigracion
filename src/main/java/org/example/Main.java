package org.example;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.simple.JSONObject;


import java.util.Scanner;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("reporte.csv"));
        JSONObject body=new JSONObject();
        FileWriter fileWriter=new FileWriter("LogMigracion.txt");
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.println("Por favor ingrese la url del servicio");
        System.out.println("Por ejemplo: http://localhost:8080/api/1/federados/federado");
        String url=reader.readLine();
        System.out.println("Por favor ingrese el token de autenticación");
        String token=reader.readLine();

        while (sc.hasNextLine()) {
            String line=sc.nextLine();
            System.out.println("Leo la linea: "+line);
            fileWriter.write("Leo la linea: "+line+"\n");
            String[] objs=line.split(";");
            if(objs.length!=3){
                System.out.println("Error de inconsistencia en el archivo. La linea no tenia 3 parametros: "+line);
                fileWriter.write("Error de inconsistencia en el archivo. La linea no tenia 3 parametros: "+line+"\n");
                System.out.println("__________________________________________________________________");
                fileWriter.write("__________________________________________________________________"+"\n");
                continue;
            }
            body.put("email",objs[0]);
            body.put("dominio",objs[1]);
            body.put("modoAutorizacion",objs[2]);
            System.out.println("Voy a pushear el registro: "+body);
            fileWriter.write("Voy a pushear el registro: "+body+"\n");

            Client client = ClientBuilder.newClient();


            WebTarget resource = client.target(url+"?ACCESS_TOKEN="+token);

            Invocation.Builder request = resource.request();
            request.accept(MediaType.APPLICATION_JSON);


            Response response = request.post(Entity.json(body));

            if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
                System.out.println("Exito! " + response.getStatus());
                fileWriter.write("Exito! " + response.getStatus()+"\n");
            } else {
                System.out.println("ERROR! " + response.getStatus());
                fileWriter.write("ERROR! " + response.getStatus()+"\n");
            }

            System.out.println("__________________________________________________________________");
            fileWriter.write("__________________________________________________________________"+"\n");
            body=new JSONObject();

        }
        sc.close();
        fileWriter.close();
    }
}