package uk.ac.cam.ch.wwmm.oscar.oscarcli;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nu.xom.Builder;
import nu.xom.Document;
import uk.ac.cam.ch.wwmm.oscar.chemnamedict.ChemNameDictRegistry;
import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;
import uk.ac.cam.ch.wwmm.oscar.document.ProcessingDocument;
import uk.ac.cam.ch.wwmm.oscar.document.ProcessingDocumentFactory;
import uk.ac.cam.ch.wwmm.oscar.document.Token;
import uk.ac.cam.ch.wwmm.oscar.document.TokenSequence;
import uk.ac.cam.ch.wwmm.oscar.opsin.OpsinDictionary;
import uk.ac.cam.ch.wwmm.oscarMEMM.MEMMRecogniser;
import uk.ac.cam.ch.wwmm.oscartokeniser.Tokeniser;

/**
 * @author egonw
 */
public class Oscar {

	ChemNameDictRegistry registry;

	public Oscar() throws URISyntaxException {
		registry = ChemNameDictRegistry.getInstance();
		registry.register(new OpsinDictionary());
	};
	
	public static void main(String[] args) throws Exception {
		String input = "some 1-propanol would be nice";
		if (args.length > 0) {
			StringBuilder builder = new StringBuilder();
			for (String arg : args) builder.append(arg);
			input = builder.toString();
		}
		Oscar oscar = new Oscar();
		input = oscar.normalize(input);
		List<TokenSequence> tokens = oscar.tokenize(input);
		List<NamedEntity> entities = oscar.recognizeNamedEntities(tokens);
		Map<NamedEntity,String> molecules = oscar.resolveNamedEntities(entities);
		for (String element : molecules.values())
			System.out.println(element);
	}

	public Map<NamedEntity,String> resolveNamedEntities(List<NamedEntity> entities) {
		Map<NamedEntity,String> hits = new HashMap<NamedEntity,String>();
		for (NamedEntity entity : entities) {
			String name = entity.getSurface();
			System.out.println("Entity: " + name);
			Set<String> inchis = registry.getInChI(name);
			if (inchis.size() == 1) {
				hits.put(entity, inchis.iterator().next());
			} else if (inchis.size() > 1) {
				System.out.println("Warning: multiple hits, returning only one");
				hits.put(entity, inchis.iterator().next());
			}
		}
		return hits;
	}

	public List<TokenSequence> tokenize(String input) throws Exception {
		Builder parser = new Builder();
		Document doc = parser.build(
			"<P>" + input + "</P>",
			"http://whatever.example.org/"
		);
		ProcessingDocument procDoc = new ProcessingDocumentFactory().
			makeTokenisedDocument(Tokeniser.getInstance(),
				doc, true, false, false
			);
		List<TokenSequence> tokenSequences = procDoc.getTokenSequences();
		for (TokenSequence tokens : tokenSequences) {
			for (Token token : tokens.getTokens())
				System.out.println("token: " + token.getValue());
		}
		return tokenSequences;
	}

	public String normalize(String input) {
		return input;
	}

	public List<NamedEntity> recognizeNamedEntities(List<TokenSequence> tokens) throws Exception {
		MEMMRecogniser mer = new MEMMRecogniser();
		return mer.findNamedEntities(tokens);
	}

}
