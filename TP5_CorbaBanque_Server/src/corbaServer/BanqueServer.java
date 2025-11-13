package corbaServer;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import service.BanqueImpl;
import corbaBanque.IBanqueRemoteHelper;
import java.util.Properties;

public class BanqueServer {
    public static void main(String[] args) {
        try {
            System.out.println("Démarrage du serveur Banque CORBA...");
            
            // a. Initialiser l'ORB
            Properties props = new Properties();
            props.put("org.omg.CORBA.ORBInitialPort", "900");
            ORB orb = ORB.init(args, props);
            
            // b. Initialiser le POA
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
            
            // c. Créer le servant
            BanqueImpl banqueService = new BanqueImpl();
            
            // d. Obtenir la référence CORBA
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(banqueService);
            corbaBanque.IBanqueRemote href = IBanqueRemoteHelper.narrow(ref);
            
            // e. Obtenir le contexte de nommage
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            
            // f. Enregistrer dans l'annuaire
            String name = "BanqueService";
            NameComponent[] path = ncRef.to_name(name);
            ncRef.rebind(path, href);
            
            System.out.println("Serveur Banque CORBA prêt et enregistré sous le nom: " + name);
            System.out.println("En attente de requêtes des clients sur le port 900...");
            
            // g. Mettre en attente
            orb.run();
            
        } catch (Exception e) {
            System.err.println("Erreur du serveur: " + e);
            e.printStackTrace();
        }
    }
}