public class Test {
	public static void main(String[] args) {
		String num= "1234";
		excute(num);
	}
	
	
	public static void excute(String num) {
		String[] digits= new String[]{"个","十","百","千","万"};
		System.out.println("位数："+num.length()+"位数");
		for (int index=0; index < num.length(); index++) {
			System.out.println(digits[num.length()-1-index]+"位数是:"+num.charAt(index));
		}
		System.out.println("逆序："+new StringBuilder(num).reverse().toString());
	}
}
