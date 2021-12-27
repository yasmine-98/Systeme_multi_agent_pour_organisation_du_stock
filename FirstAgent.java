

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Toolkit;

public class FirstAgent extends Agent {
    //liste des produits
	private AID[] Produits;
	Astar A;
	public ArrayList<Objets> listChemin = new ArrayList<Objets>();
	ArrayList<Produits> produitsDispo = new ArrayList<Produits>();
	ArrayList<Objets> listArmoires = new ArrayList<Objets>();
	ArrayList<Integer> distance = new ArrayList<Integer>();
	float Capacite=30;
	int posL;
	int posC,pL,pC;
	Magasin m;
	Object[] args;
	int en=0;
	boolean envoie=false;
	protected void setup() 
    { 
        args = getArguments();
        posL=(int) args[1];
        posC=(int)args[2];
        pL=(int) args[1];
        pC=(int) args[2];
        m=(Magasin)args[0];
        System.out.println("Hello World. ");
        System.out.println("My name is "+ getLocalName()); 
        
        addBehaviour( new DailyBehaviour() );
    }
	
	class DailyBehaviour extends CyclicBehaviour{
		
		public void action() {
			
			ACLMessage msg= receive();
			if(msg!=null) {
				System.out.println("d5al");
                
            if((produitsDispo.isEmpty())&&(posL==pL)&&(posC==pC)&&(m.produits.isEmpty()==false)) {
            	System.out.println("hi");
            	ChoisirProduits();
            	
            	System.out.println("dispo:"+produitsDispo.size());
            	ChercherMeilleureArmoire();
            	System.out.println("sizePp"+m.produits.size());
            }
            if(produitsDispo.isEmpty()&&(posL!=pL)&&(posC!=pC)&&m.produits.isEmpty()){
            	GoDepart();
            	envoie=false;
            }
            if(((m.produits.isEmpty()==false)&&(produitsDispo.isEmpty())&&(posL!=pL)&&(posC!=pC))) {
            	System.out.println("hii");
            	GoDepart();
            	ChoisirProduits();
            	
            	ChercherMeilleureArmoire();
            }}
           
	}
	
