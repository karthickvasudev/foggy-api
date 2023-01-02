package com.application.foggy.support.websecurityconfig;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class SecretConfig {
    String secret = "bjJyyZX0iuAg2ELNWru9xeKULh3q6D6tAtl9GJoFWpshMHBUYS46UBHlSB24QC7itc6ycluokVjMk6QGI5DP7EVKac49GwdNqp9eHL3Dir0C72vBMRLLccDGN10svZEVZAnXey3lSM0CXBixYhzzseabym4cAGuUo4z21JT8SdlQLDB924YT4C1fymN6cDyi53JGvcyJeHLzdMziXz4s1P1zdzqGyjomcjT7Y3uGiGw2OClGGgJrcrsjvGjbNMMr";

    public SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
