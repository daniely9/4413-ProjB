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
	public static final String REDIRECT = "https://www.eecs.yorku.ca/~roumani/servers/auth/oauth.cgi?back=file:Documents/EECS4413/ProjB/src/ctrl/OAuth.do";
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("calc") == null) {
			
			this.getServletContext().getRequestDispatcher("/OAuth.html").forward(request, response);
			
		} else {
			response.sendRedirect(REDIRECT);
			System.out.println(response.getContentType());
			try {
				String  result = response.getHeader(REDIRECT);
				response.setContentType("text/html");
				Writer out = response.getWriter();
				String html = "<html><body>";
				html += "<p><a href='Dash.do'>Back to Dashboard</a></p>";
				html += "<b> Authentication Result:";
				html += result;
				html += "</body></html>";
				out.write(html);
			} catch (Exception e) {
				response.setContentType("text/html");
				Writer out = response.getWriter();
				String html = "<html><body>";
				html += "<p><a href=' Dash.do'>Back to Dashboard</a></p>";
				html += "<p>Error " + e.getMessage() + "</p>";
				out.write(html);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
