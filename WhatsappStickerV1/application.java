/**
 * @author Mursyid Afiq
 * Tuesday, November 20, 2018
 * Email: syid98@gmail.com
 * Facebook: https://facebook.com/your.server
 */

package WhatsappStickerV1;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Scanner;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;

public class application {
	final static String mainFolder   		 = "C:\\testing\\";
	final static String identifierFilename   = "identifier.conf";
	final static String listFilename 		 = "list.conf";
	final static String activityLogFilename  = "activity.log";
	final static String buildPathLogFilename = "build-path.log";
	final static String nameLogFilename 	 = "name.log";
	
	@SuppressWarnings("unused")
	private static Scanner Scanner() {
		return new Scanner(System.in);
	}
	
	public static void main(String... mursyid) throws IOException {
		String[] buildpath  = getBuildPathLog();
		String[] identifier = getIdentifierFile();
		for(int i = 0; i <= buildpath.length-1 ; i++) {
			if(buildpath[i].isEmpty()) continue;
			else {
				out.println(buildpath[i]);
				out.println(identifier[i]);
				copyToDestination(buildpath[i], "C:\\Users\\admin\\Desktop\\android\\stickers\\Android\\app\\src\\main\\assets\\" + identifier[i]);
			}
		}
		
//		for(String v : getBuildPathLog()) {
//			if(v.isEmpty()) continue;
//			else {
//				out.println(v);
//			}
//		}
		//copyToDestination();
		//for(String sticker_url : getLines() ) Application(sticker_url);
	}
	
