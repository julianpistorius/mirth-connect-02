/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mirth.
 *
 * The Initial Developer of the Original Code is
 * WebReach, Inc.
 * Portions created by the Initial Developer are Copyright (C) 2006
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *   Gerald Bortis <geraldb@webreachinc.com>
 *
 * ***** END LICENSE BLOCK ***** */


package com.webreach.mirth.server.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.webreach.mirth.model.SystemEvent;
import com.webreach.mirth.model.User;
import com.webreach.mirth.model.converters.ObjectXMLSerializer;
import com.webreach.mirth.server.controllers.ControllerException;
import com.webreach.mirth.server.controllers.SystemLogger;
import com.webreach.mirth.server.controllers.UserController;

public class UserServlet extends MirthServlet {
	public static final String SESSION_USER = "user";
	public static final String SESSION_AUTHORIZED = "authorized";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserController userController = new UserController();
		SystemLogger systemLogger = new SystemLogger();
		PrintWriter out = response.getWriter();
		String operation = request.getParameter("op");
		ObjectXMLSerializer serializer = new ObjectXMLSerializer();

		if (operation.equals("login")) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			response.setContentType("text/plain");
			out.print(login(request, userController, systemLogger, username, password));
		} else if (operation.equals("isLoggedIn")) {
			response.setContentType("text/plain");
			out.print(isUserLoggedIn(request));
		} else if (!isUserLoggedIn(request)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			try {
				if (operation.equals("getUser")) {
					response.setContentType("application/xml");
					User user = (User) serializer.fromXML(request.getParameter("user"));
					out.println(serializer.toXML(userController.getUser(user)));
				} else if (operation.equals("updateUser")) {
					User user = (User) serializer.fromXML(request.getParameter("user"));
					userController.updateUser(user);
				} else if (operation.equals("removeUser")) {
					User user = (User) serializer.fromXML(request.getParameter("user"));
					userController.removeUser(user);
				} else if (operation.equals("logout")) {
					logout(request, systemLogger);
				}
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}

	private boolean login(HttpServletRequest request, UserController userController, SystemLogger systemLogger, String username, String password) throws ServletException {
		try {
			HttpSession session = request.getSession();
			
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			List<User> users = userController.getUser(user);
			
			if (!users.isEmpty()) {
				User validUser = users.get(0);
				
				session.setAttribute(SESSION_USER, validUser.getId());
				session.setAttribute(SESSION_AUTHORIZED, true);
				
				// this prevents the session from timing out
				session.setMaxInactiveInterval(-1);

				// log the event
				SystemEvent event = new SystemEvent("User logged in.");
				event.getAttributes().put("Session ID", session.getId());
				event.getAttributes().put("User ID", validUser.getId());
				event.getAttributes().put("User Name", validUser.getUsername());
				systemLogger.logSystemEvent(event);

				return true;
			}

			return false;
		} catch (ControllerException e) {
			throw new ServletException(e);
		}
	}

	private void logout(HttpServletRequest request, SystemLogger systemLogger) {
		HttpSession session = request.getSession();
		session.removeAttribute(SESSION_USER);
		session.removeAttribute(SESSION_AUTHORIZED);
		session.invalidate();

		// log the event
		SystemEvent event = new SystemEvent("User logged out.");
		event.getAttributes().put("Session ID", session.getId());
		systemLogger.logSystemEvent(event);
	}
}