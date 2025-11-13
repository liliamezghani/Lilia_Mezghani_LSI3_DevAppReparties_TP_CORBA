package corbaClient;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import corbaBanque.IBanqueRemote;
import corbaBanque.IBanqueRemoteHelper;
import corbaBanque.Compte;
import java.util.Properties;

public class BanqueClient {
    public static void main(String[] args) {
        try {
            System.out.println("=== Client Banque CORBA Démarrage ===");
            
            // a. Récupérer propriétés JNDI
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialPort", "900");
            ORB orb = ORB.init(args, props);
            
            // b. Récupérer référence annuaire
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            
            // c. Récupérer référence objet distant + créer Stub
            String name = "BanqueService";
            IBanqueRemote banqueService = IBanqueRemoteHelper.narrow(ncRef.resolve_str(name));
            
            System.out.println("Connecté au serveur Banque CORBA");
            
            // d. TEST de toutes les méthodes
            System.out.println("\n--- Test Création Comptes ---");
            Compte compte1 = new Compte();
            compte1.solde = 1000.0f;
            banqueService.creerCompte(compte1);
            
            Compte compte2 = new Compte();
            compte2.solde = 500.0f;
            banqueService.creerCompte(compte2);
            
            System.out.println("\n--- Test Versement ---");
            banqueService.verser(200.0f, 1);
            
            System.out.println("\n--- Test Retrait ---");
            banqueService.retirer(150.0f, 2);
            
            System.out.println("\n--- Test Consultation Compte ---");
            Compte compteConsult = banqueService.getCompte(1);
            if (compteConsult != null) {
                System.out.println("Compte 1 - Code: " + compteConsult.code + ", Solde: " + compteConsult.solde);
            }
            
            System.out.println("\n--- Test Liste Comptes ---");
            Compte[] comptes = banqueService.getComptes();
            System.out.println("Nombre de comptes: " + comptes.length);
            for (Compte c : comptes) {
                System.out.println("Compte " + c.code + " : " + c.solde + " €");
            }
            
            System.out.println("\n--- Test Conversion ---");
            double montantDT = banqueService.conversion(100.0f);
            System.out.println("100 € = " + montantDT + " DT");
            
            System.out.println("\n=== Tous les tests exécutés avec succès ===");
            
        } catch (Exception e) {
            System.err.println("Erreur client: " + e);
            e.printStackTrace();
        }
    }
}