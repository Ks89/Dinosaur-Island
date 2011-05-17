package isoladinosauri;

import isoladinosauri.modellodati.Dinosauro;

import java.util.List;

public class Test {
	public static void main(String[] args) {
		Isola i = new Isola();
		Partita p = new Partita(i);
		Turno t = new Turno(p);
		
		p.setTurnoCorrente(t);

		i.caricaMappa();

		//il valore 0 in giocatore dovrebbe essere il turno corrente, cioe' il campo nella classe turno
		Giocatore giocatore = new Giocatore(p, "", "", p.getTurnoCorrente().getContatoreTurno(), "t-rex", "carnivoro");
		giocatore.setNomeUtente("pippo");
		giocatore.setPassword("pappap");
		p.aggiungiGiocatore(giocatore);
		giocatore.getDinosauri().get(0).aumentaDimensione();
		giocatore.getDinosauri().get(0).setEnergia(2500);
		t.illuminaMappa(giocatore.getDinosauri().get(0).getPosX(),giocatore.getDinosauri().get(0).getPosY());
		giocatore.stampaMappa();
	    
		
		//depone uovo, cioe' aggiungo solo nell'array
		giocatore.getDinosauri().get(0).deponi(i.getMappa()[giocatore.getDinosauri().get(0).getPosX()][giocatore.getDinosauri().get(0).getPosY()], giocatore);

		System.out.println("L'uovo e' in coordinate" + giocatore.getUova().get(0));
		
		//fa schiudere le uova
		p.nascitaDinosauro(4);

		giocatore.getDinosauri().get(1).setEnergia(12500);
		giocatore.getDinosauri().get(1).aumentaDimensione();
		giocatore.getDinosauri().get(1).aumentaDimensione();
		giocatore.getDinosauri().get(1).aumentaDimensione();

		giocatore.getDinosauri().get(1).setEnergia(2500);

		t.illuminaMappa(giocatore.getDinosauri().get(1).getPosX(),giocatore.getDinosauri().get(1).getPosY());
		
		giocatore.stampaMappa();

		//testo la classifica
		Giocatore giocatore2 = new Giocatore(p,"", "", p.getTurnoCorrente().getContatoreTurno(), "brontosauro", "erbivoro");
		giocatore2.setNomeUtente("minny");
		giocatore2.setPassword("dsfjsdf");
		giocatore2.getDinosauri().get(0).aumentaDimensione();
		giocatore2.getDinosauri().get(0).setEnergia(9000);

		p.aggiungiGiocatore(giocatore2);
		
		Giocatore giocatore3 = new Giocatore(p,"", "", p.getTurnoCorrente().getContatoreTurno(), "stego", "erbivoro");
		giocatore3.setNomeUtente("pluto");
		giocatore3.setPassword("ddsgsdg");
		giocatore3.getDinosauri().get(0).aumentaDimensione();
		giocatore3.getDinosauri().get(0).setEnergia(9000); 

		p.aggiungiGiocatore(giocatore3);

		t.illuminaMappa(giocatore2.getDinosauri().get(0).getPosX(),giocatore2.getDinosauri().get(0).getPosY());
		t.illuminaMappa(giocatore3.getDinosauri().get(0).getPosX(),giocatore3.getDinosauri().get(0).getPosY());

		giocatore2.getDinosauri().get(0).deponi(i.getMappa()[giocatore2.getDinosauri().get(0).getPosX()][giocatore2.getDinosauri().get(0).getPosY()], giocatore2);
		giocatore3.getDinosauri().get(0).deponi(i.getMappa()[giocatore3.getDinosauri().get(0).getPosX()][giocatore3.getDinosauri().get(0).getPosY()], giocatore3);

		p.nascitaDinosauro(4);

		t.illuminaMappa(giocatore2.getDinosauri().get(1).getPosX(),giocatore2.getDinosauri().get(1).getPosY());
		t.illuminaMappa(giocatore3.getDinosauri().get(1).getPosX(),giocatore3.getDinosauri().get(1).getPosY());

		p.aggiornaClassifica();
		p.stampaClassifica();

		i.stampaMappa();

		p.aggiornaClassifica();
		System.out.println("Classifica finale:");
		p.stampaClassifica();

		i.stampaMappa();  

		giocatore.stampaMappa();
		giocatore2.stampaMappa();
		giocatore3.stampaMappa();

		System.out.println("giocatore1:" + giocatore.getIdGiocatore());
		System.out.println("giocatore2:" + giocatore2.getIdGiocatore());
		System.out.println("giocatore3:" + giocatore3.getIdGiocatore());
		
		p.rimuoviGiocatore(giocatore3);
		
		Giocatore giocatore4 = new Giocatore(p,"", "", p.getTurnoCorrente().getContatoreTurno(), "spino", "carnivoro");
		giocatore4.setNomeUtente("paperone");
		giocatore4.setPassword("asdasd");
		giocatore4.getDinosauri().get(0).aumentaDimensione();
		giocatore4.getDinosauri().get(0).setEnergia(9000); 

		p.aggiungiGiocatore(giocatore4);
		
		System.out.println("giocatore1:" + giocatore.getIdGiocatore());
		System.out.println("giocatore2:" + giocatore2.getIdGiocatore());
		System.out.println("giocatore4:" + giocatore4.getIdGiocatore());
				
		int[][] map = t.ottieniRaggiungibilita(giocatore.getDinosauri().get(1).getPosX(),giocatore.getDinosauri().get(1).getPosY());
		for(int a=0; a<map.length; a++){
			for(int b=0; b<map[0].length; b++)
				System.out.printf(map[a][b]+"\t");
			System.out.printf("\n\n");
		}
		System.out.println();
		int[][] map2 = t.ottieniStradaPercorsa(giocatore.getDinosauri().get(1).getPosX(),giocatore.getDinosauri().get(1).getPosY(),giocatore.getDinosauri().get(1).getPosX()-2,giocatore.getDinosauri().get(1).getPosY()+3);
		for(int r=0; r<map2.length; r++){
			for(int c=0; c<map2[0].length; c++)
				System.out.printf(map2[r][c]+"\t");
			System.out.printf("\n\n");
		}

	}
}