	public void ChoisirProduits()
	{	for(int k=0; k<m.produits.size();k++) {
		System.out.println("poids:"+m.produits.get(k).Poids);
		
	}
		trierProduits();
		
		System.out.println("choisir");
		int n=m.produits.size();
		Capacite=30;
		//for(int k=0; k<m.produits.size();k++) {
		int h=0;
		while(h<m.produits.size()) {
			if(Capacite>=m.produits.get(h).Poids) {
			Capacite=Capacite-m.produits.get(h).Poids;
			System.out.println("ca="+Capacite);
			produitsDispo.add(m.produits.get(h));
			m.produits.remove(h);
			//m.produits.remove(k);
			h=0;
			
			}
			else {
				h=h+1;
			}
		}
		System.out.println("Produit Choisie");
		for(int k=0; k<produitsDispo.size();k++) {
			System.out.println("poids:"+produitsDispo.get(k).Poids);
			
		}
		
	
		
	}
	public boolean notExist(Objets m) {
		
			for(int k=0; k<listArmoires.size(); k++ ) {
				if(m==listArmoires.get(k))
				return false;	
				
			}
		return true;
	}
	public void listeArmoire() {
		for(int p=0; p<8; p++) {
			for(int k=0; k<6; k++ ) {
				if(m.magasin[p][k].type=="Armoire") {  	
					int dis=((m.magasin[p][k].nC-posC)*(m.magasin[p][k].nC-posC))+((m.magasin[p][k].nL-posL)*(m.magasin[p][k].nL-posL));
					if((distance.isEmpty()==true)&&(notExist(m.magasin[p][k]))) {
					listArmoires.add(m.magasin[p][k]);
					distance.add(dis);
					}
					else {
						int i=0;
						while(i<listArmoires.size()) {
							if((distance.get(i)>dis)&&((notExist(m.magasin[p][k])))) {
								distance.add(i,dis);
								listArmoires.add(i,m.magasin[p][k]);
								break;
							}
							i++;
							
							
						}
					}
					
				}}}
	}
	public void ChercherMeilleureArmoire() {
		
			
		//System.out.println("chercherarmoire");

		
		
		listeArmoire();
		
		
		
		
		
		
		while((produitsDispo.isEmpty()==false)&&(listArmoires.isEmpty()==false)) {
			
			int k=0;
			boolean v=false;
				while((k<listArmoires.size()&&(v==false))) {
				//System.out.println(listArmoires.get(k).Capacite);
				
				if(listArmoires.get(k).Capacite>=produitsDispo.get(0).Poids) {
					
					listArmoires.get(k).produits.add(produitsDispo.get(0));
					listArmoires.get(k).Capacite=listArmoires.get(k).Capacite-produitsDispo.get(0).Poids;
				    produitsDispo.remove(0);
				    v=true;
				    Objets a=chercherDirection(listArmoires.get(k));
				    ChercherMeilleurChemin(m.magasin[posL][posC], a);
				    
				    if(listArmoires.get(k).Capacite==0) {
				    		listArmoires.get(k).btn.setBackground(Color.red);
				    		listArmoires.remove(k);
				    		k=k+1;
				    		System.out.println("Capacite=0 "+k);
				    	}
				    //System.out.println("coco");
				    //}
				}
				else {
				k=k+1;
				//System.out.println("Capacite<poids "+k);
				
				}
				}
			
			
			}
		
		
	}
	public void ChercherMeilleurChemin(Objets posI, Objets posF) {
		A= new Astar();
		//System.out.println("posI:"+posI.nL+","+posI.nC);
		//System.out.println("posF:"+posF.nL+","+posF.nC);
		listChemin=A.CheminAstar(posI, posF, m.magasin);
		for(int i=listChemin.size()-1; i>=0; i--) {
			//listChemin.get(i).btn.setBackground(Color.blue);
			ImageIcon robot =new ImageIcon(this.getClass().getResource("/robot.png"));
			
			listChemin.get(i).btn.setIcon(robot);
			if(i!=listChemin.size()-1) {
			//listChemin.get(i+1).btn.setBackground(new Color(255, 255, 204));
			listChemin.get(i+1).btn.setIcon(null);
			}
			posL=listChemin.get(i).nL;
			//System.out.println("Posrobot");
			//System.out.println(posL);
			//System.out.println(posC);
			posC=listChemin.get(i).nC;
			System.out.println("name"+getLocalName()+"POS"+posL+","+posC);
			
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
		
		
	public void trierProduits() {
		Produits p;
		int n=m.produits.size();
		//System.out.println(m.produits.size());
		
	          for (int i = 0; i < n - 1; i++)  
	          {
	        	  //System.out.println("i="+i);
	               int index = i;  
	               for (int j = i + 1; j <n; j++)
	               {
	            	   if(m.produits.get(index).Poids<m.produits.get(j).Poids) { 
	                         index = j;
	                         /*p = m.produits.get(index);
	      	                 m.produits.add(index,m.produits.get(i)); 
	      	                 m.produits.add(i, p);*/
	                         Collections.swap(m.produits, i, index);
	      	                 //System.out.println("index="+index+",i="+i);
	      	                 index=i;
	      	               for(int k=0; k<m.produits.size();k++) {
	      	 	      		//System.out.println("poidst1:"+m.produits.get(k).Poids);
	      	 	      		
	      	 	      	}
	                    }
	               }
	 
	               
	               
	          }
	          for (int i=0; i<m.produits.size(); i++) {
	        	  System.out.println("mm"+m.produits.get(i).Poids);
	          }
	     
	          //System.out.println(m.produits.size());
	          //for(int k=0; k<m.produits.size();k++) {
	      		//System.out.println("poidst:"+m.produits.get(k).Poids);
	      		
	      	//}
		/*for(int i=0; i<n; i++) {
			if(m.produits.get(i).Poids<m.produits.get(i+1).Poids) {
				p=m.produits.get(i);
				m.produits.add(i,m.produits.get(i+1));
				m.produits.add(i+1,p);
				
			}
			
			//v=i<(produitsDispo.size()-1);
		}*/
		
	
	}
	public Objets chercherDirection( Objets ar) {
		
        int p=0;
        
        Objets a=ar;
        //System.out.println("hi");
        while((p<8)) {
        	//System.out.println("hii");
        	int k=0;
        	while((k<6)) {
        		//System.out.println(k);
        		//System.out.println(magasin[p][k].nL+","+magasin[p][k].nC);
				if((m.magasin[p][k].nL==ar.nL)&&(m.magasin[p][k].nC==ar.nC)) {
					//System.out.println(m.magasin[p][k].nL+","+m.magasin[p][k].nC);
					if(ar.nL==0) {
					a= m.magasin[p+1][k];
					//System.out.println(p+1);
					break;
				    }
				    else {
					a= m.magasin[p][k+1];
					//System.out.println(k+1);
					break;
				    }
				
			    }
				k=k+1;
				}
        	//System.out.println(p+1);
        	p=p+1;
        
        }
        System.out.println("dis:"+a.nL+","+a.nC);
        return a;
	}
	
	public void GoDepart() {
		    //System.out.println("ll");
		    ChercherMeilleurChemin(m.magasin[posL][posC],m.magasin[pL][pC]);
			posL=pL;
			posC=pC;
		
		
	}
	
}}