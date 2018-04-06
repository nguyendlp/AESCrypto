package com.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.cli.*;

import com.crypto.ZipHelper;

public class App {
	
	public static void decryptFile(String fileInput, String fileOutput, String key) {
		try
		{
			 InputStream fis = new FileInputStream(new File(fileInput));
	         OutputStream fos = new FileOutputStream(new File(fileOutput));
	         BcAes encrypter = new BcAes(key);
	         encrypter.decrypt(fis, fos);
	         fis.close();
	         fos.close();
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
	}
	
	public static void main(String[] args)
	{
		Options options = new Options();
		Option input = new Option("i", "input", true, "input file path. For example: /<path>/<file_name>.dat");
		input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file. For example: /<path>/<file_name>.zip");
        output.setRequired(true);
        options.addOption(output);
        
        Option key = new Option("k", "key", true, "256 bit aes secret key");
        output.setRequired(true);
        options.addOption(key);
        
        Option unzipFolder = new Option("u", "unzipFolder", true, "The folder path contains unzip files. For example: /<path>/unzip/");
        output.setRequired(true);
        options.addOption(unzipFolder);
        
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("arg", options);
            System.exit(1);
            return;
        }
        
        String inputFilePath = cmd.getOptionValue("input");
        String outputFilePath = cmd.getOptionValue("output");
        String secretKey =  cmd.getOptionValue("key");
        String unzipF = cmd.getOptionValue("unzipFolder");
        try {
			byte[] secretKeyByteArray = secretKey.getBytes("UTF-8");
			if(secretKeyByteArray.length != 32) {
				System.out.println("An AES 256-bit key must be expressed as a hexadecimal string with 32 characters (32 bytes)");
				System.exit(1);
				return;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.exit(1);
			return;
		}
        
		decryptFile(inputFilePath, outputFilePath, secretKey);
		ZipHelper.unZipIt(outputFilePath, unzipF);
	}
}
