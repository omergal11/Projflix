package com.example.netflixandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
//class to manage the shared preferences
public class PreferencesManager {

    private static SharedPreferences sharedPreferences;

    private static String IP_ADDRESS_KEY = "ip_address" ; // key for IP address
    private static String PORT_KEY = "port"; //key for the port
    private static final String TOKEN_KEY = "auth_token";
    private static final String PREF_NAME = "UserPreferences";
    private static final String USER_ID_KEY = "user_id"; //save the user ID
    private static final String USER_ROLE_KEY = "user_role"; //save the user role
    private static final String JWT_SECRET = "jwt_secret"; //secret key for the token

    //basic function to save the token
    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        ConfigManager configManager = new ConfigManager(context);
        String ipAddress = configManager.getIpAddress();
        String port = configManager.getPort();
        String jwtSecret = configManager.getSecretData();
        saveNetworkSettings(ipAddress,port,jwtSecret);
    }

    //function to save the token
    public static void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jwtSecret = sharedPreferences.getString(JWT_SECRET,null);
        editor.putString(TOKEN_KEY, token);

        try {
            //decode the token
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            Thread.sleep(3000);
            DecodedJWT decodedJWT = verifier.verify(token);

            //extract the user ID and role
            String userId = decodedJWT.getClaim("userId").asString();
            String userRole = decodedJWT.getClaim("role").asString();

            //save the user ID and role in the shared preferences
            editor.putString(USER_ID_KEY, userId);
            editor.putString(USER_ROLE_KEY, userRole);

            editor.apply();
            Log.d("PreferencesManager", "User ID: " + userId + ", User Role: " + userRole);
        } catch (JWTDecodeException e) {
            Log.e("PreferencesManager", "Error decoding token", e);
        } catch (InvalidClaimException e) {
            Log.e("PreferencesManager", "Token is not valid before: " + e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PreferencesManager", "Error extracting user ID or role from token", e);
        }
    }
    public static void saveNetworkSettings(String ipAddress, String port,String jwtSecret) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IP_ADDRESS_KEY, ipAddress);
        editor.putString(PORT_KEY, port);
        editor.putString(JWT_SECRET,jwtSecret);
        editor.apply();
    }
    //get the token
    public static String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    //get the user role
    public static String getUserRole() {
        return sharedPreferences.getString(USER_ROLE_KEY, null);  
    }

    public static String getIpAddress() {
        return sharedPreferences.getString(IP_ADDRESS_KEY, "default_ip");  //default_ip if not set
    }

    // Get port
    public static String getPort() {
        return sharedPreferences.getString(PORT_KEY, "default_port");  //default_port if not set
    }

    //clear the user data
    public static void clearUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TOKEN_KEY);
        editor.remove(USER_ID_KEY);
        editor.remove(USER_ROLE_KEY); 
        editor.apply();
    }
    //get the user ID
    public static String getUserId() {
        return sharedPreferences.getString(USER_ID_KEY, null);
    }
}
