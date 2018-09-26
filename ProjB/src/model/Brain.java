package model;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.Date;
import java.util.Random;

public class Brain
{
	public static final double BITS_PER_DIGIT = 3.0;
	public static final int TWO = 2;
	public static final Random RNG = new Random();	
	public static final String TCP_SERVER = "red.eecs.yorku.ca";
	public static final int TCP_PORT = 12345;
	public static final String DB_URL = "jdbc:derby://red.eecs.yorku.ca:64413/EECS;user=student;password=secret";
	public static final String HTTP_URL = "https://www.eecs.yorku.ca/~roumani/servers/4413/f18/World.cgi";
	public static final String ROSTER_URL = "https://www.eecs.yorku.ca/~roumani/servers/4413/f18/Roster.cgi";
	private static Brain instance = null;
	
	
	public static Brain getInstance() {
		if(instance == null) {
			 instance = new Brain();
		}
		return instance;
	}
	
	public String doTime(){
		return new Date().toString();
	}
	
	public String doPrime(String digits) {
		BigInteger result = null;
		int digLen = Integer.parseInt(digits);
		digLen *= BITS_PER_DIGIT;
		if (!(digLen < TWO)) {
			result = BigInteger.probablePrime(digLen, RNG);
		}else {
			result = BigInteger.ZERO;
		}
		return result.toString();
	}
	
	public String doTcp(String digits) throws UnknownHostException, IOException {
		Socket client = new Socket(TCP_SERVER, TCP_PORT);
		OutputStream output = client.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
		writer.println("Prime "+digits);
		
		
		InputStream input = client.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		reader.read();
		return reader.readLine();
		
	}
}
