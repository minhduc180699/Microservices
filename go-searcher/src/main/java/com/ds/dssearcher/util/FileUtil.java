package com.ds.dssearcher.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * This class provides some file handling helper functions.
 * @author Tran Trung Kien
 */
public class FileUtil {

	/**
	 * List all files in a folder and sort files by Last modified date in
	 * Ascending order
	 * 
	 * @param folder
	 *            : folder to list files
	 * @return
	 */
	public static File[] dirListByAscendingDate(File folder) {
		if (!folder.isDirectory()) {
			return null;
		}
		File files[] = folder.listFiles();
		Arrays.sort(files, new Comparator() {
			public int compare(final Object o1, final Object o2) {
				return new Long(((File) o1).lastModified()).compareTo(new Long(
						((File) o2).lastModified()));
			}
		});
		return files;
	}

	/**
	 * List all files in a folder and sort files by Last modified date in
	 * Descending order
	 * 
	 * @param folder
	 *            : folder to list files
	 * @return
	 */
	public static File[] dirListByDescendingDate(File folder) {
		if (!folder.isDirectory()) {
			return null;
		}
		File files[] = folder.listFiles();
		Arrays.sort(files, new Comparator() {
			public int compare(final Object o1, final Object o2) {
				return new Long(((File) o2).lastModified()).compareTo(new Long(
						((File) o1).lastModified()));
			}
		});
		return files;
	}

	/**
	 * Get oldest file in the given folder
	 * 
	 * @param folder
	 * @return
	 */
	public static File getOldestFileName(File folder) {
		File[] files = dirListByDescendingDate(folder);
		if (files != null && files.length > 0)
			return files[0];

		return null;
	}

