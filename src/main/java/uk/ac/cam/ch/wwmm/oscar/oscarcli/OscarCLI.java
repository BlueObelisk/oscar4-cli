package uk.ac.cam.ch.wwmm.oscar.oscarcli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;
import uk.ac.cam.ch.wwmm.oscar.document.TokenSequence;
import uk.ac.cam.ch.wwmm.oscar.opsin.OpsinDictionary;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;

/**
 * @author egonw
 */
public class OscarCLI {

    @Argument(
      value="accepts",
      description="Mime type of expected output. Supported: text/plain, text/turtle."
    )
	private String accepts = "text/plain";

	private Oscar oscar;

	private int counter;

	public OscarCLI() throws Exception {
		oscar = new Oscar();
		oscar.getDictionaryRegistry().register(new OpsinDictionary());
		counter = 0;
	};
	
	public void processLine(String line) throws Exception {
		line = oscar.normalize(line);
		List<TokenSequence> tokens = oscar.tokenize(line);
		List<NamedEntity> entities = oscar.recognizeNamedEntities(tokens);
		Map<NamedEntity,String> molecules = oscar.resolveNamedEntities(entities);
		if ("text/plain".equals(accepts)) {
			for (NamedEntity entity : molecules.keySet()) {
				System.out.print(entity.getSurface() + ": ");
				System.out.println(molecules.get(entity));
			}
		} else if ("text/turtle".equals(accepts)) {
			for (NamedEntity entity : molecules.keySet()) {
				System.out.println("ex:entity" + (counter++) + " [");
				System.out.println("  a cheminf:CHEMINF_000000");
				System.out.println("  dc:label \"" + entity.getSurface() + "\"");
				System.out.println("] .");
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		OscarCLI command = new OscarCLI();
		List<String> extras = Args.parse(command, args);

		if ("text/turtle".equals(command.accepts)) {
			System.out.println("@prefix dc: <http://purl.org/dc/elements/1.1/> .");
			System.out.println("@prefix ex: <http://example.org/stuff/1.0/> .");
			System.out.println("@prefix cheminf: <http://semanticscience.org/resource/> .");
		}

		if (extras.size() == 1 && extras.get(0).equals("--")) {
			// read from STDIN
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(System.in)
			);
			String line;
			while ((line = reader.readLine()) != null) {
				// FIXME: need to do something clever to work around chemical
				//   entity names split over two (or more) lines
				command.processLine(line);
			}
		} else {
			// concat all strings and pass that
			StringBuilder builder = new StringBuilder();
			for (String string : extras) builder.append(string);
			command.processLine(builder.toString());
		}
	}


}
