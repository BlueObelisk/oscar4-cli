package uk.ac.cam.ch.wwmm.oscar.oscarcli;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Builder;
import nu.xom.Document;
import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;
import uk.ac.cam.ch.wwmm.oscar.document.TokenSequence;
import uk.ac.cam.ch.wwmm.oscar.document.ProcessingDocument;
import uk.ac.cam.ch.wwmm.oscarMEMM.MEMMRecogniser;

/**
 * @author egonw
 */
public class Oscar {

	public Oscar() {};
	
	public static void main(String[] args) throws Exception {
		String input = args[0]; // yes, yes, very dirty...
		Oscar oscar = new Oscar();
		input = oscar.normalize(input);
		List<TokenSequence> tokens = oscar.tokenize(input);
		List<NamedEntity> entities = oscar.recognizeNamedEntities(tokens);
		// List<IMolecule> molecules = oscar.resolveNamedEntities(entities);
	}

	public List<TokenSequence> tokenize(String input) throws Exception {
		Builder parser = new Builder();
		Document doc = parser.build(
			"<node>" + input + "</node>",
			"http://whatever.example.org/"
		);
		throw new Exception(
			"Need to port ProcessingDocumentFactory to oscar4-core first"
		);
//		ProcessingDocument procDoc = new ProcessingDocumentFactory().
//			makeTokenisedDocument(
//				doc, true, false, false
//			);
//		List<String> tokens = new ArrayList<String>();
//		List<TokenSequence> tokenSequences = procDoc.getTokenSequences();
//		return tokenSequences;
//		for (TokenSequence sequence : tokenSequences) {
//			List<Token> realTokens = sequence.getTokens();
//			for (Token token : realTokens) {
//				tokens.add(token.getValue());
//			}
//		}
//		return tokens;
	}

	public String normalize(String input) {
		return input;
	}

	public List<NamedEntity> recognizeNamedEntities(List<TokenSequence> tokens) {
		List<String> entities = new ArrayList<String>();
		throw new RuntimeException(
			"I need to make MEMMRecognizer use the classes from oscar4-core first."
		);
//		MEMMRecogniser mer = new MEMMRecogniser();
//		return mer.findNamedEntities(tokens);
	}

}
