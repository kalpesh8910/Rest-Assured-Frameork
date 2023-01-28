package stepDefinations;

import java.io.IOException;

import io.cucumber.java.Before;

public class Hooks {

	@Before("@DeletePlace")
	public void beforescenario() throws IOException {
	
		// write a code that will give you place id
		// execute this code only when place id is null
		
		StepDefinations m = new StepDefinations();
		
		if(StepDefinations.place_id== null)
		{
			m.add_Place_Payload_with("kalpesh", "French", "Asia");
			m.user_class_with_http_request("AddPlaceAPI", "POST");
			m.verify_place_Id_created_map_to_using("kalpesh", "getPlaceAPI");
		}
	}
}
