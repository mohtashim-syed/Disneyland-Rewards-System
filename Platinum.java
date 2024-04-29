public class Platinum extends Customer{
	

	private int bbucks;
		
		public int getBucks() {
			return bbucks;
		}
		
		public void setBucks(int bbucks) {
			this.bbucks = bbucks;
		}
		
		public Platinum(String customerID, String firstName, String lastName, double amtSpent, int bbucks) {
			super(customerID, firstName, lastName, amtSpent);
			this.bbucks = bbucks;
			// TODO Auto-generated constructor stub
		}
		
}
