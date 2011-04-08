package uk.ac.cam.ch.wwmm.oscar.oscarcli;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.ac.cam.ch.wwmm.oscar.chemnamedict.core.ChemNameDictRegistry;
import uk.ac.cam.ch.wwmm.oscarMEMM.memm.MEMMTrainer;

/**
 * Command line utility to train a new MEMM model from the input data.
 * It takes as command line arguments the location of input files. The
 * trained model is return to STDOUT.
 * 
 * @author egonw
 */
public class MEMMTrainerCLI {

	public static void main(String[] args) throws Exception {
		List<File> files = new ArrayList<File>();
		for (String fileName : args) {
			File file = new File(fileName);
			if (!file.exists()) {
				System.out.println("File does not exist: " + fileName);
				System.exit(-1);
			}
			files.add(file);
		}
		if (files.size() == 0) {
			System.out.println(
				"Syntax: java uk.ac.cam.ch.wwmm.oscar.oscarcli.MEMMtrainer <file1> ..."
			);
			System.exit(0);
		}
		
		MEMMTrainer trainer = new MEMMTrainer(ChemNameDictRegistry.getDefaultInstance());
		trainer.trainOnSbFiles(files);
		trainer.finishTraining();
		System.out.println(trainer.getModel().writeModel().toXML());
	}
}
