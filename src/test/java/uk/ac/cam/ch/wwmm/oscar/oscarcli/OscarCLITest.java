package uk.ac.cam.ch.wwmm.oscar.oscarcli;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.adv.AdvancedOscar;
import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;
import uk.ac.cam.ch.wwmm.oscar.document.TokenSequence;
import uk.ac.cam.ch.wwmm.oscar.opsin.OpsinDictionary;
import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;

/**
 * @author egonw
 */
@RunWith(JExample.class)
public class OscarCLITest {

	@Test public Oscar testConstructor() throws Exception {
		AdvancedOscar oscar = new AdvancedOscar(getClass().getClassLoader());
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
		oscar.getDictionaryRegistry().register(new OpsinDictionary());
		Map<NamedEntity,String> structures = oscar.resolveNamedEntities(entities);
		Assert.assertNotNull(structures);
		Assert.assertEquals(1, structures.size());
		System.out.println(""+ structures.values().iterator().next());
	}

	@Given("#testConstructor")
	public void testGetNamedEntities(Oscar oscar) throws Exception {
		List<NamedEntity> structures = oscar.getNamedEntities(
			"Ingredients: acetic acid, water."
		);
		Assert.assertNotNull(structures);
		for (NamedEntity ent : structures)
			System.out.println(ent.getSurface());
		Assert.assertEquals(3, structures.size());
	}

	@Given("#testConstructor")
	public void testGetResolvedEntities(Oscar oscar) throws Exception {
		oscar.getDictionaryRegistry().register(new OpsinDictionary());
		Map<NamedEntity,String> structures = oscar.getResolvedEntities(
			"Ingredients: acetic acid, water."
		);
		Assert.assertNotNull(structures);
		Assert.assertEquals(2, structures.size());
	}

	@Test public void testMain() throws Exception {
		OscarCLI.main(new String[]{"This is a simple input string with benzene."});
	}

}
