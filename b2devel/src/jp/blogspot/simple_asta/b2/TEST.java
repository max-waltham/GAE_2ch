package jp.blogspot.simple_asta.b2;

import java.util.regex.Pattern;

public class TEST {
	
	private static final String regex3 = ".*?((ttp://|http://).*?(\\<BR\\>|\\.jpg|\\.gif)).*?";
	private static final Pattern REG_PICS = Pattern.compile(regex3);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String r3 = "rasdf ttp://asdfasdf.com/asdfa/ <BR>http://asdfasdf.com/asdfa/saf.jpg asdfa";
		java.util.regex.Matcher bm2 = REG_PICS.matcher(r3);
		while (bm2.find()) {
			String res = bm2.group(1);
			System.out.println(bm2.group(0));
			System.out.println(bm2.group(1));
			System.out.println(bm2.group(2));
			System.out.println(bm2.group(3));
			if(!res.startsWith("h")&&!res.startsWith("H")){
				res = "h"+res;
			}
			final StringBuilder respopup = new StringBuilder();
			respopup.append("<a href='").append(res).append("'><img width='64' height='48' src='").append(res).append("' />").append(res).append("</a>");							;
			r3 = r3.replace(bm2.group(1), respopup.toString());
		}
		System.out.println(r3);
	}

}
