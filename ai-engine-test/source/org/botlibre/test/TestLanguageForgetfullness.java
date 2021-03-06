/******************************************************************************
 *
 *  Copyright 2014 Paphus Solutions Inc.
 *
 *  Licensed under the Eclipse Public License, Version 1.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/legal/epl-v10.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/
package org.botlibre.test;

import java.util.List;
import java.util.logging.Level;

import org.botlibre.Bot;
import org.botlibre.api.knowledge.Network;
import org.botlibre.knowledge.Bootstrap;
import org.botlibre.sense.text.TextEntry;
import org.botlibre.thought.forgetfulness.Forgetfulness;
import org.botlibre.util.Utils;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Test the state decompiler.
 */

public class TestLanguageForgetfullness extends TestLanguage {

	@BeforeClass
	public static void setup() {
		bootstrap();
		Bot bot = Bot.createInstance();
		bot.setName("test");

		TextEntry text = bot.awareness().getSense(TextEntry.class);
		List<String> output = registerForOutput(text);
		text.input("ping");
		waitForOutput(output);

		Utils.sleep(100);
		
		new Bootstrap().rebootstrapMemory(bot.memory());
		
		Utils.sleep(500);

		Network network = bot.memory().newMemory();
		Forgetfulness forgetfulness = bot.mind().getThought(Forgetfulness.class);
		try {
			forgetfulness.setMaxRelationships(50);
			forgetfulness.setMaxSize(1000);
			forgetfulness.forget(network, true, 100);
			forgetfulness.forget(network, true, 100);
		} catch (Exception exception) {
			bot.log(bot, exception);
		}
		network.save();

		text.input("sky blue red dog cat green grass tall like very loves");
		waitForOutput(output);
		Utils.sleep(20000);
		
		bot.shutdown();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		shutdown();
	}
}

