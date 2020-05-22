package com.pixop.sdk.services.accounts;

import com.pixop.sdk.services.accounts.AccountsServiceClient.InvalidJWTTokenFoundException;
import com.pixop.sdk.services.accounts.config.AccountsServiceClientConfig;
import com.pixop.sdk.services.accounts.model.Team;
import com.pixop.sdk.services.accounts.model.User;
import com.pixop.sdk.services.accounts.response.AuthenticatedIdentity;
import com.pixop.sdk.services.accounts.response.GetUserResponse;
import com.pixop.sdk.services.accounts.response.NewAuthToken;
import com.pixop.sdk.services.accounts.response.UpdateUserResponse;

/**
 * @author  Paul Cook
 * @version
 */
public final class TestAccountsService {

    //public static final String EXPIRED_JWT_TOKEN_STRING = "eyJraWQiOiJrZXktMSIsImFsZyI6IkhTMjU2In0.eyJqdGkiOiIzNWNmMTM2Yy00YzJjLTQxMmEtOWJhYi04NjU2Mzg0Y2RhYjkiLCJpYXQiOjE1ODc0OTczMzIsInN1YiI6IkFQSS1BY2Nlc3MtQ3JlZGVudGlhbHMiLCJpc3MiOiJQaXhvcCIsImV4cCI6MTU4NzQ5ODIzMiwibmJmIjoxNTg3NDk3MzMyLCJ1c2VyX2lkIjoiMTBmYWEyZmMtN2FhNi00OWMzLThlNzItN2M5ZjFmZDJkMWJkIn0.mu5NFmzAQqRSSjfi4mk6O1EO6P9PkYVGBaj7GlqoD6k";
    public static final String INVALID_JWT_TOKEN_STRING = "eyJraWQiOiJrZXktMSIsImFsZyI6IkhTMjU2In0.eyJqdGkiOiIzNWNmMTM2Yy00YzJjLTQxMmEtOWJhYi04NjU2Mzg0Y2RhYjkiLCJpYXQiOjE1ODc0OTczMzIsInN1YiI6IkFQSS1BY2Nlc3MtQ3JlZGVudGlhbHMiLCJpc3MiOiJQaXhvcCIsImV4cCI6MTU4NzQ5ODIzMiwibmJmIjoxNTg3NDk3MzMyLCJ1c2VyX2lkIjoiMTBmYWEyZmMtN2FhNi00OWMzLThlNzItN2M5ZjFmZDJkMWJkIn0.mu5NFmzAQqRSSjfi4mk6O1EO6P9PkYVGBaj7GlqoD6k";

    public static void main(String[] args) throws Exception {
        final AccountsServiceClientConfig accountsServiceClientConfig = new AccountsServiceClientConfig.Builder()
                .setAccountsServiceHost("staging-api.pixop.com")
                .setAccountsServicePort(443)
                .setSsl(true)
                .build();

        final AccountsServiceClient accountsServiceClient = new AccountsServiceClient(accountsServiceClientConfig);

        final String email = System.getenv("PIXOP_API_EMAIL");
        final String password = System.getenv("PIXOP_API_PASSWORD");

        User user = null;
        System.out.println(".... generate an auth secret for JWT signing ....");
        final UpdateUserResponse updateUserResponse = accountsServiceClient.generateAuthSecret(email,
                                                                                               password);
        user = updateUserResponse.getUser();
        System.out.println(".... finished generate an auth secret for JWT signing .... user :: " + user);

        String teamId = null;
        final NewAuthToken newToken = accountsServiceClient.newToken(email,
                                                                     password,
                                                                     teamId);

        System.out.println("::: got new JWT token for user [ " + email + " ] ::: TOKEN [ " + newToken.getJwtTokenString() + " ] identified user id [ " + newToken.getUserId() + " ] Team [ " + newToken.getTeamId() + " ] ");

        final NewAuthToken newToken2 = accountsServiceClient.newToken(newToken.getJwtTokenString(),
                                                                      teamId);

        System.out.println(":::: got new JWT token from existing token .... [ " + newToken2.getJwtTokenString() + " ] ");

        /*
        teamId = "9411d7f6-cb8d-40fa-8052-b0968087bdf3";

        final NewAuthToken newToken3 = accountsServiceClient.newToken(email,
                                                                      password,
                                                                      teamId);

        System.out.println("::: got new JWT token for user [ " + email + " ] authenticating against an additional team [ " + teamId + "] ::: TOKEN [ " + newToken3.getJwtTokenString() + " ] identified user id [ " + newToken3.getUserId() + " ] Team [ " + newToken3.getTeamId() + " ] ");

        final NewAuthToken newToken4 = accountsServiceClient.newToken(newToken.getJwtTokenString(), // use first token, as this will cause us to switch teams for the new token
                                                                      teamId);

        System.out.println(":::: got new JWT token from existing token and changing team in the new token from [ " + newToken2.getTeamId() + " ] to [ " + newToken4.getTeamId() + " ] ... [ " + newToken4.getJwtTokenString() + " ] ");
        */

        final String sourceIp = "1.2.3.4";
        AuthenticatedIdentity authenticatedIdentity = accountsServiceClient.validateToken(newToken.getJwtTokenString(), sourceIp);
        user = authenticatedIdentity.getUser();
        final Team team = authenticatedIdentity.getTeam();
        final String jwtToken = authenticatedIdentity.getJwtTokenString();

        System.out.println("..... validated token .. got user :: " + user + " \nGot Team :: " + team + "\n JWT-TOKEN :: " + jwtToken);

        authenticatedIdentity = accountsServiceClient.validateToken(newToken2.getJwtTokenString(), sourceIp);
        System.out.println("..... validated token generated from first token.. got user :: " + authenticatedIdentity.getUser() + " \nGot Team :: " + authenticatedIdentity.getTeam() + "\n JWT-TOKEN :: " + authenticatedIdentity.getJwtTokenString());

        /*
        authenticatedIdentity = accountsServiceClient.validateToken(newToken3.getJwtTokenString(), sourceIp);
        System.out.println("..... validated token generated against alternative team.. got user :: " + authenticatedIdentity.getUser() + " \nGot Team :: " + authenticatedIdentity.getTeam() + "\n JWT-TOKEN :: " + authenticatedIdentity.getJwtTokenString());

        authenticatedIdentity = accountsServiceClient.validateToken(newToken4.getJwtTokenString(), sourceIp);
        System.out.println("..... validated token generated from first token against alternative team.. got user :: " + authenticatedIdentity.getUser() + " \nGot Team :: " + authenticatedIdentity.getTeam() + "\n JWT-TOKEN :: " + authenticatedIdentity.getJwtTokenString());
        */

        boolean gotCorrectException = false;
        try {
            System.out.println("::: about to validate a 'known expired' token :: " + INVALID_JWT_TOKEN_STRING);
            final AuthenticatedIdentity authenticatedIdentity2 = accountsServiceClient.validateToken(INVALID_JWT_TOKEN_STRING, sourceIp);
            System.out.println("... result :: " + authenticatedIdentity2);
        } catch (final InvalidJWTTokenFoundException e) {
            System.out.println("... caught expected invalid token exception ...." + e);
            gotCorrectException = true;
        }
        if (!gotCorrectException)
            throw new Exception("Did not catch invalid token exception!!");

        final GetUserResponse getUserResponse = accountsServiceClient.getUserAccount(jwtToken);
        System.out.println(":::: retrieved details of current user account / team :::::" + getUserResponse);

        System.out.println("==== DONE ===");
    }

}
