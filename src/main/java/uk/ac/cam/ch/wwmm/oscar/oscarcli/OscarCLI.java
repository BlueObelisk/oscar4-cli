package uk.ac.cam.ch.wwmm.oscar.oscarcli;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nu.xom.Builder;
import nu.xom.Document;
import uk.ac.cam.ch.wwmm.oscar.Oscar;
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
public class OscarCLI {

	ChemNameDictRegistry registry;

	public OscarCLI() throws URISyntaxException {
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


}
