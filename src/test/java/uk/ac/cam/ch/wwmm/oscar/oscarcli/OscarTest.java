package uk.ac.cam.ch.wwmm.oscar.oscarcli;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;
import uk.ac.cam.ch.wwmm.oscar.document.TokenSequence;
import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;

/**
 * @author egonw
 */
@RunWith(JExample.class)
public class OscarTest {

	@Test public Oscar testConstructor() throws URISyntaxException {
		Oscar oscar = new Oscar();
		Assert.assertNotNull(oscar);
		return oscar;
	}

	@Given("#testConstructor")
	public String testNormalize(Oscar oscar) throws URISyntaxException {
		String input = oscar.normalize("This is a simple input string with benzene.");
		Assert.assertNotNull(input);
		return input;
	}

	@Given("#testConstructor,#testNormalize")
	public List<TokenSequence> testTokenize(Oscar oscar, String input) throws Exception {
		List<TokenSequence> tokens = oscar.tokenize(input);
		Assert.assertNotNull(tokens);
		Assert.assertNotSame(0, tokens.size());
		return tokens;
	}

	@Given("#testConstructor,#testTokenize")
	public List<NamedEntity> testRecognizeNamedEntities(Oscar oscar, List<TokenSequence> tokens) throws Exception {
		List<NamedEntity> entities = oscar.recognizeNamedEntities(tokens);
		Assert.assertNotNull(entities);
		Assert.assertEquals(1, entities.size());
		System.out.println(""+ entities.get(0));
		return entities;
	}

	@Given("#testConstructor,#testRecognizeNamedEntities")
	public void testResolveNamedEntities(Oscar oscar, List<NamedEntity> entities) throws Exception {
		Map<NamedEntity,String> structures = oscar.resolveNamedEntities(entities);
		Assert.assertNotNull(structures);
		Assert.assertEquals(1, structures.size());
		System.out.println(""+ structures.values().iterator().next());
	}

	@Test public void testMain() throws Exception {
		Oscar.main(new String[]{"This is a simple input string with benzene."});
	}

}
