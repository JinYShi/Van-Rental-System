import java.util.ArrayList;

public class Depot {
	private String depotName;
	private ArrayList<Campervan> vans;
	
	//Depot include depot name and the arraylist of vans in the depot
	public Depot(String depotName, ArrayList<Campervan> vans) {
		this.depotName = depotName;
		this.vans = vans;
	}
	
	/*
	 * when the details of van is matched, then it will be add to the depot
	 * @precondition: the depot is exits
	 * @param :name of vans and type of vans
	 */
	//add belonged depot name, van name, van type, count the van number in this list?
	public void addVans(String name, String type) {
		Campervan c = new Campervan(this.depotName, name, type, vans.size());
				vans.add(c);
	}
	
	public String getDepotName() {
		return depotName;
	}
	
	public ArrayList<Campervan> getVans() {
		return vans;
	}
}
