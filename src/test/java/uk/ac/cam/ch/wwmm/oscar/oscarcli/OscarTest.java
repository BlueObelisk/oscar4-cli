package uk.ac.cam.ch.wwmm.oscar.oscarcli;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;
import uk.ac.cam.ch.wwmm.oscar.document.TokenSequence;

/**
 * @author egonw
 */
public class OscarTest {

	@Test public void testConstructor() throws URISyntaxException {
		Oscar oscar = new Oscar();
		Assert.assertNotNull(oscar);
	}

	@Test public void testNormalize() throws URISyntaxException {
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

	@Test
	public void testRecognizeNamedEntities() throws Exception {
		Oscar oscar = new Oscar();
		List<TokenSequence> tokens = oscar.tokenize("This is a simple input string with benzene.");
		List<NamedEntity> entities = oscar.recognizeNamedEntities(tokens);
		Assert.assertNotNull(entities);
		Assert.assertEquals(1, entities.size());
		System.out.println(""+ entities.get(0));
	}

	@Test
	public void testResolveNamedEntities() throws Exception {
		Oscar oscar = new Oscar();
		List<TokenSequence> tokens = oscar.tokenize("This is a simple input string with benzene.");
		List<NamedEntity> entities = oscar.recognizeNamedEntities(tokens);
		Map<NamedEntity,String> structures = oscar.resolveNamedEntities(entities);
		Assert.assertNotNull(structures);
		Assert.assertEquals(1, structures.size());
		System.out.println(""+ structures.values().iterator().next());
	}

	@Test public void testMain() throws Exception {
		Oscar.main(new String[]{"This is a simple input string with benzene."});
	}

}
