package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Chambre")
public class Chambre {

    @Id
    private Long id;

    private String numero;
    private String type;
    private String etat;
    private double prixNuitee;
    private String description;
    private boolean disponibilite;

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public double getPrixNuitee() {
        return prixNuitee;
    }

    public void setPrixNuitee(double prixNuitee) {
        this.prixNuitee = prixNuitee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(boolean disponibilite) {
        this.disponibilite = disponibilite;
    }

    @Override
    public String toString() {
        return numero; // Affiche directement le numéro de la chambre
        // Ou par exemple : return numero + " (" + type + ")"; pour plus de détail
    }
}
