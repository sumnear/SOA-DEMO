import java.util.ArrayList;


public class TestOOM {
	static class Obj{
		public byte[] bytes = "hello everyone".getBytes();
	}
	public static void main(String[] args) {		 
		ArrayList<Obj> list = new ArrayList<Obj>();
		while(true){
			list.add(new Obj());
		}
	}
}
