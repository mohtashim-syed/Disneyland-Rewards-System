public class Customer {
	protected String firstName;
	protected String lastName;
	protected String customerID;
	protected double amtSpent;
	
	//create an object by creating an overloaded constructor
	public Customer(String customerID, String firstName, String lastName, double amtSpent){
		this.customerID = customerID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.amtSpent = amtSpent;
	}
	
	//getters
	
	
	public String getFirst() {
		return firstName;
	}
	
	public String getLast() {
		return lastName;
	}
	
	public String getID() {
		return customerID;
	}
	
	public double getSpent() {
		return amtSpent;
	}
	

	// setters
	public void setFirst( String firstName) {
		this.firstName = firstName;
	}
	
	public void setLast(String lastName) {
		this.lastName = lastName;
	}
	
	public void setID(String customerID) {
		this.customerID = customerID;
	}
	
	public void setSpent(double amtSpent) {
		this.amtSpent =  amtSpent;
	}
	
	
}
