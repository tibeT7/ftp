package controllers;

import java.io.IOException;
import play.Logger;

import models.*;
import play.*;
import play.data.*;
import play.mvc.*;
import server.Server;
import views.html.*;
import static play.data.Form.*;


public class Application extends Controller {
	
	protected static String rootPath = "/Users/zg/FTPROOT";
	
	static private Logger logger;

	public static Result login() {
		return ok(login.render(form(Login.class)));
	}
	
	public static Result logout() {
		return ok(login.render(form(Login.class)));
	}
	
	public static Result authenticate() {
	    Form<Login> loginForm = form(Login.class).bindFromRequest();
	    
	    if (loginForm.hasErrors()) {
	        return badRequest(login.render(loginForm));
	    } else {
	    	Server serverInstance = null;
	    	String currentUserEmail = loginForm.get().email;
	        session().clear();
	        session("email", currentUserEmail);
	        String prefix = rootPath + "/" + User.find.where().eq("email", currentUserEmail).findUnique().name + "-" + currentUserEmail;
	        logger = new Logger();
	        try {
				 serverInstance = new Server(prefix, logger);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    
	        if(serverInstance == null)
	        	return internalServerError("Opps, something is broken!");
	        else {
	        	String tmp = (serverInstance.dir())[0];
	        	return ok(index.render(tmp));
	        }
	        	
	    }
	}
	
	public static Result guest() {
		return ok(index.render("Welcome!"));
	}
	
	public static Result register() {
		return ok(index.render("Under constructing!"));
	}

	public static class Login {
		public String email;
		public String password;
		
		public String validate() {
		    if (User.authenticate(email, password) == null) {
		      return "Invalid user or password";
		    }
		    return null;
		}
	}

}
