package com.belk.configui.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.springframework.stereotype.Service;

/**The class contains the various functions used in connection with the validation rules.
 * Update: This class is not used now due to the validation changes being parked.
 * @author Mindtree
 *
 */
@Service
public class UpdateValidationRules {
	
	
	/**Method to read content from a file specified. 
	 * @param filePath Physical path of the file to be read from
	 * @return Single string representation of the file's contents
	 * @throws IOException Exception thrown by this method when a file operation fails.
	 */
	public final String readFile(final String filePath) throws IOException {
		final File file = new File(filePath);
		final StringBuilder fileContents = new StringBuilder((int) file.length());
		final Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)));
		final String lineSeparator = System.getProperty("line.separator");
		try {
			if (scanner.hasNextLine()) {
				fileContents.append(scanner.nextLine());
			}
			while (scanner.hasNextLine()) {
				fileContents.append(lineSeparator + scanner.nextLine());
			}
			return fileContents.toString();
		} finally {
			scanner.close();
		}
	}

	/** Method to write content into a specified file.
	 * @param filePath Physical path of the file to be written to 
	 * @param content Content to be written to
	 * @return The status of the write operation.
	 */
	public final boolean writeFile(final String filePath, final String content) {

		FileOutputStream fop = null;
		File file;
		try {

			file = new File(filePath);
			fop = new FileOutputStream(file);
			// if file doesn't exist, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// get the content in bytes
			final byte[] contentInBytes = content.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace(); //TODO: write to logs
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	
}
