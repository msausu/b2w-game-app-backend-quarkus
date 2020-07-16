package com.b2w.game.model;

import java.util.List;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author msa
 */
@MongoEntity(collection = "planetas")
public class Planet extends PanacheMongoEntity {

    private static final String ID_PAT = "\\w+",
            NM_PAT = "\\w+( \\w+)*",
            ST_PAT = "\\w+(( |, ?)\\w)*";

    @Size(min = 1, max = 60)
    @Pattern(regexp = NM_PAT)
    String nome;

    @Size(min = 3, max = 20)
    @Pattern(regexp = ST_PAT)
    String clima;

    @Size(min = 4, max = 120)
    @Pattern(regexp = ST_PAT)
    String terreno;

    public Planet() {
    }

    public Planet(String nome, String clima, String terreno) {
        this.nome = nome;
        this.clima = clima;
        this.terreno = terreno;
    }
    
    public static Planet findByNome(String nome) {
        return find("nome", nome).firstResult();
    }

    public static List<Planet> todos() {
        return Planet.listAll();
    }

    public static void deleteLoics() {
        delete("name", "Loïc");
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getClima() {
        return clima;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    public String getTerreno() {
        return terreno;
    }

    public void setTerreno(String terreno) {
        this.terreno = terreno;
    }
}
