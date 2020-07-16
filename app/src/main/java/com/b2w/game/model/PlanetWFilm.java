
package com.b2w.game.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 *
 * @author msa
 */
@RegisterForReflection
public class PlanetWFilm extends Planet {
    
    @Min(-1)
    @Max(15) // ?
    int numFilm;

    public PlanetWFilm() {
    }

    public PlanetWFilm(Planet planet, int numFilm) {
        super.id = planet.id;
        super.nome= planet.nome;
        super.clima = planet.clima;
        super.terreno = planet.terreno;
        this.numFilm = numFilm;
    }
    
    public int getNumFilmes() {
        return numFilm;
    }

    public void setNumFilmes(int numFilm) {
        this.numFilm = numFilm;
    }
    
}