	public boolean zipAndClearFolder(String zipFolder, String zipFile,
			File folder, boolean recursive) {
		// File fold = new File(folder);
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			// Delete empty folder
			if (files.length <= 0) {
				folder.delete();
				return true;
			}
			// The file being added to zip
			BufferedInputStream origin = null;
			try {
				// Reference to zip file
				File zf = new File(zipFolder);
				// Create zip output folder if not exist
				if (!zf.exists())
					if (!zf.mkdirs())
						return false;

				String zfn = zipFolder + File.separator + zipFile;

				FileOutputStream dest = new FileOutputStream(zfn, true);
				// Wrap our destination zipfile with a ZipOutputStream
				ZipOutputStream out = new ZipOutputStream(
						new BufferedOutputStream(dest));

				// Create a byte[] buffer that we will read data from the source
				// files into and then transfer it to the zip file
				int buffSize = 1024;
				byte[] data = new byte[buffSize];

				for (File f : files) {
					if (f.isFile()) {
						origin = new BufferedInputStream(
								new FileInputStream(f), buffSize);

						// Setup new zip entry
						ZipEntry entry = new ZipEntry(f.getName());
						out.putNextEntry(entry);

						// Read data from the source file and write it out to
						// the zip file
						int count;
						while ((count = origin.read(data, 0, buffSize)) != -1) {
							out.write(data, 0, count);
						}

						// Close the source file
						origin.close();
					} else if (recursive) {
						zipAndClearFolder(zipFolder, zipFile, f, recursive);
					}
					// Delete ile after adding to zip
					f.delete();
				}
				// Close zip file
				out.close();

				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public boolean zipAndClearFolder(String zipFolder, String zipFile,
			String folder, boolean recursive) {
		File fold = new File(folder);
		return zipAndClearFolder(zipFolder, zipFile, fold, recursive);
	}

	public static void copyfile(String srFile, String dtFile)
			throws IOException {
		File f1 = new File(srFile);
		File f2 = new File(dtFile);
		InputStream in = new FileInputStream(f1);

		// For Append the file.
		// OutputStream out = new FileOutputStream(f2,true);

		// For Overwrite the file.
		OutputStream out = new FileOutputStream(f2);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	/**
	 * Read file content to a string
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 * @NOTE: the OS is responsible for text encoding, call other method when
	 *        you need to specify encoding
	 */
	public static String File2String(File fileName) throws Exception {
		FileInputStream fis = new FileInputStream(fileName);
		byte bytes[] = new byte[(int) fileName.length()];
		fis.read(bytes);
		fis.close();
		return new String(bytes);
	}

	public static String File2String(String fileName) throws Exception {
		return File2String(new File(fileName));
	}

	/**
	 * Read file content to a String with specified encoding
	 * 
	 * @param fileName
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String File2String(File fileName, String encoding)
			throws Exception {
		FileInputStream fis = new FileInputStream(fileName);
		byte bytes[] = new byte[(int) fileName.length()];
		fis.read(bytes);
		fis.close();
		return new String(bytes, encoding);
	}

	public static String File2String(String fileName, String encoding)
			throws Exception {
		return File2String(new File(fileName), encoding);
	}

	/**
	 * Fetch the entire contents of a text file, and return it in a String. The
	 * default encoding is UTF-8. Call getContents(String fileName, String
	 * encoding) if you want to specify another encoding type.
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getContents(String fileName) {
		return getContents(fileName, "UTF-8");
	}

	/**
	 * Fetch the entire contents of a text file, and return it in a String. This
	 * style of implementation does not throw Exceptions to the caller.
	 * 
	 * 
	 * @param fileName
	 *            - is a file which already exists and can be read.
	 * @param encoding
	 *            - the encoding type of the file
	 */
	public static String getOnlyContentsField(String fileName, String encoding) {
		String content = null;
		String xmlFile = null;
		try {
			xmlFile = new String(getBinary(fileName), encoding);
			try {
				Pattern regex = Pattern.compile("<CONTENT>((.)*)</CONTENT>", Pattern.DOTALL);
				Matcher regexMatcher = regex.matcher(xmlFile);
				if (regexMatcher.find()) {
					content = regexMatcher.group(1);
				} 
			} catch (PatternSyntaxException ex) {
				// Syntax error in the regular expression
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return content;
	}
	
	/**
	 * Fetch the entire contents of a text file, and return it in a String. This
	 * style of implementation does not throw Exceptions to the caller.
	 * 
	 * 
	 * @param fileName
	 *            - is a file which already exists and can be read.
	 * @param encoding
	 *            - the encoding type of the file
	 */
	public static String getContents(String fileName, String encoding) {
		String content = null;
		try {
			content = new String(getBinary(fileName), encoding);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return content;
	}

	private static byte[] getBinary(String filepath) {
		File iDir = new File(filepath);
		byte[] binary = null;
		try {
			InputStream is = new FileInputStream(iDir);
			// Get the size of the file
			long length = iDir.length();
			if (length > Integer.MAX_VALUE) {
				throw new IOException("Could not completely read file "
						+ iDir.getName());
			}

			// Create the byte array to hold the data
			binary = new byte[(int) length];

			// Read in the bytes
			int offset = 0;
			int numRead = 0;

			while (offset < binary.length
					&& (numRead = is.read(binary, offset, binary.length
							- offset)) >= 0) {
				offset += numRead;
			}

			// Ensure all the bytes have been read in
			if (offset < binary.length) {
				throw new IOException("Could not completely read file "
						+ iDir.getName());
			}
			// Close the input stream and return bytes
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return binary;
	}

	static public boolean deleteDirectory(String path) {
		return deleteDirectory(new File(path));
	}

	/**
	 * Delete a directory
	 * 
	 * @param path
	 *            - the file that points to the directory
	 */
	static private boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					if (!deleteDirectory(files[i]))
						return false;
				} else {
					if (!files[i].delete())
						return false;
				}
			}
		}
		return path.delete();
	}

	/**
	 * Write the input text to a file in given encode
	 * 
	 * @param text
	 *            : content to write
	 * @param file
	 *            : output file
	 * @param encode
	 *            : Encode for the content
	 * @throws Exception
	 */
	public static void string2File(String text, File file, String encode)
			throws Exception {
		File filePath = file.getAbsoluteFile().getParentFile();
		if (!filePath.isDirectory())
			filePath.mkdir();
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), encode));
		bw.write(text);
		bw.close();
	}

	public static void string2File(String text, String fileName, String encode)
			throws Exception {
		string2File(text, new File(fileName), encode);
	}
	
	public static void string2File(String text, String filePath, String encode, boolean append)
			throws Exception {
		try {
			File fileDir = new File(filePath);
				
			Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fileDir, append), encode));

			out.write(text);
			out.write("\r\n");
			
			out.flush();
			out.close();
		        
		    } 
		   catch (Exception e) 
		   {
			System.out.println(e.getMessage());
		   } 
	}	
}