	public static void copyToDestination(String source, String destination) {
		File src = new File( source );
		File dest = new File( destination );
		try {
		    FileUtils.copyDirectory(src, dest);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	public static String[] getIdentifierFile() throws IOException {
		BufferedReader bufferReader = new BufferedReader(new FileReader(mainFolder + identifierFilename)); 
		String line = bufferReader.readLine(); 
		String[] data = new String[getLinesLength("identifier")];
		int i = 0;
		while(line != null) {
			data[i] = line;	
			line = bufferReader.readLine();
			i++;
		}
		bufferReader.close();
		return data;
	}
	
	public static String[] getBuildPathLog() throws IOException {
		BufferedReader bufferReader = new BufferedReader(new FileReader(mainFolder + buildPathLogFilename)); 
		String line = bufferReader.readLine(); 
		String[] data = new String[getLinesLength("buildpathlog")];
		int i = 0;
		while(line != null) {
			data[i] = line;	
			line = bufferReader.readLine();
			i++;
		}
		bufferReader.close();
		return data;
	}
	
	public static String getCurrentIdentifier() throws IOException {
		String[] i = getIdentifierFile();
		String current_identifier = i[i.length-1];		
		return current_identifier;
	}
	
	public static String getNextIdentifier(String stickerFolderName, String sticker_part) throws IOException {
		String[] i = getIdentifierFile();
		String current_identifier = null;
		
		if(getIdentifierFile().length == 0) {
			current_identifier = "0";
		} else {
			current_identifier = i[i.length-1];
		}

		int nextIdentifier = 0;
		nextIdentifier = Integer.parseInt(current_identifier) + 1; // cast and calculate
		
		
		String ni = Integer.toString(nextIdentifier); 
		writeIntoFile(ni, "identifier");
		writeIntoFile(ni + "\t" + dateTime() + stickerFolderName, "activitylog");
		writeIntoFile(mainFolder + stickerFolderName + "\\" + ni, "buildpathlog");
		writeIntoFile(stickerFolderName + " " + sticker_part, "namelog");
		return ni;
	}
	
	public static void Application(String sticker_url) throws IOException {
		Document documentConnection = jsoup.connectJsoup(sticker_url); // connection
		int firstID = jsoup.getRangeID( jsoup.trimStickerURL( jsoup.getStickerURL(documentConnection, "first")));
		int lastID = jsoup.getRangeID( jsoup.trimStickerURL( jsoup.getStickerURL(documentConnection, "last") ));
		String stickerFolderName = jsoup.getStickerName(documentConnection).text();
		
		firedRequest(firstID, lastID, stickerFolderName, documentConnection);
	}
	
	public static String[] getLines() throws IOException {
		BufferedReader bufferReader = new BufferedReader(new FileReader(mainFolder + listFilename));
		String line = bufferReader.readLine(); 
		String[] data = new String[getLinesLength("list")];
		int i = 0;
		while(line != null) {
			data[i] = line;	
			line = bufferReader.readLine();
			i++;
		}
		bufferReader.close();
		return data;
	}

	public static int getLinesLength(String mode) throws IOException {
		String path = null;
		
		if(mode.equals("identifier")) path = mainFolder + identifierFilename;
		else if(mode.equals("list")) path = mainFolder + listFilename;
		else if(mode.equals("buildpathlog")) path = mainFolder + buildPathLogFilename;
		else if(mode.equals("namelog")) path = mainFolder + nameLogFilename;
		
		BufferedReader bufferReader = new BufferedReader(new FileReader( path ));
		
		String line = bufferReader.readLine(); 
		int size = 0;
		while(line != null) {
			size += 1;
			line = bufferReader.readLine();
		}
		bufferReader.close();
		return size;
	}
	
	public static void writeIntoFile(String info, String mode) {
		String path = null;
		if(mode.equals("identifier")) path = identifierFilename;
		else if(mode.equals("activitylog")) path = activityLogFilename;
		else if(mode.equals("buildpathlog")) path = buildPathLogFilename;
		else if(mode.equals("namelog")) path = nameLogFilename;
		
		String filename = mainFolder + path;
	    BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(filename, true);
			bw = new BufferedWriter(fw);
			bw.write("\n");
			bw.write(info);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void firedRequest(int fr, int lr, String stickerFolderName, Document documentConnection) throws IOException {
		int averageSticker = (fr + lr) / 2;
		String first =  getNextIdentifier(stickerFolderName, "Part 1");
		createStickerFolder(stickerFolderName);
		String second = getNextIdentifier(stickerFolderName, "Part 2");
		createStickerFolder(stickerFolderName);
		
		for(int i = fr, n = 0 ; i <= lr ; i++, n++) {
			String stickerID = String.valueOf(i);
			out.println(dateTime() + "Saving " + stickerID + ".png.. \n");
			
			if(i <= averageSticker) {
				saveImage(stickerID, Integer.toString(n), stickerFolderName, first, "sticker");
			}else if(i > averageSticker) {
				saveImage(stickerID, Integer.toString(n), stickerFolderName, second, "sticker");
			}
			
			out.println(dateTime() + "OK \n");
		}
		out.println(dateTime() + "Download completed! \n");
		
		pngToWebpConverter(stickerFolderName, first, 512, 512, "sticker");
		deletePng(stickerFolderName, first, "sticker");

		downloadStickerLogo(documentConnection, stickerFolderName, first);
		pngToWebpConverter(stickerFolderName, first, 96, 96, "logo");
		deletePng(stickerFolderName, first, "logo");
		
		pngToWebpConverter(stickerFolderName, second, 512, 512, "sticker");
		deletePng(stickerFolderName, second, "sticker");

		downloadStickerLogo(documentConnection, stickerFolderName, second);
		pngToWebpConverter(stickerFolderName, second, 96, 96, "logo");
		deletePng(stickerFolderName, second, "logo");
	}
	
	public static String dateTime() {
		java.util.Date date = new java.util.Date();
		String dateTime = "[+] " + date + " - ";
		return dateTime;
	}
	
	public static void saveImage(String ID, String name, String stickerFolderName, String part, String mode) throws IOException {
		String imageUrl = null;
		String imageFormat = ".png"; // Do not change
		String destinationFile = null;
		if(mode.equals("sticker")) {
			imageUrl = "https://stickershop.line-scdn.net/stickershop/v1/sticker/" + ID 
					 + "/ANDROID/sticker.png";
			destinationFile = mainFolder + stickerFolderName 
							+ "\\" + part 
							+ "\\" + name + imageFormat;
		} else if(mode.equals("logo")) {
			imageUrl = "https://stickershop.line-scdn.net/stickershop/v1/product/" + ID 
					+ "/LINEStorePC/main.png";
			destinationFile = mainFolder + stickerFolderName 
							+ "\\" + part 
							+ "\\" + name + imageFormat;
		}
		openStreamAndSave(imageUrl, destinationFile);
	}
	
	public static void openStreamAndSave(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}
		is.close();
		os.close();
	}
	
	public static void pngToWebpConverter(String stickerFolderName, String part, int height, int width, String mode) throws ExecuteException, IOException {
		CommandLine cmdLine = new CommandLine("cmd.exe");
    	cmdLine.addArgument("/c");
        String folderPath = mainFolder + stickerFolderName + "\\" + part;
        String h = Integer.toString(height);
        String w = Integer.toString(width);
        String command = null;

        if(mode.equals("sticker")) {
        	command = "cd " + folderPath + " && convert *.png -resize " + h + "x" + w + "! %d.webp";
        }else if(mode.equals("logo")) {
        	command = "cd " + folderPath + " && convert tray_sticker.png -resize " + h + "x" + w + "! tray_sticker.webp";
        }
        cmdLine.addArgument(command,false);
        out.println(dateTime() + "Converting png to webp.. \n");
        out.println(dateTime() + stickerFolderName + ", Part: " + part + "\n");
        DefaultExecutor executor = new DefaultExecutor();
        executor.execute(cmdLine);
		out.println(dateTime() + "Finished convert! \n");
    }
	
	public static void deletePng(String stickerFolderName, String part, String mode) throws ExecuteException, IOException {
    	CommandLine cmdLine = new CommandLine("cmd.exe");
    	cmdLine.addArgument("/c");
    	String folderPath = mainFolder + stickerFolderName + "\\" + part;
    	String command = null;
    	if(mode.equals("sticker")) {
    		command = "cd " + folderPath + " && del /s /q /f *.png > NUL";
    	}else if(mode.equals("logo")) {
    		command = "cd " + folderPath + " && del /s /q /f tray_sticker.png > NUL";
    	}
        cmdLine.addArgument(command,false);
        out.println(dateTime() + "Deleting old files.. \n");
        out.println(dateTime() + stickerFolderName + ", Part: " + part + "\n");
        DefaultExecutor executor = new DefaultExecutor();
        executor.execute(cmdLine);
		out.println(dateTime() + "Finished clean up! \n");
    }
	
	public static void downloadStickerLogo(Document documentConnection, String stickerFolderName, String part) throws IOException {
		out.println(dateTime() + "Saving sticker logo.. \n");
		out.println(dateTime() + stickerFolderName + ", Part: " + part + "\n");
		String logoAttributes = jsoup.getStickerLogoURL(documentConnection);
		saveImage(jsoup.getLogoID( jsoup.trimTrayIconURL(logoAttributes)) , "tray_sticker", stickerFolderName, part, "logo");
		out.println(dateTime() + "OK \n");
		
	}
	
	public static void createStickerFolder(String stickerFolderName) throws IOException {
		String part = getCurrentIdentifier();

		File files = new File(mainFolder + stickerFolderName + "\\" + part); // create file in directory here
		if (!files.exists()) {
		    if (files.mkdirs()) {
		        out.println(dateTime() + "Folder '" + stickerFolderName + "\\" + part + "' are created! \n");
		        out.println(dateTime() + "Data will be saved inside '" + mainFolder + stickerFolderName + "\\" + part + "' \n");
		    }
		}
	}
	


}
