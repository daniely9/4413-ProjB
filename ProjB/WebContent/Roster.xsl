<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
	<html>
    <head>
      <title>Course Roster</title>
    </head>
    <body>
    <p><a href='Dash.do'>Back to Dashboard</a></p>
    <h1>Course Roster via HTTP</h1>
    <table border="1" cellpadding="4">
    <tr><th>ID</th><th>Last Name</th><th>First Name</th><th>City</th><th>Program</th><th>Hours</th><th>GPA</th></tr>
      <xsl:for-each select="/course/students">
         <tr>
           <td><xsl:value-of select="id"/></td>
           <td><xsl:value-of select="lastName"/></td>
           <td><xsl:value-of select="firstName"/></td>
           <td><xsl:value-of select="city"/></td>
           <td><xsl:value-of select="program"/></td>
           <td><xsl:value-of select="hours"/></td>
           <td><xsl:value-of select="gpa"/></td>
          </tr>
       </xsl:for-each>

    </table>
    </body>
    </html>
  </xsl:template>
	
</xsl:stylesheet>