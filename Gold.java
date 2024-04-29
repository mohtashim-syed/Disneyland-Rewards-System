public class Gold extends Customer{

	private int discount;
	
	public int getDisc() {
		return discount;
	}
	
	public void setDisc(int discount) {
		this.discount = discount;
	}

	public Gold(String customerID, String firstName, String lastName, double amtSpent, int discount) {
		super(customerID, firstName, lastName, amtSpent);
		this.discount = discount;
		// TODO Auto-generated constructor stub
	}
	
}
