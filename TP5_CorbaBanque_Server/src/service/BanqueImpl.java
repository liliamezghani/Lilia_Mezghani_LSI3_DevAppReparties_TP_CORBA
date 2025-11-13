package service;

import java.util.ArrayList;
import java.util.List;
import corbaBanque.*;

public class BanqueImpl extends IBanqueRemotePOA {
    private List<Compte> comptes = new ArrayList<>();
    private int codeCounter = 1;
    
    public void creerCompte(Compte cpte) {
        cpte.code = codeCounter++;
        comptes.add(cpte);
        System.out.println("Compte créé: Code=" + cpte.code + ", Solde=" + cpte.solde);
    }
    
    public void verser(float mt, int code) {  // INT
        for (Compte c : comptes) {
            if (c.code == code) {
                c.solde += mt;
                System.out.println("Versement: " + mt + " sur compte " + code);
                return;
            }
        }
        System.out.println("Compte " + code + " non trouvé");
    }
    
    public void retirer(float mt, int code) {  // INT
        for (Compte c : comptes) {
            if (c.code == code) {
                if (c.solde >= mt) {
                    c.solde -= mt;
                    System.out.println("Retrait: " + mt + " du compte " + code);
                } else {
                    System.out.println("Solde insuffisant sur compte " + code);
                }
                return;
            }
        }
        System.out.println("Compte " + code + " non trouvé");
    }
    
    public Compte getCompte(int code) {  // INT
        for (Compte c : comptes) {
            if (c.code == code) {
                return c;
            }
        }
        return null;
    }
    
    public Compte[] getComptes() {
        return comptes.toArray(new Compte[0]);
    }
    
    public double conversion(float mt) {
        return mt * 3.3;
    }
}