package ctrl;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class OAuth
 */
@WebServlet("/OAuth.do")
public class OAuth extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String REDIRECT = "https://www.eecs.yorku.ca/~roumani/servers/auth/oauth.cgi?back=http://localhost:4413/ProjB/OAuth.do";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
	{
		if (request.getParameter("calc") != null) 
		{
			response.sendRedirect(REDIRECT);	
		} 
		else if(request.getParameter("user") != null)
		{
			try 
			{
				response.setContentType("text/html");
				Writer out = response.getWriter();
				String html = "<html><body>";
				html += "<p><a href='Dash.do'>Back to Dashboard</a></p>";
				html += "<b> Authentication Result:</b>";
				html += "<br>" + request.getQueryString();
				html += "</body></html>";
				out.write(html);
			} 
			catch (Exception e) 
			{
				response.setContentType("text/html");
				Writer out = response.getWriter();
				String html = "<html><body>";
				html += "<p><a href=' Dash.do'>Back to Dashboard</a></p>";
				html += "<p>Error " + e.getMessage() + "</p>";
				out.write(html);
			}
		}else 
		{
			this.getServletContext().getRequestDispatcher("/OAuth.html").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
