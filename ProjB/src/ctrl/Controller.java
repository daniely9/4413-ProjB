package ctrl;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Verifier;

@WebServlet("/Check.do")
public class Controller extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (request.getParameter("enroll") == null && request.getParameter("next") == null)
		{
			this.getServletContext().getRequestDispatcher("/EntryForm2.html").forward(request, response);
		} else
		{
			HttpSession sn = request.getSession(true);
			response.setContentType("text/html");
			Writer out = response.getWriter();
			String html = "<html><body>";
			if (request.getParameter("enroll") != null) this.setup(request);
			String id = (String) sn.getAttribute("id");
			List<String> wishlist = (List<String>) sn.getAttribute("wishlist");
			if (wishlist == null || wishlist.size() == 0)
			{
				html += "<p>Check completed.</p>";
				html += "<form><input type='submit' value='Finish'/></form>";
			} else
			{
				String course = wishlist.get(0);
				wishlist.remove(0);
				try
				{
					Verifier v = Verifier.getInstance();
					html += v.check(id, course);
					html += "<form><input type='submit' name='next' value='Continue'/></form>";
					html += "<form><input type='submit' value='Abort'/></form>";
				} catch (Exception e)
				{
					html += "<p>Error " + e.getMessage() + "</p>";
				}
			}
			html += "</body></html>";
			out.write(html);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
	
	private void setup(HttpServletRequest request)
	{
		HttpSession sn = request.getSession();
		sn.setAttribute("id", request.getParameter("id"));
		String[] tokens = request.getParameter("list").split("\\s+");
		List<String> wishlist = new ArrayList<String>();
		wishlist.addAll(Arrays.asList(tokens));
		sn.setAttribute("wishlist", wishlist);
	}
}