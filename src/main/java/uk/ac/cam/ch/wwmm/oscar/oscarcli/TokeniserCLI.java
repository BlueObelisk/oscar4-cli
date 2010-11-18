package uk.ac.cam.ch.wwmm.oscar.oscarcli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import net.htmlparser.jericho.Source;
import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.document.Token;
import uk.ac.cam.ch.wwmm.oscar.document.TokenSequence;
import uk.ac.cam.ch.wwmm.oscar.opsin.OpsinDictionary;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;

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
		line = oscar.normalize(line);
		List<TokenSequence> tokens = oscar.tokenize(line);
		for (TokenSequence sequence : tokens) {
			for (Token token : sequence.getTokens()) {
				System.out.println(token.getValue());
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
