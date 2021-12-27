
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


//import FirstAgent.MyBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class SecondeAgent extends Agent 

     { 
	Object[] args;
	Magasin m;
	int posL;
	int posC;
	ImageIcon robot;
	float capacite;
	//Objets[][] args;
        protected void setup() 
        { 
             args = getArguments();
             m=(Magasin)args[0];
             addBehaviour( new DailyBehaviour() );
            
        }
        class DailyBehaviour extends SimpleBehaviour{
    		
    		
    		public void action() 
    	 	{
    			
    			m.envoyer.addActionListener(new ActionListener() {
    				public void actionPerformed(ActionEvent e) {
    					JOptionPane.showMessageDialog(m.contentPane, "Produit envoyé");
    					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
    		            msg.setContent( "Demande envoyé" );
    		            msg.addReceiver( new AID( "Agent1" , AID.ISLOCALNAME) );
    		            msg.addReceiver( new AID( "Agent2" , AID.ISLOCALNAME) );
    		            send(msg);
    		            
    		           System.out.println("demande envoyé");
    		           
    				}
    			});
    	 	}
    		
    		public  boolean done() {  return true;  }
    		
    	}
       
    			
        
     
}