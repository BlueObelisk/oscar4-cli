package uk.ac.cam.ch.wwmm.oscar.oscarcli;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.chemnamedict.entities.ResolvedNamedEntity;
import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;
import uk.ac.cam.ch.wwmm.oscar.document.TokenSequence;

/**
 * @author egonw
 */
public class OscarCLITest {

	public static Oscar oscar;
	
	@BeforeClass
	public static void setUp() {
		oscar = new Oscar();
	}
	
	@AfterClass
	public static void cleanUp() {
		oscar = null;
	}
	
	
	@Test
	public void testConstructor() {
		assertNotNull(oscar);
	}

	//TODO oscar.normalise isn't implemented yet!
//	@Test
//	public void testNormalize() {
//		String input = oscar.normalise("This is a simple input string with benzene.");
//		assertNotNull(input);
//	}

	@Test
	public void testTokenise() {
		List<TokenSequence> tokens = oscar.tokenise("This is a simple input string with benzene.");
		assertNotNull(tokens);
		assertNotSame(0, tokens.size());
	}

	@Test
	public void testRecogniseNamedEntities() {
		List<TokenSequence> tokens = oscar.tokenise("This is a simple input string with benzene.");
		List<NamedEntity> entities = oscar.recogniseNamedEntities(tokens);
		assertNotNull(entities);
		assertEquals(1, entities.size());
		System.out.println(""+ entities.get(0));
	}

// Oscar.resolveNamedEntities method is currently private	
//	@Test
//	public void testResolveNamedEntities() {
//		List<TokenSequence> tokens = oscar.tokenise("This is a simple input string with benzene.");
//		List<NamedEntity> entities = oscar.recogniseNamedEntities(tokens);
//		oscar.getDictionaryRegistry().register(new OpsinDictionary());
//		Map<NamedEntity,String> structures = oscar.resolveNamedEntities(entities);
//		assertNotNull(structures);
//		assertEquals(1, structures.size());
//		System.out.println(""+ structures.values().iterator().next());
//	}

	@Test
	public void testGetNamedEntities() {
		List<NamedEntity> structures = oscar.findNamedEntities(
			"Acetic acid is an acid - water is not."
		);
		assertNotNull(structures);
		for (NamedEntity ent : structures)
			System.out.println(ent.getSurface());
		assertEquals(3, structures.size());
	}

	@Test
	public void testFindResolvableEntities() {
		List <ResolvedNamedEntity> structures = oscar.findResolvableEntities(
			"Acetic acid is an acid - water is not."
		);
		assertEquals(2, structures.size());
	}

	@Test
	public void testMainPlainText() throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PrintStream pStream = new PrintStream(stream, true);
		PrintStream originalOut = System.out;
		System.setOut(pStream);
		OscarCLI.main(
			new String[]{
				"This is a simple input string with benzene."
			}
		);
		System.setOut(originalOut);
		String output = stream.toString();
		System.out.println("output: " + output);
		assertTrue(output.contains("InChI"));
	}

	@Test
	public void testMainRDF() throws IOException  {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PrintStream pStream = new PrintStream(stream, true);
		PrintStream originalOut = System.out;
		System.setOut(pStream);
		OscarCLI.main(
			new String[]{
				"-output", "text/turtle",
				"This is a simple input string with benzene."
			}
		);
		System.setOut(originalOut);
		String output = stream.toString();
		System.out.println("output: " + output);
		assertTrue(output.contains("@prefix"));
		assertTrue(output.contains("cheminf"));
		assertTrue(output.contains("ex:entity"));
		assertTrue(output.contains("CHEMINF_000113"));
		assertTrue(output.contains("SIO_000300"));
		assertTrue(output.contains("InChI"));
	}
}
