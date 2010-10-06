package uk.ac.cam.ch.wwmm.oscar.oscarcli;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import uk.ac.cam.ch.wwmm.oscar.document.TokenSequence;

/**
 * @author egonw
 */
public class OscarTest {

	@Test public void testConstructor() {
		Oscar oscar = new Oscar();
		Assert.assertNotNull(oscar);
	}

	@Test public void testNormalize() {
		Oscar oscar = new Oscar();
		String input = oscar.normalize("This is a simple input string with benzene.");
		Assert.assertNotNull(input);
	}

	@Test
	public void testTokenize() throws Exception {
		Oscar oscar = new Oscar();
		List<TokenSequence> tokens = oscar.tokenize("This is a simple input string with benzene.");
		Assert.assertNotNull(tokens);
		Assert.assertNotSame(0, tokens.size());
	}

	@Test public void testMain() throws Exception {
		Oscar.main(new String[]{"This is a simple input string with benzene."});
	}

}
