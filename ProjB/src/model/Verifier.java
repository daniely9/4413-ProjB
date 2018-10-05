package model;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.JsonParser;

public class Verifier
{
	private static Verifier instance = null;
	private final Map<String,List<String>> prereq;

	public synchronized static Verifier getInstance()
	{
		if (instance == null) instance = new Verifier();
		return instance;
	}

	private Verifier()
	{
		prereq = new HashMap<String, List<String>>();
		this.populate();
	}
		
	// populate the prerequisite table
	private void populate()
	{
		String course = "EECS2011";
		List<String> list = Arrays.asList("EECS1019", "EECS1022");
		prereq.put(course,  list);
		course = "EECS3221";
		list = Arrays.asList("EECS2021", "EECS2031", "EECS2030");
		prereq.put(course,  list);
		course = "EECS3101";
		list = Arrays.asList("EECS2011", "MATH1090");
		prereq.put(course,  list);
		course = "EECS4413";
		list = Arrays.asList("EECS2011", "EECS3421");
		prereq.put(course,  list);
		course = "EECS4422";
		list = Arrays.asList("EECS2031", "MATH1025", "MATH1310");
		prereq.put(course,  list);
	}
	
	// See if the student with the given id can enroll in the given course
	public String check(String id, String course) throws Exception
	{
		String result = "Enrolment in " + course + " is ";
		List<String> pre = prereq.get(course);
		if (pre == null)
		{
			result += "allowed.";
		}
		else
		{
			boolean ok = true;
			for (int i = 0; i < pre.size() && ok; i++)
			{
				Socket client = new Socket("175.34.2.101", 7298);
				PrintStream out = new PrintStream(client.getOutputStream(), true);
				Scanner in = new Scanner(client.getInputStream());
				out.println("check " + id + " " + pre.get(i));
				JsonParser parser = new JsonParser();
				ok = ok & parser.parse(in.nextLine()).getAsJsonObject().get("completed").getAsBoolean();
				in.close(); client.close();
				if (!ok)
				{
					result += "not allowed because you haven't completed " + pre.get(i);
				}
			}
			if (ok) result += "allowed.";
		}
		return result;
	}	
}