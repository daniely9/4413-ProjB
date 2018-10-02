package ctrl;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Brain;


@WebServlet("/Roster.do")
public class Roster extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public Roster() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("calc") == null)
		  {
		     this.getServletContext().getRequestDispatcher("/Roster.html").forward(request, response);
		  }
		  else
		  {
		     Brain model = new Brain();
		     try
		     {
		    	String course = request.getParameter("course");
		        String courseQuery = model.doRoster(course);
		        response.setContentType("text/html");
		        Writer out = response.getWriter();
		        String html = courseQuery;
		        out.write(html);
		     }
		     catch (Exception e)
		     {
		        response.setContentType("text/html");
		        Writer out = response.getWriter();
		        String html = "<html><body>";
		        html += "<p><a href='Dash.do'>Back to Dashboard</a></p>";
		        html += "<p>Error " + e.getMessage() + "</p>";
		        out.write(html);
		     }
		  }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
