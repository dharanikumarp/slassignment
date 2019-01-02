package controllers;

import static utils.UserProfileUtil.checkIfEmailExists;

import action.AccessTokenVerifierAction;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utils.ChatMessageUtil;

public class MessagesController extends Controller {

	@Security.Authenticated(AccessTokenVerifierAction.class)
	@BodyParser.Of(BodyParser.Json.class)
	public Result getMessages(final String emailId) {
		if (checkIfEmailExists(emailId)) {
			return ok(Json.toJson(ChatMessageUtil.getChatMessages(emailId)));
		} else {
			return notFound("User with email address " + emailId + " is not registered in our app.");
		}
	}
	
	@Security.Authenticated(AccessTokenVerifierAction.class)
	@BodyParser.Of(BodyParser.Json.class)
	public Result getAllMessages() {
		return ok(Json.toJson(ChatMessageUtil.getAllChatMessages()));
	}
}
