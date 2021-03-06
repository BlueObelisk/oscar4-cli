package uk.ac.cam.ch.wwmm.oscar.oscarcli;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;
import net.htmlparser.jericho.Source;
import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.chemnamedict.entities.FormatType;
import uk.ac.cam.ch.wwmm.oscar.chemnamedict.entities.ResolvedNamedEntity;
import uk.ac.cam.ch.wwmm.oscar.formatter.IOutputFormatter;
import uk.ac.cam.ch.wwmm.oscar.formatter.STDOUTFormatter;
import uk.ac.cam.ch.wwmm.oscar.formatter.rdf.CHEMINFFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Command line utility to invoke Oscar. It takes plain text as input, and
 * outputs recognized chemical entites as output in either plain text or
 * in Turtle output.
 *
 * @author egonw
 */
public class OscarCLI {

    @Argument(
      value="output",
      description="Mime type of expected output. Supported: text/plain, text/turtle."
    )
	private String output = "text/plain";

    @Argument(description = "If true, reads the input from STDIN.")
    private boolean stdin = false;

    @Argument(description = "If true, the input is HTML.")
    private boolean html = false;

	private Oscar oscar;

	public OscarCLI() {
		oscar = new Oscar();
	};
	
	public void processLine(String line, IOutputFormatter formatter) {
		//TODO oscar.normalise isn't implemented yet!
//		line = oscar.normalise(line);
		List<ResolvedNamedEntity> resolved = oscar.findResolvableEntities(line);
		for (ResolvedNamedEntity rne : resolved) {
			formatter.write(rne.getNamedEntity(), rne.getFirstChemicalStructure(FormatType.STD_INCHI).getValue());
		}
	}
	
	public static void main(String[] args) throws IOException {
		OscarCLI command = new OscarCLI();
		List<String> extras = Args.parse(command, args);
		IOutputFormatter formatter;

		if ("text/turtle".equals(command.output)) {
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
