package uk.ac.cam.ch.wwmm.oscar.oscarcli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import net.htmlparser.jericho.Source;

import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.document.NamedEntity;
import uk.ac.cam.ch.wwmm.oscar.document.TokenSequence;
import uk.ac.cam.ch.wwmm.oscar.formatter.IOutputFormatter;
import uk.ac.cam.ch.wwmm.oscar.formatter.STDOUTFormatter;
import uk.ac.cam.ch.wwmm.oscar.formatter.rdf.CHEMINFFormatter;
import uk.ac.cam.ch.wwmm.oscar.opsin.OpsinDictionary;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;

/**
 * Command line utility to invoke Oscar. It takes plain text as input, and
 * outputs recognized chemical entites as output in either plain text or
 * in Turtle output.
 *
 * @author egonw
 */
public class OscarCLI {

    @Argument(
      value="accepts",
      description="Mime type of expected output. Supported: text/plain, text/turtle."
    )
	private String accepts = "text/plain";

    @Argument(description = "If true, reads the input from STDIN.")
    private boolean stdin = false;

    @Argument(description = "If true, the input is HTML.")
    private boolean html = false;

	private Oscar oscar;

	public OscarCLI() throws Exception {
		oscar = new Oscar();
		oscar.getDictionaryRegistry().register(new OpsinDictionary());
	};
	
	public void processLine(String line, IOutputFormatter formatter) throws Exception {
		line = oscar.normalize(line);
		List<TokenSequence> tokens = oscar.tokenize(line);
		List<NamedEntity> entities = oscar.recognizeNamedEntities(tokens);
		Map<NamedEntity,String> molecules = oscar.resolveNamedEntities(entities);
		for (NamedEntity entity : molecules.keySet()) {
			formatter.write(entity, molecules.get(entity));
		}
	}
	
	public static void main(String[] args) throws Exception {
		OscarCLI command = new OscarCLI();
		List<String> extras = Args.parse(command, args);
		IOutputFormatter formatter;

		if ("text/turtle".equals(command.accepts)) {
			formatter = new CHEMINFFormatter(System.out);
		} else {
			formatter = new STDOUTFormatter(System.out);
		}

		StringBuilder builder = new StringBuilder();
		if (command.stdin) {
			// read from STDIN
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(System.in)
			);
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} else {
			for (String string : extras) {
				builder.append(string).append(' ');
			}
		}
		String content = builder.toString();
		if (command.html) {
			Source source = new Source(content);
			content = source.getTextExtractor().toString();
		}
		
		command.processLine(content, formatter);
	}


}
