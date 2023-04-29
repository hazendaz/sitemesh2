/*
 * sitemesh2 (https://github.com/hazendaz/sitemesh2)
 *
 * Copyright 2011-2023 Hazendaz.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of The Apache Software License,
 * Version 2.0 which accompanies this distribution, and is available at
 * https://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Contributors:
 *     Hazendaz (Jeremy Landis).
 */
package testsuite.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class OutputServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mode = request.getParameter("out");
        PrintWriter pw = null;

        if (mode.equals("stream")) {
            OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream());
            pw = new PrintWriter(osw);
        }

        if (pw == null) {
            pw = response.getWriter();
        }
        response.setContentType("text/html");
        pw.println("<html><head><title>Servlet using " + mode + "</title></head>");
        pw.println("<body>This is a servlet using " + mode + " to output</body></html>");
        pw.flush();
    }
}
