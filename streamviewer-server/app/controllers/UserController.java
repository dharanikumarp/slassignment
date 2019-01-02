package controllers;

import action.AccessTokenVerifierAction;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utils.UserProfileUtil;

public class UserController extends Controller {

	@Security.Authenticated(AccessTokenVerifierAction.class)
	@BodyParser.Of(BodyParser.Json.class)
	public Result getAllUsers() {
		return ok(Json.toJson(UserProfileUtil.getAllUsers()));
	}
}
