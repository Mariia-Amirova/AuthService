package net.absoft;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.absoft.data.Response;
import net.absoft.services.AuthenticationService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class AuthenticationServiceTest extends BaseTestPositive {
  private Response authenticationServiceGoalPositive;

  @BeforeMethod(
          groups = "Positive"
  )
  public void setUp() {
    authenticationServiceGoalPositive = new AuthenticationService().authenticate("user1@test.com", "password1");
    System.out.println("Preconditions Positive");
  }

  @Test(
          groups = "Positive"
  )
  public void testSuccessfulAuthentication() {
    Response response =authenticationServiceGoalPositive;
    assertEquals(response.getCode(), 200, "Response code should be 200");
    assertTrue(validateToken(response.getMessage()),
            "Token should be the 32 digits string. Got: " + response.getMessage());
  }


  @DataProvider(name ="invalidCredentials", parallel = true)
  public Object[][] generateInvalidCredentials(){
  return new Object[][]{
          new Object[]{"user1@test.com", "wrong_password1",
            new Response(401, "Invalid email or password")},
          new Object[]{"", "wrong_password1",
            new Response(400, "Email should not be empty string")},
          new Object[]{"user1@test.com", "",
            new Response(400, "Password should not be empty string")},
          new Object[]{"user1", "wrong_password1",
            new Response(400, "Invalid email")}

  };
}

  @Test(
          groups = "Negative",
          dataProvider = "invalidCredentials"
  )
  public void testAuthenticationWithWrongCredentials(String email, String password, Response expectedResponse) {
    Response actualResponse = new AuthenticationService().authenticate(email, password);
    SoftAssert softAssert = new SoftAssert();
    softAssert.assertEquals(actualResponse, expectedResponse, "Unexpected response");
    softAssert.assertAll();
  }

  private boolean validateToken(String token) {
    final Pattern pattern = Pattern.compile("\\S{32}", Pattern.MULTILINE);
    final Matcher matcher = pattern.matcher(token);
    return matcher.matches();
  }
}
