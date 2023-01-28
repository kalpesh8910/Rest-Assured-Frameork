package stepDefinations;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;
import resources.APIResource;
import resources.TestDataBuild;
import resources.Utils;

public class StepDefinations extends Utils{

	RequestSpecification res;
	ResponseSpecification resspec;
	Response response;
	static String place_id;
	JsonPath js;
	TestDataBuild data = new TestDataBuild();
	
	
	@Given("Add Place Payload with {string} {string} {string}")
	public void add_Place_Payload_with(String name, String language, String address) throws IOException {
	    
		res = given().spec(requestSpecifications())
				.body(data.addPlacePayload(name,language,address));

	}
	
	@When("user calls {string} with {string} http request")
	public void user_class_with_http_request(String resource, String method) {
		
		// constructor will be called with value of resource which you pass 
		APIResource resourceAPI = APIResource.valueOf(resource);
		String addplace = resourceAPI.getResource();
		
		System.out.println("app place resource is:-"+addplace);

		if(method.equalsIgnoreCase("POST"))		
			response = res.when().post(resourceAPI.getResource());
		else if(method.equalsIgnoreCase("GET"))
			response = res.when().get(resourceAPI.getResource());
		
	}

	@Then("the API call is success with status code {int}")
	public void the_API_call_is_success_with_status_code(Integer int1) {
	   
		assertEquals(response.getStatusCode(), 200);	
	}

	@Then("{string} in response body is {string}")
	public void in_response_body_is(String keyvalue, String Expectedvalue) {
	   
		assertEquals(getJsonPath(response, keyvalue),Expectedvalue);
	}
	
	@Then("verify place_Id created map to {string} using {string}")
	public void verify_place_Id_created_map_to_using(String expectedName, String resource) throws IOException {
	   
	//	requestSpec
		place_id = getJsonPath(response, "place_id");
		res = given().spec(requestSpecifications()).queryParam("place_id", place_id);
		
		user_class_with_http_request(resource, "GET");
		String actualName = getJsonPath(response, "name");
		assertEquals(expectedName, actualName);		
	}
	
	@Given("DeletePlace Payload")
	public void deleteplace_Payload() throws IOException {
	    
		// Link:-https://www.freeformatter.com/json-escape.html
		res = given().spec(requestSpecifications()).body(data.deletePlacePayload(place_id));
	}
}
