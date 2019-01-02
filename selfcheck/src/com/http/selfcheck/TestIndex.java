package com.http.selfcheck;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TestIndex extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		this.doPost(req, resp);
	}

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {

	try {
		SemaphoreSingleton.getSemaphore().acquire();
	} catch (InterruptedException e) {}
	
	response.setHeader("Server-Status", "ok");
	response.getWriter().write("hello\n");

	return ;
}

}
