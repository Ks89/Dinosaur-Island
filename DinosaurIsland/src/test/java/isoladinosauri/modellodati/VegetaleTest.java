/*
Copyright 2011-2015 Stefano Cappa
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package isoladinosauri.modellodati;

import org.junit.Test;
import server.modellodati.Vegetale;

public class VegetaleTest {

	/**
	 * Test method for {@link isoladinosauri.modellodati.Vegetale#cresci()}.
	 */
	@Test
	public void testCresci() {
		Vegetale v = new Vegetale();
		v.cresci();
		v.setEnergia(10); //abbasso l'energia per poi farla ricrescere
		v.cresci();
	}
}
