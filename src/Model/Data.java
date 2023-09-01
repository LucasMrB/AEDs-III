package Model;

import java.util.Random;

public class Data {
    private int dia;
    private int mes;
    private int ano;
    private static Random rand = new Random(); 

    //Construtores
    public Data(){
        dia = rand.nextInt(26)+1;
        mes = rand.nextInt(12)+1;
        ano = rand.nextInt(53)+1970;
    }

    public Data(int dia,int mes, int ano){
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;

    }

    public Data(int mes,int ano){

        dia = rand.nextInt(26)+1;
        this.mes = mes;
        this.ano = ano;
    }

    public Data(int ano){
        dia = rand.nextInt(26)+1;
        mes = rand.nextInt(12)+1;
        this.ano = ano;
    }

    // varios construtores pois algumas musicas possuem apenas mes e ano, ou apenas ano.

    //gets
    public int getAno(){ return ano;}
    public int getMes(){ return mes;}
    public int getDia(){ return dia;}

    //sets
    public void setAno(int ano){ this.ano = ano;}
    public void setMes(int mes){ this.mes = mes;}
    public void setDia(int dia){ this.dia = dia;}

}
