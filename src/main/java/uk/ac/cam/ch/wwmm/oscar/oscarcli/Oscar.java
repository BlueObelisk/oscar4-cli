package uk.ac.cam.ch.wwmm.oscar.oscarcli;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import uk.ac.cam.ch.wwmm.opsin.NameToStructure;
import uk.ac.cam.ch.wwmm.opsin.NameToStructureException;
import uk.ac.cam.ch.wwmm.opsin.OpsinResult;
import uk.ac.cam.ch.wwmm.opsin.OpsinResult.OPSIN_RESULT_STATUS;
import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;
import uk.ac.cam.ch.wwmm.oscar.document.ProcessingDocument;
import uk.ac.cam.ch.wwmm.oscar.document.ProcessingDocumentFactory;
import uk.ac.cam.ch.wwmm.oscar.document.Token;
import uk.ac.cam.ch.wwmm.oscar.document.TokenSequence;
import uk.ac.cam.ch.wwmm.oscarMEMM.MEMMRecogniser;
import uk.ac.cam.ch.wwmm.oscartokeniser.Tokeniser;

/**
 * @author egonw
 */
public class Oscar {

	private NameToStructure nameToStructure;
	
	public Oscar() throws NameToStructureException {
		nameToStructure = NameToStructure.getInstance();
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
		Map<Element,NamedEntity> molecules = oscar.resolveNamedEntities(entities);
		for (Element element : molecules.keySet())
			System.out.println(element.toXML());
	}

	private Map<Element,NamedEntity> resolveNamedEntities(List<NamedEntity> entities) {
		Map<Element,NamedEntity> cmlMols = new HashMap<Element,NamedEntity>();
		for (NamedEntity entity : entities) {
			System.out.println("Entity: " + entity.getSurface());
			OpsinResult result = nameToStructure.parseChemicalName(
				entity.getSurface(), false
			);
			if (result.getStatus() == OPSIN_RESULT_STATUS.SUCCESS) {
				cmlMols.put(result.getCml(), entity);
			}
		}
		return cmlMols;
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
