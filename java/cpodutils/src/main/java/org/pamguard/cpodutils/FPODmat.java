package org.pamguard.cpodutils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.pamguard.cpodutils.FPODReader.FPODdata;

/**
 * Useful functions for calling from MATLAB. 
 */
public class FPODmat {

	public FPODmat() {

	}

	/**
	 * Import FPOD data from a FP1 or FP3 file. 
	 * @param filePath - the filepath
	 * @param from - the index of detection to import from. 0 starts form the first detection
	 * @param maxnum - the maximum 
	 * @return
	 */
	public ArrayList<FPODdata> importFPODFile(String filePath, int from, int maxnum) {

		File fpfile = new File(filePath); 

		ArrayList<FPODdata> fpodData = new ArrayList<FPODdata>();

		try {

			FPODReader.importFile( fpfile, fpodData, 0, Integer.MAX_VALUE);

			System.out.println("Total number of FPOD clicks imported: " +  fpodData.size()); 

			return fpodData; 

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		}

	}



	/**
	 * Import FPOD or CPOD data from a FP1 or FP3 file. 
	 * @param filePath - the file path. 
	 * @param from - the index of detection to import from. 0 starts form the first detection
	 * @param maxnum - the maximum number of clicks to import. 
	 * @return a list of cicks
	 */
	public ArrayList<CPODClick> importPODFile(String filePath, int from, int maxnum) {

		File fpfile = new File(filePath); 

		String extension = FilenameUtils.getExtension(filePath);

		try {

			ArrayList<CPODClick>  clicks;
			if (extension.equals("FP1") || extension.equals("FP3")) {
				clicks = FPODReader.importFPODFile(fpfile,  from,  maxnum);

			}
			else if (extension.equals("CP1") || extension.equals("CP3")) {
				clicks = CPODReader.importCPODFile(fpfile,  from,  maxnum);
			}
			else {
				System.err.println("Wrong file extension: " + extension);
				return null;
			}
			
			System.out.println("Total number of POD clicks imported: " +  clicks.size()); 
			return clicks;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
