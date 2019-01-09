package model;

import java.io.*;
import java.math.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

import javax.xml.*;
import javax.xml.bind.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Brain {
	public static final double BITS_PER_DIGIT = 3.0;
	public static final int TWO = 2;
	public static final Random RNG = new Random();
	public static final String TCP_SERVER = "red.eecs.yorku.ca";
	public static final int TCP_PORT = 12345;
	public static final String DB_URL = "jdbc:derby://localhost:64413/EECS;user=student;password=secret";
	public static final String HTTP_URL = "https://www.eecs.yorku.ca/~roumani/servers/4413/f18/World.cgi";
	public static final String ROSTER_URL = "https://www.eecs.yorku.ca/~roumani/servers/4413/f18/Roster.cgi";
	private static Brain instance = null;

	public static Brain getInstance() {
		if (instance == null) {
			instance = new Brain();
		}
		return instance;
	}

	public String doTime(String digits) {
		int value = Integer.parseInt(digits);
		long HOUR = 3600*1000;
		String result = "";
		Date date = new Date();
		if(value <= 0) {
			result = date.toString();
		}else {
			Date date2 = new Date(date.getTime() + value * HOUR);
			result = date.toString() + "\n" + date2.toString();		
		}
		return result;
	}

	public String doPrime(String digits) {
		BigInteger result = null;
		int digLen = Integer.parseInt(digits);
		digLen *= BITS_PER_DIGIT;
		if (!(digLen < TWO)) {
			result = BigInteger.probablePrime(digLen, RNG);
		} else {
			result = BigInteger.ZERO;
		}
		return result.toString();
	}

	public String doTcp(String digits) throws UnknownHostException, IOException {
		Socket client = new Socket(TCP_SERVER, TCP_PORT); // create socket
		OutputStream output = client.getOutputStream(); // output to server from client
		PrintWriter writer = new PrintWriter(output, true); // writer
		writer.println("Prime " + digits); // request from server

		InputStream input = client.getInputStream(); // input from server stream
		BufferedReader reader = new BufferedReader(new InputStreamReader(input)); // make it stringable
		reader.read(); // open read channel
		String result = reader.readLine();
		writer.println("Bye");
		return result;/// return the result

	}

	public String doDb(String itemNo) throws Exception {
		Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
		Connection con = DriverManager.getConnection(DB_URL);
		Statement s = con.createStatement();
		s.executeUpdate("set schema roumani");
		String query = "SELECT NAME, PRICE FROM ITEM WHERE NUMBER='" + itemNo + "'";// SQL query to obtain the NAME and
																					// PRICE of an item whose number is
																					// itemNo in a table ITEM
		ResultSet r = s.executeQuery(query);
		String result = "";
		if (r.next()) {
			result = "$" + r.getDouble("PRICE") + " - " + r.getString("NAME");
		} else {
			throw new Exception(itemNo + " not found!");
		}
		r.close();
		s.close();
		con.close();
		return result;
	}

	public String doHttp(String country, String request) throws Exception {
		URL url = new URL(HTTP_URL + "?country=" + country + "&query=" + request);
		URLConnection connection = url.openConnection();
		InputStream response = connection.getInputStream();
		Scanner scanner = new Scanner(response);
		String result = scanner.useDelimiter("\\A").next();
		return result;
	}

	public String doRoster(String course) throws Exception {
		URL url = new URL(ROSTER_URL + "?course=" + course); //Create URL
		URLConnection connection = url.openConnection(); //Connect URL
		InputStream response = connection.getInputStream(); //Open stream from URL
		Scanner scanner = new Scanner(response); //Create a scanner with connections response
		StringWriter sw = new StringWriter();
		String result = scanner.useDelimiter("\\A").next();

		//System.out.println(result);
		
		StringReader reader = new StringReader(result);
	    StringWriter writer = new StringWriter();
	    TransformerFactory tFactory = TransformerFactory.newInstance();
	    Transformer transformer = tFactory.newTransformer(new javax.xml.transform.stream.StreamSource("Documents/EECS4413/ProjB/WebContent/Roster.xsl"));
	    transformer.transform(new javax.xml.transform.stream.StreamSource(reader), new javax.xml.transform.stream.StreamResult(writer));
	    //System.out.println(writer.toString());
		return writer.toString();
	}

}
