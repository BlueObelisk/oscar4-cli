package uk.ac.cam.ch.wwmm.oscar.oscarcli;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Command line utility to train a new MEMM model from the input data.
 * It takes as command line arguments the location of input files. The
 * trained model is return to STDOUT.
 * 
 * @author egonw
 */
public class MEMMTrainer {

	public static void main(String[] args) throws Exception {
		List<File> files = new ArrayList<File>();
		for (String fileName : args) {
			File file = new File(fileName);
			if (!file.exists()) {
				System.out.println("File does not exist: " + fileName);
				System.exit(-1);
			}
		}
		if (files.size() == 0) {
			System.out.println(
				"Syntax: java uk.ac.cam.ch.wwmm.oscar.oscarcli.MEMMtrainer <file1> ..."
			);
			System.exit(0);
		}
		
		uk.ac.cam.ch.wwmm.oscarMEMM.memm.MEMMTrainer trainer =
			new uk.ac.cam.ch.wwmm.oscarMEMM.memm.MEMMTrainer();
		trainer.trainOnSbFiles(files, null);
		trainer.finishTraining();
		System.out.println(trainer.writeModel().toXML());
	}
}
