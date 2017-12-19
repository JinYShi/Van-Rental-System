import java.util.ArrayList;
import java.util.Calendar;

public class Booking {
	private int id;
	private ArrayList<String> vanReqs;
	private Calendar start;
	private Calendar end;
	private ArrayList<Campervan> vanUsed;
	
	
	public Booking(int id, ArrayList<String> vanReqs, Calendar start, Calendar end) {
		this.id = id;
		this.vanReqs = vanReqs;
		this.start = start;
		this.end = end;
		vanUsed = new ArrayList<Campervan>();
	}
	public int getId() {
		return id;
	}

	public ArrayList<String> getVanReqs() {
		return vanReqs;
	}

	public void setVanReqs(ArrayList<String> vanReqs) {
		this.vanReqs = vanReqs;
	}

	public Calendar getStart() {
		return start;
	}

	public void setStart(Calendar start) {
		this.start = start;
	}

	public Calendar getEnd() {
		return end;
	}

	public void setEnd(Calendar end) {
		this.end = end;
	}

	public ArrayList<Campervan> getVansUsed() {
		return vanUsed;
	}
	
	public boolean confirmPass(ArrayList<Depot> depotL) {
		//1.找出每个车的session符合的，放到arrayList
		//2.check selected list 的不同数量是否符合num of type
		//3.进去找一下匹配的车，并且把时间加进去
		//因为是针对一个booking，所以如果接受了，可以创一个vanUsed
		//如果booking fail，就clear list

		//1.
		
		
		
//		System.out.println();
//		System.out.println("check the depotL---------------------");
//		for(int i = 0;i < depotL.size();i++) {
//			System.out.print(depotL.get(i).getDepotName()+": ");
//			for(int j = 0;j < depotL.get(i).getVans().size();j++) {
//				Campervan van = depotL.get(i).getVans().get(j);
//				System.out.print(van.getName()+":"+van.getOccup()+":"+van.getType()+"   ");
//				for(int k = 0;k < van.getBookDates().size();k++) {
//					Calendar time = van.getBookDates().get(k);
//					System.out.print(+time.get(Calendar.HOUR_OF_DAY)+revParseMonth(time.get(Calendar.MONTH))+time.get(Calendar.DAY_OF_MONTH)+" ");
//				}
//			}
//			System.out.println();
//		}
//		System.out.println("check done-------------------------");
		
		
		ArrayList<Campervan> selected = new ArrayList<Campervan>();
		for(int i = 0; i < depotL.size(); i++) {
			Depot d = depotL.get(i);
			for(int j = 0; j < d.getVans().size(); j++) {
				Campervan c = d.getVans().get(j);
				ArrayList<Calendar> bookTime = c.getBookDates();				
				if(bookTime.size() == 0 || bookTime.isEmpty()){
					selected.add(c);
				} else {
					int a = 1;
					for(int count1 = 0; count1 < bookTime.size();count1 = count1 + 2) {
						if(bookTime.get(count1).after(end) || start.after(bookTime.get(count1+1)));
						else{
							a = 0;
							break;
						}
							
//							int check = 0;
//							for( int a = 0;a < selected.size();a++) {
//								if(selected.get(a).getName().equals(c.getName())) {
//									check++;
//								}
//							}
//							if(check == 0) {
//								selected.add(c);
//							}
							
						
					}
					if(a == 1) {
						selected.add(c);
					}
				}			
			}
		}
		//2.
		
		int countMa = 0;
		int countAu = 0;
		for(int a = 0;a < selected.size();a++) {
			if(selected.get(a).getType().equals("Manual")) {
				countMa++;
			} else {
				countAu++;
			}
		}

		int reMa = 0;
		int reAu = 0;
		if(vanReqs.size() == 4){
			if(vanReqs.get(1).equals("Automatic")) {
				reAu = Integer.parseInt(vanReqs.get(0));
				reMa = Integer.parseInt(vanReqs.get(2));
			} else {
				reAu = Integer.parseInt(vanReqs.get(2));
				reMa = Integer.parseInt(vanReqs.get(0));
			}
		}else{
			if(vanReqs.get(1).equals("Automatic")) {
				reAu = Integer.parseInt(vanReqs.get(0));
				reMa = 0;
			}else {
				reAu = 0;
				reMa = Integer.parseInt(vanReqs.get(0));
			}
		}

		//3.
	//	System.out.println("gonna check number of different type: requir Au & Ma "+reAu+" " + reMa);
	//	System.out.println("aviailabe number of Au & Ma "+countAu+" "+countMa);
		if(reAu <= countAu && reMa <= countMa) {
			for(int a = 0;a < selected.size(); a++) {
				if(selected.get(a).getType().equals("Automatic") && reAu > 0) {
					selected.get(a).addBookedtime(start, end);
					vanUsed.add(selected.get(a));
					reAu--;
				}else if(selected.get(a).getType().equals("Manual") && reMa > 0){
					selected.get(a).addBookedtime(start, end);
					vanUsed.add(selected.get(a));
					reMa--;
				}
				if(reAu == 0 && reMa == 0) {
					for(int i = 0;i < vanUsed.size(); i++) {
						vanUsed.get(i).setOccup(true);
					}
					
					return true;
				} 
			}
		}
		
		selected.clear();
		//vanUsed.clear();		
		return false;
	}
	
	public boolean changeBooking(ArrayList<String> vanReqs, Calendar start, Calendar end, ArrayList<Depot> depotL) {
		Booking b = new Booking(-1, vanReqs, start, end);
		//System.out.println();
		////
		for(int i = 0; i < this.vanUsed.size(); i++) {
			//this.vanUsed.get(i).setOccup(false);
			this.vanUsed.get(i).getBookDates().remove(this.start);
			this.vanUsed.get(i).getBookDates().remove(this.end);
			if (vanUsed.get(i).getBookDates().size() == 0) {
				vanUsed.get(i).setOccup(false);
			}
		}

		if(b.confirmPass(depotL)) {
			this.end = end;
			this.vanReqs = vanReqs;
			this.start = start;			
			this.vanUsed = b.vanUsed;
			b = null;
			return true;
			
			
		}else {
			for(int i = 0;i < this.vanUsed.size(); i++) {
				this.vanUsed.get(i).setOccup(true);
				this.vanUsed.get(i).addBookedtime(this.start, this.end);
			}
			return false;
		}

	}
	
	public void cancel() {		
		for (int x = 0; x < vanUsed.size(); x++) {
			vanUsed.get(x).getBookDates().remove(start);
			vanUsed.get(x).getBookDates().remove(end);
			if (vanUsed.get(x).getBookDates().size() == 0) {
				vanUsed.get(x).setOccup(false);
			}
		}
		vanUsed.clear();
	}
}
