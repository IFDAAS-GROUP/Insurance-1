package lyit.oscar.insurance;
import java.util.ArrayList;
//imports
import java.util.Random;

/**
 * @author Melissa Melaugh
 */
public class Pricing {
	//Variables
	private User currentUser2;
	
	/**
	 * The constructor for the pricing class
	 * @param aUser2	this is the current User2
	 */
	public Pricing(User aUser2){
		this.currentUser2 = aUser2;
		generatePrice();
	}
	
	/**
	 * This method looks at the current User2 and generates a price based on their data.
	 * If they wish to purchase, it generates a policy number and updates the User2's information.
	 */
	public void generatePrice(){
		//TODO Generate a price for the User2, and then ask them if they want to purchase 
		//TODO: When purchasing, ask how often they would like to pay
		
		//If the User2 accepts, generate policy number
		generatePolicyNum();
		//Update Pricing on the User2's profile
	}
	
	/**
	 * This method generates a unique policy number for the User2 between 100000000 and 999999999. 
	 * It updates the User2's profile with their unique policy number.
	 */
	public void generatePolicyNum(){
		int polNum = 0;
		
		//Generates the policy number
		while(true){
			Random rnd = new Random();
			polNum = rnd.nextInt(899999999) + 100000000; //makes sure the policy number is between 100000000 and 999999999
			ArrayList<User> userList = List.getUserList(true);
			Boolean generated = true;
			
			//Check to make sure that policy numbers are not duplicated
			for(User user:userList){
				String toCheck = "" + polNum;
				if(toCheck.equals(user.getPolicyNo())){
					generated = false; //If it is a duplicate, it's not properly generated
				}
			}
			
			if(generated){
				break;
			}
		}
		currentUser2.setPolicyNo("" + polNum);
	}
	
}
