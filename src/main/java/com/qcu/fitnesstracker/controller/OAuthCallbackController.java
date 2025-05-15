//package com.qcu.fitnesstracker.controller;
//
//import com.qcu.fitnesstracker.model.GoogleUser;
//import com.qcu.fitnesstracker.repository.GoogleUserRepository;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//@Controller
//public class OAuthCallbackController {
//
//	@Autowired
//	private GoogleUserRepository googleUserRepository;
//
//	@GetMapping("/auth/callback")
//	@ResponseBody
//	public String oauthCallback(
//			@AuthenticationPrincipal OAuth2User principal,
//			HttpServletRequest request,
//			HttpServletResponse response,
//			HttpSession session) {
//
//		if (principal == null) {
//			return "Authentication failed";
//		}
//
//		try {
//			String googleId = principal.getAttribute("sub");
//			String email = principal.getAttribute("email");
//			String name = principal.getAttribute("name");
//			String picture = principal.getAttribute("picture");
//
//			// Store in session
//			session.setAttribute("LOGGED_IN_USER", googleId);
//			session.setAttribute("USER_EMAIL", email);
//			session.setAttribute("USER_NAME", name);
//			session.setAttribute("USER_PICTURE", picture);
//
//			// Store/update in database
//			GoogleUser existingUser = googleUserRepository.findByGoogleId(googleId);
//			if (existingUser == null) {
//				GoogleUser newUser = new GoogleUser(googleId, email, name, picture, null);
//				googleUserRepository.save(newUser);
//			}
//
//			// Set session cookie in response
//			response.addHeader("Set-Cookie", "JSESSIONID=" + session.getId() +
//					"; Path=/; HttpOnly; SameSite=None; Secure");
//
//			System.out.println("Successfully authenticated user via OAuth callback: " + email);
//
//			// Return success message that will be shown in the browser
//			return """
//               <!DOCTYPE html>
//            <html>
//            <head>
//                <title>Login Successful</title>
//                <meta charset="UTF-8">
//                <meta name="viewport" content="width=device-width, initial-scale=1.0">
//                <style>
//                    body {
//                        font-family: Arial, sans-serif;
//                        display: flex;
//                        justify-content: center;
//                        align-items: center;
//                        height: 100vh;
//                        margin: 0;
//                        background-color: #f0f2f5;
//                    }
//                    .container {
//                        text-align: center;
//                        padding: 2rem;
//                        background: white;
//                        border-radius: 8px;
//                        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
//                        max-width: 400px;
//                        width: 90%;
//                    }
//                    h1 {
//                        color: #1a73e8;
//                        margin-bottom: 1rem;
//                    }
//                    p {
//                        color: #5f6368;
//                        margin-bottom: 2rem;
//                    }
//                    .spinner {
//                        border: 4px solid #f3f3f3;
//                        border-top: 4px solid #1a73e8;
//                        border-radius: 50%;
//                        width: 40px;
//                        height: 40px;
//                        animation: spin 1s linear infinite;
//                        margin: 0 auto;
//                    }
//                    @keyframes spin {
//                        0% { transform: rotate(0deg); }
//                        100% { transform: rotate(360deg); }
//                    }
//                </style>
//            </head>
//            <body>
//                <div class="container">
//                    <h1>Login Successful!</h1>
//                    <p>You have successfully logged in. Returning to the application...</p>
//                    <div class="spinner"></div>
//                </div>
//                <script>
//                    // Send message to parent window before closing
//                    window.opener.postMessage({ type: 'LOGIN_SUCCESS' }, '*');
//
//                    // Close the window after 2 seconds
//                    setTimeout(() => {
//                        window.close();
//                    }, 2000);
//                </script>
//            </body>
//            </html>
//            """;
//
//		} catch (Exception e) {
//			System.err.println("Error in OAuth callback: " + e.getMessage());
//			e.printStackTrace();
//			return "Authentication error: " + e.getMessage();
//		}
//	}
//}