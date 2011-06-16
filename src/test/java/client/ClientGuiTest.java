package client;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class ClientGuiTest {

	
	/**
	 * Test method for {@link client.ClientGui#creaUtente(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreaUtente() {
		ClientGui c = new ClientGui("localhost",1234);
		try {
			c.creaUtente(null, null);
			fail("non ha generato eccezione");
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}	
	}

	/**
	 * Test method for {@link client.ClientGui#eseguiLogin(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testEseguiLogin() {
		ClientGui c = new ClientGui("localhost",1234);
		try {
			c.eseguiLogin(null, null);
			fail("non ha generato eccezione");
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}	
	}

	/**
	 * Test method for {@link client.ClientGui#creaRazza(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreaRazza() {
		ClientGui c = new ClientGui("localhost",1234);
		try {
			c.creaRazza(null,null,null,null);
			fail("non ha generato eccezione");
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}	
	}

	/**
	 * Test method for {@link client.ClientGui#logout()}.
	 */
	@Test
	public void testLogout() {
		ClientGui c = new ClientGui("localhost",1234);
		try {
			c.logout();
			fail("non ha generato eccezione");
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}	
	}

	/**
	 * Test method for {@link client.ClientGui#classifica()}.
	 */
	@Test
	public void testClassifica() {
		ClientGui c = new ClientGui("localhost",1234);
		try {
			c.classifica();
			fail("non ha generato eccezione");
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}	
	}

	/**
	 * Test method for {@link client.ClientGui#uscitaPartita()}.
	 */
	@Test
	public void testUscitaPartita() {
		ClientGui c = new ClientGui("localhost",1234);
		try {
			c.uscitaPartita();
			fail("non ha generato eccezione");
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}	
	}

	/**
	 * Test method for {@link client.ClientGui#mappaGenerale()}.
	 */
	@Test
	public void testMappaGenerale() {
		ClientGui c = new ClientGui("localhost",1234);
		try {
			c.mappaGenerale();
			fail("non ha generato eccezione");
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}	
	}

	/**
	 * Test method for {@link client.ClientGui#statoDinosauro(java.lang.String)}.
	 */
	@Test
	public void testStatoDinosauro() {
		ClientGui c = new ClientGui("localhost",1234);
		try {
			c.statoDinosauro(null);
			fail("non ha generato eccezione");
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}	
	}

	/**
	 * Test method for {@link client.ClientGui#accessoPartita(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAccessoPartita() {
		ClientGui c = new ClientGui("localhost",1234);
		try {
			c.accessoPartita(null, null);
			fail("non ha generato eccezione");
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}	
	}

	/**
	 * Test method for {@link client.ClientGui#vistaLocale(java.lang.String)}.
	 */
	@Test
	public void testVistaLocale() {
		ClientGui c = new ClientGui("localhost",1234);
		try {
			c.vistaLocale(null);
			fail("non ha generato eccezione");
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}	
	}

	/**
	 * Test method for {@link client.ClientGui#crescitaDinosauro(java.lang.String)}.
	 */
	@Test
	public void testCrescitaDinosauro() {
		ClientGui c = new ClientGui("localhost",1234);
		try {
			c.crescitaDinosauro(null);
			fail("non ha generato eccezione");
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}	
	}

	/**
	 * Test method for {@link client.ClientGui#deponiUovo(java.lang.String)}.
	 */
	@Test
	public void testDeponiUovo() {
		ClientGui c = new ClientGui("localhost",1234);
		try {
			c.deponiUovo(null);
			fail("non ha generato eccezione");
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}	
	}

	/**
	 * Test method for {@link client.ClientGui#muoviDinosauro(java.lang.String, int, int)}.
	 */
	@Test
	public void testMuoviDinosauro() {
		ClientGui c = new ClientGui("localhost",1234);
		try {
			c.muoviDinosauro(null,1,1);
			fail("non ha generato eccezione");
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}
		try {
			c.muoviDinosauro("11",0,0);
			fail("non ha generato eccezione");
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link client.ClientGui#listaDinosauri()}.
	 */
	@Test
	public void testListaDinosauri() {
		ClientGui c = new ClientGui("localhost",1234);
		try {
			c.listaDinosauri();
			fail("non ha generato eccezione");
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link client.ClientGui#confermaTurno()}.
	 */
	@Test
	public void testConfermaTurno() {
		ClientGui c = new ClientGui("localhost",1234);
		try {
			c.confermaTurno();
			fail("non ha generato eccezione");
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link client.ClientGui#passaTurno()}.
	 */
	@Test
	public void testPassaTurno() {
		ClientGui c = new ClientGui("localhost",1234);
		try {
			c.passaTurno();
			fail("non ha generato eccezione");
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link client.ClientGui#inviaAlServer()}.
	 */
	@Test
	public void testInviaAlServer() {
		ClientGui c = new ClientGui("localhost",1234);
		try {
			c.inviaAlServer();
			fail("non ha generato eccezione");
		} catch (IOException e) {
//			e.printStackTrace();
		} catch (InterruptedException e) {
//			e.printStackTrace();
		} catch (NullPointerException e) {
//			e.printStackTrace();
		}	
	}
}
