package uk.ac.cam.ch.wwmm.oscar.oscarcli;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Builder;
import nu.xom.Document;
import uk.ac.cam.ch.wwmm.oscartokeniser.ProcessingDocument;
import uk.ac.cam.ch.wwmm.oscartokeniser.ProcessingDocumentFactory;
import uk.ac.cam.ch.wwmm.oscartokeniser.Token;
import uk.ac.cam.ch.wwmm.oscartokeniser.TokenSequence;

/**
 * @author egonw
 */
public class Oscar {

	public Oscar() {};
	
	public static void main(String[] args) throws Exception {
		String input = args[0]; // yes, yes, very dirty...
		Oscar oscar = new Oscar();
		input = oscar.normalize(input);
		List<String> tokens = oscar.tokenize(input);
		List<String> entities = oscar.recognizeNamedEntities(tokens);
		// List<IMolecule> molecules = oscar.resolveNamedEntities(entities);
	}

	public List<String> tokenize(String input) throws Exception {
		Builder parser = new Builder();
		Document doc = parser.build(
			"<node>" + input + "</node>",
			"http://whatever.example.org/"
		);
		ProcessingDocument procDoc = new ProcessingDocumentFactory().
			makeTokenisedDocument(
				doc, true, false, false
			);
		List<String> tokens = new ArrayList<String>();
		List<TokenSequence> tokenSequences = procDoc.getTokenSequences();
		for (TokenSequence sequence : tokenSequences) {
			List<Token> realTokens = sequence.getTokens();
			for (Token token : realTokens) {
				tokens.add(token.getValue());
			}
		}
		return tokens;
	}

	public String normalize(String input) {
		return input;
	}

	public List<String> recognizeNamedEntities(List<String> tokens) {
		List<String> entities = new ArrayList<String>();
		return entities;
	}

}
