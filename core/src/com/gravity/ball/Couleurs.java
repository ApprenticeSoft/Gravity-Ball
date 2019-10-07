package com.gravity.ball;

import com.badlogic.gdx.graphics.Color;

public class Couleurs {

	int couleur;
	private Color couleurBalle, couleurFond, couleurSol, couleurLéger, couleurVide, couleurEau;
	
	public Couleurs(int couleur){
		this.couleur = couleur;
			
		if(couleur == 1){ 					
			couleurBalle = new Color().set(1,1,1,1);				
			couleurFond = new Color().set(122/256f,147/256f,172/256f, 1);
			couleurSol = new Color().set(221/256f,97/256f,74/256f, 1);
			couleurLéger = new Color().set(170/256f,75/256f,57/256f, 1);
			couleurVide = new Color().set(0, 0, 0, 0);
			couleurEau = new Color().set(45/256f,109/256f,166/256f, 0.5f);
		}
		else if(couleur == 2){ 								
			couleurBalle = new Color().set(253/256f,240/256f,213/256f, 1);
			couleurFond = new Color().set(58/256f,51/256f,53/256f, 1);	
			couleurSol = new Color().set(240/256f,84/256f,79/256f,1);	
		}
		else if(couleur == 3){ 										
			couleurBalle = new Color().set(253/256f,240/256f,213/256f, 1);
			couleurFond = new Color().set(58/256f,51/256f,53/256f, 1);
			couleurSol = new Color().set(240/256f,84/256f,79/256f,1);
		}
		else if(couleur == 4){ 								
			couleurBalle = new Color().set(1,1,1,1);		
			couleurFond = new Color().set(58/256f,51/256f,53/256f, 1);
			couleurSol = new Color().set(216/256f,30/256f,91/256f, 1);
		}
		else if(couleur == 5){ 								
			couleurBalle = new Color().set(1,1,1,1);		
			couleurFond = new Color().set(76/256f,141/256f,166/256f, 1);				
			couleurSol = new Color().set(91/256f,96/256f,140/256f,1);
		}
		else if(couleur == 6){ 								
			couleurBalle = new Color().set(1,1,1,1);		
			couleurFond = new Color().set(76/256f,141/256f,166/256f, 1);
			couleurSol = new Color().set(250/256f,110/256f,105/256f, 1);
		}
		else if(couleur == 7){ 							
			couleurBalle = new Color().set(213/256f,79/256f,88/256f, 1);		
			couleurFond = new Color().set(243/256f,237/256f,211/256f, 1);
			couleurSol = new Color().set(17/256f,63/256f,89/256f, 1);
		}
		else if(couleur == 8){ 								
			couleurBalle = new Color().set(1,1,1,1);		
			couleurFond = new Color().set(230/256f,102/256f,63/256f, 1);
			couleurSol = new Color().set(92/256f,126/256f,138/256f, 1);
		}
		else if(couleur == 9){ 						
			couleurBalle = new Color().set(254/256f,29/256f,57/256f, 1);	
			couleurFond = new Color().set(145/256f,142/256f,139/256f, 1);
			couleurSol = new Color().set(54/256f,55/256f,55/256f, 1);
		}
		else if(couleur == 10){ 					
			couleurBalle = new Color().set(1,1,1,1);				
			couleurFond = new Color().set(224/256f,194/256f,132/256f, 1);
			couleurSol = new Color().set(9/256f,112/256f,104/256f, 1);
		}
		else if(couleur == 11){ 					
			couleurBalle = new Color().set(1,1,1,1);				
			couleurFond = new Color().set(221/256f,97/256f,74/256f, 1);
			couleurSol = new Color().set(115/256f,165/256f,128/256f, 1);
		}
		else if(couleur == 12){ 							//COULEURS DU MENU	
			couleurBalle = new Color().set(1,1,1,1);		
			couleurFond = new Color().set(35/256f,59/256f,95/256f, 1);	
			couleurSol = new Color().set(126/256f,0/256f,10/256f, 1);	
		}
		else if(couleur == 13){ 					
			couleurBalle = new Color().set(236/256f,235/256f,243/256f, 1);				
			couleurFond = new Color().set(199/256f,214/256f,213/256f, 1);
			couleurSol = new Color().set(93/256f,163/256f,153/256f, 1);
		}
		else if(couleur == 14){ 					
			couleurBalle = new Color().set(1,1,1,1);				
			couleurFond = new Color().set(213/256f,198/256f,122/256f, 1);
			couleurSol = new Color().set(37/256f,92/256f,153/256f, 1);
		}
		else if(couleur == 15){ 					
			couleurBalle = new Color().set(1,1,1,1);				
			couleurFond = new Color().set(84/256f,75/256f,61/256f, 1);
			couleurSol = new Color().set(78/256f,110/256f,88/256f, 1);
		}
		else if(couleur == 16){ 					
			couleurBalle = new Color().set(1,1,1,1);				
			couleurFond = new Color().set(81/256f,62/256f,71/256f, 1);
			couleurSol = new Color().set(204/256f,52/256f,71/256f, 1);
		}
		else if(couleur == 17){ 					
			couleurBalle = new Color().set(1,1,1,1);				
			couleurFond = new Color().set(204/256f,190/256f,148/256f, 1);
			couleurSol = new Color().set(108/256f,163/256f,144/256f, 1);
		}
	}
	
	public Color getCouleurBalle(){
		return couleurBalle;
	}
	
	public Color getCouleurFond(){
		return couleurFond;
	}
	
	public Color getCouleurSol(){
		return couleurSol;
	}
	
	public Color getCouleurLéger(){
		return couleurLéger;
	}
	
	public Color getCouleurVide(){
		return couleurVide;
	}
	
	public Color getCouleurEau(){
		return couleurEau;
	}
}
