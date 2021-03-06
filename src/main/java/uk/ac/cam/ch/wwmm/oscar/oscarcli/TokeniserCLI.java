package uk.ac.cam.ch.wwmm.oscar.oscarcli;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;
import net.htmlparser.jericho.Source;
import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.document.Token;
import uk.ac.cam.ch.wwmm.oscar.document.TokenSequence;
import uk.ac.cam.ch.wwmm.oscar.opsin.OpsinDictionary;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class TokeniserCLI {

    @Argument(description = "If true, reads the input from STDIN.")
    private boolean stdin = false;

    @Argument(description = "If true, the input is HTML.")
    private boolean html = false;

	private Oscar oscar;

	public TokeniserCLI() throws Exception {
		oscar = new Oscar();
		oscar.getDictionaryRegistry().register(new OpsinDictionary());
	};

	public void processLine(String line) throws Exception {
		//TODO oscar.normalise isn't implemented yet!
//		line = oscar.normalise(line);
		List<TokenSequence> tokens = oscar.tokenise(line);
		for (TokenSequence sequence : tokens) {
			for (Token token : sequence.getTokens()) {
				System.out.println(token.getSurface());
			}
		}
	}

	public static void main(String[] args) throws Exception {
		TokeniserCLI command = new TokeniserCLI();
		List<String> extras = Args.parse(command, args);

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
		
		command.processLine(content);
	}
}
