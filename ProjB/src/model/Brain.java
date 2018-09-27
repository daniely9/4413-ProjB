package model;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

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
	
	public String doDb(String itemNo) throws Exception {
		Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
		Connection con = DriverManager.getConnection(DB_URL);
		Statement s = con.createStatement();
		s.executeUpdate("set schema roumani");
		String query = "SELECT NAME, PRICE FROM ITEM WHERE NUMBER='"+itemNo+"'";//SQL query to obtain the NAME and PRICE of an item whose number is itemNo in a table ITEM
		ResultSet r = s.executeQuery(query);
		String result = "";
		if (r.next())
		{
			result = "$" + r.getDouble("PRICE") + " - " + r.getString("NAME");
		}
		else
		{
			throw new Exception(itemNo + " not found!");
		}
		r.close(); s.close(); con.close();
		return result;
	}
	
	public String doHttp(String country,String request) throws Exception{
		URL url = new URL("https://www.eecs.yorku.ca/~roumani/servers/4413/f18/World.cgi?country="+country+"&query="+request);
		URLConnection connection = url.openConnection();
		InputStream response = connection.getInputStream();
		Scanner scanner = new Scanner(response);
		String result = scanner.useDelimiter("\\A").next();
		//System.out.println(country + " " + request);

		return result;
	}
}
