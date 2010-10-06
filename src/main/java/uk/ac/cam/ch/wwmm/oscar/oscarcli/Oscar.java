package uk.ac.cam.ch.wwmm.oscar.oscarcli;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Builder;
import nu.xom.Document;
import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;
import uk.ac.cam.ch.wwmm.oscar.document.ProcessingDocument;
import uk.ac.cam.ch.wwmm.oscar.document.ProcessingDocumentFactory;
import uk.ac.cam.ch.wwmm.oscar.document.TokenSequence;
import uk.ac.cam.ch.wwmm.oscarMEMM.MEMMRecogniser;
import uk.ac.cam.ch.wwmm.oscarMEMM.memm.document.Tokeniser;

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
			"<P>" + input + "</P>",
			"http://whatever.example.org/"
		);
		ProcessingDocument procDoc = new ProcessingDocumentFactory().
			makeTokenisedDocument(Tokeniser.getInstance(),
				doc, true, false, false
			);
		List<TokenSequence> tokenSequences = procDoc.getTokenSequences();
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
